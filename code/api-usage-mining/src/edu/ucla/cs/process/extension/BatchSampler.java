package edu.ucla.cs.process.extension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.mine.ExtendedPatternMiner;
import edu.ucla.cs.mine.PatternUtils;
import edu.ucla.cs.model.APISeqItem;

public class BatchSampler {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String outputPath = "/media/troy/Disk2/Boa/sample";
		File outputDir = new File(outputPath);
		if(!outputDir.exists() || !outputDir.isDirectory()) {
			outputDir.mkdirs();
		}
		
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		for(File apiDir : rootDir.listFiles()) {
			String apiName = apiDir.getName();
//			if(!apiName.equals("JsonElement.getAsString")) continue;
			new File(outputDir.getAbsolutePath() + File.separator + apiName).mkdirs();
			
			// read previously learned API usage patterns from the patterns.txt and then sample
			Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = new HashMap<ArrayList<APISeqItem>, MutablePair<Double,Double>>();
			File pFile = new File(apiDir.getAbsolutePath() + File.separator + "patterns.txt");
			if(pFile.exists()) {
				try(BufferedReader br = new BufferedReader(new FileReader(pFile))) {
					String line;
					int numOfExamples = 0;
					while((line = br.readLine()) != null) {
						if(line.startsWith("Total Code Examples:")) {
							numOfExamples = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());
						} else if (line.startsWith("[")){
							String s1 = line.split(":")[0];
							s1 = s1.substring(1, s1.length() - 1);
							String s2 = line.split(":")[1];
							s2 = s2.substring(1, s2.length() - 1);
							Double d1 = Double.parseDouble(s2.split(",")[0]);
							Double d2 = Double.parseDouble(s2.split(",")[1]);
							patterns.put(PatternUtils.convertStringToExtendedPattern(s1), new MutablePair<Double, Double>(d1, d2));
						}
					}
					
					// all patterns have been read into the hashmap object, now sample
					ExtendedPatternMiner.sample(apiDir.getAbsolutePath() + File.separator + "large-output-resolved.txt", 
							apiDir.getAbsolutePath() + File.separator + "1-clean.txt", 
							numOfExamples, 
							patterns, 
							outputDir.getAbsolutePath() + File.separator + apiName, 
							100);
				}
			}
		}
	}
}
