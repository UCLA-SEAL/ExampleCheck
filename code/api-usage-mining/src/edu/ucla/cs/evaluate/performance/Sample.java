package edu.ucla.cs.evaluate.performance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.ucla.cs.utils.FileUtils;

public class Sample {
	ArrayList<String> seqs;
	String path;
	
	public Sample(String path) {
		this.path = path;
	}
	
	public void sample(int size) {
		String sample_output = path.substring(0, path.lastIndexOf(".")) + "-" + size + ".txt";
		File f = new File(sample_output);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			// write the first given number of examples to the output file
			// if there are less examples than the given size, repeat the existing examples
			FileUtils.writeFirstNLinesToFileWithPadding(path, size, sample_output);
		}
	}
} 
