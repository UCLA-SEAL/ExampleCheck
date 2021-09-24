package edu.ucla.cs.evaluate.sensitivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.mine.PatternMiner;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class Get {
	public static void main(String[] args) throws IOException {
		String raw_output = "/home/troy/research/BOA/Maple/example/HashMap.get/large-sequence.txt";
		String path = "/home/troy/research/BOA/Maple/example/HashMap.get/large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> query = new HashSet<String>();
		query.add("get");
		queries.add(query);
		Sample sam = new Sample(path);
//		int[] sizes = {50, 100, 500, 1000, 5000, 10000};
		int[] sizes = {100, 500, 2000};
//		double[] supports = {0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1};
		double[] supports = {0.6, 0.5, 0.4};
		for(int size : sizes) {
			if(size <= sam.seqs.size()) {
				// sample
				ArrayList<String> sample = sam.sample(size);
				String output = path.substring(0, path.lastIndexOf(".")) + "-sample-" + size + ".txt";
				File f = new File(output);
				if(!f.exists()) {
					f.createNewFile();
				}
				FileUtils.writeArrayToFile(sample, output);
				
				for(double support : supports) {
					// mine patterns with different support
					Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = PatternMiner.mine(raw_output, output, queries, support, size, support);
					System.out.println("Setting: size=" + size + " support=" + support);
					for(ArrayList<APISeqItem> pattern : patterns.keySet()) {
						System.out.println(pattern + ":" + patterns.get(pattern));
					}
				}
			}
		}
	}

}
