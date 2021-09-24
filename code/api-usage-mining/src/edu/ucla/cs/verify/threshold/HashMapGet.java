package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.PredicateVerifier;
import edu.ucla.cs.mine.SequencePatternVerifier;
import edu.ucla.cs.utils.FileUtils;

public class HashMapGet {
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/example/HashMap.get/Small/small-sequence.txt";
		String seq_output = "/home/troy/research/BOA/example/HashMap.get/Small/small-output.txt";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("get(1)");
		int size = FileUtils.countLines(seq_output);
			
		// verify sequence
		pattern.add("IF {");
		pattern.add("}");
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(seq_output);
		double r1 = ((double) pv.support.size()) / size;
		System.out.println("sequence threshold: " + r1);
		pattern.remove(2);
		pattern.remove(1);
		 
		// verify precondition
		PredicateVerifier pv2 = new PredicateVerifier(raw_output, seq_output, pattern);
		int count = pv2.verify("get(1)", "rcv.containsKey(arg0,)");
		double r2 = ((double) count) / size;
		System.out.println("precondition threshold: " + r2);			
	}
}
