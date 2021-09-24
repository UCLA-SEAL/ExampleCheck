package edu.ucla.cs.process.lightweight;

import java.util.ArrayList;

import com.google.common.collect.HashMultiset;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.model.MethodCall;

public class ArgumentProcessor extends ProcessStrategy{

	@Override
	void process(String line) {
		Method m = getMethodInstance(line);
		buildArgumentMap(m, line);
	}

	protected void buildArgumentMap(Method m, String line) {
		String s = line.substring(line.indexOf("] =") + 3).trim();
		String[] ss = s.split("@@");
		for(int i = 0; i < ss.length; i++){
			String name = ss[i].split("->")[0];
			String calls = ss[i].split("->")[1];
			
			HashMultiset<MethodCall> mcs = HashMultiset.create();
			
			String[] arr = calls.split(";;;");
			for(int j = 0; j < arr.length; j++) {
				String args = arr[j];
				ArrayList<String> l = new ArrayList<String>();
				// create a MethodCall instance first so we can use the same instance in the reverse map
				MethodCall mc = new MethodCall(name, l);
				String[] arr2 = args.split(",");
				for(int k = 0; k < arr2.length; k++) {
					l.add(arr2[k]);
					// update the reverse map
					HashMultiset<MethodCall> set;
					if(m.rev_args.containsKey(arr2[k])) {
						set = m.rev_args.get(arr2[k]);
					} else {
						set = HashMultiset.create();
					}
					set.add(mc);
					m.rev_args.put(arr2[k], set);
				}
				
				mcs.add(mc);
			}
			
			m.args.put(name, mcs);
		}
	}
}
