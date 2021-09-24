package edu.ucla.cs.postprocess;

public class AbstractedExample {
	public String repo_url;
	public String file_path;
	public String class_name;
	public String method_name;
	public String seq;
	public int frequency;
	
	public AbstractedExample(String repo_url, String file_path, String class_name, String method_name, int frequency, String seq) {
		this.repo_url = repo_url;
		this.file_path = file_path;
		this.class_name = class_name;
		this.method_name = method_name;
		this.seq = seq;
		this.frequency = frequency;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AbstractedExample) {
			AbstractedExample that = (AbstractedExample) obj;
			return this.repo_url.equals(that.repo_url)
					&& this.file_path.equals(that.file_path)
					&& this.class_name.equals(that.class_name)
					&& this.method_name.equals(that.method_name);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int hash = 37;
		hash += 31 * repo_url.hashCode() + 19;
		hash += 31 * file_path.hashCode() + 19;
		hash += 31 * class_name.hashCode() + 19;
		hash += 31 * method_name.hashCode() + 19;
		return hash;
	}
}
