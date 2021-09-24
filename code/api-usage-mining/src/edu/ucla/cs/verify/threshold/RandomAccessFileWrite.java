package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.SequencePatternVerifier;
import edu.ucla.cs.utils.FileUtils;

public class RandomAccessFileWrite {
	public static void main(String[] args) {
		String seq_output = "/home/troy/research/BOA/example/RandomAccessFile.write/1/large-output.txt";
		ArrayList<String> pattern1 = new ArrayList<String>();
		pattern1.add("write(1)");
		pattern1.add("close(0)");
		
		ArrayList<String> pattern2 = new ArrayList<String>();
		pattern2.add("TRY {");
		pattern2.add("close(0)");
		pattern2.add("}");
		
		int size = FileUtils.countLines(seq_output);
				
		// verify sequence
		SequencePatternVerifier pv1 = new SequencePatternVerifier(pattern1);
		pv1.verify(seq_output);
		double r1 = ((double) pv1.support.size()) / size;
		System.out.println("sequence threshold: " + r1);	
		
		SequencePatternVerifier pv2 = new SequencePatternVerifier(pattern2);
		pv2.verify(seq_output);
		double r2 = ((double) pv2.support.size()) / size;
		System.out.println("sequence threshold: " + r2);	
	}
}
