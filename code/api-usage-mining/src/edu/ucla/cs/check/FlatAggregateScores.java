package edu.ucla.cs.check;

import edu.ucla.cs.utils.FileUtils;

public class FlatAggregateScores {
	public static void main(String[] args) {
		String csvPath = "/media/troy/Disk2/Boa/unreliable_score.csv";
		String s = FileUtils.readFileToString(csvPath);
		for(String line : s.split(System.lineSeparator())) {
			if(!line.trim().isEmpty()) {
				String tmp = line.trim().split(",")[0];
				Integer score = Integer.parseInt(tmp);
				tmp = line.trim().split(",")[1];
				Integer count = Integer.parseInt(tmp);
				for(int i = 0; i < count; i++) {
					FileUtils.appendStringToFile(score+System.lineSeparator(), "/media/troy/Disk2/Boa/unreliable_score_flatten.csv");
				}
			}
		}
	}
}
