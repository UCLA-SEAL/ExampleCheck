package edu.ucla.cs.process.extension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import edu.ucla.cs.model.Method;

public class Process {
	public HashMap<String, Method> methods = new HashMap<String, Method>();
	public HashMap<String, HashMap<String, String>> types = new HashMap<String, HashMap<String, String>>();

	// use strategy design pattern
	public ProcessStrategy s;

	String input;

	public Process(String input) {
		this.input = input;
	}

	public void processByLine() throws IOException {
		File f = new File(input);
		String dir = f.getParent();
		File output = new File(dir + File.separator + "large-output(no-slicing).txt");

		// first construct the symbol table
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("vartypes[")) {
					String key = line.substring(line.indexOf("[") + 1,
							line.indexOf("] ="));
					String s = line.substring(line.indexOf("] =") + 3).trim();
					String[] ss = s.split("\\|");
					HashMap<String, String> map = new HashMap<String, String>();
					// skip the first element because it is empty string
					for (int i = 1; i < ss.length; i++) {
						String name = ss[i].split(":")[0];
						String type = ss[i].split(":")[1];
						map.put(name, type);
					}
					types.put(key, map);
				}
			}
		}

		// then construct the method call sequence
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				// process each line based on the strategy
				if (line.startsWith("results[")) {
					// set a threshold to avoid processing long methods
					if (line.length() < 3000) {
						s.process(line);
						System.out.println("hit!");
					}
				}

				// spill methods to the disk if there are over 1000 methods in
				// the hash map
				if (methods.size() >= 1000) {
					try (FileWriter fw = new FileWriter(output, true)) {
						for (String k : methods.keySet()) {
							fw.append(k.replaceAll("\\!", " ** ") + "---"
									+ methods.get(k).seq
									+ System.lineSeparator());
						}
					}
					methods.clear();
				}
			}
		}
	}

	public void process() {
		try {
			File f_seq = new File(input);
			String dir = f_seq.getParent();
			File output = new File(dir + File.separator + "large-output(no-slicing).txt");

			if (output.exists()) {
				output.delete();
				output.createNewFile();
			}

			this.s = new SequenceProcessor(this);
			processByLine();

			// write to file
			try (FileWriter fw = new FileWriter(output, true)) {
				int size = methods.keySet().size();
				int count = 0;
				for (String key : methods.keySet()) {
					if (count < size - 1) {
						fw.append(key.replaceAll("\\!", " ** ") + "---"
								+ methods.get(key).seq + System.lineSeparator());
					} else {
						fw.append(key.replaceAll("\\!", " ** ") + "---"
								+ methods.get(key).seq);
					}
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// make sure to exit and kill all the other threads
		// System.exit(0);
	}

	public static void main(String[] args) {
		String seq = "/media/troy/Disk2/Boa/apis/JFrame.setVisible/1.txt";
		Process p = new Process(seq);
		p.process();
	}
}
