package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.PredicateVerifier;
import edu.ucla.cs.utils.FileUtils;

public class ArrayListGet {
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/example/ArrayList.get/NO/large-sequence.txt";
		String seq_output = "/home/troy/research/BOA/example/ArrayList.get/NO/large-output.txt";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("get(1)");
		int size = FileUtils.countLines(seq_output);
		 
		// verify precondition
		PredicateVerifier pv2 = new PredicateVerifier(raw_output, seq_output, pattern);
		int count = pv2.verify("get(1)", "arg0 < rcv.size()");
		double r2 = ((double) count) / size;
		System.out.println("precondition threshold: " + r2);		
	}
}
