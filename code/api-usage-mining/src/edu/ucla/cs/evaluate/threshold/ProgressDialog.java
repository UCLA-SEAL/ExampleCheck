package edu.ucla.cs.evaluate.threshold;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.mine.PatternMiner;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class ProgressDialog {
	public static void main(String[] args) {
		String seq_output = "/home/troy/research/BOA/Maple/example/ProgressDialog.dismiss/large-output.txt";
		int size = FileUtils.countLines(seq_output);
		String raw_output = "/home/troy/research/BOA/Maple/example/ProgressDialog.dismiss/large-sequence.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> query = new HashSet<String>();
		query.add("dismiss(0)");
		queries.add(query);
		double[] supports1 = {0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1};
		double[] supports2 = {0.3, 0.2, 0.1, 0.05};
		for(double sigma : supports1) {
			for(double theta : supports2) {
				// mine patterns with different support
				Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = PatternMiner.mine(raw_output, seq_output, queries, sigma, size, theta);
				System.out.println("Setting: sigma=" + sigma + " theta=" + theta);
				for(ArrayList<APISeqItem> pattern : patterns.keySet()) {
					System.out.println(pattern + ":" + patterns.get(pattern));
				}
			}
		}
	}
}
