package edu.ucla.cs.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.mine.PatternMiner;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.Answer;
import edu.ucla.cs.model.Violation;
import edu.ucla.cs.search.Search;
import edu.ucla.cs.utils.FileUtils;

public class Maple {
	HashSet<String> types;
	HashSet<ArrayList<String>> apis;
	String raw_output;
	String seq;
	double sigma;
	double theta;
	
	public Maple(HashSet<String> types, HashSet<ArrayList<String>> apis, String raw_output, String seq, double seq_threshold, double pred_threshold) {
		this.types = types;
		this.apis = apis;
		this.raw_output = raw_output;
		this.seq = seq;
		this.sigma = seq_threshold;
		this.theta = pred_threshold;
	}
	
	public void run() {
		// 1. find relevant code snippets given one or more APIs
		Search search = new Search();
		HashSet<Answer> answers = search.search(types, apis);
		
		int count = 0;
		for(Answer answer : answers) {
			count += answer.seq.keySet().size();
		}
		
		System.out.println("Total number of relevant Java snippets: " + count);

		// 2. mine sequence patterns and predicate patterns from Github
		// TODO: currently we assume that we have already retrieved code
		// examples from Github, but later on we need to also do it
		// programatically
		// TODO: we also assume that the raw output from BOA has been
		// pre-processed, e.g., sliced for the lightweight approach, formatted
		// for the traditional approach
		HashSet<HashSet<String>> sets = new HashSet<HashSet<String>>();
		for(ArrayList<String> l : apis) {
			sets.add(new HashSet<String>(l));
		}
		Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = PatternMiner.mine(raw_output, seq, sets, sigma, FileUtils.countLines(seq), theta);
		HashSet<ArrayList<APISeqItem>> set = new HashSet<ArrayList<APISeqItem>>();
		set.addAll(patterns.keySet());
		
		// 3. detect API usage anomalies
		HashMap<Answer, ArrayList<Violation>> violations = Utils.detectAnomaly(
				answers, set);
		
		// 4. classify these API usage anomalies
		Utils.classify(violations);
	}
}
