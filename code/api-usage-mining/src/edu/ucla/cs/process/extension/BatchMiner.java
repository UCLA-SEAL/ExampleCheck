package edu.ucla.cs.process.extension;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.mine.ExtendedPatternMiner;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class BatchMiner {
	public static void main(String[] args) {
		// Config this first!!!
		boolean isContinueRun = true;
//		boolean start = false;
		
		String rootPath = "/media/troy/Disk2/Boa/apis";
		File rootDir = new File(rootPath);
		for(File apiDir : rootDir.listFiles()) {
			String name = apiDir.getName();
			
//			if(name.equals("PreparedStatement.setString")) {
//				start = true;
//			}
//			
//			if(!start) {
//				continue;
//			}
			
			if(name.equals("File.mkdir")
					|| name.equals("Activity.setContentView")
//					|| name.equals("SimpleDateFormat.SimpleDateFormat")
//					|| name.equals("StringBuilder.append")
//					|| name.equals("String.indexOf")
//					|| name.equals("Pattern.matcher")
					|| name.equals("String.substring")
//					|| name.equals("String.getBytes")
//					|| name.equals("String.replaceAll")
					|| name.equals("SortedMap.firstKey")
//					|| name.equals("Matcher.group")
//					|| name.equals("Map.put")
//				    || name.equals("PreparedStatement.setString")
//				    || name.equals("Integer.parseInt")
				    || name.equals("Toast.makeText")){
				continue;
			}
			
			if(!name.equals("ByteBuffer.get")) {
				continue;
			}
			
			String boa_raw = apiDir.getAbsolutePath() + File.separator + "1-clean.txt";
			String processed = apiDir.getAbsolutePath() + File.separator + "large-output-resolved.txt";
			File file1 = new File(boa_raw);
			File file2 = new File(processed);
			if(!file1.exists() || !file2.exists()) {
				continue;
			}
			
			if(isContinueRun && new File(apiDir.getAbsolutePath() + File.separator + "patterns.txt").exists()) {
				continue;
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println(dateFormat.format(date) + " start mining for " + apiDir);
			
			String[] ss = name.split("\\.");
			String className = ss[0];
			String methodName;
			if(ss.length == 2) {
				methodName = ss[1];
			} else {
				methodName = ss[2];
			}
			
			ArrayList<String> classNames = new ArrayList<String>();
			for(String s : className.split("-")) {
				classNames.add(s);
			}
			
			ArrayList<String> methodNames = new ArrayList<String>();
			for(String s : methodName.split("-")) {
				if(classNames.contains(s)) {
					s = "new " + s;
				}
				methodNames.add(s);
			}
			
			HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
			HashSet<String> q1 = new HashSet<String>();
			q1.addAll(methodNames);
			queries.add(q1);
			int size = FileUtils.countLines(processed);
			double sigma = 0.2;
			double theta = 0.2;
			long startTime = System.currentTimeMillis();
			Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = ExtendedPatternMiner.mine(
					boa_raw, processed, queries, sigma, size, theta);
			long estimatedTime = System.currentTimeMillis() - startTime;
			if(patterns.size() != 0) {
				String s = "Total Code Examples: " + size + System.lineSeparator();
				s += "Mining time (millis) : " + estimatedTime + System.lineSeparator();
				for (ArrayList<APISeqItem> sp : patterns.keySet()) {
					s += sp + ":" + patterns.get(sp) + System.lineSeparator();
				}
				FileUtils.writeStringToFile(s, apiDir.getAbsolutePath() + File.separator + "patterns.txt");
			}
		}
	}
}
