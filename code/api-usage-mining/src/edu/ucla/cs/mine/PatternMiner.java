package edu.ucla.cs.mine;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ControlConstruct;
import edu.ucla.cs.utils.FileUtils;

public class PatternMiner {
	public static Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> mine(
			String raw_output, String sequence, HashSet<HashSet<String>> apis,
			double sigma, int size, double theta) {
		long startTime = System.currentTimeMillis();
		int sequence_support = (int) (sigma * size);
		SequencePatternMiner pm = new FrequentSequenceMiner(
				"/home/troy/research/BOA/Maple/mining/freq_seq.py", sequence,
				sequence_support, apis);
		HashMap<ArrayList<String>, Integer> patterns = pm.mine();
		HashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>> composed_patterns = new HashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>>();
		for (ArrayList<String> seq_pattern : patterns.keySet()) {
			PredicatePatternMiner pm2 = new TraditionalPredicateMiner(
					seq_pattern, raw_output, sequence);
			pm2.process();
			int support = patterns.get(seq_pattern);
			int predicate_support = (int) (theta * support);
			HashMap<String, HashMap<String, Integer>> predicate_patterns = pm2
					.find_the_most_common_predicate(predicate_support);
			HashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>> cp = compose(seq_pattern,
					support, predicate_patterns, size);
			if (cp != null) {
				composed_patterns.putAll(cp);
			}
		}

		// rank by support numbers
		Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> ranked_patterns = sortByValue(composed_patterns);
		
		// if querying a single API, remove the pattern "api@true"
		if(apis.size() == 1) {
			HashSet<String> query = apis.iterator().next();
			ArrayList<APISeqItem> remove = null;
			for(ArrayList<APISeqItem> pattern : ranked_patterns.keySet()) {
				if(pattern.size() == query.size()) {
					boolean flag = true;
					for(APISeqItem item : pattern) {
						if(item instanceof APICall && !((APICall)item).condition.equals("true")) {
							flag = false;
						}
					}
					
					if(flag) {
						remove = pattern;
					}
				}
			}
			
			if(remove != null) {
				ranked_patterns.remove(remove);
			}
		}
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Mining time (millis) : " + estimatedTime);
		System.out.println("Total Count: " + size);
		return ranked_patterns;
	}

