package edu.ucla.cs.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.Answer;
import edu.ucla.cs.model.Violation;
import edu.ucla.cs.model.ViolationType;
import edu.ucla.cs.utils.FileUtils;

public class Utils {
	/**
	 * 
	 * Group violations based on their types
	 * 
	 * @param violations
	 */
	public static void classify(
			HashMap<Answer, ArrayList<Violation>> violations) {
		HashSet<Answer> miss_structure = new HashSet<Answer>();
		HashSet<Answer> disorder_structure = new HashSet<Answer>();
		HashSet<Answer> miss_api = new HashSet<Answer>();
		HashSet<Answer> disorder_api = new HashSet<Answer>();
		HashSet<Answer> wrong_precondition = new HashSet<Answer>();
		for(Answer a : violations.keySet()) {
			for(Violation v : violations.get(a)) {
				if(v.type.equals(ViolationType.MissingStructure)) {
					miss_structure.add(a);
				} else if (v.type.equals(ViolationType.DisorderStructure)) {
					disorder_structure.add(a);
				} else if (v.type.equals(ViolationType.MissingMethodCall)) {
					miss_api.add(a);
				} else if (v.type.equals(ViolationType.DisorderMethodCall)) {
					disorder_api.add(a);
				} else if (v.type.equals(ViolationType.IncorrectPrecondition)) {
					wrong_precondition.add(a);
				}
			}
		}
		
		System.out.println("Missing Control-flow Structure: " + miss_structure.size());
		System.out.println("Disorder Control-flow Structure: " + disorder_structure.size());
		System.out.println("Missing API Call: " + miss_api.size());
		System.out.println("Disorder API Call: " + disorder_api.size());
		System.out.println("Incorrect Predicates: " + wrong_precondition.size());
	}
	
	public static void classify(
			HashMap<Answer, ArrayList<Violation>> violations, String output) {
		HashSet<Answer> miss_structure = new HashSet<Answer>();
		HashSet<Answer> disorder_structure = new HashSet<Answer>();
		HashSet<Answer> miss_api = new HashSet<Answer>();
		HashSet<Answer> disorder_api = new HashSet<Answer>();
		HashSet<Answer> wrong_precondition = new HashSet<Answer>();
		for(Answer a : violations.keySet()) {
			for(Violation v : violations.get(a)) {
				if(v.type.equals(ViolationType.MissingStructure)) {
					miss_structure.add(a);
				} else if (v.type.equals(ViolationType.DisorderStructure)) {
					disorder_structure.add(a);
				} else if (v.type.equals(ViolationType.MissingMethodCall)) {
					miss_api.add(a);
				} else if (v.type.equals(ViolationType.DisorderMethodCall)) {
					disorder_api.add(a);
				} else if (v.type.equals(ViolationType.IncorrectPrecondition)) {
					wrong_precondition.add(a);
				}
			}
		}
		
		FileUtils.appendStringToFile("Missing Control-flow Structure: " + miss_structure.size() + System.lineSeparator(), output);
		FileUtils.appendStringToFile("Disorder Control-flow Structure: " + disorder_structure.size() + System.lineSeparator(), output);
		FileUtils.appendStringToFile("Missing API Call: " + miss_api.size() + System.lineSeparator(), output);
		FileUtils.appendStringToFile("Disorder API Call: " + disorder_api.size() + System.lineSeparator(), output);
		FileUtils.appendStringToFile("Incorrect Predicates: " + wrong_precondition.size() + System.lineSeparator(), output);
	}
	
	/**
	 * 
	 * use the pattern to check for the code snippets in the answers
	 * 
	 * @param snippets
	 * @param pattern
	 * @return
	 */
	public static HashMap<Answer, ArrayList<Violation>> detectAnomaly(
			HashSet<Answer> snippets, HashSet<ArrayList<APISeqItem>> patterns) {
		HashMap<Answer, ArrayList<Violation>> violations = new UseChecker().check(patterns, snippets);
//		int buggy_snippet_count = 0;
//		for(Answer a : violations.keySet()) {
//			buggy_snippet_count += a.buggy_seq_count;
//		}
//		System.out.println("Total number of unreliable Java snippets: " + buggy_snippet_count);
		System.out.println("Total number of unreliable Java snippets: " + violations.keySet().size());
		
		for(Answer a : violations.keySet()) {
			System.out.println("Answer Id --- http://stackoverflow.com/questions/" + a.id);
			for(Violation v : violations.get(a)) {
				System.out.println("Violation: " + v.type + ", " + v.item);
			}
		}
		return violations;
	}
	
	/**
	 * Check whether the second sequence is a subseqeunce of the first one.
	 * 
	 * @param seq1
	 * @param seq2
	 * @return
	 */
	public static boolean isSubsequence(ArrayList<String> seq1, ArrayList<String> seq2){
		int pos1 = 0;
		int pos2 = 0;
		for (; pos1 < seq1.size() && pos2 < seq2.size();) {
			if(seq1.get(pos1).equals(seq2.get(pos2))) {
				pos2 ++;
			} 
			pos1++;
		}
		
		return pos2 == seq2.size();
	}
}
