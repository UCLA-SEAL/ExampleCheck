package edu.ucla.cs.process.traditional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.utils.FileUtils;

public class ExampleCleanser {
	
	public HashMap<String, ArrayList<String>> readAPISeqeunces(String path){
		HashMap<String, ArrayList<String>> seqs = new HashMap<String, ArrayList<String>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))){
			String line;
			while((line = br.readLine()) != null) {
				if(line.contains("---")){
					String id = line.split("---")[0];
					String s = line.split("---")[1];
					s = s.substring(1, s.length() - 1);
					ArrayList<String> seq = new ArrayList<String>();
					for(String api : s.split(",")){
						seq.add(api.trim());
					}
					seqs.put(id, seq);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return seqs;
	}
	
	public void clean(String path, ArrayList<String> query){
		HashSet<String> set = verify(path, query);
		
		// remove the sequences that do not follow the query
		FileUtils.removeLinesById(path, set);
	}
	
	public void cleanAll(String path, ArrayList<ArrayList<String>> queries){
		HashSet<String> set = new HashSet<String>();
		
		if(queries.isEmpty()) {
			return;
		}
		
		ArrayList<String> query = queries.get(0);
		set = verify(path, query);
		
		for(int i = 1; i < queries.size(); i++) {
			query = queries.get(i);
			HashSet<String> temp = verify(path, query);
			set.retainAll(temp);
		}
		
		// remove the sequences that do not follow the query
		FileUtils.removeLinesById(path, set);
	}
	
	private HashSet<String> verify(String path, ArrayList<String> query) {
		HashMap<String, ArrayList<String>> seqs = readAPISeqeunces(path);
		
		if(query.isEmpty()) {
			return new HashSet<String>();
		}
		
		HashSet<String> set = new HashSet<String>();
		ArrayList<String> temp = new ArrayList<String>(query);
		for(String id : seqs.keySet()){
			ArrayList<String> seq = seqs.get(id); 
			for(String api : seq){
				String s = temp.get(0);
				if(api.equals(s)) {
					temp.remove(0);
					if(temp.isEmpty()) {
						break;
					}
				}
			}
			
			if(!temp.isEmpty()) {
				set.add(id);
			}
			
			// reset
			temp.clear();
			temp.addAll(query);
		}
		
		return set;
	}
	
	public static void main(String[] args) {
		String path = "/media/troy/Disk2/Boa/apis/FileChannel.write/large-output-resolved.txt";
		ArrayList<String> query1 = new ArrayList<String>();
		query1.add("write(ByteBuffer)");
//		query1.add("getInt(0)");
//		ArrayList<String> query2 = new ArrayList<String>();
//		query2.add("new SimpleDateFormat(2)");
//		query2.add("new String(2)");
//		ArrayList<ArrayList<String>> queries = new ArrayList<ArrayList<String>>();
//		queries.add(query1);
//		queries.add(query2);
		ExampleCleanser ec = new ExampleCleanser();
		ec.clean(path, query1);
//		ec.cleanAll(path, queries);
	}
}
