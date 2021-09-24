package edu.ucla.cs.process.traditional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.slice.Slicer;

public class Process {
		public static HashMap<String, Method> methods = new HashMap<String, Method>();
		
		// use strategy design pattern
		public ProcessStrategy s;
		
		public void processByLine(String path) throws IOException {
			File f = new File(path);
			String dir = f.getParent();
			File output = new File(dir + File.separator + "large-output.txt");
			
			try (BufferedReader br = new BufferedReader(new FileReader(f))){
				String line = null;
			    while ((line = br.readLine()) != null) {
			        //process each line based on the strategy
			    	if(line.startsWith("results[")) {
			    		s.process(line);
			    		System.out.println("hit!");
			    	}
			    	
			    	// spill methods to the disk if there are over 1000 methods in the hash map
					if(Process.methods.size() >= 1000) {
						try (FileWriter fw = new FileWriter(output, true)) {
							for(String k : Process.methods.keySet()) {
								fw.append(k.replaceAll("\\!", " ** ") + "---" + Process.methods.get(k).seq + System.lineSeparator());
							}
						}
						Process.methods.clear();
					}
			    }
			}
		}
		
		/**
		 * Cross-check the result of the traditional analysis with the light-weight analysis
		 */
		public void crosscheck() {
			Slicer.setup();
			
			Set<String> set1 = Slicer.methods.keySet();
			Set<String> set2 = methods.keySet();
			System.out.println("Seqeunces detected by the lightweight apporach but not the traditional approach");
			for(String s : set1) {
				s = s.replaceAll(" \\*\\* ", "!");
				if(!set2.contains(s)) {
					System.out.println(s);
				}
			}
			System.out.println("Seqeunces detected by the traditional apporach but not the lightweight approach");
			for(String s : set2) {
				s = s.replaceAll("\\!", " ** ");
				if(!set1.contains(s)) {
					System.out.println(s);
				}
			}
		}
		
		public static void main(String[] args) {
			Process p = new Process();
			String seq = "/home/troy/research/BOA/example/RandomFileAccess.write/NO/large-sequence.txt";
			try {
				File f_seq = new File(seq);
				String dir = f_seq.getParent();
				File output = new File(dir + File.separator + "/large-output.txt");
				
				if(output.exists()) {
					output.delete();
					output.createNewFile();
				}
				
				p.s = new SequenceProcessor();
				p.processByLine(seq);
				
				// cross-check
				// p.crosscheck();
				
				// print the split method calls
//				for(String key : methods.keySet()) {
//					System.out.println(key.replaceAll("\\!", " ** ") + "---" + methods.get(key).seq);
//				}
				
				// write to file
				try (FileWriter fw = new FileWriter(output, true)) {
					int size = methods.keySet().size();
					int count = 0;
					for(String key : methods.keySet()) {
						if(count < size - 1) {
							fw.append(key.replaceAll("\\!", " ** ") + "---" + methods.get(key).seq + System.lineSeparator());
						} else {
							fw.append(key.replaceAll("\\!", " ** ") + "---" + methods.get(key).seq);
						}
						count++;
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			// make sure to exit and kill all the other threads
			System.exit(0);
		}
}
