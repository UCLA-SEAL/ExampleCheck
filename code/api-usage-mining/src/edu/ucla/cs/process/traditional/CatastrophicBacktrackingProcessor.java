package edu.ucla.cs.process.traditional;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.utils.ProcessUtils;

public class CatastrophicBacktrackingProcessor extends SequenceProcessor{
	
	String line;
	
	@Override
	void process(String line) {
		// update current line
		this.line = line;
		
		Method m = getMethodInstance(line);
		buildSequenceMap(m, line);
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
						items.add(apiName + "(" + arguments.size() + ")");
						
						// then process the rest of the API calls in the chain (if any)
						if(rest != null) {
							ArrayList<String> apis3 = extractMethodCalls(rest);
							items.addAll(apis3);
						}
					} else {
						items.add(apiName + "(0)");
					}
				}
				return "yes";
			});

			f.get(1, TimeUnit.SECONDS);
		} catch (final TimeoutException e) {
			// catastrophic backtracking
			System.out.println("Regex runs more than 1 second! Ignore it!");
			System.out.println(line);
			CatastrophicBacktrackingProcess.troubledLines.add(line);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		} finally {
			service.shutdown();
		}

		return items;
	}
}
