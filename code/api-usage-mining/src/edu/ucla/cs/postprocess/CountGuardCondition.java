package edu.ucla.cs.postprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.ucla.cs.mine.PredicatePatternMiner;
import edu.ucla.cs.mine.TraditionalPredicateMiner;

public class CountGuardCondition {
	public static void main(String[] args) {
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("get(1)");
		String path = "/home/troy/research/BOA/example/HashMap.get/INF/large-sequence.txt";
		String sequence_path = "/home/troy/research/BOA/example/HashMap.get/INF/large-output.txt";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern,
				path, sequence_path);
		// disable Z3
//		PredicatePatternMiner.enableSMT = false;
		pm.process();
		int min_support = 300;
		HashMap<String, HashMap<String, Integer>> predicates = pm.find_the_most_common_predicate(min_support);
		
		for(String api : predicates.keySet()) {
			System.out.println(api);
			System.out.println("{");

			// sort the guard conditions
			List<Map.Entry<String, Integer>> l = new ArrayList<Map.Entry<String,Integer>>(predicates.get(api).entrySet());
			Collections.sort(l, new Comparator<Map.Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1,
						Entry<String, Integer> o2) {
					if(o1.getValue() > o2.getValue()) {
						return -1;
					} else if (o1.getValue() < o2.getValue()) {
						return 1;
					} else {
						return 0;
					}
				}
			});
			
			for(Map.Entry<String, Integer> entry : l) {
				System.out.println("\"" + entry.getKey() + "\":" + entry.getValue());
			}

			System.out.println("}");
		}
	}
}
