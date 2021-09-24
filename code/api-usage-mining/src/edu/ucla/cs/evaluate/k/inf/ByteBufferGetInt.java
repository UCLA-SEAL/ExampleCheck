package edu.ucla.cs.evaluate.k.inf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.mine.PatternMiner;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class ByteBufferGetInt {
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/example/ByteBuffer.getInt/INF/large-sequence.txt";
		String seq = "/home/troy/research/BOA/example/ByteBuffer.getInt/INF/large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("put(1)");
		q1.add("getInt(0)");
		queries.add(q1);
		int size = FileUtils.countLines(seq);
		Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = PatternMiner.mine(
				raw_output, seq, queries, 0.3, size, 0.5);
		for (ArrayList<APISeqItem> sp : patterns.keySet()) {
			System.out.println(sp + ":" + patterns.get(sp));
		}
		PatternMiner.sample(seq, raw_output, size, patterns, "/home/troy/research/BOA/patterns/ByteBuffer.getInt", 10);
	}
}
