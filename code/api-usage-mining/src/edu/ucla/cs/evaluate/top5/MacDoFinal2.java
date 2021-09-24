package edu.ucla.cs.evaluate.top5;

import java.util.ArrayList;
import java.util.HashSet;

import edu.ucla.cs.check.APIMisuseDetection;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ControlConstruct;

public class MacDoFinal2 {
	public static void main(String[] args) {
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("doFinal", "true", 1));
		pattern1.add(new APICall("new String", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("getInstance", "true", 1));
		pattern2.add(new APICall("doFinal", "true", 1));
		pattern2.add(new APICall("new String", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(ControlConstruct.TRY);
		pattern3.add(new APICall("doFinal", "true", 1));
		pattern3.add(new APICall("new String", "true", 1));
		pattern3.add(ControlConstruct.END_BLOCK);
		pattern3.add(ControlConstruct.CATCH);
		pattern3.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("init", "true", 2));
		pattern4.add(new APICall("doFinal", "true", 1));
		pattern4.add(new APICall("new String", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		ArrayList<APISeqItem> pattern5 = new ArrayList<APISeqItem>();
		pattern5.add(new APICall("new SecretKeySpec", "true", 2));
		pattern5.add(new APICall("doFinal", "true", 1));
		pattern5.add(new APICall("new String", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns5 = new HashSet<ArrayList<APISeqItem>>();
		patterns5.add(pattern5);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		pset.add(patterns5);
		
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
		
		APIMisuseDetection detect = new APIMisuseDetection(types, queries, patterns1);
		detect.run2(pset);
	}
}
