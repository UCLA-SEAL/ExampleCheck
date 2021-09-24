package edu.ucla.cs.mine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ucla.cs.utils.ProcessUtils;
import edu.ucla.cs.utils.SAT;

public class TraditionalPredicateMiner extends PredicatePatternMiner {
	static final Pattern METHOD_CALL = Pattern
			.compile("((new )?[a-zA-Z0-9_]+)\\(((.+),)*\\)");

	protected String path;
	String sequence_path;

	public TraditionalPredicateMiner(ArrayList<String> pattern,
			String raw_output, String seq) {
		super(pattern);
		this.path = raw_output;
		this.sequence_path = seq;
	}

	@Override
	protected void loadAndFilterPredicate() {
		// find API call sequences that follow the pattern
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(sequence_path);

		// read the full output of the traditional analysis
		File output = new File(path);
		try (BufferedReader br = new BufferedReader(new FileReader(output))) {
			String line;
			while ((line = br.readLine()) != null) {
				if(!line.startsWith("results[")) {
					continue;
				}
				String key = line.substring(line.indexOf("[") + 1,
						line.indexOf("][SEQ]"));
				key = key.replaceAll("\\!", " ** ");
				if (pv.support.containsKey(key)) {
//					System.out.println("Processing " + key);
					// this sequence follows the pattern
					String seq = line.substring(line.indexOf("] =") + 3).trim();
//					if(seq.contains("?") || seq.contains(">>") || seq.contains("<<") || seq.matches("^.+(?<!\\|)\\|(?!\\|).+$") || seq.matches("^.+(?<!&)&(?!&).+$")) {
//						// cannot handle conditional expressions, bit shift operators, bitwise operators
//						continue;
//					}
					
					ArrayList<String> arr = ProcessUtils.splitByArrow(seq);

					HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
					// skip the first element because it is empty string
					for(String str : arr) {
						str = str.trim();
						
						if(str.isEmpty()) continue;
						
						// strip off the close parentheses at the end (if any)
						if (str.endsWith("}")) {
							while (str.endsWith("}")) {
								str = str.substring(0, str.lastIndexOf("}"))
										.trim();
							}
						}

						// strip off the control flow at the end (if any)
						while (str.endsWith("} ELSE {")
								|| str.endsWith("} CATCH {")
								|| str.endsWith("} FINALLY {")) {
							str = str.substring(0, str.lastIndexOf('}') + 1);
							while (str.endsWith("}")) {
								str = str.substring(0, str.lastIndexOf("}"))
										.trim();
							}
						}

						// skip if it is empty or it is a control-flow construct
						if (str.isEmpty() || str.equals("IF {")
								|| str.equals("ELSE {") || str.equals("TRY {")
								|| str.equals("CATCH {")
								|| str.equals("LOOP {")
								|| str.equals("FINALLY {")) {
							continue;
						}

						// split by @
						String[] ss = ProcessUtils.splitByAt(str);
						String predicate = null;
						String item = ss[0];
						if(ss[1].trim().isEmpty()) {
							predicate = "true";
						} else {
							predicate = ss[1];
						}
						
						// pre-check to avoid unnecessary pattern matching for
						// the performance purpose
						if (!item.contains("(") || !item.contains(")")) {
							continue;
						}

						// extract predicates for each method call in this
						// expression
						HashMap<String, ArrayList<String>> predicates = propagatePredicates(
								item.trim(), item.trim(), predicate);

						// aggregate the predicates with those from previous
						// items
						for (String name : predicates.keySet()) {
							if (map.containsKey(name)) {
								map.get(name).addAll(predicates.get(name));
							} else {
								map.put(name, predicates.get(name));
							}
						}
					}

					this.predicates.put(key, map);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, ArrayList<String>> propagatePredicates(String expr, String original,
			String predicate) {
		HashMap<String, ArrayList<String>> predicates = new HashMap<String, ArrayList<String>>();
 		Matcher m = METHOD_CALL.matcher(expr);
		while (m.find()) {
			String apiName = m.group(1);
			
			// first check about whether there are chained or nested method calls in the "arguments" (not real arguments due to inaccurate regex)
			String args = m.group(3);
			ArrayList<String> arguments = new ArrayList<String>();
			if (args != null) {
				String rest = null;
				// check whether this is a chained method call by checking
				// whether the argument is balanced
				if (!ProcessUtils.isBalanced(args)) {
					// this is a call chain
					// the regex cannot handle the method calls properly if one
					// method call
					// after the first one in the chain contains arguments
					// the following method calls with arguments will be
					// considered as the
					// argument of the first one
					int position = ProcessUtils
							.findFirstUnbalancedCloseParenthesis(args);
					if (position == -1) {
						// something goes wrong, return empty list
						return new HashMap<String, ArrayList<String>>();
					} else {
						// adjust the string of the argument list
						String newArgs = args.substring(0, position);
						if (position + 2 <= args.length()) {
							rest = args.substring(position + 2) + ")";
						}
						args = newArgs;
					}
				}

				// split arguments
				// this has to be done here because we do not want previous
				// argument to be part of the receiver of API calls in the next
				// argument
				arguments = ProcessUtils.getArguments(args);
				for (String arg : arguments) {
					HashMap<String, ArrayList<String>> p2 = propagatePredicates(
							arg, arg, predicate);
					// aggregate
					for (String name : p2.keySet()) {
						if (predicates.containsKey(name)) {
							predicates.get(name).addAll(p2.get(name));
						} else {
							predicates.put(name, p2.get(name));
						}
					}
				}

				// then process the rest of the API calls in the chain (if any)
				if (rest != null) {
					HashMap<String, ArrayList<String>> p3 = propagatePredicates(
							rest, original, predicate);
					// aggregate
					for (String name : p3.keySet()) {
						if (predicates.containsKey(name)) {
							predicates.get(name).addAll(p3.get(name));
						} else {
							predicates.put(name, p3.get(name));
						}
					}
				}
			}

			if (!pattern.contains(apiName + "(" + arguments.size() + ")")) {
				// skip if the API call does not appear in the sequence pattern
				continue;
			}

			String receiver = getReceiver(original, apiName);
			
			HashSet<String> relevant_elements = new HashSet<String>();
			if (receiver != null && !receiver.isEmpty()) {
				relevant_elements.add(receiver);
			}
			relevant_elements.addAll(arguments);

			// remove irrelevant clauses
			String conditioned_predicate;
			if(!predicate.equals("true")) {
				conditioned_predicate = condition(relevant_elements,predicate);
			} else {
				conditioned_predicate = "true";
			}
					
			// normalize names
			// declare temporary variables to fit the API
			ArrayList<String> temp1 = new ArrayList<String>();
			if (receiver != null && !receiver.isEmpty()) {
				temp1.add(receiver);
			}
			ArrayList<ArrayList<String>> temp2 = new ArrayList<ArrayList<String>>();
			temp2.add(arguments);

			String normalized_predicate;
			if(!conditioned_predicate.equals("true")) {
				normalized_predicate = normalize(conditioned_predicate,
						temp1, temp2);
			} else {
				normalized_predicate = "true";
			}
			
			if(enableSMT) {
				// Optimize 2: remove 'true &&' and '&& true'
				normalized_predicate = normalized_predicate.replaceAll("true\\s*&&", "");
				normalized_predicate = normalized_predicate.replaceAll("&&\\s*true", "");
				normalized_predicate = normalized_predicate.trim();
			}
			
			
			if(normalized_predicate.isEmpty()) {
				normalized_predicate = "true";
			}
			
			if(normalized_predicate.startsWith("&&")) {
				normalized_predicate = normalized_predicate.substring(2).trim();
			}

			if (normalized_predicate.equals("true||pageList.size()==arg0 && !rcv==null||rcv.size()==arg0 && true")) {
				System.out.println("oops");
			}
			
			ArrayList<String> value;
			if (predicates.containsKey(apiName + "(" + arguments.size() + ")")) {
				value = predicates.get(apiName + "(" + arguments.size() + ")");
			} else {
				value = new ArrayList<String>();
			}
			value.add(normalized_predicate);
			predicates.put(apiName + "(" + arguments.size() + ")", value);
		}

		return predicates;
	}

	public String getReceiver(String expr, String apiName) {
		String receiver = null;
		if (!apiName.startsWith("new ")) {
			// make sure this is not a call to class constructor since class
			// constructors do not have receivers
			int index = expr.indexOf(apiName);
			String sub = expr.substring(0, index);
			if (sub.endsWith(".")) {
				// make sure it is not a call to local method since local method
				// calls also do not have receivers
				if (sub.startsWith("return ")) {
					// return statement
					receiver = sub.substring(7, sub.length() - 1);
				} else if (sub.matches("[a-zA-Z0-9_]+=.+")) {
					// assignment statement
					receiver = sub.substring(sub.indexOf("=") + 1,
							sub.length() - 1);
				} else {
					// regular method call
					receiver = sub.substring(0, sub.length() - 1);
				}

				// strip off any type casting of the return value before the receiver
				// but be careful and do not strip off the type casting in the receiver
				receiver = receiver.trim();
				if (receiver.matches("^\\([a-zA-Z0-9_\\.<>\\?\\s]+\\).+$")) {
					receiver = receiver.substring(receiver.indexOf(')') + 1);
					receiver = receiver.trim();
				}
				
				if(!ProcessUtils.isBalanced(receiver)) {
					// truncate the expression starting from the first unbalanced open parenthesis
					int pos = ProcessUtils.findFirstUnbalancedOpenParenthesis(receiver);
					if(pos != -1) {
						receiver = receiver.substring(pos + 1);
					}
				}
				receiver = SAT.stripUnbalancedParentheses(receiver);
			}
		}
		return receiver;
	}

//	public ArrayList<String> getArguments(String args) {
//		ArrayList<String> list = new ArrayList<String>();
//		int stack = 0;
//		String[] ss = args.split(",");
//		String arg = "";
//		for (String s : ss) {
//			int open = StringUtils.countMatches(s, "(");
//			int close = StringUtils.countMatches(s, ")");
//			stack += open - close;
//			if (stack != 0) {
//				arg += s + ",";
//			} else {
//				arg += s;
//				if (!arg.isEmpty()) {
//					list.add(arg);
//				}
//				arg = "";
//			}
//		}
//		return list;
//	}
	
	

	public static void main(String[] args) {
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("get(1)");
		String path = "/home/troy/research/BOA/example/HashMap.get/1/large-sequence-new.txt";
		String sequence_path = "/home/troy/research/BOA/example/HashMap.get/1/large-output.txt";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern,
				path, sequence_path);
		pm.process();
		int min_support = (int) (0.04 * 43769);
		HashMap<String, HashMap<String, Integer>> predicates = pm.find_the_most_common_predicate(min_support);
		for(String api : predicates.keySet()) {
			System.out.println(api);
			HashMap<String, Integer> pset = predicates.get(api);
			for(String p : pset.keySet()) {
				System.out.println(p + " : " + pset.get(p));
			}
		}
	}
}
