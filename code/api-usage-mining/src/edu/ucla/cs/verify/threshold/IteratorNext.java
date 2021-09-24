package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.PredicateVerifier;
import edu.ucla.cs.mine.SequencePatternVerifier;
import edu.ucla.cs.utils.FileUtils;

public class IteratorNext {
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/example/Iterator.next/Small/small-sequence.txt";
		String seq_output = "/home/troy/research/BOA/example/Iterator.next/Small/small-output.txt";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("next(0)");
		int size = FileUtils.countLines(seq_output);
				
		// verify sequence
		pattern.add("add(1)");
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(seq_output);
		double r1 = ((double) pv.support.size()) / size;
		System.out.println("sequence threshold: " + r1);
		pattern.remove(1);
		 
		// verify precondition
		PredicateVerifier pv2 = new PredicateVerifier(raw_output, seq_output, pattern);
		int count = pv2.verify("next(0)", "rcv.hasNext()");
		double r2 = ((double) count) / size;
		System.out.println("precondition threshold: " + r2);			
	}
}
