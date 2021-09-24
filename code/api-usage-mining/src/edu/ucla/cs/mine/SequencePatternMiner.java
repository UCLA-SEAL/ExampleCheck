package edu.ucla.cs.mine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class SequencePatternMiner {
	final private String script_path;
	protected final String seqs_path;
	final private int min_support;
	//ArrayList<String> query;
	HashSet<HashSet<String>> queries;
	public HashMap<ArrayList<String>, Integer> patterns;
	
	public SequencePatternMiner(String script_path, String seqs_path, int min_support, HashSet<HashSet<String>> filters){
		this.script_path = script_path;
		this.seqs_path = seqs_path;
		this.min_support = min_support;
		this.queries = filters;
		this.patterns = new HashMap<ArrayList<String>, Integer>();
	}
	
	protected String run() {
		String output = "";
		try {
			Process p = Runtime.getRuntime().exec("python " + script_path + " " + seqs_path + " " + min_support);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String s = null;
			while((s = stdInput.readLine()) != null) {
				output += s + System.lineSeparator();
			}
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
	
	public HashMap<ArrayList<String>, Integer> mine(){
		String output = run();
		process(output);
		filter();
		return this.patterns;
	}
	
	/**
	 * Check whether the pattern has balanced parentheses
	 * @param pattern
	 * @return
	 */
	protected boolean isBalanced(ArrayList<String> pattern) {
		int count = 0;
		for(String s : pattern) {
			if(count < 0) return false;
			
			if(s.contains("{")) {
				count ++;
			} else if (s.contains("}")) {
				count --;
			}
		}
		
		if(count != 0) return false;
		else return true;
	}
	
	/**
	 * Check whether the pattern has lingering catch or else blocks
	 * @param pattern
	 * @return
	 */
	protected boolean isLingering(ArrayList<String> pattern) {
		for(int i = 0; i < pattern.size(); i++) {
			String item = pattern.get(i);
			if(item.equals("ELSE {") || item.contains("CATCH")) {
				if(i == 0) {
					return true;
				} else {
					String prev = pattern.get(i-1);
					if(!prev.equals("}")) {
						return true;
					}
				}
			} 
		}
		
		return false;
	}
	
	/**
	 * Check whether the pattern has complete try catch
	 * 
	 * @param pattern
	 * @return
	 */
	protected boolean isComplete(ArrayList<String> pattern) {
		int count = 0;
		for(String s : pattern) {
			if(count < 0) return false;
			
			if(s.contains("TRY")) {
				count ++;
			} else if (s.contains("CATCH")) {
				count --;
			}
		}
		
		if(count != 0) return false;
		else return true;
	}
	
	abstract protected void filter(); 
	
	abstract protected void process(String patterns);
}
