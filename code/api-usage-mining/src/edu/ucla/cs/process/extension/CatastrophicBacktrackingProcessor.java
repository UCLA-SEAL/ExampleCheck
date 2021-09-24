package edu.ucla.cs.process.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.utils.ProcessUtils;

public class CatastrophicBacktrackingProcessor extends SequenceProcessor{
	public HashSet<String> troubledLines = new HashSet<String>();

	String line;
	private HashMap<String, String> symbol_table = null;
	
	public CatastrophicBacktrackingProcessor(Process p) {
		super(p);
	}
	
	@Override
	void process(String line) {
		// update current line
		this.line = line;
		
		String key = line.substring(line.indexOf("[") + 1, line.indexOf("][SEQ]"));
		if(p.types.containsKey(key)) {
			symbol_table = p.types.get(key);
			Method m = getMethodInstance(line, key);
			buildSequenceMap(m, line);
		}
	}
	
	@Override
	protected ArrayList<String> extractMethodCalls(String expr) {
		ArrayList<String> items = new ArrayList<String>();

		final ExecutorService service = Executors.newSingleThreadExecutor();
		try {
			final Future<Object> f = service.submit(() -> {
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
				return "yes";
			});

			f.get(1, TimeUnit.SECONDS);
		} catch (final TimeoutException e) {
			// catastrophic backtracking
			System.out.println("Regex runs more than 1 second! Ignore it!");
			System.out.println(line);
			troubledLines.add(line);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		} finally {
			service.shutdown();
		}

		return items;
	}
}
