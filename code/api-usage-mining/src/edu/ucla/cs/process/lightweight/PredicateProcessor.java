package edu.ucla.cs.process.lightweight;

import com.google.common.collect.HashMultiset;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.model.Predicate;

public class PredicateProcessor extends ProcessStrategy{

	@Override
	void process(String line) {
		Method m = getMethodInstance(line);
		buildPredicateMap(m, line);
	}

	private void buildPredicateMap(Method m, String line) {
		String s = line.substring(line.indexOf("] =") + 3).trim();
		String[] ss = s.split(";;;");
		for(int i = 0; i < ss.length; i++){
			String method = ss[i].split("::")[0];
			String conds = ss[i].split("::")[1];
			
			HashMultiset<Predicate> rs = HashMultiset.create();
			
			String[] arr = conds.split("\\*\\*\\*");
			for(int j = 0; j < arr.length; j++) {
				String cond = arr[j];
				Predicate p = new Predicate(method, cond);
				rs.add(p);
			}
			
			m.predicates.put(method, rs);
		}
	}

}
