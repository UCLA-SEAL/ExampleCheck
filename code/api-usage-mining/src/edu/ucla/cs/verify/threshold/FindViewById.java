package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.SequencePatternVerifier;
import edu.ucla.cs.utils.FileUtils;

public class FindViewById {
	public static void main(String[] args) {
		String seq_output = "/home/troy/research/BOA/example/Activity.findViewById/3/large-output.txt";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("findViewById(1)");
		pattern.add("IF {");
		pattern.add("}");
		int size = FileUtils.countLines(seq_output);
				
		// verify sequence
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(seq_output);
		double r1 = ((double) pv.support.size()) / size;
		System.out.println("sequence threshold: " + r1);
		pattern.remove(0);		
	}
}