	private static Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> sortByValue(
			Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> unsortMap) {

		// 1. Convert Map to List of Map
		List<Map.Entry<ArrayList<APISeqItem>, MutablePair<Double, Double>>> list = new LinkedList<Map.Entry<ArrayList<APISeqItem>, MutablePair<Double, Double>>>(
				unsortMap.entrySet());

		// 2. sort the list of map in descending order
		Collections.sort(list, new Comparator<Map.Entry<ArrayList<APISeqItem>, MutablePair<Double, Double>>>() {
			public int compare(Map.Entry<ArrayList<APISeqItem>, MutablePair<Double, Double>> o1,
					Map.Entry<ArrayList<APISeqItem>, MutablePair<Double, Double>> o2) {
				Double seq1 = o1.getValue().getLeft();
				Double seq2 = o2.getValue().getLeft();
				Double pre1 = o1.getValue().getRight();
				Double pre2 = o2.getValue().getRight();
				Double total1 = new Double(seq1 * pre1);
				Double total2 = new Double(seq2 * pre2);
				return total2.compareTo(total1);
			}
		});

		// 3. Loop the sorted list and put it into a new insertion order Map
		// LinkedHashMap
		Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> sortedMap = new LinkedHashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>>();
		for (Map.Entry<ArrayList<APISeqItem>, MutablePair<Double, Double>> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private static HashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>> compose(
			ArrayList<String> seq_pattern, int seq_support,
			HashMap<String, HashMap<String, Integer>> pred_patterns, int size) {
		HashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>> composed_patterns = new HashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>>();
		double seq_ratio = ((double)seq_support) / size;
		seq_ratio = round(seq_ratio, 2);
		
		for (String api : seq_pattern) {
			if (api.equals("}")) {
				if (composed_patterns.isEmpty()) {
					// this is the first element in the sequence pattern
					ArrayList<APISeqItem> s = new ArrayList<APISeqItem>();
					s.add(ControlConstruct.END_BLOCK);
					MutablePair<Double, Double> pair = new MutablePair<Double, Double>(seq_ratio, Double.MAX_VALUE);
					composed_patterns.put(s, pair);
				} else {
					for (ArrayList<APISeqItem> s : composed_patterns.keySet()) {
						s.add(ControlConstruct.END_BLOCK);
					}
				}
			} else if (api.equals("TRY {")) {
				if (composed_patterns.isEmpty()) {
					// this is the first element in the sequence pattern
					ArrayList<APISeqItem> s = new ArrayList<APISeqItem>();
					s.add(ControlConstruct.TRY);
					MutablePair<Double, Double> pair = new MutablePair<Double, Double>(seq_ratio, Double.MAX_VALUE);
					composed_patterns.put(s, pair);
				} else {
					for (ArrayList<APISeqItem> s : composed_patterns.keySet()) {
						s.add(ControlConstruct.TRY);
					}
				}
			} else if (api.equals("IF {")) {
				if (composed_patterns.isEmpty()) {
					// this is the first element in the sequence pattern
					ArrayList<APISeqItem> s = new ArrayList<APISeqItem>();
					s.add(ControlConstruct.IF);
					MutablePair<Double, Double> pair = new MutablePair<Double, Double>(seq_ratio, Double.MAX_VALUE);
					composed_patterns.put(s, pair);
				} else {
					for (ArrayList<APISeqItem> s : composed_patterns.keySet()) {
						s.add(ControlConstruct.IF);
					}
				}
			} else if (api.equals("LOOP {")) {
				if (composed_patterns.isEmpty()) {
					// this is the first element in the sequence pattern
					ArrayList<APISeqItem> s = new ArrayList<APISeqItem>();
					s.add(ControlConstruct.LOOP);
					MutablePair<Double, Double> pair = new MutablePair<Double, Double>(seq_ratio, Double.MAX_VALUE);
					composed_patterns.put(s, pair);
				} else {
					for (ArrayList<APISeqItem> s : composed_patterns.keySet()) {
						s.add(ControlConstruct.LOOP);
					}
				}
			} else if (api.equals("CATCH {")) {
				if (composed_patterns.isEmpty()) {
					// this is the first element in the sequence pattern
					ArrayList<APISeqItem> s = new ArrayList<APISeqItem>();
					s.add(ControlConstruct.CATCH);
					MutablePair<Double, Double> pair = new MutablePair<Double, Double>(seq_ratio, Double.MAX_VALUE);
					composed_patterns.put(s, pair);
				} else {
					for (ArrayList<APISeqItem> s : composed_patterns.keySet()) {
						s.add(ControlConstruct.CATCH);
					}
				}
			} else if (api.equals("FINALLY {")) {
				if (composed_patterns.isEmpty()) {
					// this is the first element in the sequence pattern
					ArrayList<APISeqItem> s = new ArrayList<APISeqItem>();
					s.add(ControlConstruct.FINALLY);
					MutablePair<Double, Double> pair = new MutablePair<Double, Double>(seq_ratio, Double.MAX_VALUE);
					composed_patterns.put(s, pair);
				} else {
					for (ArrayList<APISeqItem> s : composed_patterns.keySet()) {
						s.add(ControlConstruct.FINALLY);
					}
				}
			} else if (api.equals("ELSE {")) {
				if (composed_patterns.isEmpty()) {
					// this is the first element in the sequence pattern
					ArrayList<APISeqItem> s = new ArrayList<APISeqItem>();
					s.add(ControlConstruct.ELSE);
					MutablePair<Double, Double> pair = new MutablePair<Double, Double>(seq_ratio, Double.MAX_VALUE);
					composed_patterns.put(s, pair);
				} else {
					for (ArrayList<APISeqItem> s : composed_patterns.keySet()) {
						s.add(ControlConstruct.ELSE);
					}
				}
			} else {
				HashMap<String, Integer> conditions;
				if (pred_patterns.containsKey(api)) {
					conditions = pred_patterns.get(api);
					if(conditions.isEmpty()) {
						conditions.put("true", seq_support);
					}
				} else {
					conditions = new HashMap<String, Integer>();
					conditions.put("true", seq_support);
				}

				// split the name and the number of arguments of the API
				String tmp = api.substring(api.indexOf('(') + 1,
						api.indexOf(')'));
				int args = Integer.parseInt(tmp);
				String name = api.substring(0, api.indexOf('('));

				if (composed_patterns.isEmpty()) {
					// this is the first element in the sequence pattern
					for (String condition : conditions.keySet()) {
						// initialize
						ArrayList<APISeqItem> s = new ArrayList<APISeqItem>();
						s.add(new APICall(name, condition, args));
						int support2 = conditions.get(condition);
						double pre_ratio = ((double) support2) / seq_support;
						if(pre_ratio > 1) {
							pre_ratio = 1;
						}
						pre_ratio = round(pre_ratio, 2);
						MutablePair<Double, Double> pair = new MutablePair<Double, Double>(seq_ratio, pre_ratio);
						composed_patterns.put(s, pair);
					}
				} else {
					HashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>> newAll = new HashMap<ArrayList<APISeqItem>, MutablePair<Double, Double>>();
					boolean flag = isConditional(seq_pattern, api);
					// propagate the new API with all possible predicates to
					// each record in the map
					for (Map.Entry<ArrayList<APISeqItem>, MutablePair<Double, Double>> entry : composed_patterns
							.entrySet()) {
						ArrayList<APISeqItem> s = entry.getKey();
						MutablePair<Double, Double> pair = entry.getValue();

						if (conditions.size() > 1
								&& conditions.containsKey("true")) {
							// we prefer conditions that are not true above the
							// threshold
							conditions.remove("true");
						} else if (conditions.size() == 1
								&& conditions.containsKey("true") && flag) {
							// ignore this pattern
							return null;
						}

						for (String condition : conditions.keySet()) {
							// int support2 = (int) (conditions.get(condition) *
							// ((double) support1) / size);
							int support2 = conditions.get(condition);
							double pre_ratio = ((double) support2) / seq_support;
							if(pre_ratio > 1) {
								pre_ratio = 1;
							}
							pre_ratio = round(pre_ratio, 2);
							double value = Math.min(pair.getRight(), pre_ratio);
							MutablePair<Double, Double> newPair = new MutablePair<Double, Double>(pair.getLeft(), value);
							ArrayList<APISeqItem> newS = new ArrayList<APISeqItem>();
							newS.addAll(s);
							newS.add(new APICall(name, condition, args));
							newAll.put(newS, newPair);
						}
					}
					composed_patterns = newAll;
				}
			}
		}

		return composed_patterns;
	}

	/**
	 * 
	 * check whether this API is wrapped by a conditional statement like if-else
	 * or loop
	 * 
	 * @param seq_pattern
	 * @param api
	 * @return
	 */
	private static boolean isConditional(ArrayList<String> seq_pattern,
			String api) {
		int count = 0;
		for (String s : seq_pattern) {
			if (api.equals(s)) {
				break;
			} else if (s.equals("IF {") || s.equals("LOOP {")) {
				count++;
			} else if (s.equals("}")) {
				count--;
			}
		}

		return count > 0;
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	 
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.FLOOR);
	    return bd.doubleValue();
	}
	
	/**
	 * 
	 * Print the patterns to pattern.txt in the given destination path
	 * Also print n samples to the sample-i.txt for the i-th pattern
	 * 
	 * @param seqFile
	 * @param size
	 * @param patterns
	 * @param dest
	 * @param n
	 * @throws IOException
	 */
	public static void sample(String seqFile, String orgFile, int size, Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns, String dest, int n) {
		File dir = new File(dest);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		String pFile = dir.getAbsolutePath() + File.separator + "pattern.txt";
		String s = "Total Count:" + size + System.lineSeparator();
		int i = 1;
		for (ArrayList<APISeqItem> pattern : patterns.keySet()) {
			double d = size * patterns.get(pattern).left * patterns.get(pattern).right;
			int count = (int) Math.round(d);
			s += i + ":" + pattern.toString() + ":" + count + System.lineSeparator();
			// find 10 sample examples of this pattern
			String sFile = dir.getAbsolutePath() + File.separator + "sample-" + i + ".txt";
			
			PatternSampler sampler = new PatternSampler(seqFile, orgFile);
			ArrayList<String> sample = sampler.sample(pattern, n);
			FileUtils.writeArrayToFile(sample, sFile);
			i++;
		}
		
		FileUtils.writeStringToFile(s, pFile);
	}

	public static void main(String[] args) {
		String raw_output = "/home/troy/research/BOA/Maple/example/InputStream.read/1/large-sequence.txt";
		String seq = "/home/troy/research/BOA/Maple/example/InputStream.read/1/large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("read(0)");
		queries.add(q1);
		int size = FileUtils.countLines(seq);
		Map<ArrayList<APISeqItem>, MutablePair<Double, Double>> patterns = PatternMiner.mine(
				raw_output, seq, queries, 0.16, size, 0.05);
		for (ArrayList<APISeqItem> sp : patterns.keySet()) {
			System.out.println(sp + ":" + patterns.get(sp));
		}
	}
}
