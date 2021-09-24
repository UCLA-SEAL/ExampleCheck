package edu.ucla.cs.evaluate.k.no;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.mine.PatternMiner;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class FileMkdirs {
	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/example/File.mkdir/NO/large-sequence.txt";
		String seq = "/home/troy/research/BOA/example/File.mkdir/NO/large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("mkdirs(0)");
		HashSet<String> q2 = new HashSet<String>();
		q2.add("mkdir(0)");
		queries.add(q1);
		queries.add(q2);
		int size = FileUtils.countLines(seq);
		Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = PatternMiner.mine(
				raw_output, seq, queries, 0.5, size, 0.5);
		for (ArrayList<APISeqItem> sp : patterns.keySet()) {
			System.out.println(sp + ":" + patterns.get(sp));
		}
	} 
}
