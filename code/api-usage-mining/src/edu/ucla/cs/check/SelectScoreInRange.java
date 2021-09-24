package edu.ucla.cs.check;

import java.io.File;
import java.io.IOException;

import edu.ucla.cs.utils.FileUtils;

public class SelectScoreInRange {
	public static void main(String[] args) throws IOException {
		String csvPath = "/media/troy/Disk2/Boa/score.csv";
		String output = "/media/troy/Disk2/Boa/score_100.csv";
		File fout = new File(output);
		if(fout.exists()) fout.delete();
		fout.createNewFile();
		String s = FileUtils.readFileToString(csvPath);
		String[] ss = s.split(System.lineSeparator());
		for(String line : ss) {
			if(line.trim().isEmpty()) continue;
			String[] arr = line.split(",");
			int score = Integer.parseInt(arr[0]);
			int ratio = Integer.parseInt(arr[1]);
			if(score < 100) {
				FileUtils.appendStringToFile(score + "," + ratio + System.lineSeparator(), output);
			}
		}
	}
}
