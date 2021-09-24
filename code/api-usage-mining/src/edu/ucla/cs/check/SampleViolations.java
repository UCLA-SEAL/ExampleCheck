package edu.ucla.cs.check;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import edu.ucla.cs.utils.FileUtils;

public class SampleViolations {
	public static void main(String[] args) { 
		File csv = new File("/media/troy/Disk2/Boa/sample.csv");
		if(csv.exists()) csv.delete();
		try {
			csv.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String previousSamplePath = "/media/troy/Disk2/Boa/sample(200).tsv";
		HashSet<String> prevSampleSet = readSampleFromTSV(previousSamplePath);
		
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		ArrayList<String> violations = new ArrayList<String>();
		for (File apiDir : rootDir.listFiles()) {
			String api = apiDir.getName();
			String logPath = apiDir.getAbsolutePath() + File.separator
					+ "violations.txt";
			File logFile = new File(logPath);
			if (logFile.exists()) {
				ArrayList<String> links = SampleViolations.readViolationsFromFile(logPath);
				for(String link : links) {
					violations.add(api + "\t" + link);
				}
			}
		}

		// create two samples, one sample includes 200 posts with violations
		// from all 100 APIs and the other includes those posts in the first
		// sample but only from 25 example APIs
		Random random = new Random();
		int count = 0;
		int size = violations.size();
		while(count < 200) {
			int n = random.nextInt(size);
			String sample = violations.get(n);
			String[] elems = sample.split("\t");
			String api = elems[0];
			String link = elems[1];
			String postId = link.substring(link.lastIndexOf("/") + 1);
			if(prevSampleSet.contains(api + "-" + postId)) {
				// skip this sample because it has been sampled before
				continue;
			}
			FileUtils.appendStringToFile(sample + System.lineSeparator(), csv.getAbsolutePath());
			count ++;
		}
	}
	
	public static HashSet<String> readSampleFromTSV(String path) {
		File sample = new File(path);
		HashSet<String> sampleSet = new HashSet<String>();
		if(sample.exists()) {
			String s = FileUtils.readFileToString(path);
			String[] ss = s.split(System.lineSeparator());
			// skip the header row
			for(int i = 1; i < ss.length; i++) {
				String line = ss[i];
				String[] elems = line.split("\t");
				String api = elems[0];
				String link = elems[1];
				String postId = link.substring(link.lastIndexOf("/") + 1);
				sampleSet.add(api + "-" + postId);
			}
		}
		
		return sampleSet;
	}
	
	public static ArrayList<String> readViolationsFromFile(String path) {
		ArrayList<String> links = new ArrayList<String >();
		String s = FileUtils.readFileToString(path);
		String[] ss = s.split(System.lineSeparator());
		for(int i = 0; i < ss.length; i++) {
			String line = ss[i];
			if(line.startsWith("Answer Id")) {
				String link = line.split("---")[1].trim();
				String violations = "";
				for(int j = i+1; j<ss.length; j++) {
					String next = ss[j];
					if(next.startsWith("Violation:")) {
						violations += next.substring(next.indexOf(':') + 1) + "@";
					} else {
						i=j-1;
						break;
					}
				}
				links.add(link + "\t" + violations);
			}
		}
		
		return links;
	}
}
