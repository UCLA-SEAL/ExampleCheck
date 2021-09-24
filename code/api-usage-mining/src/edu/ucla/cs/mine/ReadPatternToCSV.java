package edu.ucla.cs.mine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.ucla.cs.utils.FileUtils;

public class ReadPatternToCSV {
	public static void main(String[] args) throws IOException {
		String csvPath = "/media/troy/Disk2/Boa/patterns.csv";
		File csvFile = new File(csvPath);
		if(csvFile.exists()) {
			csvFile.delete();
		}
		csvFile.createNewFile();
		
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		for(File f: rootDir.listFiles()) {
			String apiName = f.getName();
			String className = apiName.split("\\.")[0];
			String methodName = apiName.split("\\.")[1];
			File pFile = new File(f.getAbsolutePath() + File.separator + "patterns.txt");
			if(pFile.exists()) {
				try(BufferedReader br = new BufferedReader(new FileReader(pFile))) {
					String line;
					int count = 0;
					while((line = br.readLine()) != null) {
						if(line.startsWith("Total Code Examples:")) {
							count = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());
						} else if (line.startsWith("[")){
							String s1 = line.split(":")[0];
							s1 = s1.substring(1, s1.length() - 1);
							String s2 = line.split(":")[1];
							s2 = s2.substring(1, s2.length() - 1);
							Double d1 = Double.parseDouble(s2.split(",")[0]);
							Double d2 = Double.parseDouble(s2.split(",")[1]);
							int support = (int) Math.ceil(d1 * d2 * count);
							FileUtils.appendStringToFile(className + "\t" + methodName + "\t" + count + "\t" + s1 + "\t" + support + System.lineSeparator(),  csvPath);
						}
					}
				}
			} else {
				// no patterns learned
				FileUtils.appendStringToFile(className + "\t" + methodName + "\t" + System.lineSeparator(), csvPath);
			}
		}
	}
}
