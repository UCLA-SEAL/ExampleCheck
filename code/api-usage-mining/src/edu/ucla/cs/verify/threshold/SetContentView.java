package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.PredicateVerifier;
import edu.ucla.cs.mine.SequencePatternVerifier;
import edu.ucla.cs.utils.FileUtils;

public class SetContentView {
	public static void main(String[] args) {
		String seq_output = "/home/troy/research/BOA/example/Activity.setContentView/Small/small-output.txt";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("setContentView(1)");
		int size = FileUtils.countLines(seq_output);
				
		// verify sequence
		pattern.add(0, "onCreate(1)");
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(seq_output);
		double r1 = ((double) pv.support.size()) / size;
		System.out.println("sequence threshold: " + r1);
		pattern.remove(0);	
	}
}
