package edu.ucla.cs.evaluate.k.no;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.mine.PatternMiner;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class TypedArrrayGetString {
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/example/TypedArray.getString/NO/large-sequence.txt";
		String seq = "/home/troy/research/BOA/example/TypedArray.getString/NO/large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("getString(1)");
		queries.add(q1);
		int size = FileUtils.countLines(seq);
		Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = PatternMiner.mine(
				raw_output, seq, queries, 0.2, size, 0.5);
		for (ArrayList<APISeqItem> sp : patterns.keySet()) {
			System.out.println(sp + ":" + patterns.get(sp));
		}
	}
}
