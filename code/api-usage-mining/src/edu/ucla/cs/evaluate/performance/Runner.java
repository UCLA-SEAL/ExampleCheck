package edu.ucla.cs.evaluate.performance;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.ucla.cs.mine.PatternMiner;
import edu.ucla.cs.utils.FileUtils;

public class Runner {
	final static double SIGMA = 0.1;
	final static double THETA = 0.1;
	final static String LOG_FILE = "/home/troy/research/BOA/performance/size.csv";
	final static String ROOT_FOLDER = "/home/troy/research/BOA/example/";
	final static String K = "1";
	final int[] sizes = {10, 50, 100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600, 51200, 102400};
	
	public static void main(String[] args) {
		Runner runner = new Runner();
		try {
			runner.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(String raw_output, String seq_output, HashSet<HashSet<String>> queries) throws IOException {
		Sample sample = new Sample(seq_output);
		String log = "";
		
		for(int size : sizes) {
			sample.sample(size);
			
			long startTime = System.currentTimeMillis();
			String sample_output = seq_output.substring(0, seq_output.lastIndexOf(".")) + "-" + size + ".txt";
			
			final ExecutorService service = Executors.newSingleThreadExecutor();
			try {
				final Future<Object> f = service.submit(() -> {
					PatternMiner.mine(raw_output, sample_output, queries, SIGMA, size, THETA);
					return "yes";
				});
				
				f.get(2, TimeUnit.HOURS);
			} catch (final TimeoutException e) {
				System.out.println("The mining process does not terminate with sigma=" + SIGMA + " and theta=" + THETA + " on " + sample_output);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			} finally {
				service.shutdown();
			}
			
			long estimatedTime = System.currentTimeMillis() - startTime;
			log += estimatedTime + ",";
		}
		
		log += System.lineSeparator();
		FileUtils.appendStringToFile(log, LOG_FILE);
	}
	
	public void run() throws IOException {
		// delete the previous log file
		File log_file = new File(LOG_FILE);
		if(log_file.exists()) {
			log_file.delete();
			log_file.createNewFile();
		}
		
		// warm up the JVM
		warmup();
		
		runActivityFindViewById();
		runActivitySetContentView();
		runApplicationInfoLoadIcon();
		runArrayListGet();
		runByteBufferGetInt();
		runCipherInit();
		runDataOutputStreamWrite();
		runFileChannelWrite();
		runFileCreateNewFile();
		runFileInputStreamNew();
		runFileMkdirs();
		runHashMapGet();
		runInputStreamRead();
		runIteratorNext();
		runJFrameSetPreferredSize();
		runJsonElementGetAsString();
		runMacDoFinal();
		runMacDoFinal2();
		runPrintWriterClose();
		runPrintWriterWrite();
		runProgressDialogDismiss();
		runRandomFileAccessClose();
		runRandomFileAccessRead();
		runRandomFileAccessWrite();
		runSimpleDateFormatNew();
//		runSortedMapFirstKey();
		runSQLiteDatabaseQuery();
		runStringGetBytes();
		runStringTokenizer();
		runTypedArrrayGetString();
	}
	
	private void warmup() {
		String raw_output = ROOT_FOLDER + "Activity.findViewById" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "Activity.findViewById" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("findViewById(1)");
		queries.add(q1);
		int size = FileUtils.countLines(seq_output);
		PatternMiner.mine(raw_output, seq_output, queries, SIGMA, size, THETA);
	}
	
	private void runActivityFindViewById() throws IOException {
		String raw_output = ROOT_FOLDER + "Activity.findViewById" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "Activity.findViewById" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("findViewById(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runActivitySetContentView() throws IOException {
		String raw_output = ROOT_FOLDER + "Activity.setContentView" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "Activity.setContentView" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("setContentView(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runApplicationInfoLoadIcon() throws IOException {
		String raw_output = ROOT_FOLDER + "ApplicationInfo.loadIcon" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "ApplicationInfo.loadIcon" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("loadIcon(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runArrayListGet() throws IOException {
		String raw_output = ROOT_FOLDER + "ArrayList.get" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "ArrayList.get" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("get(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runByteBufferGetInt() throws IOException {
		String raw_output = ROOT_FOLDER + "ByteBuffer.getInt" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "ByteBuffer.getInt" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("put(1)");
		q1.add("getInt(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runCipherInit() throws IOException {
		String raw_output = ROOT_FOLDER + "Cipher.init" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "Cipher.init" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("init(2)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runDataOutputStreamWrite() throws IOException {
		String raw_output = ROOT_FOLDER + "DataOutputStream.write" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "DataOutputStream.write" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("write(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runFileChannelWrite() throws IOException {
		String raw_output = ROOT_FOLDER + "FileChannel.write" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "FileChannel.write" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("write(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runFileCreateNewFile() throws IOException {
		String raw_output = ROOT_FOLDER + "File.createNewFile" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "File.createNewFile" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("createNewFile(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runFileInputStreamNew() throws IOException {
		String raw_output = ROOT_FOLDER + "FileInputStream.new" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "FileInputStream.new" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("new FileInputStream(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runFileMkdirs() throws IOException {
		String raw_output = ROOT_FOLDER + "File.mkdir" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "File.mkdir" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("mkdirs(0)");
		HashSet<String> q2 = new HashSet<String>();
		q2.add("mkdir(0)");
		queries.add(q1);
		queries.add(q2);
		run(raw_output, seq_output, queries);
	}
	
	private void runHashMapGet() throws IOException {
		String raw_output = ROOT_FOLDER + "HashMap.get" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "HashMap.get" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("get(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runInputStreamRead() throws IOException {
		String raw_output = ROOT_FOLDER + "InputStream.read" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "InputStream.read" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("read(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runIteratorNext() throws IOException {
		String raw_output = ROOT_FOLDER + "Iterator.next" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "Iterator.next" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("next(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runJFrameSetPreferredSize() throws IOException {
		String raw_output = ROOT_FOLDER + "JFrame.setPreferredSize" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "JFrame.setPreferredSize" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("setPreferredSize(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runJsonElementGetAsString() throws IOException {
		String raw_output = ROOT_FOLDER + "JsonElement.getAsString" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "JsonElement.getAsString" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("getAsString(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runMacDoFinal() throws IOException {
		String raw_output = ROOT_FOLDER + "Mac.doFinal" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "Mac.doFinal" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("getBytes(0)");
		q1.add("doFinal(1)");
		HashSet<String> q2 = new HashSet<String>();
		q2.add("getBytes(1)");
		q2.add("doFinal(1)");
		queries.add(q1);
		queries.add(q2);
		run(raw_output, seq_output, queries);
	}
	
	private void runMacDoFinal2() throws IOException {
		String raw_output = ROOT_FOLDER + "Mac.doFinal2" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "Mac.doFinal2" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("doFinal(1)");
		q1.add("new String(1)");
		HashSet<String> q2 = new HashSet<String>();
		q2.add("doFinal(1)");
		q2.add("new String(2)");
		queries.add(q1);
		queries.add(q2);
		run(raw_output, seq_output, queries);
	}
	
	private void runPrintWriterClose() throws IOException {
		String raw_output = ROOT_FOLDER + "PrintWriter.close" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "PrintWriter.close" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("close(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runPrintWriterWrite() throws IOException {
		String raw_output = ROOT_FOLDER + "PrintWriter.write" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "PrintWriter.write" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("write(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runProgressDialogDismiss() throws IOException {
		String raw_output = ROOT_FOLDER + "ProgressDialog.dismiss" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "ProgressDialog.dismiss" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("dismiss(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runRandomFileAccessClose() throws IOException {
		String raw_output = ROOT_FOLDER + "RandomFileAccess.close" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "RandomFileAccess.close" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("close(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runRandomFileAccessRead() throws IOException {
		String raw_output = ROOT_FOLDER + "RandomFileAccess.read" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "RandomFileAccess.read" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("read(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runRandomFileAccessWrite() throws IOException {
		String raw_output = ROOT_FOLDER + "RandomFileAccess.write" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "RandomFileAccess.write" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("write(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runSimpleDateFormatNew() throws IOException {
		String raw_output = ROOT_FOLDER + "SimpleDateFormat.new" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "SimpleDateFormat.new" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("new SimpleDateFormat(1)");
		HashSet<String> q2 = new HashSet<String>();
		q2.add("new SimpleDateFormat(2)");
		queries.add(q1);
		queries.add(q2);
		run(raw_output, seq_output, queries);
	}
	
	private void runSortedMapFirstKey() throws IOException {
		String raw_output = ROOT_FOLDER + "SortedMap.firstKey" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "SortedMap.firstKey" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("firstKey(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runSQLiteDatabaseQuery() throws IOException {
		String raw_output = ROOT_FOLDER + "SQLiteDatabase.query" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "SQLiteDatabase.query" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("query(7)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runStringGetBytes() throws IOException {
		String raw_output = ROOT_FOLDER + "String.getBytes" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "String.getBytes" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("getBytes(1)");
		HashSet<String> q2 = new HashSet<String>();
		q2.add("getBytes(0)");
		queries.add(q1);
		queries.add(q2);
		run(raw_output, seq_output, queries);
	}
	
	private void runStringTokenizer() throws IOException {
		String raw_output = ROOT_FOLDER + "StringTokenizer.nextToken" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "StringTokenizer.nextToken" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("nextToken(0)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
	
	private void runTypedArrrayGetString() throws IOException {
		String raw_output = ROOT_FOLDER + "TypedArray.getString" + File.separator + K + File.separator + "large-sequence.txt";
		String seq_output = ROOT_FOLDER + "TypedArray.getString" + File.separator + K + File.separator + "large-output.txt";
		HashSet<HashSet<String>> queries = new HashSet<HashSet<String>>();
		HashSet<String> q1 = new HashSet<String>();
		q1.add("getString(1)");
		queries.add(q1);
		run(raw_output, seq_output, queries);
	}
}
