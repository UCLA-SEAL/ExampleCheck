package edu.ucla.cs.process.extension;

import edu.ucla.cs.model.Method;

public abstract class ProcessStrategy {
	public Process p;
	
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
	
	protected Method getMethodInstance(String line, String key){
		Method m = null;
		
		if(p.methods.containsKey(key)){
			m = p.methods.get(key);
		} else {
			String repo = getRepo(key);
			String file = getFile(key);
			String className = getClassName(key);
			String methodName = getMethodName(key);
			m = new Method(repo, file, className, methodName);
			p.methods.put(key, m);
		}
		
		return m;
	}
}
