package edu.ucla.cs.check;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ResultAggregate {
	public static void main(String[] args) {
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		
		int totalPosts = 0;
		int unreliablePosts = 0;
		int recognizedPosts = 0;
		int unreliableRecognizedPosts = 0;
		HashMap<String, Double> recognized = new HashMap<String, Double>();
		HashMap<String, Double> unrecognized = new HashMap<String, Double>();
		HashMap<String, Double> all = new HashMap<String, Double>();
		for(File f : rootDir.listFiles()) {
			String logPath = f.getAbsolutePath() + File.separator + "violations.txt";
			File logFile = new File(logPath);
			if(logFile.exists()) {
				try(BufferedReader br = new BufferedReader(new FileReader(logFile))) {
					String line = null;
					int total = 0;
					int unreliable = 0;
					int totalRecognized = 0;
					int unreliableRecognized = 0;
					while((line = br.readLine()) != null) {
						if(line.startsWith("Total number of relevant")) {
							String tmp = line.substring(line.indexOf(':') + 1).trim();
							total = Integer.parseInt(tmp);
							totalPosts += total;
						} else if (line.startsWith("Total number of unreliable")) {
							String tmp = line.substring(line.indexOf(':') + 1).trim();
							unreliable = Integer.parseInt(tmp);
							unreliablePosts += unreliable;
						} else if (line.startsWith("Total number of recognized")) {
							String tmp = line.substring(line.indexOf(':') + 1).trim();
							totalRecognized = Integer.parseInt(tmp);
							recognizedPosts += totalRecognized;
						} else if (line.startsWith("Unreliable recognized")) {
							String tmp = line.substring(line.indexOf(':') + 1).trim();
							unreliableRecognized = Integer.parseInt(tmp);
							unreliableRecognizedPosts += unreliableRecognized;
						}
					}
					
					recognized.put(f.getName(), ((double) unreliableRecognized)/totalRecognized);
					unrecognized.put(f.getName(), ((double) (unreliable - unreliableRecognized))/ (total - totalRecognized));
					all.put(f.getName(),((double) unreliable)/total);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Total number of SO posts: " + totalPosts);
		System.out.println("Number of unreliable SO posts: " + unreliablePosts);
		System.out.println("Ratio: " + ((double)unreliablePosts)/totalPosts);
		System.out.println("Total number of Recognized SO posts: " + recognizedPosts);
		System.out.println("Number of unreliable but recognized SO posts: " + unreliableRecognizedPosts);
		System.out.println("Ratio: " + ((double)unreliableRecognizedPosts)/recognizedPosts);
		System.out.println("Total number of unrecognized SO posts: " + (totalPosts - recognizedPosts));
		System.out.println("Number of unreliable but unrecognized SO posts: " + (unreliablePosts - unreliableRecognizedPosts));
		System.out.println("Ratio: " + ((double)(unreliablePosts - unreliableRecognizedPosts))/(totalPosts - recognizedPosts));
		
		// print to the r script format
		String r1 = "Recog <- c(";
		for(String api : recognized.keySet()) {
			r1 += (int) (recognized.get(api) * 100) + ",";
		}
		r1 = r1.substring(0, r1.length() - 1);
		r1 += ")";
		System.out.println(r1);
		
		String r2 = "Unrecog <- c(";
		for(String api : unrecognized.keySet()) {
			r2 += (int) (unrecognized.get(api) * 100) + ",";
		}
		r2 = r2.substring(0, r2.length() - 1);
		r2 += ")";
		System.out.println(r2);
		
		String r3 = "Overall <- c(";
		for(String api : all.keySet()) {
			r3 += (int) (all.get(api) * 100) + ",";
		}
		r3 = r3.substring(0, r3.length() - 1);
		r3 += ")";
		System.out.println(r3);
	}
}
