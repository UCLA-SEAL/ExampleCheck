package edu.ucla.cs.evaluate.threshold;

import java.util.ArrayList;
import java.util.HashSet;

import edu.ucla.cs.mine.FrequentSequenceMiner;
import edu.ucla.cs.mine.SequencePatternMiner;
import edu.ucla.cs.utils.FileUtils;

public class CreateNewFile {
	public static void main(String[] args) {
		String output = "/home/troy/research/BOA/Maple/example/File.createNewFile/large-output.txt";
		int num = FileUtils.countLines(output);
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> query = new HashSet<String>();
		query.add("createNewFile(0)");
		queries.add(query);
		double[] supports = {0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1};
		for(double support: supports) {
			SequencePatternMiner pm = new FrequentSequenceMiner("/home/troy/research/BOA/Maple/mining/freq_seq.py", 
					output,
					(int) (support * num),
					queries);

			pm.mine();
			
			System.out.println("Threshold=" + (int) (support * num));
			for(ArrayList<String> pattern : pm.patterns.keySet()) {
				System.out.println(pattern + ":" + pm.patterns.get(pattern));
			}
		}
	}
}
