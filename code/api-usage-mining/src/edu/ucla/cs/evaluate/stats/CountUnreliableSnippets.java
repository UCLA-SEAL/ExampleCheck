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

public class CountUnreliableSnippets {
	private HashMap<Answer, ArrayList<Violation>> violations;
	
	public CountUnreliableSnippets() {
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
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(new APICall("createNewFile", "!rcv.exists()", 0));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("createNewFile", "true", 0));
		pattern2.add(ControlConstruct.IF);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(new APICall("firstKey", "!rcv.isEmpty()", 0));
				
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("firstKey", "rcv.size() > 0", 0));
			
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
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
			
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
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
		pattern1.add(new APICall("get", "rcv.containsKey(arg0,)", 1));
		
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(new APICall("get", "true", 1));
		pattern2.add(ControlConstruct.IF);
		pattern2.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(new APICall("getBytes", "true", 1));
		pattern1.add(new APICall("doFinal", "true", 1));
			
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(new APICall("next", "rcv.hasNext()", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		pattern2.add(new APICall("nextToken", "rcv.hasMoreElements()", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(ControlConstruct.FINALLY);
		pattern1.add(new APICall("close", "true", 0));
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(ControlConstruct.FINALLY);
		pattern1.add(new APICall("close", "true", 0));
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(new APICall("close", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(new APICall("setPreferredSize", "true", 1));
		pattern1.add(new APICall("pack", "true", 0));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(new APICall("new SimpleDateFormat", "true", 2));
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
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
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		pattern1.add(ControlConstruct.TRY);
		pattern1.add(new APICall("getString", "true", 1));
		pattern1.add(ControlConstruct.END_BLOCK);
		pattern1.add(ControlConstruct.CATCH);
		pattern1.add(ControlConstruct.END_BLOCK);
		
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		
		detectAPIMisuse(result, patterns);
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
		CountUnreliableSnippets c = new CountUnreliableSnippets();
		c.check();
		c.count();
	}
}
