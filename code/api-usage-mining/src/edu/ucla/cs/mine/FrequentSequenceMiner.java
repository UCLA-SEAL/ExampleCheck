package edu.ucla.cs.mine;

import java.util.ArrayList;
import java.util.HashSet;

public class FrequentSequenceMiner extends SequencePatternMiner {

	public FrequentSequenceMiner(String script_path, String seqs_path,
			int min_support, HashSet<HashSet<String>> filters) {
		super(script_path, seqs_path, min_support, filters);
	}

	@Override
	protected void process(String input) {
		String[] ss = input.split(System.lineSeparator());
		
		for(String s : ss) {
			// process each pattern detected by the frequent item miner
			if(s.startsWith("(") && s.endsWith(")")) {
				int support = Integer.valueOf(s.substring(s.lastIndexOf(", ") + 2, s.lastIndexOf(")")));
				String sequence = s.substring(s.indexOf("(") + 1, s.lastIndexOf(", "));
				
				ArrayList<String> pattern = new ArrayList<String>();
				sequence = sequence.substring(sequence.indexOf("(") + 1, sequence.lastIndexOf(")"));
				// cannot split by comma only since API call arguments are also split by comma
				String[] arr = sequence.split("'");
				for(String api : arr){
					api = api.trim();
					if(api.equals(",") || api.isEmpty()) {
						continue;
					}
					pattern.add(api);
				}
				
				this.patterns.put(pattern, support);
			}
		}
	}

	@Override
	protected void filter() {
		ArrayList<ArrayList<String>> remove = new ArrayList<ArrayList<String>>();
		
		// Filter 1: check whether patterns follow any queries and also the grammar
		for(ArrayList<String> pattern : this.patterns.keySet()) {
			if(!isBalanced(pattern) || isLingering(pattern)) {
				remove.add(pattern);
				continue;
			}
			
			boolean flag = false;
			for(HashSet<String> query : this.queries) {
				if(pattern.containsAll(query)){
					flag = true;
					break;
				}
			}
			
			if(!flag) {
				// does not follow any queries
				remove.add(pattern);
				continue;
			}
		}
		
		// remove unsatisfied patterns
		for(ArrayList<String> pattern : remove) {
			this.patterns.remove(pattern);
		}
		
		// Filter 2: remove empty control construct blocks unless it is an if check following an API call
		remove.clear();
		for(ArrayList<String> pattern : this.patterns.keySet()) {	
			for(int i = 0; i < pattern.size(); i ++) {
				String item = pattern.get(i);
				if(item.equals("IF {") || item.equals("TRY {") || item.equals("LOOP {") || item.equals("CATCH {") || item.equals("ELSE {") || item.equals("FINALLY {")) {
					if(i < pattern.size() - 1) {
						String next = pattern.get(i+1);
						if(next.equals("}")) {
							if(item.equals("IF {") && i > 0) {
								String prev = pattern.get(i-1);
								if(prev.contains("(")) {
									// value check
									continue;
								}
							}
							
							remove.add(pattern);
							break;
						}
					}
				} else if (item.contains("(")) {
					int count = 0;
					for(int j = 0; j < pattern.size(); j ++) {
						if(pattern.get(j).equals(item)) {
							count++;
						}
					}
					if(count > 1){
						// repetitive
						remove.add(pattern);
					}
				}
			}
		}
		
		// remove unsatisfied patterns
		for(ArrayList<String> pattern : remove) {
			this.patterns.remove(pattern);
		}
	}
	
	public static void main(String[] args){
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("loadIcon(1)");
		queries.add(q1);
		//query.add("mkdirs");
		// learn from the output of the light-weight output
		//PatternMiner pm = new FrequentSequenceMiner("/home/troy/research/BOA/Maple/mining/freq_seq.py", 
		//		"/home/troy/research/BOA/Maple/example/output.txt",
		//		40,
		//		query);
		
		// learn from the output of the traditional output
		SequencePatternMiner pm = new FrequentSequenceMiner("/home/troy/research/BOA/Maple/mining/freq_seq.py", 
				"/home/troy/research/BOA/example/ApplicationInfo.loadIcon/1/large-output.txt",
				(int) (780 * 0.5),
				queries);

		pm.mine();
		for(ArrayList<String> pattern : pm.patterns.keySet()) {
			System.out.println(pattern + ":" + pm.patterns.get(pattern));
		}
	}
}
