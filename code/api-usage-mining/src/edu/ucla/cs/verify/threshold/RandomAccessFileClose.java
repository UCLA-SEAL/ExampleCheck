package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.SequencePatternVerifier;
import edu.ucla.cs.utils.FileUtils;

public class RandomAccessFileClose {
	public static void main(String[] args) {
		String seq_output = "/home/troy/research/BOA/example/RandomAccessFile.close/NO/large-output.txt";
		ArrayList<String> pattern1 = new ArrayList<String>();
		pattern1.add("FINALLY {");
		pattern1.add("close(0)");
		pattern1.add("}");
		
		int size = FileUtils.countLines(seq_output);
				
		// verify sequence
		SequencePatternVerifier pv1 = new SequencePatternVerifier(pattern1);
		pv1.verify(seq_output);
		double r1 = ((double) pv1.support.size()) / size;
		System.out.println("sequence threshold: " + r1);		
	}
}
