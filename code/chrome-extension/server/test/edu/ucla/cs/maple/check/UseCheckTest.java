package edu.ucla.cs.maple.check;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import edu.ucla.cs.maple.server.MySQLAccess;
import edu.ucla.cs.maple.server.PartialProgramAnalyzer;
import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ControlConstruct;
import edu.ucla.cs.model.Pattern;
import edu.ucla.cs.model.Violation;
import edu.ucla.cs.model.ViolationType;
import edu.ucla.cs.utils.CheckerUtils;
import edu.ucla.cs.utils.FileUtils;
import edu.ucla.cs.utils.PatternUtils;

public class UseCheckTest {
	@Test
	public void testLCS() {
		APICall call1 = new APICall("createNewFile", null, null, null, null);
		APICall call2 = new APICall("new File", null, null, null, null);
		APICall call3 = new APICall("exists", null, null, null, null);
		APICall call4 = new APICall("foo", null, null, null, null);
		APICall call5 = new APICall("bar", null, null, null, null);
		
		ArrayList<APISeqItem> pattern = new ArrayList<APISeqItem>();
		pattern.add(ControlConstruct.IF);
		pattern.add(call1);
		pattern.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> seq = new ArrayList<APISeqItem>();
		assertEquals(0, new UseChecker().LCS(pattern, seq).size());
		
		seq.add(ControlConstruct.IF);
		assertEquals(1, new UseChecker().LCS(pattern, seq).size());
		
		seq.add(ControlConstruct.IF);
		assertEquals(1, new UseChecker().LCS(pattern, seq).size());
		

		seq.add(call2);
		seq.add(call3);
		assertEquals(1, new UseChecker().LCS(pattern, seq).size());
		
		seq.add(ControlConstruct.END_BLOCK);
		assertEquals(2, new UseChecker().LCS(pattern, seq).size());
	
		seq.add(call4);
		seq.add(call1);
		seq.add(call5);
		assertEquals(2, new UseChecker().LCS(pattern, seq).size());
		
		seq.add(ControlConstruct.END_BLOCK);
		assertEquals(3, new UseChecker().LCS(pattern, seq).size());
	}
	
	@Test
	public void testSameSequenceButDifferentPrecondition() {
		APICall call1 = new APICall("createNewFile", "true", "f", "File", new ArrayList<String>());
		APICall call2 = new APICall("createNewFile", " flag && !f.exists()", "f", "File", new ArrayList<String>());
		APICall call3 = new APICall("createNewFile", "!rcv.exists()", null, "File", new ArrayList<String>());
		
		ArrayList<APISeqItem> seq1 = new ArrayList<APISeqItem>();
		seq1.add(ControlConstruct.IF);
		seq1.add(call1);
		seq1.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> seq2 = new ArrayList<APISeqItem>();
		seq2.add(ControlConstruct.IF);
		seq2.add(call2);
		seq2.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> pattern = new ArrayList<APISeqItem>();
		pattern.add(ControlConstruct.IF);
		pattern.add(call3);
		pattern.add(ControlConstruct.END_BLOCK);
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern);
		
		UseChecker check = new UseChecker();
		ArrayList<Violation> vios1 = check.validate(patterns, seq1);
		assertEquals(1, vios1.size());
		
