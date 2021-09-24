package edu.ucla.cs.process.lightweight;

import java.util.HashMap;

import com.google.common.collect.Multimap;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.model.Class;

public class TypeProcessor extends ProcessStrategy{

	@Override
	public void process(String line) {
		if(line.startsWith("fields")) {
			Class c = getClassInstance(line);
			buildTypeMap(c.fields, c.rev_fields, line); 
		} else if(line.startsWith("locals")){
			Method m = getMethodInstance(line);
			buildTypeMap(m.locals, m.rev_locals, line);
		}
	}
	
	protected void buildTypeMap(HashMap<String, String> map, Multimap<String, String> rev, String line){
		String s = line.substring(line.indexOf("] =")).trim();
		String[] ss = s.split("\\|");
		// skip the first element because it is empty string
		for(int i = 1; i < ss.length; i++){
			String name = ss[i].split(":")[0];
			String type = ss[i].split(":")[1];
			map.put(name, type);
			rev.put(type, name);
		}
	}
}
