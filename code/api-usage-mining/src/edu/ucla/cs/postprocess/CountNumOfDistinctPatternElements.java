package edu.ucla.cs.postprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class CountNumOfDistinctPatternElements {
	
	public static void main(String[] args) {
		String path = "/home/troy/research/BOA/patterns";
		File root = new File(path);
		for(File f : root.listFiles()) {
			if(f.isDirectory()) {
				String s = f.getAbsolutePath() + File.separator + "pattern.txt";
				File pFile = new File(s);
				if(pFile.exists()) {
					HashSet<String> set = new HashSet<String>();
					try (BufferedReader br = new BufferedReader(new FileReader(pFile))) {
						String line;
						while((line = br.readLine()) != null) {
							if(line.contains(":[") && line.contains("]:")) {
								String pattern = line.substring(line.indexOf(":[") + 2, line.indexOf("]:"));
								String[] es = pattern.split(", ");
								for(String e : es) {
									if(e.contains("@")) {
										e = e.substring(0, e.indexOf('@'));
									} else if (e.equals("END_BLOCK")) {
										continue;
									}
									set.add(e);
								}
							}
						}
						System.out.println(set.size());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
 			}
		}
	}
}