package edu.ucla.cs.mine;

import java.util.ArrayList;
import java.util.HashSet;

public class FrequentItemMiner extends SequencePatternMiner{
	
	public FrequentItemMiner(String script_path, String seqs_path,
			int min_support, HashSet<HashSet<String>> queries) {
		super(script_path, seqs_path, min_support, queries);
	}

	@Override
	protected void process(String input) {
		String[] ss = input.split(System.lineSeparator());
		
		for(String s : ss) {
			// process each pattern detected by the frequent item miner
			if(s.startsWith("(") && s.endsWith(")")) {
				int support = Integer.valueOf(s.substring(s.lastIndexOf(", ") + 2, s.lastIndexOf(")")));
				String frozenset = s.substring(s.indexOf("(") + 1, s.lastIndexOf(", "));
				
				ArrayList<String> pattern = new ArrayList<String>();
				frozenset = frozenset.substring(frozenset.indexOf("[") + 1, frozenset.lastIndexOf("]"));
				String[] arr = frozenset.split(", ");
				for(String api : arr){
					// strip ' '
					api = api.substring(1, api.length()-1);
					pattern.add(api);
				}
				
				this.patterns.put(pattern, support);
			}
		}
	}

	@Override
	protected void filter() {
		ArrayList<ArrayList<String>> remove = new ArrayList<ArrayList<String>>();
		
		// check whether each pattern is satisfiable
		for(ArrayList<String> pattern : this.patterns.keySet()) {
			if(!pattern.containsAll(this.queries) || !isBalanced(pattern) || !isComplete(pattern)){
				remove.add(pattern);
			}
		}
		
		// remove unsatisfied patterns
		for(ArrayList<String> pattern : remove) {
			this.patterns.remove(pattern);
		}
	}
	
	public static void main(String[] args){
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> query = new HashSet<String>();
		query.add("createNewFile");
		queries.add(query);
		SequencePatternMiner pm = new FrequentItemMiner("/home/troy/research/BOA/Slicer/mining/freq_itemset.py", 
				"/home/troy/research/BOA/Slicer/example/output.txt",
				40,
				queries);
		pm.mine();
		for(ArrayList<String> pattern : pm.patterns.keySet()) {
			System.out.println(pattern + " : " + pm.patterns.get(pattern));
		}
	}
}
