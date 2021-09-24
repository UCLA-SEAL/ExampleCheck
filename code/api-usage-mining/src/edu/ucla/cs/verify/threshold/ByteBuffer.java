package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.SequencePatternVerifier;
import edu.ucla.cs.utils.FileUtils;

public class ByteBuffer {
	public static void main(String[] args) {
		String seq_output = "/home/troy/research/BOA/example/ByteBuffer.getInt/2/large-output.txt";
		ArrayList<String> pattern0 = new ArrayList<String>();
		pattern0.add("put(1)");
		pattern0.add("getInt(0)");
		ArrayList<String> pattern1 = new ArrayList<String>();
		pattern1.add("put(1)");
		pattern1.add("flip(0)");
		pattern1.add("getInt(0)");
		ArrayList<String> pattern2 = new ArrayList<String>();
		pattern2.add("put(1)");
		pattern2.add("rewind(0)");
		pattern2.add("getInt(0)");
		ArrayList<String> pattern3 = new ArrayList<String>();
		pattern3.add("put(1)");
		pattern3.add("position(1)");
		pattern3.add("getInt(0)");
		
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
		SequencePatternVerifier pv3 = new SequencePatternVerifier(pattern3);
		pv3.verify(seq_output);
		double r3 = ((double) pv3.support.size()) / size;
		System.out.println("sequence threshold: " + r3);		
	}
}
