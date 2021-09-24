package edu.ucla.cs.process.lightweight;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.model.Class;
import edu.ucla.cs.slice.Slicer;

public abstract class ProcessStrategy {
	abstract void process(String line);
	
	protected String getRepo(String key){
		String[] ss = key.split("\\*\\*");
		return ss[0].trim();
	}
	
	protected String getFile(String key){
		String[] ss = key.split("\\*\\*");
		return ss[1].trim();
	}
	
	protected String getClassName(String key){
		String[] ss = key.split("\\*\\*");
		return ss[2].trim();
	}
	
	protected String getMethodName(String key){
		String[] ss = key.split("\\*\\*");
		return ss[3].trim();
	}
	
	protected Method getMethodInstance(String line){
		String key = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
		Method m = null;
		if(Slicer.methods.containsKey(key)){
			m = Slicer.methods.get(key);
		} else {
			String repo = getRepo(key);
			String file = getFile(key);
			String className = getClassName(key);
			String methodName = getMethodName(key);
			m = new Method(repo, file, className, methodName);
			Slicer.methods.put(key, m);
		}
		
		return m;
	}
	
	protected Class getClassInstance(String line) {
		String key = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
		Class c = null;
		
		if(Slicer.classes.containsKey(key)){
			c = Slicer.classes.get(key);
		} else {
			String repo = getRepo(line);
			String file = getFile(line);
			String className = getClassName(line);
			c = new Class(repo, file, className);
			Slicer.classes.put(key, c);
		}
		
		return c;
	}
}
