package edu.ucla.cs.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.utils.ProcessUtils;

public class ExtendedFrequentSequenceMiner extends FrequentSequenceMiner {

	public ExtendedFrequentSequenceMiner(String script_path, String seqs_path,
			int min_support, HashSet<HashSet<String>> filters) {
		super(script_path, seqs_path, min_support, filters);
	}

	@Override
	protected void filter() {
		ArrayList<ArrayList<String>> remove = new ArrayList<ArrayList<String>>();

		// Filter 1: check whether patterns follow any queries and also the
		// grammar
		for (ArrayList<String> pattern : this.patterns.keySet()) {
			if (!isBalanced(pattern) || isLingering(pattern)) {
				remove.add(pattern);
				continue;
			}

			// In the extended version, we do not specify the argument types of
			// an API call in the query
			// But the API calls in a pattern contain the corresponding argument
			// types
			// So we have to strip off the arguments of an API call in a pattern
			// first
			ArrayList<String> patternWithoutArguments = ProcessUtils
					.stripOffArguments(pattern);

			boolean flag = false;
			for (HashSet<String> query : this.queries) {
				if (patternWithoutArguments.containsAll(query)) {
					flag = true;
					break;
				}
			}

			if (!flag) {
				// does not follow any queries
				remove.add(pattern);
				continue;
			}
		}

		// remove unsatisfied patterns
		for (ArrayList<String> pattern : remove) {
			this.patterns.remove(pattern);
		}

		// Filter 2: remove empty control construct blocks unless it is an if
		// check following an API call
		remove.clear();
		for (ArrayList<String> pattern : this.patterns.keySet()) {
			for (int i = 0; i < pattern.size(); i++) {
				String item = pattern.get(i);
				if (item.equals("IF {") || item.equals("TRY {")
						|| item.equals("LOOP {") || item.equals("ELSE {") || item.equals("FINALLY {")) {
					if (i < pattern.size() - 1) {
						String next = pattern.get(i + 1);
						if (next.equals("}")) {
							if (item.equals("IF {") && i > 0) {
								String prev = pattern.get(i - 1);
								if (prev.contains("(")) {
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
					for (int j = 0; j < pattern.size(); j++) {
						if (pattern.get(j).equals(item)) {
							count++;
						}
					}
					if (count > 1) {
						// repetitive
						remove.add(pattern);
					}
				}
			}
		}

		// remove unsatisfied patterns
		for (ArrayList<String> pattern : remove) {
			this.patterns.remove(pattern);
		}

		// add a catch block with the most general Exception type for lingering
		// try blocks
		HashMap<ArrayList<String>, ArrayList<String>> replacement = new HashMap<ArrayList<String>, ArrayList<String>>();
		for (ArrayList<String> pattern : this.patterns.keySet()) {
			for (int i = 0; i < pattern.size(); i++) {
				String elem = pattern.get(i);
				if (elem.equals("TRY {")) {
					// scan the rest to look for a matching catch block
					int braceCounter = 1;
					int matchedCloseBrace = -1;
					boolean foundMatchedCatch = false;
					for (int j = i + 1; j < pattern.size(); j++) {
						String elem2 = pattern.get(j);
						if (elem2.equals("IF {") || elem2.equals("LOOP {")
								|| elem2.equals("ELSE {")
								|| elem2.equals("FINALLY {")
								|| elem2.equals("TRY {")) {
							braceCounter ++;
						} else if (elem2.equals("}")) {
							braceCounter --;
							if(braceCounter == 0) {
								matchedCloseBrace = j;
							}
						} else if (elem2.contains("CATCH(") && elem2.endsWith("Exception) {")) {
							if(braceCounter == 0) {
								// match!!!
								foundMatchedCatch = true;
								break;
							} else {
								// the catch block for another try block
								braceCounter++;
							}
						}
					}
					
					if(!foundMatchedCatch && matchedCloseBrace != -1) {
						ArrayList<String> patternCopy = new ArrayList<String>(pattern);
						patternCopy.add(matchedCloseBrace + 1, "CATCH(Exception) {");
						patternCopy.add(matchedCloseBrace + 2, "}");
						replacement.put(pattern, patternCopy);
					}
				}
			}
		}
		
		for(ArrayList<String> pattern : replacement.keySet()) {
			int count = this.patterns.get(pattern);
			this.patterns.remove(pattern);
			this.patterns.put(replacement.get(pattern), count);
		}
	}

	public static void main(String[] args) {
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("getAsString");
//		q1.add("get");
		queries.add(q1);
		// query.add("mkdirs");
		// learn from the output of the light-weight output
		// PatternMiner pm = new
		// FrequentSequenceMiner("/home/troy/research/BOA/Maple/mining/freq_seq.py",
		// "/home/troy/research/BOA/Maple/example/output.txt",
		// 40,
		// query);

		// learn from the output of the traditional output
		SequencePatternMiner pm = new ExtendedFrequentSequenceMiner(
				"/home/troy/SO-Empirical-Study/code/Maple/mining/freq_seq.py",
				"/media/troy/Disk2/Boa/apis/JsonElement.getAsString/large-output-resolved.txt",
				(int) (865 * 0.5), queries);

		pm.mine();
		for (ArrayList<String> pattern : pm.patterns.keySet()) {
			System.out.println(pattern + ":" + pm.patterns.get(pattern));
		}
	}
}
