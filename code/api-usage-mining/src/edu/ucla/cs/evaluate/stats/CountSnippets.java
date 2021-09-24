package edu.ucla.cs.evaluate.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.ucla.cs.model.Answer;
import edu.ucla.cs.search.Search;

public class CountSnippets {
	private HashSet<Answer> answers;
	
	public CountSnippets() {
		answers = new HashSet<Answer>();
	}
	
	private void searchByteBuffer() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ByteBuffer");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("put(1)");
		apis.add("getInt(0)"); // specify the number of arguments to be 0 to refer to relative get
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchCreateNewFile() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("createNewFile(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchDataOutputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("DataOutputStream");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchDialogDismiss() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ProgressDialog");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("dismiss(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchFileChannel() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("FileChannel");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchFileInputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("new FileInputStream(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchFindViewById() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Activity");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("findViewById(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchFirstKey() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("firstKey(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchGetBytes() {
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
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchHashMapGet() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("HashMap");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("get(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchInit() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Cipher");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("init(2)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchInputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("InputStream");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("read(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchJsonElement() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("JsonElement");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("getAsString(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchListGet() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ArrayList");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("get(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchLoadIcon() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		//types.add("ApplicationInfo");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("loadIcon(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchMacDoFinal() {
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
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchMacDoFinal2() {
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
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchMkdirs() {
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
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchNext() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Iterator");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("next(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchNextToken() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("StringTokenizer");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("nextToken(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchPrintWriterClose() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("PrintWriter");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("close(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchPrintWriterWrite() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("PrintWriter");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchRandomFileAccessFileClose() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("close(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchRandomFileAccessFileRead() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("read(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchRandomFileAccessFileWrite() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchSetContentView() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
//		types.add("Activity");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("setContentView(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchSetPreferredSize() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("JFrame");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("setPreferredSize(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchSimpleDateFormat() {
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
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchSQLiteDatabase() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("SQLiteDatabase");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("query(7)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	private void searchTypeArray() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("TypedArray");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("getString(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		for(Answer answer : result) {
			if(!answers.contains(answer)) {
				answers.add(answer);
			}
		}
	}
	
	public void search() {
		searchByteBuffer();
		searchCreateNewFile();
		searchDataOutputStream();
		searchDialogDismiss();
		searchFileChannel();
		searchFileInputStream();
		searchFindViewById();
		searchFirstKey();
		searchGetBytes();
		searchHashMapGet();
		searchInit();
		searchInputStream();
		searchJsonElement();
		searchListGet();
		searchLoadIcon();
		searchMacDoFinal();
		searchMacDoFinal2();
		searchMkdirs();
		searchNext();
		searchNextToken();
		searchPrintWriterClose();
		searchPrintWriterWrite();
		searchRandomFileAccessFileClose();
		searchRandomFileAccessFileRead();
		searchRandomFileAccessFileWrite();
		searchSetContentView();
		searchSetPreferredSize();
		searchSimpleDateFormat();
		searchSQLiteDatabase();
		searchTypeArray();
	}
	
	public void count() {
		System.out.println("Total Snippets: " + answers.size());
		
		int inComplete = 0;
		int recognized = 0;
		int accepted = 0;
		int viewCount = 0;
		int score = 0;
		HashMap<Integer, Integer> distr = new HashMap<Integer, Integer>();
		for(Answer a : answers) {
			if(a.containsIncompleteSnippet) {
				inComplete ++;
			}
			
			if(a.isAccepted || a.score > 0) {
				recognized ++;
				
				if(a.isAccepted) {
					accepted++;
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
		
		System.out.println("Incomplete Snippets: " + inComplete);
		
		System.out.println("Recognized Snippets: " + recognized);
		System.out.println("Unrecognized Snippets: " + recognized);
		System.out.println("Accepted Snippets: " + accepted);
		
		System.out.println("Average View Count (Total):" + ((double)viewCount)/answers.size());
		System.out.println("Average Score (Total):" + ((double)score)/answers.size());
		System.out.println("Average Score (Recognized):" + ((double)score)/recognized);
		
		for(Integer sc : distr.keySet()) {
			System.out.println(sc + "," + distr.get(sc));
		}
	}
			
	public static void main(String[] args) {
		CountSnippets c = new CountSnippets();
		c.search();
		c.count();
	}
}
