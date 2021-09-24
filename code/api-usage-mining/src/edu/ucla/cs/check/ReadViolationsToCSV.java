package edu.ucla.cs.check;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.mine.PatternUtils;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class ReadViolationsToCSV {
	
	
	
	public static void main(String[] args) {
		// read patterns first 
		HashMap<String, HashSet<ArrayList<APISeqItem>>> required_patterns;
		HashMap<String, HashSet<ArrayList<APISeqItem>>> alternative_patterns;
		
		String s = FileUtils.readFileToString("/media/troy/Disk2/Boa/valid_patterns.tsv");
		String[] ss = s.split(System.lineSeparator());
		required_patterns = new HashMap<String, HashSet<ArrayList<APISeqItem>>>();
		alternative_patterns = new HashMap<String, HashSet<ArrayList<APISeqItem>>>();
		for(int i = 1; i < ss.length; i++) {
			// skip the first line since it is header
			String line = ss[i];
			String[] cells = line.split("\t");
			String className = cells[0].trim();
			String methodName = cells[1].trim();
			String api = className + "." + methodName;
			if(cells.length == 2) {
				// no patterns
				required_patterns.put(api, new HashSet<ArrayList<APISeqItem>>());
			} else {
				String pattern = cells[3].trim();
				ArrayList<APISeqItem> p = PatternUtils.convertStringToPattern(pattern);
				int isRequired = Integer.parseInt(cells[6].trim());
				HashSet<ArrayList<APISeqItem>> pset;
				if(isRequired == 1) {
					// this is a required pattern
					if(required_patterns.containsKey(api)) {
						pset = required_patterns.get(api);
					} else {
						pset = new HashSet<ArrayList<APISeqItem>>();
					}
					pset.add(p);
					required_patterns.put(api, pset);
				} else {
					// this is one of the alternative patterns
					if(alternative_patterns.containsKey(api)) {
						pset = alternative_patterns.get(api);
					} else {
						pset = new HashSet<ArrayList<APISeqItem>>();
					}
					pset.add(p);
					alternative_patterns.put(api, pset);
				}
			}
		}
		
		// read violations
		String csvPath = "/media/troy/Disk2/Boa/violations.tsv";
		File csv = new File(csvPath);
		if(csv.exists()) csv.delete();
		try {
			csv.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		ArrayList<String> violations = new ArrayList<String>();
		int count = 0;
		for (File apiDir : rootDir.listFiles()) {
			String api = apiDir.getName();
			String logPath = apiDir.getAbsolutePath() + File.separator
					+ "violations.txt";
			File logFile = new File(logPath);
			if (logFile.exists()) {
				ArrayList<String> links = SampleViolations.readViolationsFromFile(logPath);
				count += links.size();
				
				HashSet<ArrayList<APISeqItem>> rp = required_patterns.get(api);
				HashSet<ArrayList<APISeqItem>> ap = alternative_patterns.get(api);
				HashSet<ArrayList<APISeqItem>> all = new HashSet<ArrayList<APISeqItem>>();
				if(rp != null) {
					all.addAll(rp);
				}
				if(ap != null) {
					all.addAll(ap);
				}
				
				if(all.size() == 1) {
					// only one pattern
					ArrayList<APISeqItem> pattern = null;
					if(rp != null) pattern = rp.iterator().next();
					if(ap != null) pattern = ap.iterator().next();
					
					for(String link : links) {
						violations.add(api + "\t" + link.split("\t")[0] + "\t" + pattern + "\t" + link.split("\t")[1]);
					}
				} else {
					for(String link : links) {
						// check for the violations and try to find out which pattern this post violates
						String tmp = link.split("\t")[1];
						String[] vs = tmp.split("@");
						String violatedPatterns = "";
						for(String v : vs) {
							if(v.contains("LOOP")) {
								// missing or disorder loop
								for(ArrayList<APISeqItem> p : all) {
									if(p.toString().contains("LOOP")) {
										violatedPatterns += p.toString() + " | ";
									}
								}
							} else if (v.contains("IncorrectPrecondition")) {
								// incorrect precondition
								for(ArrayList<APISeqItem> p : all) {
									boolean flag = false; 
									for(APISeqItem i : p) {
										if(i instanceof APICall) {
											String cond = ((APICall) i).condition;
											if(!cond.trim().equals("true")) {
												flag = true;
												break;
											}
										}
									}
									
									if(flag) {
										violatedPatterns += p.toString() + " | ";
									}
								}
							} else if (v.contains("CATCH") || v.contains("TRY")) {
								// missing or disordered try
								for(ArrayList<APISeqItem> p : all) {
									if(p.toString().contains("TRY")) {
										violatedPatterns += p.toString() + " | ";
									}
								}
							} else if (v.contains("Method")) {
								// missing or disordered call
								String call = v.substring(v.indexOf("MethodCall,") + 11).trim();
								for(ArrayList<APISeqItem> p : all) {
									if(p.toString().contains(call)) {
										violatedPatterns += p.toString() + " | ";
									}
								}
							} else if (v.contains("IF")) {
								// missing or disordered if
								for(ArrayList<APISeqItem> p : all) {
									if(p.toString().contains("IF")) {
										violatedPatterns += p.toString() + " | ";
									}
								}
							} else if (v.contains("FINALLY")) {
								// missing or disordered if
								for(ArrayList<APISeqItem> p : all) {
									if(p.toString().contains("FINALLY")) {
										violatedPatterns += p.toString() + " | ";
									}
								}
							}
						}
						violations.add(api + "\t" + link.split("\t")[0] + "\t" + violatedPatterns +  "\t" + link.split("\t")[1]);
					}
				}
			}
		}
		
		for(String violation : violations) {
			FileUtils.appendStringToFile(violation + System.lineSeparator(), csvPath);
		}
		
		System.out.println(count);
	}
}
