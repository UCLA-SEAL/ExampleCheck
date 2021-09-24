package edu.ucla.cs.check;

import java.io.File;

import edu.ucla.cs.utils.FileUtils;

public class CalcMiningTime {
	public static void main(String[] args) {
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		int allTime = 0;
		for(File f : rootDir.listFiles()) {
			String logPath = f.getAbsolutePath() + File.separator + "patterns.txt";
			File logFile = new File(logPath);
			if(logFile.exists()) {
				String s = FileUtils.readFileToString(logPath);
				String[] ss = s.split(System.lineSeparator());
				int time = 0;
				double min_threshold = 1;
				for(String line : ss) {
					if(line.startsWith("Mining time (millis) :")) {
						String tmp = line.substring(line.indexOf(':') + 1).trim();
						time = Integer.parseInt(tmp);
					} else if (line.startsWith("[")) {
						String tmp = line.substring(line.indexOf("]:(") + 3).trim();
						tmp = tmp.substring(0, tmp.indexOf(",")).trim();
						double d = Double.parseDouble(tmp);
						if(d < min_threshold) {
							min_threshold = d;
						}
					}
				}
				int iteration = 1;
				if(min_threshold < 0.5) {
					double diff = 0.5 - min_threshold;
					iteration = (int) (diff / 0.1);
				}
				
				allTime += iteration * time;
			}
		}
		System.out.println("Average mining time : " + allTime/30);
	}
}
