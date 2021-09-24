package edu.ucla.cs.process.traditional;

import edu.ucla.cs.model.Method;

public abstract class ProcessStrategy {
	abstract void process(String line);
	
	protected String getRepo(String key){
		String[] ss = key.split("\\!");
		return ss[0].trim();
	}
	
	protected String getFile(String key){
		String[] ss = key.split("\\!");
		return ss[1].trim();
	}
	
	protected String getClassName(String key){
		String[] ss = key.split("\\!");
		return ss[2].trim();
	}
	
	protected String getMethodName(String key){
		String[] ss = key.split("\\!");
		return ss[3].trim();
	}
	
	protected Method getMethodInstance(String line){
		String key = line.substring(line.indexOf("[") + 1, line.indexOf("][SEQ]"));
		Method m = null;
		
		if(Process.methods.containsKey(key)){
			m = Process.methods.get(key);
		} else {
			String repo = getRepo(key);
			String file = getFile(key);
			String className = getClassName(key);
			String methodName = getMethodName(key);
			m = new Method(repo, file, className, methodName);
			Process.methods.put(key, m);
		}
		
		return m;
	}
}
