package edu.ucla.cs.mine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LightweightPredicateMiner extends PredicatePatternMiner{
	// id -> arguments
	HashMap<String, HashMap<String, ArrayList<String>>> arguments;
	// id -> receivers
	HashMap<String, HashMap<String, ArrayList<String>>> receivers; 
	
	final String predicate_path = "/home/troy/research/BOA/Maple/example/predicate.txt";
	final String sequence_path = "/home/troy/research/BOA/Maple/example/output.txt";
	final String argument_path = "/home/troy/research/BOA/Maple/example/argument.txt";
	final String receiver_path = "/home/troy/research/BOA/Maple/example/receiver.txt";
	final String logicSeparator = "&&|\\|\\|";

	public LightweightPredicateMiner(ArrayList<String> pattern) {
		super(pattern);
		arguments = new HashMap<String, HashMap<String, ArrayList<String>>>();
		receivers = new HashMap<String, HashMap<String, ArrayList<String>>>();
	}
	
	@Override
	protected void loadAndFilterPredicate() {
		// find API call sequences that follow the pattern
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(sequence_path);

		// load arguments and receivers
		loadArugmentInfo(pv.support.keySet());
		loadReceiverInfo(pv.support.keySet());

		// load and filter predicates, and normalize variable names
		loadAndFilterPredicate(pv.support.keySet());
	}
	
	private void loadAndFilterPredicate(Set<String> ref) {
		File output = new File(predicate_path);
		try (BufferedReader br = new BufferedReader(new FileReader(output))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("conds[")) {
					String id = line.substring(line.indexOf("[") + 1,
							line.indexOf("] =")).trim();
					if (!ref.contains(id)) {
						continue;
					}

					HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

					String tmp = line.substring(line.indexOf("] =") + 3).trim();
					String[] records = tmp.split(";;;");
					for (String record : records) {
						String api = record.split("::")[0];
						if (!pattern.contains(api)) {
							continue;
						}

						HashSet<String> relevant_elements = new HashSet<String>();
						ArrayList<String> rcv_candidates = find_receivers(id,
								api);
						relevant_elements.addAll(rcv_candidates);
						ArrayList<ArrayList<String>> args_candidates = find_arguments(
								id, api);
						for (ArrayList<String> a : args_candidates) {
							relevant_elements.addAll(a);
						}

						String conds = record.split("::")[1];
						
						if(conds.contains(",)")) {
							conds = conds.replaceAll(",\\)", ")");
						}

						ArrayList<String> arr = new ArrayList<String>();

						String[] ss = conds.split("\\*\\*\\*");
						for (String s : ss) {
							boolean flag = false;
							for (String var : relevant_elements) {
								if (s.contains(var)) {
									flag = true;
									break;
								}
							}

							if (flag) {
								// condition on the relevant variables (i.e.,
								// receivers, arguments)
								// in order to compute the weakest precondition
								s = condition(relevant_elements, s);
								// normalize names
								s = normalize(s, rcv_candidates,
										args_candidates);
								arr.add(s);
							}
						}

						if (arr.isEmpty()) {
							arr.add("true");
						}

						map.put(api, arr);
					}

					for (String api : pattern) {
						if (!map.containsKey(api)) {
							// no predicate for this api => the precondition for
							// this api is true
							ArrayList<String> arr = new ArrayList<String>();
							arr.add("true");
							map.put(api, arr);
						}
					}

					predicates.put(id, map);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> find_receivers(String id, String api) {
		HashMap<String, ArrayList<String>> all_rcvs = this.receivers.get(id);

		ArrayList<String> vars = new ArrayList<String>();
		ArrayList<String> rcvs = all_rcvs.get(api);
		if(rcvs != null) {
			for (String rcv : rcvs) {
				if (rcv.startsWith("v::")) {
					vars.add(rcv.substring(3));
				}
			}
		}

		return vars;
	}

	private ArrayList<ArrayList<String>> find_arguments(String id, String api) {
		HashMap<String, ArrayList<String>> all_args = this.arguments.get(id);

		ArrayList<ArrayList<String>> vars = new ArrayList<ArrayList<String>>();
		ArrayList<String> argss = all_args.get(api);
		for (String args : argss) {
			String[] arr = args.split(",");
			ArrayList<String> temp = new ArrayList<String>();
			for (int i = 0; i < arr.length; i++) {
				String arg = arr[i];
				if (arg.startsWith("v::")) {
					temp.add(arg.substring(3));
				}
			}
			vars.add(temp);
		}

		return vars;
	}

	private void loadArugmentInfo(Set<String> ref) {
		File output = new File(argument_path);
		try (BufferedReader br = new BufferedReader(new FileReader(output))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("margs[")) {
					String id = line.substring(line.indexOf("[") + 1,
							line.indexOf("] =")).trim();
					if (!ref.contains(id)) {
						// this sequence does not follow the given pattern
						continue;
					}

					String tmp = line.substring(line.indexOf("] =") + 3).trim();
					String[] ss = tmp.split("@@");

					HashMap<String, ArrayList<String>> call_args_map = new HashMap<String, ArrayList<String>>();
					for (int i = 0; i < ss.length; i++) {
						String name = ss[i].split("->")[0];
						String calls = ss[i].split("->")[1];

						String[] arr = calls.split(";;;");
						ArrayList<String> al = new ArrayList<String>();
						for (int j = 0; j < arr.length; j++) {
							String args = arr[j];
							al.add(args);
						}
						call_args_map.put(name, al);
					}

					arguments.put(id, call_args_map);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadReceiverInfo(Set<String> ref) {
		File output = new File(receiver_path);
		try (BufferedReader br = new BufferedReader(new FileReader(output))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("receivers[")) {
					String id = line.substring(line.indexOf("[") + 1,
							line.indexOf("] =")).trim();
					if (!ref.contains(id)) {
						// this sequence does not follow the given pattern
						continue;
					}

					String tmp = line.substring(line.indexOf("] =") + 3).trim();
					String[] ss = tmp.split("@@");

					HashMap<String, ArrayList<String>> call_receivers_map = new HashMap<String, ArrayList<String>>();
					for (int i = 0; i < ss.length; i++) {
						String call = ss[i].split("->")[0];
						String vars = ss[i].split("->")[1];

						String[] arr = vars.split(";;;");
						ArrayList<String> rcvs = new ArrayList<String>();
						for (int j = 0; j < arr.length; j++) {
							String receiver = arr[j];
							rcvs.add(receiver);
						}
						call_receivers_map.put(call, rcvs);
					}

					receivers.put(id, call_receivers_map);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void print() {
		for (String key : predicates.keySet()) {
			System.out.println(key);
			System.out.println(predicates.get(key));
		}
	}

	public static void main(String[] args) {
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("IF {");
		pattern.add("createNewFile");
		pattern.add("}");
		LightweightPredicateMiner pm = new LightweightPredicateMiner(pattern);
		pm.process();
		HashMap<String, HashMap<String, Integer>> predicate_patterns = pm.find_the_most_common_predicate(0);
		for(String api: predicate_patterns.keySet()) {
			System.out.println(api + ":" + predicate_patterns.get(api));
		}
		//pm.print();
	}
}
