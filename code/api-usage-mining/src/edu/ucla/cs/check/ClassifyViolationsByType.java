package edu.ucla.cs.check;

import java.io.File;
import java.util.ArrayList;

public class ClassifyViolationsByType {
	// four categories
	int exception_handling = 0;
	int missing_finally = 0;
	int missing_if = 0;
	int guard_condition = 0;
	int api_call = 0;

	public void classifyViolations(String rootPath) {
		File rootDir = new File(rootPath);
		for (File apiDir : rootDir.listFiles()) {
			String logPath = apiDir.getAbsolutePath() + File.separator
					+ "violations.txt";
			File logFile = new File(logPath);
			if (logFile.exists()) {
				ArrayList<String> violations = SampleViolations
						.readViolationsFromFile(logPath);
				for (String v : violations) {
					if(v.contains("TRY") || v.contains("CATCH")) {
						exception_handling ++;
					}
					
					if(v.contains("FINALLY")) {
						missing_finally ++;
					}
					
					if(v.contains("IF")) {
						missing_if ++;
					}
					
					if(v.contains("IncorrectPrecondition")) {
						guard_condition ++;
					}
					
					if(v.contains("MethodCall")) {
						api_call ++;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ClassifyViolationsByType classifier = new ClassifyViolationsByType();

		// read violations
		String rootPath = "/media/troy/Disk2/Boa/apis";
		classifier.classifyViolations(rootPath);
		
		System.out.println("Missing Exception Handling:" + classifier.exception_handling);
		System.out.println("Missing Finally:" + classifier.missing_finally);
		System.out.println("Missing If Check:" + classifier.missing_if);
		System.out.println("Incorrect Precondition:" + classifier.guard_condition);
		System.out.println("Missing or Disordered Precondition:" + classifier.api_call);
	}
}