		ArrayList<Violation> vios2 = check.validate(patterns, seq2);
		assertEquals(0, vios2.size());
	}
	
	@Test
	public void testMultiplePatterns() {
		ArrayList<String> args = new ArrayList<String>();
		args.add("key");
		APICall call1 = new APICall("get", "map.containsKey(key,)", "map", "HashMap", args);
		APICall call2 = new APICall("get", "true", "map", "HashMap", args);
		APICall call3 = new APICall("get", "rcv.containsKey(arg0,)", null, "HashMap", new ArrayList<String>());
		APICall call4 = new APICall("get", "true", null, "HashMap", new ArrayList<String>());
		
		ArrayList<APISeqItem> seq1 = new ArrayList<APISeqItem>();
		seq1.add(ControlConstruct.IF);
		seq1.add(call1);
		seq1.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> seq2 = new ArrayList<APISeqItem>();
		seq2.add(call2);
		seq2.add(ControlConstruct.IF);
		seq2.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(call3);
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(call4);
		pattern2.add(ControlConstruct.IF);
		pattern2.add(ControlConstruct.END_BLOCK);
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		UseChecker check = new UseChecker();
		ArrayList<Violation> vios1 = check.validate(patterns, seq1);
		assertEquals(0, vios1.size());
		
		ArrayList<Violation> vios2 = check.validate(patterns, seq2);
		assertEquals(0, vios2.size());
	}
	
	@Test
	public void testMultiplePatternsWithMinimumDistance() {
		ArrayList<String> args = new ArrayList<String>();
		APICall call1 = new APICall("firstKey", "!map.isEmpty()", "map", "HashMap", args);
		APICall call2 = new APICall("firstKey", "map.size() > 0", "map", "HashMap", args);
		APICall call3 = new APICall("firstKey", "!rcv.isEmpty()", null, "HashMap", new ArrayList<String>());
		APICall call4 = new APICall("firstKey", "rcv.size() > 0", null, "HashMap", new ArrayList<String>());
		
		ArrayList<APISeqItem> seq1 = new ArrayList<APISeqItem>();
		seq1.add(ControlConstruct.IF);
		seq1.add(call1);
		seq1.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> seq2 = new ArrayList<APISeqItem>();
		seq2.add(ControlConstruct.IF);
		seq2.add(call2);
		seq2.add(ControlConstruct.END_BLOCK);
		
		ArrayList<APISeqItem> pattern1 = new ArrayList<APISeqItem>();
		pattern1.add(call3);
		ArrayList<APISeqItem> pattern2 = new ArrayList<APISeqItem>();
		pattern2.add(call4);
		HashSet<ArrayList<APISeqItem>> patterns = new HashSet<ArrayList<APISeqItem>>();
		patterns.add(pattern1);
		patterns.add(pattern2);
		
		UseChecker check = new UseChecker();
		ArrayList<Violation> vios1 = check.validate(patterns, seq1);
		assertEquals(0, vios1.size());
		
		ArrayList<Violation> vios2 = check.validate(patterns, seq2);
		assertEquals(0, vios2.size());
	}
	
	@Test
	public void testSubSequence() {
		ArrayList<String> s1 = new ArrayList<String>();
		s1.add("put");
		s1.add("flip");
		s1.add("get");
		ArrayList<String> s2 = new ArrayList<String>();
		s2.add("get");
		s2.add("flip");
		s2.add("put");
		ArrayList<String> s3 = new ArrayList<String>();
		s3.add("put");
		s3.add("get");
		
		assertTrue(CheckerUtils.isSubsequence(s1, s3));
		assertFalse(CheckerUtils.isSubsequence(s2, s3));
	}
	
	@Test
	public void testSnippet() throws Exception {
		String snippet = FileUtils.readFileToString("test/snippet_cursor_close.txt");
		MySQLAccess dbAccess = new MySQLAccess();
		dbAccess.connect();
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = analyzer
                .retrieveAPICallSequences();
		ArrayList<APISeqItem> callSeq = seqs.get("getNames");
		HashSet<Pattern> pset = dbAccess.getPatterns("close", "Cursor");
		ArrayList<Violation> vios = new ArrayList<Violation>();
		for(Pattern p : pset) {
			HashSet<ArrayList<APISeqItem>> pset2 = new HashSet<ArrayList<APISeqItem>>();
			pset2.add(PatternUtils.convert(p.pattern));
			UseChecker checker = new UseChecker();
			vios.addAll(checker.validate(pset2, callSeq));
		}
		
		assertEquals(1, vios.size());
		assertEquals(ViolationType.MissingStructure, vios.get(0).type);
		assertEquals(ControlConstruct.TRY, vios.get(0).item);
		dbAccess.close();
	}
	
	@Test
	public void testSnippetWithConstructorCall() throws Exception {
		String snippet = FileUtils.readFileToString("test/snippet_with_constructor_call.txt");
		MySQLAccess dbAccess = new MySQLAccess();
		dbAccess.connect();
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = analyzer
                .retrieveAPICallSequences();
		ArrayList<APISeqItem> callSeq = seqs.get("foo");
		HashSet<Pattern> pset = dbAccess.getPatterns("SimpleDateFormat", "SimpleDateFormat");
		ArrayList<Violation> vios = new ArrayList<Violation>();
		for(Pattern p : pset) {
			HashSet<ArrayList<APISeqItem>> pset2 = new HashSet<ArrayList<APISeqItem>>();
			pset2.add(PatternUtils.convert(p.pattern));
			UseChecker checker = new UseChecker();
			vios.addAll(checker.validate(pset2, callSeq));
		}
		
		assertTrue(vios.size() > 0);
		dbAccess.close();
	}
}
