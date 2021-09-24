package edu.ucla.cs.mine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ExtendedAPICall;
import edu.ucla.cs.utils.SAT;
import edu.ucla.cs.utils.SubsequenceCounter;

public class PatternSampler {
	public String seqFile;
	public String orgFile;
	public HashMap<String, ArrayList<String>> seqs;

	public PatternSampler(String seqFile, String orgFile) {
		this.seqFile = seqFile;
		this.orgFile = orgFile;
		seqs = PatternUtils.readAPISequences(seqFile);
	}

	public ArrayList<String> sample(ArrayList<APISeqItem> pattern, int n) {

		// extract the API call sequence only without guard conditions
		ArrayList<String> patternS = PatternUtils
				.extractSequenceWithoutGuard(pattern);
		SequencePatternVerifier verifyS = new SequencePatternVerifier(patternS);
		verifyS.verify(seqFile);

		ExtendedPredicateMiner minerP = new ExtendedPredicateMiner(patternS, orgFile, seqFile);
		// TraditionalPredicateMiner minerP = new TraditionalPredicateMiner(
		//		patternS, orgFile, seqFile);
		minerP.loadAndFilterPredicate();
		
		// rank code examples that support the sequence ordering based on their
		// conciseness (i.e., number of API calls and control constructs in a
		// sequence) and their ambiguity (i.e., whether there is an n-n mapping
		// between items in the pattern and items in the sequence)
		List<Map.Entry<String, ArrayList<String>>> sortedList = new LinkedList<Map.Entry<String, ArrayList<String>>>(verifyS.support.entrySet());
		
		Collections.sort(sortedList, new Comparator<Map.Entry<String, ArrayList<String>>>() {

			@Override
			public int compare(Entry<String, ArrayList<String>> o1,
					Entry<String, ArrayList<String>> o2) {
				// first we prefer non-ambiguous examples
				ArrayList<String> seq1 = o1.getValue();
				ArrayList<String> seq2 = o2.getValue();
				
				if (!isAmbiguous(patternS, seq1) && isAmbiguous(patternS, seq2)) {
					return -1;
				} else if (isAmbiguous(patternS, seq1) && !isAmbiguous(patternS, seq2)) {
					return 1;
				}
				
				if (seq1.size() < seq2.size()) {
					return -1;
				} else if (seq1.size() > seq2.size()) {
					return 1;
				} else {
					return 0;
				}
			}

			private boolean isAmbiguous(ArrayList<String> pattern,
					ArrayList<String> seq) {
				// make a copy of seq, remove all parentheses
				ArrayList<String> seqCopy = new ArrayList<String>();
				for(String s : seq) {
					if(!s.equals("}")) {
						seqCopy.add(s);
					}
				}
				
				// make a copy of pattern, remove all parentheses
				ArrayList<String> patternCopy = new ArrayList<String>();
				for(String s : pattern) {
					if(!s.equals("}")) {
						patternCopy.add(s);
					}
				}
				
				// strip all items in value that are not in pattern to reduce complexity
				for(int i = seqCopy.size() -1; i > 0; i--) {
					if(!pattern.contains(seqCopy.get(i))) {
						seqCopy.remove(i);
					}
				}
				
				SubsequenceCounter counter = new SubsequenceCounter(seqCopy, patternCopy);
				int count = counter.countMatches();
				return count > 1;
			}
			
		});
		
		// get ids of all code examples that respect the sequence ordering and the guard condition
		ArrayList<String> sampleIDs = new ArrayList<String>();
		// store the sampled sequences to check for duplication
		HashMap<ArrayList<String>, String> seq_id = new HashMap<ArrayList<String>, String>();
		HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
		SAT sat = new SAT();
		for (Map.Entry<String, ArrayList<String>> entry : sortedList) {
			String id = entry.getKey();
			ArrayList<String> seq = entry.getValue();
			HashMap<String, ArrayList<String>> map = minerP.predicates.get(id);
			if(map == null)  continue;
			boolean flag = true;
			// check guard conditions
			for (APISeqItem item : pattern) {
				if (!flag) {
					break;
				}
				if (item instanceof ExtendedAPICall) {
					ExtendedAPICall call = (ExtendedAPICall) item;
					if (call.condition.equals("true")) {
						continue;
					} else {
						ArrayList<String> predicates = map.get(call.name);
						if (predicates == null) {
							flag = false;
							break;
						}
						for (String predicate : predicates) {
							if(predicate.equals("true") && call.condition.equals("true")) {
								// no need to check implication if both are true
								continue;
							} else if (predicate.equals("true") && !call.condition.equals("true")) {
								// no need to check because true does not imply something that is not constantly true
								flag = false;
								break;
							} else {
								if (!sat.checkImplication(predicate, call.condition)) {
									flag = false;
									break;
								}
							}
						}
					}
				}
			}
			

			if (flag) {
//				// check for if checks on return values
//				boolean containIfCheck = false;
//				for(int i = 0; i < pattern.size(); i++) {
//					APISeqItem item = pattern.get(i);
//					if(item == ControlConstruct.IF) {
//						APISeqItem next = pattern.get(i+1);
//						if(next == ControlConstruct.END_BLOCK) {
//							containIfCheck = true;
//							break;
//						}
//					}
//				}
//				
//				if(containIfCheck) {
//					// check if the if check checks the return value of the previous API call
//					String theAPICall = null;
//					ArrayList<String> temp = new ArrayList<String>(patternS);
//					for(int i = 0; i < seq.size(); i ++) {
//						String item = seq.get(i);
//						String s = temp.get(0);
//						if(item.equals(s)) {
//							if(s.equals("IF {") && temp.get(1).equals("}")) {
//								theAPICall = seq.get(i+1);
//								break;
//							} else {
//								temp.remove(item);
//							}
//						}
//						
//						if(temp.isEmpty()) {
//							break;
//						}
//					}
//					
//					if(theAPICall == null || !theAPICall.contains("(")) {
//						continue;
//					} else {
//						boolean flag2 = false;
//						String s = ProcessUtils.readRawSequenceById(id.replaceAll(" \\*\\* ", "!"), orgFile);
//						if(s == null) {
//							continue;
//						}
//						
//						ArrayList<String> arr = ProcessUtils.splitByArrow(s);
//						for(String item : arr) {
//							if(item.contains("@")) {
//								String[] ss = ProcessUtils.splitByAt(item);
//								String api = ss[0];
//								String predicate = ss[1];
//								String apiName = theAPICall.substring(0, theAPICall.indexOf("("));
//								if(api.contains(apiName) && (predicate.contains("!=null") || predicate.contains("!= null"))) {
//									flag2 = true;
//									break;
//								}
//							}
//						}
//						
//						if(!flag2) {
//							continue;
//						}
//					}
//				} 
				
				if(seq_id.containsKey(seq)) {
					// duplicate sequence
					String sampledId = seq_id.get(seq);
					int count = frequencies.get(sampledId);
					frequencies.put(sampledId, count + 1);
				} else {
					// no duplicated sequences
					sampleIDs.add(id);
					seq_id.put(seq, id);
					frequencies.put(id, 1);
					if(sampleIDs.size() == n) {
						break;
					}
				}
			}
		}
		
		// read the original file again to get the complete sequence of each
		// sample
		ArrayList<String> sample = new ArrayList<String>();
		HashMap<String, String> id_sample = new HashMap<String, String>();
		File output = new File(orgFile);
		try (BufferedReader br = new BufferedReader(new FileReader(output))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("results[")) {
					continue;
				}
				String key = line.substring(line.indexOf("[") + 1,
						line.indexOf("][SEQ]"));
				key = key.replaceAll("\\!", " ** ");
				if (sampleIDs.contains(key)) {
					id_sample.put(key, line.replace("[SEQ]", "[" + frequencies.get(key) + "]"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String id : sampleIDs) {
			sample.add(id_sample.get(id));
		}

		return sample;
	}
}
