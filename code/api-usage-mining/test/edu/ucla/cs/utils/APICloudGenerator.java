package edu.ucla.cs.utils;

public class APICloudGenerator {
	public static void main(String[] args) {
		String path = "/home/troy/Downloads/popularAPIs.csv";
		String output = "/home/troy/Downloads/APICloud.txt";
		String fContent = FileUtils.readFileToString(path);
		String[] lines = fContent.split(System.lineSeparator());
		for(String line : lines) {
			String apiName = line.split(",")[0];
			String className = apiName.split("\\.")[0];
			String count = line.split(",")[1];
			int countI = Integer.parseInt(count) / 1000;
			for(int i = 0; i < countI; i ++) {
				FileUtils.appendStringToFile(className + " ", output);
			}
		}
	}
}
