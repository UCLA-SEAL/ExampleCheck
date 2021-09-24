package edu.ucla.cs.process.extension;

import java.io.File;

public class Preprocess {
	/**
	 * Need to run RemoveTypeAnnotationProcessor.java first
	 * 
	 * The preprocess step includes
	 * 1. change formatting
	 * 2. remove the extra comma after the last argument
	 * 3. unchain method calls
	 * 4. remove the call sequences that will cause catastrophic backtracking in regex matching (handling forever)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Config these two flags first!!!
		boolean isFirstRun = true;
		boolean isContinuousRun = true;
		
		String rootPath = "/media/troy/Disk2/Boa/raw";
		String outPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
//		for(File f : rootDir.listFiles()) {
//			String fName = f.getName();
//			if(fName.endsWith("boa.txt")) {
//				String[] ss = fName.split("\\.");
//				String k = ss[ss.length - 3];
//				String name = "";
//				for(int i = 0; i < ss.length - 3; i++) {
//					name += ss[i] + ".";
//				}
//				name = name.substring(0, name.length() - 1);
//				File dir = new File(outPath + File.separator + name);
//				if(!dir.exists()) {
//					dir.mkdirs();
//				}
//				FileUtils.copyFileContent(f.getAbsolutePath(), dir.getAbsolutePath() + File.separator + k + ".txt", false);
//			}
//		}
		
		File outDir = new File(outPath);
		for(File apiDir : outDir.listFiles()) {
			String apiName = apiDir.getName();
			if(//apiName.equals("StringBuilder.append") 
				//	|| apiName.equals("String.indexOf") 
				//	|| apiName.equals("String.getBytes") 
				//	|| apiName.equals("String.replaceAll") 
				//	|| apiName.equals("Mac-String.doFinal-String") 
				//	|| apiName.equals("Map.put") || 
					apiName.equals("Integer.parseInt")
					|| apiName.equals("PreparedStatement.setString")) {
				continue;
			}
			for(File f : apiDir.listFiles()) {
				if(f.getName().equals("NO.txt")) {
					// only process the output of k = 1 so far
					if(!isContinuousRun || !new File(apiDir.getAbsolutePath() + File.separator + "large-output(no-slicing).txt").exists()) {
						// avoid preprocess the same file twice
						System.out.println("Process the initial Boa output of " + apiDir.getName());
						if(isFirstRun) {
							CatastrophicBacktrackingProcess cbp = new CatastrophicBacktrackingProcess(f.getAbsolutePath());
							cbp.process();
						} else {
							Process p = new Process(f.getAbsolutePath());
							p.process();
						}
					}
					
					if(!isContinuousRun || !new File(apiDir.getAbsolutePath() + File.separator + "large-output(no-slicing)-resolved.txt").exists()) {
						// avoid preprocess the same file twice
						System.out.println("Resolve types in the processed Boa output of " + apiDir.getName());
						ResolveTypeProcessor rtp = new ResolveTypeProcessor(apiDir.getAbsolutePath() + File.separator + "large-output(no-slicing).txt");
						rtp.process();
					}
				}
			}
		}
	}
}
