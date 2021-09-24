package edu.ucla.cs.verify.threshold;

import java.util.ArrayList;

import edu.ucla.cs.mine.PredicateVerifier;
import edu.ucla.cs.utils.FileUtils;

public class JsonElement {
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/example/JsonElement.getAsString/1/large-sequence.txt";
		String seq_output = "/home/troy/research/BOA/example/JsonElement.getAsString/1/large-output.txt";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("getAsString(0)");
		int size = FileUtils.countLines(seq_output);
				
		// verify precondition
		PredicateVerifier pv2 = new PredicateVerifier(raw_output, seq_output, pattern);
		int count = pv2.verify("getAsString(0)", "rcv.isJsonPrimitive()");
		double r2 = ((double) count) / size;
		System.out.println("precondition threshold: " + r2);		
	}
}
