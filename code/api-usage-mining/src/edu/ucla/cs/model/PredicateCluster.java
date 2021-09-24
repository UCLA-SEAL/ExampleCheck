package edu.ucla.cs.model;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class PredicateCluster {
	public Multiset<String> cluster;
	public String shortest;
	
	public PredicateCluster(String predicate, int count) {
		cluster = HashMultiset.create();
		cluster.add(predicate, count);
		shortest = predicate;
	}
	
	/**
	 * 
	 * Merge two predicate clusters
	 * 
	 * @param p1
	 * @param p2
	 */
	public PredicateCluster(PredicateCluster p1, PredicateCluster p2) {
		cluster = HashMultiset.create();
		cluster.addAll(p1.cluster);
		for(String p : p2.cluster.elementSet()) {
			if(cluster.contains(p)) {
				// the same predicate can be merged to different cluster in previous iteration
				// avoid adding such predicate redundantly
				continue;
			} else {
				cluster.add(p, p2.cluster.count(p));
			}
		}
		
		shortest = p1.shortest.length() > p2.shortest.length() ? p2.shortest : p1.shortest;
		
		if(shortest.equals("!(true")) {
			shortest = p1.shortest.equals("!(true") ? p2.shortest : p1.shortest;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PredicateCluster) {
			return cluster.equals(((PredicateCluster)obj).cluster);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return cluster.hashCode();
	}
	
	@Override
	public String toString() {
		return cluster.toString() + System.lineSeparator() + "Shortest: " + shortest; 
	}
}
