package edu.ucla.cs.check;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.mine.PatternUtils;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.search.Search;
import edu.ucla.cs.utils.FileUtils;
import edu.ucla.cs.utils.SAT;

public class BatchDetector {
	final String rootPath = "/media/troy/Disk2/Boa/apis";
	boolean isContinueRun = false;
	
	// aggregate results
	HashMap<Integer, Integer> score_totalPosts = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> score_unreliablePosts = new HashMap<Integer, Integer>();
	long view_totalPosts = 0;
	long view_unreliablePosts = 0;
	int totalPosts = 0;
	int unreliablePosts = 0;
	int totalAccepted = 0;
	int totalPostive = 0;
	int acceptedUnreliable = 0;
	int positiveUnreliable = 0;
	
	HashMap<String, HashSet<ArrayList<APISeqItem>>> required_patterns;
	HashMap<String, HashSet<ArrayList<APISeqItem>>> alternative_patterns;
	
	public BatchDetector(String path) {
		readPatternsFromTSV(path);
	}
	
	private void readPatternsFromTSV(String path) {
		String s = FileUtils.readFileToString(path);
		String[] ss = s.split(System.lineSeparator());
		required_patterns = new HashMap<String, HashSet<ArrayList<APISeqItem>>>();
		alternative_patterns = new HashMap<String, HashSet<ArrayList<APISeqItem>>>();
		for(int i = 1; i < ss.length; i++) {
			// skip the first line since it is header
			String line = ss[i];
			String[] cells = line.split("\t");
			String className = cells[0].trim();
			String methodName = cells[1].trim();
			String api = className + "." + methodName;
			if(cells.length == 2) {
				// no patterns
				required_patterns.put(api, new HashSet<ArrayList<APISeqItem>>());
			} else {
				String pattern = cells[3].trim();
				ArrayList<APISeqItem> p = PatternUtils.convertStringToPattern(pattern);
				int isRequired = Integer.parseInt(cells[6].trim());
				HashSet<ArrayList<APISeqItem>> pset;
				if(isRequired == 1) {
					// this is a required pattern
					if(required_patterns.containsKey(api)) {
						pset = required_patterns.get(api);
					} else {
						pset = new HashSet<ArrayList<APISeqItem>>();
					}
					pset.add(p);
					required_patterns.put(api, pset);
				} else {
					// this is one of the alternative patterns
					if(alternative_patterns.containsKey(api)) {
						pset = alternative_patterns.get(api);
					} else {
						pset = new HashSet<ArrayList<APISeqItem>>();
					}
					pset.add(p);
					alternative_patterns.put(api, pset);
				}
			}
		}
	}
	
