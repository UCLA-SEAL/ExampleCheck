package edu.ucla.cs.process.extension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Tianyi
 *
 * Replace * with concrete types by reconstructing symbol tables
 * 
 */
public class ResolveTypeProcessor {
	String input;
	
	public ResolveTypeProcessor(String input) {
		this.input = input;
	}
	
	public void process() {
		String output = input.substring(0, input.lastIndexOf('.')) + "-resolved.txt";
		File fin = new File(input);
		File fout = new File(output);
		if(fout.exists()) {
			fout.delete();
		}
		
		try(BufferedReader br = new BufferedReader(new FileReader(fin))) {
			String line = null;
			ArrayList<String> lines = new ArrayList<String>();
			// build the oracle
			HashMap<String, HashMap<String, Integer>> oracle = new HashMap<String, HashMap<String, Integer>>();
			while((line = br.readLine()) != null) {
				if(line.contains("---[")) {
					lines.add(line);
		    		String seq = line.substring(line.indexOf("---[") + 4, line.lastIndexOf(']'));
		    		String[] items = seq.split(", ");
		    		for(String item : items) {
		    			if(item.contains("(") && item.contains(")")) {
		    				String name = item.substring(0, item.indexOf('('));
		    				String sub = item.substring(item.indexOf('(') + 1, item.indexOf(')'));
		    				if(!sub.contains("*")) {
		    					HashMap<String, Integer> map;
		    					if(oracle.containsKey(name)) {
		    						map = oracle.get(name);
		    						if(map.containsKey(sub)) {
		    							map.put(sub, map.get(sub) + 1);
		    						} else {
		    							map.put(sub, 1);
		    						}
		    					} else {
		    						map = new HashMap<String, Integer>();
		    						map.put(sub, 1);
		    					}
		    					oracle.put(name, map);
		    				}
		    			}
		    		}
		    	}
			}
			
//			HashMap<String, Integer> hmap = new HashMap<String, Integer>();
//			hmap.put("Component,String,String,int", Integer.MAX_VALUE);
//			hmap.put("ECUEditor,String,String,int", Integer.MAX_VALUE-1);
//			hmap.put("Component,String,String,int,Icon", Integer.MAX_VALUE);
//			hmap.put("ECUEditor,String,String,int,Icon", Integer.MAX_VALUE-1);
//			oracle.put("showMessageDialog", hmap);
			
			// resolve the types
			for(int i = 0; i < lines.size(); i++) {
				String l = lines.get(i);
				String key = l.substring(0, l.indexOf("---["));
				String seq = l.substring(l.indexOf("---[") + 4, l.lastIndexOf(']'));
				String[] items = seq.split(", ");
	    		for(String item : items) {
	    			if(item.contains("(") && item.contains(")")) {
	    				String name = item.substring(0, item.indexOf('('));
	    				String sub = item.substring(item.indexOf('(') + 1, item.indexOf(')'));
	    				if(!sub.contains("*")) {
	    					continue;
	    				}
	    				String[] arguments = sub.split(",");
	    				if(oracle.containsKey(name)) {
	    					HashMap<String, Integer> map = oracle.get(name);
	    					HashSet<String> matches = new HashSet<String>();
	    					for(String signature : map.keySet()) {
	    						if(signature.trim().isEmpty()) continue;
	    						String[] ss = signature.split(",");
	    						if(ss.length == arguments.length) {
	    							boolean isMatch = true;
	    							for(int j = 0; j < arguments.length; j++) {
		    							String a = arguments[j];
		    							String b = ss[j];
		    							if(!a.equals("*") && !a.equals(b)) {
		    								// not match
		    								isMatch = false;
		    								break;
		    							}
		    						}
	    							
	    							if(isMatch) {
	    								matches.add(signature);
	    							}
	    						} 
	    					}
	    					
	    					// find the best match
	    					int max = 0;
	    					String best = null;
	    					for(String match : matches) {
	    						int count = map.get(match);
	    						if(count > max) {
	    							best = match;
	    							max = count;
	    						}
	    					}
	    					
	    					if(best != null) {
	    						// replace the unresolved signature with the best resolved one
	    						seq = seq.replace(name + "(" + sub + ")", name + "(" + best + ")");
	    					}
	    				}
	    			}
	    		}
	    		
	    		lines.remove(i);
	    		lines.add(i, key + "---[" + seq + "]");
			}
			
			// write the resolved sequences to the output file
			// write to file
			try (FileWriter fw = new FileWriter(output, true)) {
				int size = lines.size();
				int count = 0;
				for (String l : lines) {
					if (count < size - 1) {
						fw.append(l + System.lineSeparator());
					} else {
						fw.append(l);
					}
					count++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String input = "/media/troy/Disk2/Boa/apis/JFrame.setVisible/large-output.txt";
		ResolveTypeProcessor p =  new ResolveTypeProcessor(input);
		p.process();
	}
}
