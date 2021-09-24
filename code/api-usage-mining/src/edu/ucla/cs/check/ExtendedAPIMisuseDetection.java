package edu.ucla.cs.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.Answer;
import edu.ucla.cs.model.Violation;
import edu.ucla.cs.search.Search;
import edu.ucla.cs.utils.FileUtils;

public class ExtendedAPIMisuseDetection {
	HashSet<String> typeQuery;
	HashSet<ArrayList<String>> apiQueries;
	HashSet<HashSet<ArrayList<APISeqItem>>> pset;
	HashMap<Integer, Integer> score_totalPosts = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> score_unreliablePosts = new HashMap<Integer, Integer>();
	long view_totalPosts = 0;
	long view_unreliablePosts = 0;
	int totalPosts = 0;
	int unreliablePosts = 0;
	
	public ExtendedAPIMisuseDetection(HashSet<String> typeQuery, HashSet<ArrayList<String>> apiQueries, HashSet<HashSet<ArrayList<APISeqItem>>> pset) {
		this.typeQuery = typeQuery;
		this.apiQueries = apiQueries;
		this.pset = pset;
	}
	
	public void run(String output) {
		// 1. find relevant code snippets given one or more APIs
		Search search = new Search();
		HashSet<Answer> answers = search.search(typeQuery, apiQueries);
		
		FileUtils.appendStringToFile("Total number of relevant SO posts: " + answers.size() + System.lineSeparator(), output);
		totalPosts = answers.size();
		
		HashMap<Answer, ArrayList<Violation>> violations = new HashMap<Answer, ArrayList<Violation>>();
		for(HashSet<ArrayList<APISeqItem>> patterns : pset) {
			HashMap<Answer, ArrayList<Violation>> temp = new UseChecker().check(patterns, answers);
			for(Answer a : temp.keySet()) {
				if(violations.containsKey(a)) {
					ArrayList<Violation> arr = violations.get(a);
					arr.addAll(temp.get(a));
					violations.put(a, arr);
				} else {
					violations.put(a, temp.get(a));
				}
			}
		}
		
		FileUtils.appendStringToFile("Total number of unreliable SO posts: " + violations.keySet().size() + System.lineSeparator(), output);
		unreliablePosts = violations.keySet().size();
		
		for(Answer a : violations.keySet()) {
			FileUtils.appendStringToFile("Answer Id --- http://stackoverflow.com/questions/" + a.id + System.lineSeparator(), output);
			for(Violation v : violations.get(a)) {
				FileUtils.appendStringToFile("Violation: " + v.type + ", " + v.item + System.lineSeparator(), output);
			}
		}
		
		// 4. classify these API usage anomalies
		Utils.classify(violations, output);
		
		// 5. count unreliable ones for recognized answers
		int unreliable_recognized = 0;
		for(Answer a : violations.keySet()) {
			this.view_unreliablePosts += a.viewCount;
			
			if(a.isAccepted || a.score > 0) {
				unreliable_recognized ++;
			}
			int count = 0;
			if(score_unreliablePosts.containsKey(a.score)) {
				count = score_unreliablePosts.get(a.score) + 1; 
			} else {
				count = 1;
			}
			
			score_unreliablePosts.put(a.score, count);
		}
		
		int total_recognized = 0;
		int total_score = 0;
		for(Answer a : answers) {
			this.view_totalPosts += a.viewCount;
			
			if(a.isAccepted || a.score > 0) {
				total_recognized ++;
				total_score += a.score;
			}
			
			int count = 0;
			if(score_totalPosts.containsKey(a.score)) {
				count = score_totalPosts.get(a.score) + 1; 
			} else {
				count = 1;
			}
			
			score_totalPosts.put(a.score, count);
		}
		
		FileUtils.appendStringToFile("Total number of recognized posts: " + total_recognized + System.lineSeparator(), output);
		double avg = ((double) total_score) / total_recognized;
		FileUtils.appendStringToFile("Average score: " + avg + System.lineSeparator(), output);
		FileUtils.appendStringToFile("Unreliable recognized posts: " + unreliable_recognized + System.lineSeparator(), output);
		FileUtils.appendStringToFile("Total number of unrecognized posts: " + (answers.size() - total_recognized) + System.lineSeparator(), output);
		FileUtils.appendStringToFile("Unreliable unrecognized posts: " + (violations.keySet().size() - unreliable_recognized) + System.lineSeparator(), output);
	}
}
