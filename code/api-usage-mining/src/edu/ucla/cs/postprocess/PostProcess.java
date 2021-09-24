package edu.ucla.cs.postprocess;

import java.io.File;

public class PostProcess {
	public String root;
	
	public PostProcess(String rootDir) {
		this.root = rootDir;
	}
	
	public void updateExamplesWithValidURLs() {
		File rootDir = new File(root);
		if(!rootDir.exists() || !rootDir.isDirectory()) {
			System.err.println("The specified directory " + root + " does not exist or is not a directory.");
			return;
		}
		
		for(File f : rootDir.listFiles()) {			
			if(!f.isDirectory()) {
				continue;
			}
			
			System.out.println("Processing " + f.getName());
			for(File f2: f.listFiles()) {
				if (f2.getName().startsWith("sample-") && !f2.getName().endsWith("~")) {
					System.out.println("Processing " + f2.getName());
					ConstructURL constructor = new ConstructURL(f2.getAbsolutePath());
					constructor.construct();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String dir = "/media/troy/Disk2/Boa/sample";
		PostProcess pp = new PostProcess(dir);
		pp.updateExamplesWithValidURLs();
	}
}
