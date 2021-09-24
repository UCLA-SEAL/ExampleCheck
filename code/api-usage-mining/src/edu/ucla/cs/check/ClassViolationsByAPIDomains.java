package edu.ucla.cs.check;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.cs.utils.FileUtils;

public class ClassViolationsByAPIDomains {
	public static void main(String[] args) {
		// read the domain of each API
		String domainCSV = "/media/troy/Disk2/Boa/domains.tsv";
		String s = FileUtils.readFileToString(domainCSV);
		String[] ss = s.split(System.lineSeparator());
		HashMap<String, String> domains = new HashMap<String, String>();
		for(int i = 1; i < ss.length; i++) {
			String line = ss[i];
			if(!line.trim().isEmpty()) {
				String[] cells = line.split("\t");
				String className = cells[0];
				String methodName = cells[1];
				String domain = cells[2];
				String api = className + "." + methodName;
				domains.put(api, domain);
			}
		}
		
		// read the violations of each API
		HashMap<String, Double> violations = new HashMap<String, Double>(); 
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		for (File apiDir : rootDir.listFiles()) {
			String logPath = apiDir.getAbsolutePath() + File.separator
					+ "violations.txt";
			File logFile = new File(logPath);
			if (logFile.exists()) {
				String api = apiDir.getName();
				int total = 0;
				int unreliable = 0;
				try (BufferedReader br = new BufferedReader(
						new FileReader(logFile))) {
					String line;
					while ((line = br.readLine()) != null) {
						if (line.startsWith("Total number of relevant")) {
							String tmp = line.substring(
									line.indexOf(':') + 1).trim();
							total = Integer.parseInt(tmp);
						} else if (line.startsWith("Total number of unreliable")) {
							String tmp = line.substring(line.indexOf(':') + 1).trim();
							unreliable = Integer.parseInt(tmp);
						}
					}
					
					double ratio = ((double) unreliable / total);
					if(total == 0) {
						System.out.println(api);
					}
					violations.put(api, ratio);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// classify by domains
		HashMap<String, ArrayList<Double>> violationsByDomains = new HashMap<String, ArrayList<Double>>();
		for(String api : domains.keySet()) {
			String domain = domains.get(api);
			double ratio = violations.get(api);
			ArrayList<Double> list;
			if(violationsByDomains.containsKey(domain)) {
				list = violationsByDomains.get(domain);
			} else {
				list = new ArrayList<Double>();
			}
			list.add(ratio);
			violationsByDomains.put(domain, list);
		}
		
		// print to R script format
		for(String domain : violationsByDomains.keySet()) {
			String r = domain.toLowerCase() + " <- c(";
			for(double ratio : violationsByDomains.get(domain)) {
				r += (int) (ratio * 100) + ",";
			}
			r = r.substring(0, r.length() - 1);
			r += ")";
			System.out.println(r);
		}
		
		for(String domain : violationsByDomains.keySet()) {
			System.out.print(violationsByDomains.get(domain).size() + ",");
		}
		
		for(String api : violations.keySet()) {
			if(domains.get(api).equals("Swing")) {
				System.out.println(api + " : " + violations.get(api));
			}
		}
				
	}
}
