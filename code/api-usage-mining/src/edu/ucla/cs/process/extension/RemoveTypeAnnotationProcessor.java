package edu.ucla.cs.process.extension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class RemoveTypeAnnotationProcessor {
	String input;

	public RemoveTypeAnnotationProcessor(String input) {
		this.input = input;
	}

	public void process() {
		String output = input.substring(0, input.lastIndexOf('.'))
				+ "-clean.txt";
		File fin = new File(input);
		File fout = new File(output);
		if (fout.exists()) {
			fout.delete();
		}

		HashMap<String, HashMap<String, String>> oracle = new HashMap<String, HashMap<String, String>>();
		try (BufferedReader br = new BufferedReader(new FileReader(fin))) {
			String line = null;
			// build the oracle
			while ((line = br.readLine()) != null) {
				if (line.startsWith("vartypes[")) {
					String key = line.substring(line.indexOf("[") + 1,
							line.indexOf("] ="));
					String s = line.substring(line.indexOf("] =") + 3).trim();
					String[] ss = s.split("\\|");
					HashMap<String, String> map = new HashMap<String, String>();
					// skip the first element because it is empty string
					for (int i = 1; i < ss.length; i++) {
						String name = ss[i].split(":")[0];
						String type = ss[i].split(":")[1];
						map.put(name, type);
					}
					oracle.put(key, map);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// then construct the method call sequence
		try (BufferedReader br = new BufferedReader(new FileReader(fin))) {
			FileWriter fw = new FileWriter(output, true);
			String line = null;
			while ((line = br.readLine()) != null) {
				// process each line based on the strategy
				if (line.startsWith("results[")) {
					String key = line.substring(line.indexOf("[") + 1, line.indexOf("][SEQ]"));
					String seq = line.substring(line.indexOf("] =") + 3).trim();
					if (oracle.containsKey(key)) {
						HashMap<String, String> map = oracle.get(key);
						for (String var : map.keySet()) {
							String annotatedType = var + ":" + map.get(var);
							if(var.contains("$")) {
								continue;
							}
							if (line.contains(annotatedType)) {
								seq = seq.replaceAll(Pattern.quote(annotatedType),
										var);
							}
						}
						fw.append("results[" + key + "][SEQ] = " + seq + System.lineSeparator());
					}
				}
			}
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		for(File apiDir : rootDir.listFiles()) {
//			if(!apiDir.getName().equals("Activity.setContentView")) continue;
			String input = apiDir.getAbsolutePath() + File.separator + "NO.txt";
			// check whether it has been processed
			String cleanedInput = apiDir.getAbsolutePath() + File.separator + "NO-clean.txt";
			if(new File(cleanedInput).exists()) continue;
			System.out.println("Removing type annotations in " + apiDir.getName());
			RemoveTypeAnnotationProcessor p = new RemoveTypeAnnotationProcessor(input);
			p.process();
		}
	}
}
