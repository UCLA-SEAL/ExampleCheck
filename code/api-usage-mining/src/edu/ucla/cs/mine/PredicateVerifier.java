package edu.ucla.cs.mine;

import java.util.ArrayList;

import edu.ucla.cs.model.PredicateCluster;
import edu.ucla.cs.utils.SAT;

public class PredicateVerifier{
	TraditionalPredicateMiner pm;
	
	public PredicateVerifier (String raw_output, String seq_output, ArrayList<String> query) {
		pm = new TraditionalPredicateMiner(query, raw_output, seq_output);
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
		String raw = "/home/troy/research/BOA/Maple/example/ProgressDialog.dismiss/large-sequence.txt";
		String seq = "/home/troy/research/BOA/Maple/example/ProgressDialog.dismiss/large-output.txt";
		ArrayList<String> query = new ArrayList<String>();
		query.add("dismiss(0)");
		PredicateVerifier pv = new PredicateVerifier(raw, seq, query);
		int num = pv.verify("dismiss(0)", "rcv.isShowing()");
		System.out.println(num);
	}
}
