package edu.ucla.cs.evaluate.stats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import edu.ucla.cs.model.Answer;
import edu.ucla.cs.search.Search;

public class SampleSnippets {
	private void sample(HashSet<Answer> set) {
		if(set.size() <= 20) {
			// do not sample, just output all
			for(Answer a : set) {
				System.out.println("http://stackoverflow.com/questions/" + a.id);
			}
			
			return;
		}
		
		ArrayList<Answer> temp = new ArrayList<Answer>(set);
		Random rand = new Random();
		for(int i = 0; i < 20; i++) {
			int n = rand.nextInt(set.size());
			System.out.println("http://stackoverflow.com/questions/" + temp.get(n).id);
		}
	}
	
	private void sampleByteBuffer() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ByteBuffer");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("put(1)");
		apis.add("getInt(0)"); // specify the number of arguments to be 0 to refer to relative get
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("ByteBuffer.getInt");
		sample(result);
	}
	
	private void sampleCreateNewFile() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("createNewFile(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("File.createNewFile");
		sample(result);
	}
	
	private void sampleDataOutputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("DataOutputStream");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("DataOutputStream.write");
		sample(result);
	}
	
	private void sampleDialogDismiss() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ProgressDialog");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("dismiss(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("ProgressDialog.dismiss");
		sample(result);
	}
	
	private void sampleFileChannel() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("FileChannel");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("FileChannel.write");
		sample(result);
	}
	
	private void sampleFileInputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("new FileInputStream(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("new FileInputStream");
		sample(result);
	}
	
	private void sampleFindViewById() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Activity");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("findViewById(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("Activity.findViewById");
		sample(result);
	}
	
	private void sampleFirstKey() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("firstKey(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("SortedMap.firstKey");
		sample(result);
	}
	
	private void sampleGetBytes() {
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
		System.out.println("String.getBytes");
		sample(result);
	}
	
	private void sampleHashMapGet() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("HashMap");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("get(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("HashMap.get");
		sample(result);
	}
	
	private void sampleInit() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Cipher");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("init(2)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("Cipher.init");
		sample(result);
	}
	
	private void sampleInputStream() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("InputStream");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("read(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("InputStream.read");
		sample(result);
	}
	
	private void sampleJsonElement() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("JsonElement");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("getAsString(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("JsonElement.getAsString");
		sample(result);
	}
	
	private void sampleListGet() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("ArrayList");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("get(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("ArrayList.get");
		sample(result);
	}
	
	private void sampleLoadIcon() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		//types.add("ApplicationInfo");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("loadIcon(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("ApplicationInfo.loadIcon");
		sample(result);
	}
	
	private void sampleMacDoFinal() {
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
		System.out.println("String.getBytes, Mac.doFinal");
		sample(result);
	}
	
	private void sampleMacDoFinal2() {
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
		System.out.println("Mac.doFinal, new String");
		sample(result);
	}
	
	private void sampleMkdirs() {
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
		System.out.println("File.mkdirs");
		sample(result);
	}
	
	private void sampleNext() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("Iterator");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("next(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("Iterator.next");
		sample(result);
	}
	
	private void sampleNextToken() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("StringTokenizer");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("nextToken(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("StringTokenizer.nextToken");
		sample(result);
	}
	
	private void samplePrintWriterClose() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("PrintWriter");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("close(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("PrintWriter.close");
		sample(result);
	}
	
	private void samplePrintWriterWrite() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("PrintWriter");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("PrintWriter.write");
		sample(result);
	}
	
	private void sampleRandomFileAccessFileClose() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("close(0)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("RandomAccessFile.close");
		sample(result);
	}
	
	private void sampleRandomFileAccessFileRead() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("read(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("RandomAccessFile.read");
		sample(result);
	}
	
	private void sampleRandomFileAccessFileWrite() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("RandomAccessFile");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("write(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("RandomAccessFile.write");
		sample(result);
	}
	
	private void sampleSetContentView() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
//		types.add("Activity");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("setContentView(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("Activity.setContentView");
		sample(result);
	}
	
	private void sampleSetPreferredSize() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("JFrame");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("setPreferredSize(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("JFrame.setPreferredSize");
		sample(result);
	}
	
	private void sampleSimpleDateFormat() {
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
		System.out.println("new SimpleDateFormat");
		sample(result);
	}
	
	private void sampleSQLiteDatabase() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("SQLiteDatabase");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("query(7)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("SQLiteDatabase.query");
		sample(result);
	}
	
	private void sampleTypeArray() {
		Search search = new Search();
		
		HashSet<String> types = new HashSet<String>();
		types.add("TypedArray");
		HashSet<ArrayList<String>> queries = new HashSet<ArrayList<String>>();
		ArrayList<String> apis = new ArrayList<String>();
		apis.add("getString(1)");
		queries.add(apis);
		
		HashSet<Answer> result = search.search(types, queries);
		System.out.println("TypeArray.getString");
		sample(result);
	}
	
	public void sample() {
		sampleByteBuffer();
		sampleCreateNewFile();
		sampleDataOutputStream();
		sampleDialogDismiss();
		sampleFileChannel();
		sampleFileInputStream();
		sampleFindViewById();
		sampleFirstKey();
		sampleGetBytes();
		sampleHashMapGet();
		sampleInit();
		sampleInputStream();
		sampleJsonElement();
		sampleListGet();
		sampleLoadIcon();
		sampleMacDoFinal();
		sampleMacDoFinal2();
		sampleMkdirs();
		sampleNext();
		sampleNextToken();
		samplePrintWriterClose();
		samplePrintWriterWrite();
		sampleRandomFileAccessFileClose();
		sampleRandomFileAccessFileRead();
		sampleRandomFileAccessFileWrite();
		sampleSetContentView();
		sampleSetPreferredSize();
		sampleSimpleDateFormat();
		sampleSQLiteDatabase();
		sampleTypeArray();
	}
			
	public static void main(String[] args) {
		SampleSnippets ss = new SampleSnippets();
		ss.sample();
	}
}
