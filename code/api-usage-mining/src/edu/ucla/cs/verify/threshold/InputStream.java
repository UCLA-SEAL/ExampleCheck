package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.SequencePatternVerifier;

public class InputStream {
	public static void main(String[] args) {
		String seq_output = "/home/troy/research/BOA/example/InputStream.read/1/large-output.txt";
		ArrayList<String> pattern1 = new ArrayList<String>();
		pattern1.add("read(1)");
		pattern1.add("close(0)");
						
		// verify sequence
		SequencePatternVerifier pv1 = new SequencePatternVerifier(pattern1);
		pv1.verify(seq_output);
		double r1 = ((double) pv1.support.size()) / pv1.seqs.size();
		System.out.println("sequence threshold: " + r1);		
	}
}
