package edu.ucla.cs.process.extension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CountResolvedSequences {
	public static void main(String[] args) {
		String input = "/media/troy/Disk2/Boa/apis/SwingUtilities.invokeLater/large-output-resolved.txt";
		File f = new File(input);
		int count = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(f))){
			String line = null;
		    while ((line = br.readLine()) != null) {
		    	if(line.contains("---[")) {
		    		String seq = line.substring(line.indexOf("---[") + 4, line.lastIndexOf(']'));
		    		if(!seq.contains("*")) {
		    			count ++;
		    		}
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(count);
	}
}
