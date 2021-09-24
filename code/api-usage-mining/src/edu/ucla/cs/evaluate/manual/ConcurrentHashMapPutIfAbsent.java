package edu.ucla.cs.evaluate.manual;

import java.util.ArrayList;
import java.util.HashSet;

import edu.ucla.cs.check.APIMisuseDetection;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ControlConstruct;

public class ConcurrentHashMapPutIfAbsent {
	public static void main(String[] args) {
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("get", "true", 1));
		pattern1.add(new APICall("putIfAbsent", "arg1==null", 2));
		pattern1.add(ControlConstruct.IF);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		HashSet<String> types = new HashSet<String>();
		types.add("ConcurrentHashMap");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis1 = new ArrayList<String>();
		apis1.add("get(1)");
		apis1.add("put(2)"); 
		ArrayList<String> apis2 = new ArrayList<String>();
		apis2.add("get(1)");
		apis2.add("putIfAbsent(2)");
		queries.add(apis1);
		queries.add(apis2);
		
		APIMisuseDetection detect = new APIMisuseDetection(types, queries, patterns);
		detect.run();
	}
}
