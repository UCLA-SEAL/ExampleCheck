package edu.ucla.cs.process.traditional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import edu.ucla.cs.utils.FileUtils;

public class CatastrophicBacktrackingProcess {
	public static HashSet<String> troubledLines = new HashSet<String>();
	
	public static void main(String[] args) {
		Process p = new Process();
		String seq = "/home/troy/research/BOA/Maple/res/test/createNewFile-new.txt";
		try {
			File f_seq = new File(seq);
			String dir = f_seq.getParent();
			File output = new File(dir + File.separator + "large-output.txt");
			if(output.exists()) {
				output.delete();
				output.createNewFile();
			}
			
			p.s = new CatastrophicBacktrackingProcessor();
			p.processByLine(seq);
			
			// write to file
			try (FileWriter fw = new FileWriter(output, true)) {
				int size = Process.methods.keySet().size();
				int count = 0;
				for(String key : Process.methods.keySet()) {
					if(count < size - 1) {
						fw.append(key.replaceAll("\\!", " ** ") + "---" + Process.methods.get(key).seq + System.lineSeparator());
					} else {
						fw.append(key.replaceAll("\\!", " ** ") + "---" + Process.methods.get(key).seq);
					}
					count++;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		// remove sequences in the raw output that will induce catastrophic backtracking
		FileUtils.removeLines(seq, troubledLines);
		
		// make sure to exit and kill all the other threads
		System.exit(0);
	}
}
