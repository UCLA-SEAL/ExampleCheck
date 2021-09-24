package edu.ucla.cs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Answer {
	public int id;
	public int parentId;
	public String body;
	public int score;
	public boolean isAccepted;
	public HashSet<String> tags;
	public int viewCount;
	
	public HashMap<String, ArrayList<APISeqItem>> seq;
	public int buggy_seq_count = 0;
	
	public boolean containsIncompleteSnippet = false;
	
	public ArrayList<String> snippets = new ArrayList<String>();

	public Answer(String id, String parentId, String body, String score,
			String isAccepted, String tags, String viewCount) {
		this.id = Integer.parseInt(id);
		this.parentId = Integer.parseInt(id);
		this.body = body;
		this.score = Integer.parseInt(score);
		this.isAccepted = Integer.parseInt(isAccepted) == 0 ? false : true;
		this.tags = new HashSet<String>();
		if(tags != null) {
			String[] ss = tags.split(",");
			for(String s : ss) {
				this.tags.add(s);
			}
		}
		
		
		if(viewCount != null) {
			this.viewCount = Integer.parseInt(viewCount);
		} else {
			this.viewCount = 0;
		}
		
		// initialize seq with empty map, value should be obtained via partial program analysis
		this.seq = new HashMap<String, ArrayList<APISeqItem>>();
	}
	
	@Override
	public int hashCode() {
		int hash = 37;
		hash += 31 * id;
//		hash += 31 * parentId;
//		hash += 31 * viewCount;
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Answer) {
			Answer other = (Answer)obj;
			return this.id == other.id;
		} else {
			return false;
		}
	}
}
