package edu.ucla.cs.process.lightweight;

import java.util.ArrayList;

import com.google.common.collect.HashMultiset;

import edu.ucla.cs.model.Assignment;
import edu.ucla.cs.model.Method;

public class AssignmentProcessor extends ProcessStrategy{

	@Override
	void process(String line) {
		Method m = getMethodInstance(line);
		buildAssignmentMap(m, line);
	}

	protected void buildAssignmentMap(Method m, String line) {
		String s = line.substring(line.indexOf("] =") + 3).trim();
		String[] ss = s.split("@@");
		for(int i = 0; i < ss.length; i++){
			String lhs = ss[i].split("->")[0];
			String rs = ss[i].split("->")[1];
			
			HashMultiset<Assignment> as = HashMultiset.create();
			
			String[] arr = rs.split(";;;");
			for(int j = 0; j < arr.length; j++) {
				String rhs = arr[j];
				
				ArrayList<String> uses = new ArrayList<String>();
				// create an Assignment instance first so we can also use the same instance in the reverse map
				Assignment assign = new Assignment(lhs, uses);
				String[] arr2 = rhs.split("\\|");
				// skip the first element because it is empty
				for(int k = 1; k < arr2.length; k++) {
					uses.add(arr2[k]);
					// update the reverse map
					HashMultiset<Assignment> set;
					if(m.rev_assigns.containsKey(arr2[k])) {
						set = m.rev_assigns.get(arr2[k]);
					} else {
						set = HashMultiset.create();
					}
					set.add(assign);
					m.rev_assigns.put(arr2[k], set);
				}
				
				as.add(assign);
			}
			
			m.assigns.put(lhs, as);
		}
	}
}