	public void check() {
		HashSet<String> allAPIs = new HashSet<String>();
		allAPIs.addAll(required_patterns.keySet());
		allAPIs.addAll(alternative_patterns.keySet());
		for(String api : allAPIs) {
			System.out.println("Detecting API misuse for " + api);
			// configure the SAT temporary file to avoid conflicts
			SAT.temp = "/home/troy/" + api + ".txt";
			
			if(api.equals("Activity.super") || api.equals("Activity.setContentView")) {
				// need special handling
				continue;
			}
			
//			if(!api.equals("ByteBuffer.get")) {
//				continue;
//			}
			
			String path;
			if(api.equals("SharedPreferences.Editor")) {
				path = "/media/troy/Disk2/Boa/apis/SharedPreferences.Editor.commit/violations.txt"; 
			} else if (api.equals("Activity.onCreate")){
				path = "/media/troy/Disk2/Boa/apis/Activity.super.onCreate/violations.txt";
			} else {
				path = rootPath + File.separator + api + File.separator + "violations.txt";
			}
			
			File logFile = new File(path);
			if(isContinueRun && logFile.exists()) {
				continue;
			}
			
			if(logFile.exists()) logFile.delete();
			
			HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
			HashSet<ArrayList<APISeqItem>> rp = required_patterns.get(api);
			HashSet<ArrayList<APISeqItem>> ap = alternative_patterns.get(api);
			if(rp != null) {
				for(ArrayList<APISeqItem> p : rp) {
					HashSet<ArrayList<APISeqItem>> newP = new HashSet<ArrayList<APISeqItem>>();
					newP.add(p);
					pset.add(newP);
				}
			}
			
			if(ap != null) {
				pset.add(ap);
			}
			
			HashSet<String> typeQuery = new HashSet<String>();
			String[] types = api.split("\\.")[0].split("-");
			for(String type : types) {
				typeQuery.add(type);
			}
			
			HashSet<ArrayList<String>> callQueries = new HashSet<ArrayList<String>>();
			if(api.equals("File.mkdir")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("mkdir(0)");
				ArrayList<String> l2 = new ArrayList<String>();
				l2.add("mkdirs(0)");
				callQueries.add(l1);
				callQueries.add(l2);
			} else if (api.equals("JOptionPane.showMessageDialog")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("showMessageDialog(4)");
				callQueries.add(l1);
			} else if (api.equals("PreferenceManager.getDefaultSharedPreferences")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("getDefaultSharedPreferences(1)");
				callQueries.add(l1);
			} else if (api.equals("Activity.setContentView")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("setContentView(1)");
				callQueries.add(l1);
			} else if (api.equals("LayoutInflater.inflate")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("inflate(2)");
				callQueries.add(l1);
			} else if (api.equals("LayoutInflater.from")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("from(1)");
				callQueries.add(l1);
			} else if (api.equals("Pattern.matcher")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("matcher(1)");
				callQueries.add(l1);
			} else if (api.equals("System.nanoTime")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("nanoTime(0)");
				callQueries.add(l1);
			} else if (api.equals("ListView.setAdapter")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("setAdapter(1)");
				callQueries.add(l1);
			} else if (api.equals("Calendar.getInstance")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("getInstance(0)");
				ArrayList<String> l2 = new ArrayList<String>();
				l2.add("getInstance(1)");
				ArrayList<String> l3 = new ArrayList<String>();
				l3.add("getInstance(2)");
				callQueries.add(l1);
				callQueries.add(l2);
				callQueries.add(l3);
			} else if (api.equals("ByteBuffer.put-get")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("put(1)");
				l1.add("get(0)");
				callQueries.add(l1);
			} else if (api.equals("String.format")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("format(2)");
				ArrayList<String> l2 = new ArrayList<String>();
				l2.add("format(3)");
				callQueries.add(l1);
				callQueries.add(l2);
			} else if (api.equals("List.add") || api.equals("Set.add") || api.equals("JFrame.add")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("add(1)");
				callQueries.add(l1);
			} else if (api.equals("Activity.findViewById")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("findViewById(1)");
				callQueries.add(l1);
			} else if (api.equals("Mac-String.doFinal-String")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("doFinal(1)");
				l1.add("new String(1)");
				callQueries.add(l1);
			} else if (api.equals("Mac-String.doFinal-getBytes")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("getBytes(1)");
				l1.add("doFinal(1)");
				callQueries.add(l1);
			} else if (api.equals("TextView.setText") || api.equals("EditText.setText")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("setText(1)");
				callQueries.add(l1);
			} else if (api.equals("Map.put")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("put(2)");
				callQueries.add(l1);
			} else if (api.equals("SharedPreferences.Editor")) {
				typeQuery.clear();
				typeQuery.add("SharedPreferences.Editor");
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("commit(0)");
				callQueries.add(l1);
			} else if (api.equals("String.getBytes")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("getBytes(0)");
				ArrayList<String> l2 = new ArrayList<String>();
				l2.add("getBytes(1)");
				callQueries.add(l1);
				callQueries.add(l2);
			} else if (api.equals("String.getBytes")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("getBytes(0)");
				ArrayList<String> l2 = new ArrayList<String>();
				l2.add("getBytes(1)");
				callQueries.add(l1);
				callQueries.add(l2);
			} else if (api.equals("View.findViewById")) {
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("findViewById(1)");
				callQueries.add(l1);
			} else {
				String[] calls = api.split("\\.")[1].split("-");
				for(int i = 0; i < calls.length; i++) {
					String call = calls[i];
					if(typeQuery.contains(call)) {
						calls[i] = "new " + call;
					}
				}
				if(calls.length == 1) {
					String call = calls[0];
					HashSet<String> set = new HashSet<String>();
					if(rp != null) {
						for(ArrayList<APISeqItem> p : rp) {
							for(APISeqItem item : p) {
								if(item instanceof APICall) {
									String name = ((APICall) item).name;
									if(call.equals(name.substring(0, name.indexOf('(')))) {
										set.add(name);
									}
								}
							}
						}
					}
					
					if(ap != null) {
						for(ArrayList<APISeqItem> p : ap) {
							for(APISeqItem item : p) {
								if(item instanceof APICall) {
									String name = ((APICall) item).name;
									if(call.equals(name.substring(0, name.indexOf('(')))) {
										set.add(name);
									}
								}
							}
						}
					}
					
					for(String s : set) {
						ArrayList<String> l = new ArrayList<String>();
						l.add(s);
						callQueries.add(l);
					}
				} else {
					ArrayList<String> l = new ArrayList<String>();
					for(String call : calls) {
						String callWithArgCount = null;
						if(rp != null) {
							for(ArrayList<APISeqItem> p : rp) {
								for(APISeqItem item : p) {
									if(item instanceof APICall) {
										String name = ((APICall) item).name;
										if(call.equals(name.substring(0, name.indexOf('(')))) {
											callWithArgCount = name;
											break;
										}
									}
								}
							}
						}
						
						if(callWithArgCount == null && ap != null) {
							for(ArrayList<APISeqItem> p : ap) {
								for(APISeqItem item : p) {
									if(item instanceof APICall) {
										String name = ((APICall) item).name;
										if(call.equals(name.substring(0, name.indexOf('(')))) {
											callWithArgCount = name;
											break;
										}
									}
								}
							}
						}
						l.add(callWithArgCount);
					}
					callQueries.add(l);
				}
			}
			
 			ExtendedAPIMisuseDetection ead = new ExtendedAPIMisuseDetection(typeQuery, callQueries, pset);
			ead.run(path);
			
			// aggregate the results
			this.totalPosts += ead.totalPosts;
			this.unreliablePosts += ead.unreliablePosts;
			this.view_totalPosts += ead.view_totalPosts;
			this.view_unreliablePosts += ead.view_unreliablePosts;
			for(Integer i : ead.score_totalPosts.keySet()) {
				int count = 0;
				if(this.score_totalPosts.containsKey(i)) {
					count = this.score_totalPosts.get(i) + ead.score_totalPosts.get(i);
				} else {
					count = ead.score_totalPosts.get(i);
				}
				
				this.score_totalPosts.put(i, count);
			}
			for(Integer i : ead.score_unreliablePosts.keySet()) {
				int count = 0;
				if(this.score_unreliablePosts.containsKey(i)) {
					count = this.score_unreliablePosts.get(i) + ead.score_unreliablePosts.get(i);
				} else {
					count = ead.score_unreliablePosts.get(i);
				}
				
				this.score_unreliablePosts.put(i, count);
			}
		}
	}
	
