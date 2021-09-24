package edu.ucla.cs.mine;

import java.util.ArrayList;
import java.util.HashMap;

public class SequencePatternVerifier {
	public HashMap<String, ArrayList<String>> seqs;
	public ArrayList<String> pattern;
	public HashMap<String, ArrayList<String>> support;
	
	public SequencePatternVerifier(ArrayList<String> pattern) {
		// remove CATCH(Exception) so that it can be matched with any specific exception types
		if(pattern.contains("CATCH(Exception) {")) {
			pattern.remove("CATCH(Exception) {");
		}
		this.pattern = pattern;
		this.seqs = new HashMap<String, ArrayList<String>>();
		this.support = new HashMap<String, ArrayList<String>>();
	}
	
	public void verify(String path){
		seqs = PatternUtils.readAPISequences(path);
		
		if(pattern.isEmpty()) {
			for(String id : seqs.keySet()) {
				support.put(id, seqs.get(id));
			}
			
			return;
		}
		
		ArrayList<String> temp = new ArrayList<String>(pattern);
		for(String id : seqs.keySet()){
			ArrayList<String> seq = seqs.get(id); 
			for(String api : seq){
				String s = temp.get(0);
				if(api.equals(s)) {
					temp.remove(0);
					if(temp.isEmpty()) {
						support.put(id, seq);
						break;
					}
				}
			}
			
			// reset
			temp.clear();
			temp.addAll(pattern);
		}
	}
	
	public void verify2(String path){
		seqs = PatternUtils.readOnlyOneSequenceFromEachProject(path);
		
		if(pattern.isEmpty()) {
			for(String id : seqs.keySet()) {
				support.put(id, seqs.get(id));
			}
			
			return;
		}
		
		ArrayList<String> temp = new ArrayList<String>(pattern);
		for(String id : seqs.keySet()){
			ArrayList<String> seq = seqs.get(id); 
			for(String api : seq){
				String s = temp.get(0);
				if(api.equals(s)) {
					temp.remove(0);
					if(temp.isEmpty()) {
						support.put(id, seq);
						break;
					}
				}
			}
			
			// reset
			temp.clear();
			temp.addAll(pattern);
		}
	}
	
	public static void main(String[] args){
		String output = "/media/troy/Disk2/Boa/apis/ByteBuffer.get/large-output-resolved.txt";
		ArrayList<String> pattern = new ArrayList<String>();
//		pattern.add("show(0)");
//		pattern.add("flip()");
		pattern.add("get(byte[])");
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(output);
		System.out.println(pv.support.size());
//		for(String id : pv.seqs.keySet()) {
//			if(!pv.support.containsKey(id)) {
//				// print violations
//				System.out.println(id);
//				System.out.println(pv.seqs.get(id));
//			}
//		}
	}
}
