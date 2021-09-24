package edu.ucla.cs.mine;

import java.util.ArrayList;

import edu.ucla.cs.model.PredicateCluster;
import edu.ucla.cs.utils.SAT;

public class ExtendedPredicateVerifier {
	ExtendedPredicateMiner pm;
	
	public ExtendedPredicateVerifier (String raw_output, String seq_output, ArrayList<String> query) {
		pm = new ExtendedPredicateMiner(query, raw_output, seq_output);
		pm.process();
	}
	
	public int verify(String api, String pattern) {
		int count = 0;
		SAT sat = new SAT();
		if(pm.clusters.containsKey(api)) {
			ArrayList<PredicateCluster> cset = pm.clusters.get(api);
			for(PredicateCluster c : cset) {
				String p = c.shortest;
				if(sat.checkImplication(p, pattern)) {
					if(c.cluster.size() > count) {
						count = c.cluster.size();
					}
				}
			}
		}
		
		return count;
	}
	
	public static void main(String[] args) {
		String raw = "/media/troy/Disk2/Boa/apis/String.substring/1-clean.txt";
		String seq = "/media/troy/Disk2/Boa/apis/String.substring/large-output-resolved.txt";
		ArrayList<String> query = new ArrayList<String>();
		query.add("substring(int,int)");
		ExtendedPredicateVerifier pv = new ExtendedPredicateVerifier(raw, seq, query);
		int num = pv.verify("substring(int,int)", "arg0<=rcv.length()");
		System.out.println(num);
	}
}
