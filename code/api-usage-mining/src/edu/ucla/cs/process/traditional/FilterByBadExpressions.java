package edu.ucla.cs.process.traditional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.mine.TraditionalPredicateMiner;
import edu.ucla.cs.utils.FileUtils;
import edu.ucla.cs.utils.SAT;

public class FilterByBadExpressions extends TraditionalPredicateMiner{

	public FilterByBadExpressions(ArrayList<String> pattern, String raw_output,
			String seq) {
		super(pattern, raw_output, seq);
	}
	
	public void filter() {
		super.loadAndFilterPredicate();
		
		HashSet<String> removes = new HashSet<String>();
		SAT sat = new SAT();
		for(String id : this.predicates.keySet()) {
			HashMap<String, ArrayList<String>> api_predicates = predicates
					.get(id);
			for(String api : api_predicates.keySet()) {
				if(pattern.contains(api)) {
					ArrayList<String> conditions = api_predicates.get(api);
					for(String condition : conditions) {
						boolean res = sat.checkImplication(condition, "false");
						if(res) {
							// formatting error
							removes.add(id);
						}
					}
				}
			}
		}
		
		FileUtils.removeLinesById(path, removes);
	}
	
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/Maple/example/ArrayList.get/large-sequence.txt";
		String seq = "/home/troy/research/BOA/Maple/example/ArrayList.get/large-output.txt";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("get(1)");
		FilterByBadExpressions f = new FilterByBadExpressions(pattern, raw_output, seq);
		f.filter();
	}
}
