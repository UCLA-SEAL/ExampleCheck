package edu.ucla.cs.evaluate.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.check.UseChecker;
import edu.ucla.cs.check.Utils;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.Answer;
import edu.ucla.cs.model.ControlConstruct;
import edu.ucla.cs.model.Violation;
import edu.ucla.cs.search.Search;

public class CountUnreliableSnippetsTop5 {
private HashMap<Answer, ArrayList<Violation>> violations;
	
	public CountUnreliableSnippetsTop5() {
		violations = new HashMap<Answer, ArrayList<Violation>>();
	}
	
	private void detectAPIMisuse(HashSet<Answer> answers, HashSet<ArrayList<APISeqItem>> patterns) {
		HashMap<Answer, ArrayList<Violation>> vios = Utils.detectAnomaly(answers, patterns);
		for(Answer a : vios.keySet()) {
			ArrayList<Violation> vv;
			if(violations.containsKey(a)) {
				vv = violations.get(a);
			} else {
				vv = new ArrayList<Violation>();
			}
			vv.addAll(vios.get(a));
			violations.put(a, vv);
		}
	}
	
	private void detectAPIMisuse2(HashSet<Answer> answers, HashSet<HashSet<ArrayList<APISeqItem>>> pset) {
		for(HashSet<ArrayList<APISeqItem>> patterns : pset) {
			HashMap<Answer, ArrayList<Violation>> temp = new UseChecker().check(patterns, answers);
			for(Answer a : temp.keySet()) {
				if(violations.containsKey(a)) {
					ArrayList<Violation> arr = violations.get(a);
					arr.addAll(temp.get(a));
					violations.put(a, arr);
				} else {
					violations.put(a, temp.get(a));
				}
			}
		}
	}
	
