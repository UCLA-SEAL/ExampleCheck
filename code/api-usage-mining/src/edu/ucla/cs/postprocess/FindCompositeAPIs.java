package edu.ucla.cs.postprocess;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.ucla.cs.mine.PatternUtils;

/**
 * @author Tianyi Zhang
 * 
 * Find GitHub code fragments that use APIs in different domains
 *
 */
public class FindCompositeAPIs {
	public static void main(String[] args) {
		// load APIs in different domains
		String csvFile = "res/API-domains.csv";
		HashMap<String, String> api_domains = loadAPIsInDifferentDomains(csvFile);
		
		// read API call sequences and check if they contain APIs in different domains
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		for(File apiDir : rootDir.listFiles()) {
			String name = apiDir.getName();
			System.out.println("Searching in " + name);
			String seq_path = apiDir.getAbsolutePath() + File.separator + "large-output-resolved.txt";
			HashMap<String, ArrayList<String>> sequences = PatternUtils.readAPISequences(seq_path);
			HashSet<String> domains = new HashSet<String>();
			for(String key : sequences.keySet()) {
				ArrayList<String> sequence = sequences.get(key);
				for(String item : sequence) {
					if(item.contains("(")) {
						
					}
				}
			}
		}
	}
	
	private static HashMap<String, String> loadAPIsInDifferentDomains(String path) {
		HashMap<String, String> domains = new HashMap<String, String>();

		try {
			File file = new File(path);
			List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
			for(int i = 1; i < lines.size(); i++) {
				String line = lines.get(i);
				String[] ss = line.split(",");
				if(ss.length != 3) continue;
				String className = ss[0];
				String methodName = ss[1];
				String domain = ss[2];
				domains.put(methodName, domain);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return domains;
	}
}
