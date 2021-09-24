package edu.ucla.cs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class ProcessUtils {
//	public static ArrayList<String> splitByArrow(String s) {
//		ArrayList<String> ss = new ArrayList<String>();
//		String[] arr = s.split("->");
//		int index = 0;
//		for(int i = 0; i < arr.length; i++) {
//			String item = arr[i];
//			if(!isInQuote(s, index)) {
//				ss.add(item);
//			} else {
//				String last = ss.get(ss.size() - 1);
//				ss.remove(ss.size() - 1);
//				ss.add(last + "->" + item);
//			}
//			index += item.length() + 2;
//		}
//		
//		return ss;
//	}
	
	public static ArrayList<String> splitByArrow(String s) {
		ArrayList<String> ss = new ArrayList<String>();
		char[] chars = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		for(int i = 0; i < chars.length; i++) {
			char cur = chars[i];
			if(cur == '"' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// double quote ends
					inDoubleQuote = false;
					sb.append(cur);
				} else {
					// escape quote, not the end of the quote
					sb.append(cur);
				}
			} else if(cur == '"' && !inSingleQuote && !inDoubleQuote) {
				// double quote starts
				inDoubleQuote = true;
				sb.append(cur);
			} else if (cur == '\'' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// single quote ends
					inSingleQuote = false;
					sb.append(cur);
				} else {
					// escape single quote, not the end of the quote
					sb.append(cur);
				}
			} else if(cur == '\'' && !inDoubleQuote && !inSingleQuote) {
				// single quote starts
				inSingleQuote = true;
				sb.append(cur);
			} else if(cur == '"' && !inSingleQuote && inDoubleQuote) {
				// quote ends
				inDoubleQuote = false;
				sb.append(cur);
			} else if (cur == '\'' && inSingleQuote && !inDoubleQuote) {
				// single quote ends
				inSingleQuote = false;
				sb.append(cur);
			} else if (cur == '-' && i + 1 < chars.length && chars[i + 1] == '>' && !inSingleQuote && !inDoubleQuote) {
				i++;
				if(sb.length() > 0) {
					// push previous concatenated chars to the array
					ss.add(sb.toString());
					// clear the string builder
					sb.setLength(0);
				}
			} else {
				sb.append(cur);
			}
		}
		
		// push the last token if any
		if(sb.length() > 0) {
			ss.add(sb.toString());
		}
		
		return ss;
	}
	
	public static boolean isBalanced(String expr) {
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		int parentheses = 0;
		char[] chars = expr.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			char cur = chars[i];
			if(cur == '"' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// double quote ends
					inDoubleQuote = false;
				} else {
					// escape quote, not the end of the quote
				}
			} else if(cur == '"' && !inSingleQuote && !inDoubleQuote) {
				// double quote starts
				inDoubleQuote = true;
			} else if(cur == '\'' && !inSingleQuote && !inDoubleQuote) {
				// single quote starts
				inSingleQuote = true;
			} else if (cur == '\'' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// single quote ends
					inSingleQuote = false;
				} else {
					// escape single quote, not the end of the quote
				}
			} else if(cur == '"' && !inSingleQuote && inDoubleQuote) {
				// double quote ends
				inDoubleQuote = false;
			} else if (cur == '\'' && inSingleQuote && !inDoubleQuote) {
				// single quote ends
				inSingleQuote = false;
			} else if (inSingleQuote || inDoubleQuote) {
				// ignore all parentheses in quote
			} else if (cur == '(') {
				parentheses ++;
			} else if (cur == ')') {
				parentheses --;
				if(parentheses < 0) {
					return false;
				}
			}
		}
		
		return parentheses == 0;
	}
	
	public static int findFirstUnbalancedCloseParenthesis(String expr) {
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		int parentheses = 0;
		char[] chars = expr.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			char cur = chars[i];
			if(cur == '"' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// double quote ends
					inDoubleQuote = false;
				} else {
					// escape quote, not the end of the quote
				}
			} else if(cur == '"' && !inSingleQuote && !inDoubleQuote) {
				// double quote starts
				inDoubleQuote = true;
			} else if(cur == '\'' && !inSingleQuote && !inDoubleQuote) {
				// single quote starts
				inSingleQuote = true;
			} else if (cur == '\'' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// single quote ends
					inSingleQuote = false;
				} else {
					// escape single quote, not the end of the quote
				}
			} else if(cur == '"' && !inSingleQuote && inDoubleQuote) {
				// double quote ends
				inDoubleQuote = false;
			} else if (cur == '\'' && inSingleQuote && !inDoubleQuote) {
				// single quote ends
				inSingleQuote = false;
			} else if (inSingleQuote || inDoubleQuote) {
				// ignore all parentheses in quote
			} else if (cur == '(') {
				parentheses ++;
			} else if (cur == ')') {
				parentheses --;
				if(parentheses == -1) {
					return i;
				}
			}
		}
		
		// do not find the first unbalanced close parenthesis
		return -1;
	}
	
	public static int findFirstUnbalancedOpenParenthesis(String expr) {
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		Stack<Integer> stack = new Stack<Integer>();
		char[] chars = expr.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			char cur = chars[i];
			if(cur == '"' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// double quote ends
					inDoubleQuote = false;
				} else {
					// escape quote, not the end of the quote
				}
			} else if(cur == '"' && !inSingleQuote && !inDoubleQuote) {
				// double quote starts
				inDoubleQuote = true;
			} else if(cur == '\'' && !inSingleQuote && !inDoubleQuote) {
				// single quote starts
				inSingleQuote = true;
			} else if (cur == '\'' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// single quote ends
					inSingleQuote = false;
				} else {
					// escape single quote, not the end of the quote
				}
			} else if(cur == '"' && !inSingleQuote && inDoubleQuote) {
				// double quote ends
				inDoubleQuote = false;
			} else if (cur == '\'' && inSingleQuote && !inDoubleQuote) {
				// single quote ends
				inSingleQuote = false;
			} else if (inSingleQuote || inDoubleQuote) {
				// ignore all parentheses in quote
			} else if (cur == '(') {
				stack.push(i);
			} else if (cur == ')') {
				stack.pop();
			}
		}
		
		// do not find the first unbalanced close parenthesis
		if(!stack.isEmpty()) {
			return stack.pop();
		} else {
			return -1;
		}
	}

	public static boolean isInQuote(String s, int index) {
		if(s.contains("\"") || s.contains("'")) {
			char[] chars = s.toCharArray();
			boolean inSingleQuote = false;
			boolean inDoubleQuote = false;
			for(int i = 0; i < chars.length; i++) {
				if(i == index) {
					return inSingleQuote || inDoubleQuote;
				}
				char cur = chars[i];
				if(cur == '"' && i > 0 && chars[i-1] == '\\') {
					// count the number of backslashes
					int count = 0;
					while(i - count - 1 >= 0) {
						if(chars[i - count - 1] == '\\') {
							count ++;
						} else {
							break;
						}
					} 
					if(count % 2 == 0) {
						// escape one or more backslashes instead of this quote, end of quote
						// double quote ends
						inDoubleQuote = false;
					} else {
						// escape quote, not the end of the quote
					}
				} else if(cur == '"' && !inSingleQuote && !inDoubleQuote) {
					// double quote starts
					inDoubleQuote = true;
				} else if (cur == '\'' && i > 0 && chars[i-1] == '\\') {
					// count the number of backslashes
					int count = 0;
					while(i - count - 1 >= 0) {
						if(chars[i - count - 1] == '\\') {
							count ++;
						} else {
							break;
						}
					} 
					if(count % 2 == 0) {
						// escape one or more backslashes instead of this quote, end of quote
						// single quote ends
						inSingleQuote = false;
					} else {
						// escape single quote, not the end of the quote
					}
				} else if(cur == '\'' && !inSingleQuote && !inDoubleQuote) {
					// single quote starts
					inSingleQuote = true; 
				} else if(cur == '"' && !inSingleQuote && inDoubleQuote) {
					// double quote ends
					inDoubleQuote = false;
				} else if (cur == '\'' && inSingleQuote && !inDoubleQuote) {
					// single quote ends
					inSingleQuote = false;
				}
			}
			
			return inSingleQuote;
		} else {
			return false;
		}
	}
	
	public static ArrayList<String> getArguments(String args) {
		ArrayList<String> list = new ArrayList<String>();
		boolean inQuote = false;
		int stack = 0;
		StringBuilder sb = new StringBuilder();
		char[] chars = args.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			char cur = chars[i];
			if(cur == '"' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// quote ends
					inQuote = false;
					sb.append(cur);
				} else {
					// escape quote, not the end of the quote
					sb.append(cur);
				}
			} else if(cur == '"' && !inQuote) {
				// quote starts
				inQuote = true;
				sb.append(cur);
			} else if (cur == '\'' && i > 0 && chars[i-1] == '\\') {
				// count the number of backslashes
				int count = 0;
				while(i - count - 1 >= 0) {
					if(chars[i - count - 1] == '\\') {
						count ++;
					} else {
						break;
					}
				} 
				if(count % 2 == 0) {
					// escape one or more backslashes instead of this quote, end of quote
					// quote ends
					inQuote = false;
					sb.append(cur);
				} else {
					// escape single quote, not the end of the quote
					sb.append(cur);
				}
			} else if(cur == '\'' && !inQuote) {
				// single quote starts
				inQuote = true;
				sb.append(cur);
			} else if(cur == '"' && inQuote) {
				// quote ends
				inQuote = false;
				sb.append(cur);
			} else if (cur == '\'' && inQuote) {
				// single quote ends
				inQuote = false;
				sb.append(cur);
			} else if (cur == '(' && !inQuote) {
				// look behind to check if this is a method call
				sb.append(cur);
				stack ++;
			} else if (cur == ')' && !inQuote) {
				sb.append(cur);
				stack --;
			} else if (inQuote || stack != 0) {
				// ignore any separator in quote or in a method call
				sb.append(cur);
			} else if (cur == ',' && !inQuote && stack == 0){
				if(sb.length() > 0) {
					list.add(sb.toString());
					sb.setLength(0);
				} else {
					sb.append(cur);
				}
			} else {
				sb.append(cur);
			}
		}
		
		// push the last token if any
		if(sb.length() > 0) {
			list.add(sb.toString());
		}
		
		return list;
	}
	
	public static String readRawSequenceById(String id, String path) {
		String seq = null;
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))){
			String line = null;
			while((line = br.readLine()) != null) {
				if(line.contains(id)) {
					seq = line.substring(line.indexOf("] =") + 3).trim();
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return seq;
	}
	
	public static String[] splitByAt(String s) {
		ArrayList<String> ss = new ArrayList<String>();
		String[] arr = s.split("@");
		int index = 0;
		for(int i = 0; i < arr.length; i++) {
			String item = arr[i];
			if(!ProcessUtils.isInQuote(s, index)) {
				ss.add(item);
			} else {
				String last = ss.get(ss.size() - 1);
				ss.remove(ss.size() - 1);
				ss.add(last + "@" + item);
			}
			index += item.length() + 1;
		}
		
		if(ss.size() == 1) {
			ss.add("true");
		}
		
		String[] arr2 = new String[ss.size()];
		return ss.toArray(arr2);
	}
	
	public static boolean isAnnotatedWithType(String s) {
		if(!s.contains(":")) {
			return false;
		}
		
		int index = s.lastIndexOf(':');
		for(int i = index + 1; i < s.length(); i++) {
			char c = s.charAt(index);
			if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '[' || c == ']' || c == '<' || c == '>') {
				continue;
			} else {
				return false;
			}
		}
		
		return true;
	}
	
	public static ArrayList<String> stripOffArguments(ArrayList<String> pattern) {
		ArrayList<String> patternWithoutArguments = new ArrayList<String>();
		for(String element : pattern) {
			if(element.contains("(") && element.contains(")")) {
				// this is an API call
				String apiName = element.substring(0, element.indexOf('('));
				patternWithoutArguments.add(apiName);
			} else {
				patternWithoutArguments.add(element);
			}
		}
		return patternWithoutArguments;
	}
}
