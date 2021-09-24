package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.PredicateVerifier;
import edu.ucla.cs.mine.SequencePatternVerifier;
import edu.ucla.cs.utils.FileUtils;

public class SortedMap {
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/example/SortedMap.firstKey/1/large-sequence.txt";
		String seq_output = "/home/troy/research/BOA/example/SortedMap.firstKey/1/large-output.txt";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("firstKey(0)");
		int size = FileUtils.countLines(seq_output);
				
		// verify sequence
		pattern.add(0, "put(2)");
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(seq_output);
		double r1 = ((double) pv.support.size()) / size;
		System.out.println("sequence threshold: " + r1);
		pattern.remove(0);
		 
		// verify precondition
		PredicateVerifier pv2 = new PredicateVerifier(raw_output, seq_output, pattern);
		int count = pv2.verify("firstKey(0)", "!rcv.isEmpty()");
		double r2 = ((double) count) / size;
		System.out.println("precondition threshold: " + r2);
		
		
		PredicateVerifier pv3 = new PredicateVerifier(raw_output, seq_output, pattern);
		int count3 = pv3.verify("firstKey(0)", "rcv.size() > 0");
		double r3 = ((double) count3) / size;
		System.out.println("precondition threshold: " + r3);
	}
}
