package edu.ucla.cs.process.extension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.ucla.cs.utils.FileUtils;

public class CatastrophicBacktrackingProcess {
	public String input;
	
	public CatastrophicBacktrackingProcess(String boaOutput) {
		input = boaOutput;
	}
	
	public void process() {
		Process p = new Process(input);
		try {
			File f_seq = new File(input);
			String dir = f_seq.getParent();
			File output = new File(dir + File.separator + "large-output(no-slicing).txt");
			if(output.exists()) {
				output.delete();
				output.createNewFile();
			}
			
			p.s = new CatastrophicBacktrackingProcessor(p);
			p.processByLine();
			
			// write to file
			try (FileWriter fw = new FileWriter(output, true)) {
				int size = p.methods.keySet().size();
				int count = 0;
				for(String key : p.methods.keySet()) {
					if(count < size - 1) {
						fw.append(key.replaceAll("\\!", " ** ") + "---" + p.methods.get(key).seq + System.lineSeparator());
					} else {
						fw.append(key.replaceAll("\\!", " ** ") + "---" + p.methods.get(key).seq);
					}
					count++;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		// remove sequences in the raw output that will induce catastrophic backtracking
		FileUtils.removeLines(input, ((CatastrophicBacktrackingProcessor)p.s).troubledLines);
		
		// make sure to exit and kill all the other threads
//		System.exit(0);
	}
	
	public static void main(String[] args) {
		String seq = "/media/troy/Disk2/Boa/apis/Iterator.next/1.txt";
		CatastrophicBacktrackingProcess p = new CatastrophicBacktrackingProcess(seq);
		p.process();
		System.exit(0);
	}
}