	private void checkByteBuffer() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ByteBuffer");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("put(1)");
		apis.add("getInt(0)"); // specify the number of arguments to be 0 to refer to relative get
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("put", "true", 1));
		pattern1.add(ControlConstruct.IF);
		pattern1.add(ControlConstruct.END_BLOCK);
		pattern1.add(new APICall("getInt", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("put", "true", 1));
		pattern2.add(new APICall("getInt", "true", 0));
		pattern1.add(ControlConstruct.IF);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("allocate", "true", 1));
		pattern3.add(new APICall("put", "true", 1));
		pattern3.add(new APICall("getInt", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("put", "true", 1));
		pattern4.add(ControlConstruct.IF);
		pattern4.add(ControlConstruct.END_BLOCK);
		pattern4.add(new APICall("getInt", "true", 0));
		pattern4.add(ControlConstruct.IF);
		pattern4.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		ArrayList<APISeqItem> pattern5 = new ArrayList<APISeqItem>();
		pattern5.add(new APICall("put", "true", 1));
		pattern5.add(new APICall("position", "true", 1));
		pattern5.add(new APICall("getInt", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns5 = new HashSet<ArrayList<APISeqItem>>();
		patterns5.add(pattern5);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		pset.add(patterns5);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkCreateNewFile() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("createNewFile(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(ControlConstruct.IF);
		pattern1.add(new APICall("createNewFile", "!rcv.exists()", 0));
		pattern1.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("createNewFile", "true", 0));
		pattern2.add(ControlConstruct.IF);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		patterns1.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(ControlConstruct.TRY);
		pattern3.add(new APICall("createNewFile", "!rcv.exists()", 0));
		pattern3.add(ControlConstruct.END_BLOCK);
		pattern3.add(ControlConstruct.CATCH);
		pattern3.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern3);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkDataOutputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("DataOutputStream");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("write", "true", 1));
		pattern1.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("new DataOutputStream", "true", 1));
		pattern2.add(new APICall("write", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkDialogDismiss() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ProgressDialog");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("dismiss(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("dismiss", "rcv.isShowing()", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("dismiss", "rcv != null", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkFileChannel() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("FileChannel");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("write", "true", 1));
		pattern1.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("write", "true", 1));
		pattern2.add(ControlConstruct.END_BLOCK);
		pattern2.add(ControlConstruct.CATCH);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkFileInputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("new FileInputStream(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("new FileInputStream", "arg0.exists()", 1));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("new FileInputStream", "true", 1));
		pattern2.add(ControlConstruct.END_BLOCK);
		pattern2.add(ControlConstruct.CATCH);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		detectAPIMisuse(result, patterns);
	}
	
	private void checkFindViewById() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Activity");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("findViewById(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("findViewById", "true", 1));
		pattern1.add(ControlConstruct.IF);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
	}
	
	private void checkFirstKey() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("firstKey(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("firstKey", "!(rcv==null||rcv.isEmpty())", 0));
				
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("firstKey", "rcv.size() > 0", 0));
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("firstKey", "!rcv.isEmpty()", 0));
			
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		patterns.add(pattern3);
		
		detectAPIMisuse(result, patterns);
	}
	
	private void checkGetBytes() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("String");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis1 = new ArrayList<String>();
		apis1.add("getBytes(0)");
		ArrayList<String> apis2 = new ArrayList<String>();
		apis2.add("getBytes(1)");
		queries.add(apis1);
		queries.add(apis2);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("getBytes", "true", 1));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("getBytes", "true", 0));
			
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		detectAPIMisuse(result, patterns);
	}
	
	private void checkHashMapGet() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("HashMap");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("get(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("get", "true", 1));
		pattern1.add(ControlConstruct.IF);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("get", "rcv.containsKey(arg0,)", 1));
		pattern2.add(ControlConstruct.IF);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(ControlConstruct.IF);
		pattern3.add(new APICall("get", "rcv.containsKey(arg0,)", 1));
		pattern3.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		patterns1.add(pattern2);
		patterns1.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("get", "rcv != null", 1));
		pattern4.add(ControlConstruct.IF);
		pattern4.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern4);
		
		ArrayList<APISeqItem> pattern5 = new ArrayList<APISeqItem>();
		pattern5.add(ControlConstruct.IF);
		pattern5.add(new APICall("get", "rcv != null", 1));
		pattern5.add(ControlConstruct.END_BLOCK);
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern5);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkInit() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Cipher");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("init(2)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(ControlConstruct.TRY);
		pattern1.add(new APICall("init", "true", 2));
		pattern1.add(ControlConstruct.END_BLOCK);
		pattern1.add(ControlConstruct.CATCH);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("getInstance", "true", 1));
		pattern2.add(new APICall("init", "true", 2));
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("init", "true", 2));
		pattern3.add(new APICall("doFinal", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkInputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("InputStream");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("read(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("read", "true", 1));
		pattern1.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("read", "true", 1));
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("read", "true", 1));
		pattern3.add(ControlConstruct.IF);
		pattern3.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkJsonElement() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("JsonElement");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("getAsString(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("getAsString", "rcv.isJsonPrimitive()", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("getAsString", "rcv != null", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkListGet() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ArrayList");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("get(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("get", "arg0 < rcv.size()", 1));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.LOOP);
		pattern2.add(new APICall("get", "arg0 < rcv.size()", 1));
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		detectAPIMisuse(result, patterns);
	}
	
	private void checkLoadIcon() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		//types.add("ApplicationInfo");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("loadIcon(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(ControlConstruct.TRY);
		pattern1.add(new APICall("loadIcon", "true", 1));
		pattern1.add(ControlConstruct.END_BLOCK);
		pattern1.add(ControlConstruct.CATCH);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("loadIcon", "true", 1));
		pattern2.add(ControlConstruct.IF);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("getPackageManager", "true", 0));
		pattern3.add(new APICall("loadIcon", "true", 1));
		pattern3.add(ControlConstruct.IF);
		pattern3.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("getPackageManager", "true", 0));
		pattern4.add(new APICall("loadIcon", "true", 1));
		pattern4.add(ControlConstruct.IF);
		pattern4.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkMacDoFinal() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("String");
		types.add("Mac");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis1 = new ArrayList<String>();
		apis1.add("getBytes(0)");
		apis1.add("doFinal(1)");
		ArrayList<String> apis2 = new ArrayList<String>();
		apis2.add("getBytes(1)");
		apis2.add("doFinal(1)");
		queries.add(apis1);
		queries.add(apis2);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("getBytes", "true", 0));
		pattern1.add(new APICall("doFinal", "true", 1));
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("getBytes", "true", 1));
		pattern3.add(new APICall("doFinal", "true", 1));
			
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("doFinal", "true", 1));
		pattern2.add(ControlConstruct.END_BLOCK);
		pattern2.add(ControlConstruct.CATCH);
		pattern2.add(ControlConstruct.END_BLOCK);
			
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);

		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkMacDoFinal2() {
		Search search = new Search();
		
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
		
		HashSet<Answer> result = search.search(types, queries);
		
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
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkMkdirs() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> q1 = new ArrayList<String>();
		q1.add("mkdirs(0)");
		queries.add(q1);
		ArrayList<String> q2 = new ArrayList<String>();
		q2.add("mkdir(0)");
		queries.add(q2);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("mkdirs", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
	}
	
	private void checkNext() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Iterator");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("next(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("iterator", "true", 0));
		pattern1.add(ControlConstruct.LOOP);
		pattern1.add(new APICall("next", "rcv.hasNext()", 0));
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("next", "rcv.hasNext()", 0));
		pattern3.add(ControlConstruct.IF);
		pattern3.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern3);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkNextToken() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("StringTokenizer");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("nextToken(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("nextToken", "rcv.hasMoreTokens()", 0));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.LOOP);
		pattern2.add(new APICall("nextToken", "rcv.hasMoreTokens()", 0));
		pattern2.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(ControlConstruct.IF);
		pattern4.add(new APICall("nextToken", "rcv.hasMoreTokens()", 0));
		pattern4.add(ControlConstruct.END_BLOCK);
		

		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		patterns1.add(pattern2);
		patterns1.add(pattern4);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("nextToken", "rcv.hasMoreTokens()", 0));
		pattern3.add(ControlConstruct.IF);
		pattern3.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern3);
		
		ArrayList<APISeqItem> pattern5 = new ArrayList<APISeqItem>();
		pattern5.add(new APICall("new StringTokenizer", "true", 2));
		pattern5.add(new APICall("nextToken", "rcv.hasMoreTokens()", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern5);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkPrintWriterClose() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("PrintWriter");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("close(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("new PrintWriter", "true", 1));
		pattern1.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("close", "true", 0));
		pattern2.add(ControlConstruct.END_BLOCK);
		pattern2.add(ControlConstruct.CATCH);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("println", "true", 1));
		pattern3.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("new PrintWriter", "true", 1));
		pattern4.add(new APICall("println", "true", 1));
		pattern4.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		ArrayList<APISeqItem> pattern5 = new ArrayList<APISeqItem>();
		pattern5.add(ControlConstruct.TRY);
		pattern5.add(new APICall("new PrintWriter", "true", 1));
		pattern5.add(new APICall("close", "true", 0));
		pattern5.add(ControlConstruct.END_BLOCK);
		pattern5.add(ControlConstruct.CATCH);
		pattern5.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns5 = new HashSet<ArrayList<APISeqItem>>();
		patterns5.add(pattern5);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		pset.add(patterns5);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkPrintWriterWrite() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("PrintWriter");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("write", "true", 1));
		pattern1.add(new APICall("close", "true", 0));
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("write", "true", 1));
		pattern3.add(new APICall("flush", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		patterns1.add(pattern3);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("write", "true", 1));
		pattern2.add(ControlConstruct.END_BLOCK);
		pattern2.add(ControlConstruct.CATCH);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("getWriter", "true", 0));
		pattern4.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern4);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkRandomFileAccessFileClose() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("close(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("new RandomAccessFile", "true", 2));
		pattern1.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("close", "true", 0));
		pattern2.add(ControlConstruct.END_BLOCK);
		pattern2.add(ControlConstruct.CATCH);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("seek", "true", 1));
		pattern3.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(ControlConstruct.TRY);
		pattern4.add(new APICall("new RandomAccessFile", "true", 2));
		pattern4.add(new APICall("close", "true", 0));
		pattern4.add(ControlConstruct.END_BLOCK);
		pattern4.add(ControlConstruct.CATCH);
		pattern4.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		ArrayList<APISeqItem> pattern5 = new ArrayList<APISeqItem>();
		pattern5.add(ControlConstruct.FINALLY);
		pattern5.add(new APICall("close", "true", 0));
		pattern5.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns5 = new HashSet<ArrayList<APISeqItem>>();
		patterns5.add(pattern5);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		pset.add(patterns5);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkRandomFileAccessFileRead() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("read(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("read", "true", 1));
		pattern1.add(ControlConstruct.IF);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("read", "true", 1));
		pattern2.add(ControlConstruct.END_BLOCK);
		pattern2.add(ControlConstruct.CATCH);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("new RandomAccessFile", "true", 2));
		pattern3.add(new APICall("read", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("read", "true", 1));
		pattern4.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkRandomFileAccessFileWrite() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("write", "true", 1));
		pattern1.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(ControlConstruct.TRY);
		pattern2.add(new APICall("write", "true", 1));
		pattern2.add(ControlConstruct.END_BLOCK);
		pattern2.add(ControlConstruct.CATCH);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("new RandomAccessFile", "true", 2));
		pattern3.add(new APICall("write", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("seek", "true", 1));
		pattern4.add(new APICall("write", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkSetContentView() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
//		types.add("Activity");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("setContentView(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("super.onCreate", "true", 1));
		pattern1.add(new APICall("setContentView", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
	}
	
	private void checkSetPreferredSize() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("JFrame");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("setPreferredSize(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("new Dimension", "true", 2));
		pattern1.add(new APICall("setPreferredSize", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("setPreferredSize", "true", 1));
		pattern2.add(new APICall("setVisible", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("new Dimension", "true", 2));
		pattern3.add(new APICall("setPreferredSize", "true", 1));
		pattern3.add(new APICall("setVisible", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("setPreferredSize", "true", 1));
		pattern4.add(new APICall("add", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		ArrayList<APISeqItem> pattern5 = new ArrayList<APISeqItem>();
		pattern5.add(new APICall("setPreferredSize", "true", 1));
		pattern5.add(new APICall("pack", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns5 = new HashSet<ArrayList<APISeqItem>>();
		patterns5.add(pattern5);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		pset.add(patterns5);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkSimpleDateFormat() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> q1 = new ArrayList<String>();
		q1.add("new SimpleDateFormat(2)");
		ArrayList<String> q2 = new ArrayList<String>();
		q2.add("new SimpleDateFormat(1)");
		queries.add(q1);
		queries.add(q2);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("new SimpleDateFormat", "true", 1));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("new SimpleDateFormat", "true", 2));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		detectAPIMisuse(result, patterns);
	}
	
	private void checkSQLiteDatabase() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("SQLiteDatabase");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("query(7)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("query", "true", 7));
		pattern1.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("query", "true", 7));
		pattern2.add(ControlConstruct.IF);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		
		detectAPIMisuse2(result, pset);
	}
	
	private void checkTypeArray() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("TypedArray");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("getString(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(new APICall("getString", "true", 1));
		pattern1.add(ControlConstruct.IF);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns1 = new HashSet<ArrayList<APISeqItem>>();
		patterns1.add(pattern1);
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("getString", "true", 1));
		pattern2.add(new APICall("recycle", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns2 = new HashSet<ArrayList<APISeqItem>>();
		patterns2.add(pattern2);
		
		ArrayList<APISeqItem> pattern3 = new ArrayList<APISeqItem>();
		pattern3.add(new APICall("obtainStyledAttributes", "true", 2));
		pattern3.add(new APICall("getString", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns3 = new HashSet<ArrayList<APISeqItem>>();
		patterns3.add(pattern3);
		
		ArrayList<APISeqItem> pattern4 = new ArrayList<APISeqItem>();
		pattern4.add(new APICall("getString", "true", 1));
		pattern4.add(ControlConstruct.IF);
		pattern4.add(ControlConstruct.END_BLOCK);
		pattern4.add(new APICall("recycle", "true", 1));
		
		HashSet<ArrayList<APISeqItem>> patterns4 = new HashSet<ArrayList<APISeqItem>>();
		patterns4.add(pattern4);
		
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		pset.add(patterns1);
		pset.add(patterns2);
		pset.add(patterns3);
		pset.add(patterns4);
		
		detectAPIMisuse2(result, pset);
	}
	
	public void check() {
		checkByteBuffer();
		checkCreateNewFile();
		checkDataOutputStream();
		checkDialogDismiss();
		checkFileChannel();
		checkFileInputStream();
		checkFindViewById();
		checkFirstKey();
		checkGetBytes();
		checkHashMapGet();
		checkInit();
		checkInputStream();
		checkJsonElement();
		checkListGet();
		checkLoadIcon();
		checkMacDoFinal();
		checkMacDoFinal2();
		checkMkdirs();
		checkNext();
		checkNextToken();
		checkPrintWriterClose();
		checkPrintWriterWrite();
		checkRandomFileAccessFileClose();
		checkRandomFileAccessFileRead();
		checkRandomFileAccessFileWrite();
		checkSetContentView();
		checkSetPreferredSize();
		checkSimpleDateFormat();
		checkSQLiteDatabase();
		checkTypeArray();
	}
	
	public void count() {
		int total = violations.size();
		
		int recognized = 0;
		int accepted = 0;
		int viewCount = 0;
		int score = 0;
		HashMap<Integer, Integer> distr = new HashMap<Integer, Integer>();
		for(Answer a : violations.keySet()) {
			if(a.isAccepted || a.score > 0) {
				recognized ++;
				
				if(a.isAccepted) {
					accepted ++;
				}
				
				int value = 0;
				if(distr.containsKey(a.score)) {
					value = distr.get(a.score);
				}
				value ++;
				distr.put(a.score, value);
			}
			
			viewCount+=a.viewCount;
			score +=a.score;
		}
		
		int unrecognized = total - recognized;

		System.out.println("Total Unreliable Snippets: " + total);
		System.out.println("Recognized & Unreliable Snippets: " + recognized);
		System.out.println("Unrecognized & Unreliable Snippets: " + unrecognized);
		System.out.println("Accepted & Unreliable Snippets: " + accepted);
		
		System.out.println("Average View Count (Total):" + ((double)viewCount)/total);
		System.out.println("Average Score (Total):" + ((double)score)/total);
		
		System.out.println("Average Score (Recognized):" + ((double)score)/recognized);
				
		for(Integer sc : distr.keySet()) {
			System.out.println(sc + "," + distr.get(sc));
		}
	}
			
	public static void main(String[] args) {
		CountUnreliableSnippetsTop3 c = new CountUnreliableSnippetsTop3();
		c.check();
		c.count();
	}
}
