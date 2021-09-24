package edu.ucla.cs.maple.fix;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import edu.ucla.cs.maple.server.PartialProgramAnalyzer;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.CATCH;
import edu.ucla.cs.model.ControlConstruct;
import edu.ucla.cs.utils.FileUtils;
import edu.ucla.cs.utils.PatternUtils;

public class FixGeneratorTest {
	
	@Test
	public void testTryCatch() throws Exception {
		ArrayList<String> args = new ArrayList<String>();
		args.add("String");
		ArrayList<APISeqItem> pattern = new ArrayList<APISeqItem>();
		pattern.add(ControlConstruct.TRY);
		APICall focal = new APICall("new FileInputStream", "true", "fstream", "FileInputStream", args);
		pattern.add(focal);
		pattern.add(ControlConstruct.END_BLOCK);
		pattern.add(new CATCH("FileNotFoundException"));
		pattern.add(ControlConstruct.END_BLOCK);
		
		String path = "test/snippet_with_normal_type.txt";
		String snippet = FileUtils.readFileToString(path);
		PartialProgramAnalyzer ppa = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = ppa.retrieveAPICallSequences();
		ArrayList<APISeqItem> seq = seqs.get("foo");
		
		FixGenerator fixGen = new FixGenerator();
		String fix = fixGen.generate(pattern, seq, focal);
		System.out.println(fix);
	}
	
	@Test
	public void testIfGuard() throws Exception {
		ArrayList<APISeqItem> pattern = PatternUtils.convert("IF, replaceAll(String,String)@rcv!=null, END_BLOCK");
		String path = "test/snippet_replaceAll.txt";
		String snippet = FileUtils.readFileToString(path);
		PartialProgramAnalyzer ppa = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = ppa.retrieveAPICallSequences();
		ArrayList<APISeqItem> seq = seqs.get("foo");
		
		FixGenerator fixGen = new FixGenerator();
		ArrayList<String> args = new ArrayList<String>();
		args.add("\"(^\\\\*)|(\\\\*$)|\\*\", \"<$1|$2>\"");
		String fix = fixGen.generate(pattern, seq, new APICall("replaceAll", "true", "str", "String", args));
		System.out.println(fix);	
	}
	
	@Test
	public void testMissingAPICall() throws Exception {
		ArrayList<APISeqItem> pattern = PatternUtils.convert("TRY, write(ByteBuffer)@true, close()@true, END_BLOCK, CATCH(Exception), END_BLOCK");
		String path = "test/snippet_with_missing_method_call.txt";
		String snippet = FileUtils.readFileToString(path);
		PartialProgramAnalyzer ppa = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = ppa.retrieveAPICallSequences();
		ArrayList<APISeqItem> seq = seqs.get("foo");
		
		FixGenerator fixGen = new FixGenerator();
		ArrayList<String> args = new ArrayList<String>();
		args.add("...");
		String fix = fixGen.generate(pattern, seq, new APICall("write", "true", "fileOut", "FileChannel", args));
		System.out.println(fix);	
	}
}
