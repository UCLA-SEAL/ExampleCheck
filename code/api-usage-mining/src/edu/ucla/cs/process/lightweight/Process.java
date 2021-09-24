package edu.ucla.cs.process.lightweight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Process {
	// use strategy design pattern
	public ProcessStrategy s;
	
	public void processByLine(String path) throws IOException {
		File f = new File(path);
		try (BufferedReader br = new BufferedReader(new FileReader(f))){
			String line = null;
		    while ((line = br.readLine()) != null) {
		        //process each line based on the strategy
		        s.process(line);
		    }      
		}
	}
}