	public static void main(String[] agrs) throws IOException {
		// configure the analysis level
		Search.classLevel = true;
		
		String tsv = "/media/troy/Disk2/Boa/valid_patterns.tsv";
		BatchDetector bd = new BatchDetector(tsv);
		bd.check();
		
		
		System.out.println("Total number of posts:" + bd.totalPosts);
		System.out.println("Total number of unreliable posts:" + bd.unreliablePosts);
		System.out.println("Average view count of all posts:" + ((double)bd.view_totalPosts) / bd.totalPosts);
		System.out.println("Average view count of unreliable posts:" + ((double) bd.view_unreliablePosts) / bd.unreliablePosts);
		System.out.println("Average view count of reliable posts:" + ((double) (bd.view_totalPosts - bd.view_unreliablePosts)) / (bd.totalPosts - bd.unreliablePosts));
		
		long totalVotes = 0;
		for(Integer i : bd.score_totalPosts.keySet()) {
			totalVotes += i * bd.score_totalPosts.get(i);
		}
		System.out.println("Average vote of total posts:" + ((double)totalVotes) / bd.totalPosts);
		
		long unreliableVotes = 0;
		for(Integer i : bd.score_unreliablePosts.keySet()) {
			unreliableVotes += i * bd.score_unreliablePosts.get(i);
		}
		System.out.println("Average vote of unreliable posts:" + ((double)unreliableVotes) / bd.unreliablePosts);
		System.out.println("Average vote of reliable posts:" + ((double) (totalVotes - unreliableVotes)) / (bd.totalPosts - bd.unreliablePosts));
		
		System.out.println("Total accepted posts:" + bd.totalAccepted);
		System.out.println("Total positive posts:" + bd.totalPostive);
		System.out.println("Total accepted but unreliable posts:" + bd.totalAccepted);
		System.out.println("Total positive but unreliable posts:" + bd.totalPostive);
		
		String scoreCSV = "/media/troy/Disk2/Boa/score.csv";
		File f = new File(scoreCSV);
		if(f.exists()) {
			f.delete();
		}
		f.createNewFile();
		String scoreUnreliable = "/media/troy/Disk2/Boa/unreliable_score.csv";
		File f2 = new File(scoreCSV);
		if(f2.exists()) {
			f2.delete();
		}
		f2.createNewFile();
		String scoreReliable = "/media/troy/Disk2/Boa/reliable_score.csv";
		File f3 = new File(scoreCSV);
		if(f3.exists()) {
			f3.delete();
		}
		f3.createNewFile();
		for(Integer i : bd.score_totalPosts.keySet()) {
			int count = bd.score_totalPosts.get(i);
			int count2 = 0; 
			if(bd.score_unreliablePosts.containsKey(i)) {
				count2 = bd.score_unreliablePosts.get(i);
			}
			
			FileUtils.appendStringToFile(i + "," + (int) (((double) count2 / count) * 100) + System.lineSeparator(), scoreCSV);
			FileUtils.appendStringToFile(i + "," + count2 + System.lineSeparator(), scoreUnreliable);
			FileUtils.appendStringToFile(i + "," + (count - count2) + System.lineSeparator(), scoreReliable);
		}
	}
}
