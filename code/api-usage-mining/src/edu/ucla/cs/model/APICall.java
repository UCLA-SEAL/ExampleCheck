package edu.ucla.cs.model;

import java.util.ArrayList;

public class APICall implements APISeqItem{
	public String name;
	public String condition;
	public String receiver;
	public ArrayList<String> arguments;
	
	public APICall(String name, String condition, int args) {
		this.name = name + "(" + args + ")";
		this.condition = condition;
		this.receiver = null;
		this.arguments = null;
	}
	
	public APICall(String name, String condition, String receiver, ArrayList<String> args) {
		this.name = name + "(" + args.size() + ")";
		this.condition = condition;
		this.receiver = receiver;
		this.arguments = args;
	}
	
	@Override
	public String toString() {
		return name + "@" + condition;
	}
	
	@Override
	public int hashCode() {
		int hash = 31;
		hash += 37 * this.name.hashCode();
		hash += 43 * this.condition.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof APICall) {
			APICall call = (APICall)obj;
			return this.name.equals(call.name) && this.condition.equals(call.condition);
		} else {
			return false;
		}
	}
}
