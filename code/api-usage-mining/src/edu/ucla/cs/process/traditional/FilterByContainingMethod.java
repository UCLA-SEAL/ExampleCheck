package edu.ucla.cs.process.traditional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import edu.ucla.cs.utils.FileUtils;

public class FilterByContainingMethod {
	private String extractMethod(String line) {
		String key = line.substring(line.indexOf("[") + 1, line.indexOf("][SEQ]"));
		return getMethodName(key);
	}
	
	private String getMethodName(String key){
		String[] ss = key.split("\\!");
		return ss[3].trim();
	}
	
	public void filter(String path, String method) {
		HashSet<String> removes = new HashSet<String>();
		File f = new File(path);
		try (BufferedReader br = new BufferedReader(new FileReader(f))){
			String line = null;
		    while ((line = br.readLine()) != null) {
		        //process each line based on the strategy
		    	if(line.startsWith("results[")) {
		    		String m = extractMethod(line);
		    		if(m.equals(method)) {
		    			continue;
		    		} else {
		    			removes.add(line);
		    		}
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileUtils.removeLines(path, removes);
	}
	
	public static void main(String[] args) {
		String path = "/home/troy/research/BOA/Maple/example/Activity.setContentView/3/large-sequence.txt";
		FilterByContainingMethod f = new FilterByContainingMethod();
		f.filter(path, "onCreate");
	}
}
