package edu.ucla.cs.check;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.utils.FileUtils;

public class ClassifyViolationsByConsequences {
	// four categories
	int incomplete = 0;
	int exception = 0;
	int leak = 0;
	int other = 0;

	HashMap<String, HashSet<String>> consequences = new HashMap<String, HashSet<String>>();

	public void readConsequences(String patternPath) {
		String s = FileUtils.readFileToString(patternPath);
		String[] ss = s.split(System.lineSeparator());
		for (int i = 1; i < ss.length; i++) {
			// skip the first line since it is header
			String line = ss[i];
			String[] cells = line.split("\t");
			String className = cells[0].trim();
			String methodName = cells[1].trim();
			String api = className + "." + methodName;
			if (cells.length == 2) {
				// no patterns
			} else {
				String consequence = cells[7];
				HashSet<String> set;
				if (consequences.containsKey(api)) {
					set = consequences.get(api);
				} else {
					set = new HashSet<String>();
				}
				set.add(consequence);
				consequences.put(api, set);
			}
		}
	}

	public void classifyViolations(String rootPath) {
		File rootDir = new File(rootPath);
		for (File apiDir : rootDir.listFiles()) {
			String logPath = apiDir.getAbsolutePath() + File.separator
					+ "violations.txt";
			File logFile = new File(logPath);
			if (logFile.exists()) {
				String api = apiDir.getName();
				HashSet<String> set = consequences.get(api);
				if (set != null) {
					if (set.size() == 1) {
						String consequence = set.iterator().next();
						// add all the violations to this consequence
						try (BufferedReader br = new BufferedReader(
								new FileReader(logFile))) {
							String line;
							while ((line = br.readLine()) != null) {
								if (line.startsWith("Total number of unreliable")) {
									String tmp = line.substring(
											line.indexOf(':') + 1).trim();
									int count = Integer.parseInt(tmp);
									addCountBasedOnConsequence(count,
											consequence);
									break;
								}
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						// this becomes complex
						ArrayList<String> violations = SampleViolations
								.readViolationsFromFile(logPath);
						for (String v : violations) {
							if (set.contains("resource leaks")
									&& (v.contains("close")
											|| v.contains("recycle") || v
												.contains("FINALLY"))) {
								leak++;
							} 
							
							if (set.contains("exception")
									&& (v.contains("TRY")
											|| v.contains("CATCH")
											|| v.contains("IncorrectPrecondition") || v
												.contains("MissingStructure, IF"))) {
								exception++;
							}
							
							if (set.contains("incomplete")
									&& (v.contains("MissingMethodCall") || v.contains("DisorderMethodCall"))) {
								incomplete++;
							}
						}
					}
				} else {
					// no pattern exist so no violations
				}
			}
		}
		
		incomplete = 67533 - other - exception - leak;
	}

	private void addCountBasedOnConsequence(int count, String consequence) {
		if (consequence.equals("incomplete")) {
			incomplete += count;
		} else if (consequence.equals("exception")) {
			exception += count;
		} else if (consequence.equals("resource leaks")) {
			leak += count;
		} else {
			other += count;
		}
	}

	public static void main(String[] args) {
		ClassifyViolationsByConsequences classifier = new ClassifyViolationsByConsequences();
		// read each pattern with their severity
		String csvPath = "/media/troy/Disk2/Boa/valid_patterns.tsv";
		classifier.readConsequences(csvPath);

		// read violations
		String rootPath = "/media/troy/Disk2/Boa/apis";
		classifier.classifyViolations(rootPath);
		
		System.out.println("Incomplete:" + classifier.incomplete);
		System.out.println("Exception:" + classifier.exception);
		System.out.println("Resource leaks:" + classifier.leak);
		System.out.println("Other:" + classifier.other);
	}
}
