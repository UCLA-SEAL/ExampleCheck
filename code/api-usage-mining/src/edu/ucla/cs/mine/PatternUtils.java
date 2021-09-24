package edu.ucla.cs.mine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.CATCH;
import edu.ucla.cs.model.ControlConstruct;
import edu.ucla.cs.model.ExtendedAPICall;

public class PatternUtils {
	
	public static HashMap<String, ArrayList<String>> readAPISequences(String path){
		HashMap<String, ArrayList<String>> seqs = new HashMap<String, ArrayList<String>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))){
			String line;
			while((line = br.readLine()) != null) {
				if(line.contains("---")){
					String id = line.split("---")[0];
					String s = line.split("---")[1];
					s = s.substring(1, s.length() - 1);
					ArrayList<String> seq = new ArrayList<String>();
					for(String api : s.split(", ")){
						seq.add(api.trim());
					}
					seqs.put(id, seq);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return seqs;
	}
	
	public static HashMap<String, ArrayList<String>> readOnlyOneSequenceFromEachProject(String path) {
		HashMap<String, ArrayList<String>> seqs = new HashMap<String, ArrayList<String>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))){
			String line;
			while((line = br.readLine()) != null) {
				if(line.contains("---")){
					String id = line.split("---")[0];
					String project = id.split("\\*\\*")[0];
					String s = line.split("---")[1];
					s = s.substring(1, s.length() - 1);
					ArrayList<String> seq = new ArrayList<String>();
					for(String api : s.split(", ")){
						seq.add(api.trim());
					}
					if(!seqs.containsKey(project)) {
						seqs.put(project, seq);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return seqs;
	}
	
	/**
	 * 
	 * Handles both the original pattern format without types and the extended pattern format with types
	 * 
	 * @param pattern
	 * @return
	 */
	public static ArrayList<String> extractSequenceWithoutGuard(ArrayList<APISeqItem> pattern) {
		ArrayList<String> patternS = new ArrayList<String>();
		
		for(APISeqItem item : pattern) {
			if (item instanceof APICall) {
				APICall call = (APICall)item;
				patternS.add(call.name);
			} else if (item instanceof ExtendedAPICall) {
				ExtendedAPICall call = (ExtendedAPICall)item;
				String sign = call.name + "(";
				for(int i = 0; i < call.arguments.size(); i++) {
					if (i == call.arguments.size() - 1) {
						sign += call.arguments.get(i);
					} else {
						sign += call.arguments.get(i) + ",";
					}
					
				}
				sign += ")";
				patternS.add(sign);
			} else if (item instanceof ControlConstruct) {
				ControlConstruct construct = (ControlConstruct)item;
				if(construct == ControlConstruct.CATCH) {
					patternS.add("CATCH {");
				} else if (construct == ControlConstruct.ELSE) {
					patternS.add("ELSE {");
				} else if (construct == ControlConstruct.END_BLOCK) {
					patternS.add("}");
				} else if (construct == ControlConstruct.FINALLY) {
					patternS.add("FINALLY {");
				} else if (construct == ControlConstruct.IF) {
					patternS.add("IF {");
				} else if (construct == ControlConstruct.LOOP) {
					patternS.add("LOOP {");
				} else if (construct == ControlConstruct.TRY) {
					patternS.add("TRY {");
				}
			} else if (item instanceof CATCH) {
				CATCH catchBlock = (CATCH) item;
				patternS.add("CATCH(" + catchBlock.type + ") {");
			}
		}
		
		return patternS;
	}
	
	public static ArrayList<APISeqItem> convertStringToPattern(String pattern) {
		String[] strSeqItems = pattern.split(", ");
		ArrayList<APISeqItem> patternArray = new ArrayList<APISeqItem>();
		for (String strItem : strSeqItems) {
            // instantiate either a ControlConstruct or 
            // an APICallItem and add to currentPattern 
        	strItem = strItem.trim();
            switch (strItem) {
                case "TRY": 
                    patternArray.add(ControlConstruct.TRY);
                    break; 
                case "FINALLY": 
                    patternArray.add(ControlConstruct.FINALLY);
                    break; 
                case "IF": 
                    patternArray.add(ControlConstruct.IF);
                    break; 
                case "ELSE": 
                    patternArray.add(ControlConstruct.ELSE);
                    break; 
                case "LOOP": 
                	patternArray.add(ControlConstruct.LOOP);
                	break; 
                case "END_BLOCK": 
                	patternArray.add(ControlConstruct.END_BLOCK);
                	break; 
                default:
                	if(strItem.contains("CATCH")) {
                		// catch block
                		patternArray.add(ControlConstruct.CATCH);
                	} else {
                        String name = strItem.substring(0, strItem.indexOf('('));
                        String args = strItem.substring(strItem.indexOf('(') + 1, strItem.indexOf(')'));

                        int argCount;
                        if(args.trim().isEmpty()) {
                        	argCount = 0;
                        } else {
                        	argCount = args.split(",").length;
                        }
                        
                        String guard = strItem.substring(strItem.indexOf('@') + 1);
                        patternArray.add(new APICall(name, guard, argCount));
                	}
            } 
        } 
		 
		return patternArray;
	}
	
	public static ArrayList<APISeqItem> convertStringToExtendedPattern(String pattern) {
		String[] strSeqItems = pattern.split(", ");
		ArrayList<APISeqItem> patternArray = new ArrayList<APISeqItem>();
		for (String strItem : strSeqItems) {
            // instantiate either a ControlConstruct or
            // an APICallItem and add to currentPattern
        	strItem = strItem.trim();
            switch (strItem) {
                case "TRY":
                    patternArray.add(ControlConstruct.TRY);
                    break;
                case "FINALLY":
                    patternArray.add(ControlConstruct.FINALLY);
                    break;
                case "IF":
                    patternArray.add(ControlConstruct.IF);
                    break;
                case "ELSE":
                    patternArray.add(ControlConstruct.ELSE);
                    break;
                case "LOOP":
                	patternArray.add(ControlConstruct.LOOP);
                	break;
                case "END_BLOCK":
                	patternArray.add(ControlConstruct.END_BLOCK);
                	break;
                default: 
                	if(strItem.startsWith("CATCH(") && strItem.endsWith(")")) {
                		CATCH catchClause = new CATCH(strItem.substring(strItem.indexOf('(') + 1, strItem.indexOf(')')));
                		patternArray.add(catchClause);
                	} else {
                		String signature = strItem.substring(0, strItem.indexOf('@'));
                		String guard = strItem.substring(strItem.indexOf('@') + 1);
                		String name = signature.substring(0, signature.indexOf('('));
                		String args = signature.substring(signature.indexOf('(') + 1, signature.indexOf(')'));
                		ArrayList<String> argArray = new ArrayList<String>();
                		if(!args.isEmpty()) {
                			String[] ss = args.split(",");
                			for(String s : ss) {
                				argArray.add(s);
                			}
                		}
                		ExtendedAPICall call = new ExtendedAPICall(name, guard, null, argArray);
                        patternArray.add(call);
                	}
            }
        }
		
		return patternArray;
	}
}
