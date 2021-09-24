package edu.ucla.cs.model;

import java.util.ArrayList;

public class ExtendedAPICall implements APISeqItem{
	public String name;
	public String condition;
	public String receiver;
	public ArrayList<String> arguments;
	
	public ExtendedAPICall(String name, String condition) {
		this.name = name;
		this.condition = condition;
		this.receiver = null;
		this.arguments = null;
	}
	
	public ExtendedAPICall(String name, String condition, String receiver, ArrayList<String> args) {
		this.name = name;
		this.condition = condition;
		this.receiver = receiver;
		this.arguments = args;
	}
	
	@Override
	public String toString() {
		if(name.contains("(")) {
			return name + "@" + condition;
		} else {
			String s = name;
			s += "(";
			if(arguments.isEmpty()) {
				s += ")";
			} else {
				s += arguments.get(0);
				for(int i = 1; i < arguments.size(); i++) {
					s += "," + arguments.get(i);
				}
				s += ")";
			}
			s += "@" + condition;
			return s;
		}
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
