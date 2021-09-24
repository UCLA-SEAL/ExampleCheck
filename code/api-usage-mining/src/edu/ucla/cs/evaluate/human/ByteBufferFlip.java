package edu.ucla.cs.evaluate.human;

import java.util.ArrayList;
import java.util.HashSet;

import edu.ucla.cs.check.APIMisuseDetection;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;

public class ByteBufferFlip {
	public static void main(String[] args) {
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("put", "true", 1));
		pattern1.add(new APICall("flip", "true", 0));
		pattern1.add(new APICall("getInt", "true", 0));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("put", "true", 1));
		pattern2.add(new APICall("rewind", "true", 0));
		pattern2.add(new APICall("getInt", "true", 0));
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("put", "true", 1));
		pattern3.add(new APICall("position", "true", 1));
		pattern3.add(new APICall("getInt", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		patterns.add(pattern3);
		
		HashSet<String> types = new HashSet<String>();
		types.add("ByteBuffer");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("put(1)");
		apis.add("getInt(0)"); // specify the number of arguments to be 0 to refer to relative get
		queries.add(apis);
		
		APIMisuseDetection detect = new APIMisuseDetection(types, queries, patterns);
		detect.run();
	}
}
