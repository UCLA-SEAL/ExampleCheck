package edu.ucla.cs.evaluate.manual;

import java.util.ArrayList;
import java.util.HashSet;

import edu.ucla.cs.check.APIMisuseDetection;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;

public class MacDoFinal2 {
	public static void main(String[] args) {
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("doFinal", "true", 1));
		pattern1.add(new APICall("encode", "true", 1));
		pattern1.add(new APICall("new String", "true", 1));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("doFinal", "true", 1));
		pattern2.add(new APICall("new String", "true", 2));
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("doFinal", "true", 1));
		pattern3.add(new APICall("encodeBase64", "true", 1));
		pattern3.add(new APICall("new String", "true", 1));
			
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		patterns.add(pattern3);
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis1 = new ArrayList<String>();
		apis1.add("doFinal(1)");
		apis1.add("new String(1)");
		ArrayList<String> apis2 = new ArrayList<String>();
		apis2.add("doFinal(1)");
		apis2.add("new String(2)");
		queries.add(apis1);
		queries.add(apis2);
		
		APIMisuseDetection detect = new APIMisuseDetection(types, queries, patterns);
		detect.run();
	}
}
