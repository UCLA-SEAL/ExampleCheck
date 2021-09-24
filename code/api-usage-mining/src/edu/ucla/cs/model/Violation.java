package edu.ucla.cs.model;

public class Violation {
	public ViolationType type;
	public APISeqItem item; 
	
	public Violation(ViolationType type, APISeqItem item) {
		this.type = type;
		this.item = item;
	}
}
