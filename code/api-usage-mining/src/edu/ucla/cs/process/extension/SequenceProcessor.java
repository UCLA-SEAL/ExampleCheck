package edu.ucla.cs.process.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.utils.ProcessUtils;

public class SequenceProcessor extends ProcessStrategy {
	// match < and > in API names to handle constructors of parameterized types
	static final Pattern METHOD_CALL = Pattern
			.compile("((new )?[a-zA-Z0-9_<>]+)\\(((.+),)*\\)");
	
	private HashMap<String, String> symbol_table = null;
	
	public SequenceProcessor(Process p) {
		this.p = p;
	}

	@Override
	void process(String line) {
		String key = line.substring(line.indexOf("[") + 1, line.indexOf("][SEQ]"));
		if(p.types.containsKey(key)) {
			symbol_table = p.types.get(key);
			Method m = getMethodInstance(line, key);
			buildSequenceMap(m, line);
		}
	}

	protected void buildSequenceMap(Method method, String line) {
		String s = line.substring(line.indexOf("] =") + 3).trim();
		ArrayList<String> ss = ProcessUtils.splitByArrow(s);
		
		for (String str : ss) {
			str = str.trim();
			if(str.isEmpty())  continue;
			int count1 = 0;
			if(str.endsWith("}")) {
				while(str.endsWith("}")) {
					str = str.substring(0, str.lastIndexOf("}")).trim();
					count1 ++;
				}
			}
			
			ArrayList<String> rest = new ArrayList<String>();
			while (str.endsWith("} ELSE {") 
					|| (str.contains("} CATCH(") && str.endsWith("Exception) {")) 
					|| str.endsWith("} FINALLY {")) {
				String s1 = str.substring(0, str.lastIndexOf('}') + 1).trim();
				String s2 = str.substring(str.lastIndexOf('}') + 1, str.length()).trim();
				
				if(!s2.isEmpty()) {
					rest.add(s2);
				}
				
				while (s1.endsWith("}")) {
					s1 = s1.substring(0, s1.lastIndexOf("}")).trim();
					rest.add("}");
				}
				
				str = s1;

			}
			
			if (!str.isEmpty()) {
				method.seq.addAll(extractItems(str));
			}
						
			for(int j = rest.size() - 1; j >= 0; j --) {
				method.seq.add(rest.get(j));
			}
			
			while(count1 > 0) {
				method.seq.add("}");
				count1 --;
			}
		}
	}

	public ArrayList<String> extractItems(String expr) {
		ArrayList<String> items = new ArrayList<String>();

		// check if it is a control-flow construct
		String s = expr.trim();
		if (s.equals("IF {") || s.equals("ELSE {") || s.equals("TRY {")
				|| (s.contains("CATCH(") && s.endsWith("Exception) {")) || s.equals("LOOP {")
				|| s.equals("FINALLY {")) {
			items.add(s);
			return items;
		}

		String[] ss = s.split("@");
		// we assume the last @ splits the statement and its precondition
		// but we will run into trouble if there is an @ in the precondition
		if (ss.length == 1) {
			s = ss[0];
		} else {
			s = ss[ss.length - 2];
		}

		// pre-check to avoid unnecessary pattern matching for the performance
		// purpose
		if (s.contains("(") && s.contains(")")) {
			// extract method calls
			return extractMethodCalls(s);
		} else {
			// no method call, skip
			return items;
		}
	}

	protected ArrayList<String> extractMethodCalls(String expr) {
		ArrayList<String> items = new ArrayList<String>();
		Matcher m = METHOD_CALL.matcher(expr);
		while (m.find()) {
			String apiName = m.group(1);
			String args = m.group(3);
			String rest = null;
			ArrayList<String> arguments = new ArrayList<String>();
			if (args != null) {
				// check whether this is a chained method call by checking whether the argument is balanced
				if(!ProcessUtils.isBalanced(args)) {
					// this is a call chain
					// the regex cannot handle the method calls properly if one method call
					// after the first one in the chain contains arguments
					// the following method calls with arguments will be considered as the
					// argument of the first one
					int position = ProcessUtils.findFirstUnbalancedCloseParenthesis(args);
					if(position == -1) {
						// something goes wrong, return empty list
						return new ArrayList<String>();
					} else {
						// adjust the string of the argument list
						String newArgs = args.substring(0, position);
						if(position + 2 <= args.length()) {
							rest = args.substring(position + 2) + ")";
						}
						args = newArgs;
					}
				}
				
				arguments = ProcessUtils.getArguments(args);
				
				// this api call has arguments
				ArrayList<String> apis2 = extractMethodCalls(args);
				items.addAll(apis2);
				
				// then add this API call
				String signature = apiName + "(";
				for(String argument : arguments) {
					if(argument.contains(":")) {
						argument = argument.substring(0, argument.lastIndexOf(':'));
					}
					
					if(symbol_table.containsKey(argument)) {
						signature += symbol_table.get(argument);
					} else {
						String tmp = resolveAsPrimitiveType(argument);
						if (tmp != null) {
							signature += tmp;
						} else {
							signature += "*";
						}
					}
					signature += ",";
				}
				
				if(arguments.size() > 0) {
					signature = signature.substring(0, signature.length() - 1) + ")";
				} else {
					signature = signature + ")";
				}
				
				
				items.add(signature);
				
				// then process the rest of the API calls in the chain (if any)
				if(rest != null) {
					ArrayList<String> apis3 = extractMethodCalls(rest);
					items.addAll(apis3);
				}
			} else {
				items.add(apiName + "()");
			}
		}
		return items;
	}

	protected String resolveAsPrimitiveType(String argument) {
		argument = argument.trim();
		if(argument.startsWith("\"") || argument.endsWith("\"")) {
			return "String";
		} else if (isInteger(argument)){
			return "int";
		} else if (argument.equals("true") || argument.equals("false")) {
			return "boolean";
		} else if (isDouble(argument)) {
			return "double";
		} else if (argument.startsWith("'") || argument.endsWith("'")) {
			return "char";
		}
 		return null;
	}

	private boolean isInteger(String str) {
	    if (str == null) {
	        return false; 
	    } 
	    int length = str.length();
	    if (length == 0) {
	        return false; 
	    } 
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false; 
	        } 
	        i = 1;
	    } 
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false; 
	        } 
	    } 
	    return true; 
	}
	
	private boolean isDouble(String str) {
	    if (str == null) {
	        return false; 
	    } 
	    int length = str.length();
	    if (length == 0) {
	        return false; 
	    } 
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false; 
	        } 
	        i = 1;
	    } 
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if ((c < '0' || c > '9') && c != '.') {
	            return false; 
	        } 
	    }
	    
	    return str.contains("."); 
	}
}