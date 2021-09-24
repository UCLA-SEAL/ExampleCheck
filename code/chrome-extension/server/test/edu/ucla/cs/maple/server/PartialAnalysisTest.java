package edu.ucla.cs.maple.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class PartialAnalysisTest {
	@Test
	public void testResolveParameterizedType() throws Exception {
		String path = "test/snippet_with_parameterized_type.txt";
		String snippet = FileUtils.readFileToString(path);
		PartialProgramAnalyzer ppa = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = ppa.retrieveAPICallSequences();
		ArrayList<APISeqItem> seq = seqs.get("foo");
		APICall iteratorNext = (APICall)seq.get(2);
		assertEquals("Iterator", iteratorNext.receiver_type);
		System.out.println(seq);
	}
	
	@Test
	public void testResolveFieldType() throws Exception {
		String path = "test/snippet_with_field_type.txt";
		String snippet = FileUtils.readFileToString(path);
		PartialProgramAnalyzer ppa = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = ppa.retrieveAPICallSequences();
		ArrayList<APISeqItem> seq = seqs.get("onTouchEvent");
		APICall call = (APICall)seq.get(1);
		assertEquals("ViewPager", call.receiver_type);
		System.out.println(seq);
	}
	
	@Test
	public void testResolveVariableType() throws Exception {
		String path = "test/snippet_with_normal_type.txt";
		String snippet = FileUtils.readFileToString(path);
		PartialProgramAnalyzer ppa = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = ppa.retrieveAPICallSequences();
		ArrayList<APISeqItem> seq = seqs.get("foo");
		APICall close = (APICall)seq.get(seq.size() - 1);
		assertEquals("BufferedReader", close.receiver_type);
		System.out.println(seq);
	}
	
	@Test
	public void testResolveVariableTypeInEnhancedForLoop() throws Exception {
		String path = "test/snippet_enhanced_for.txt";
		String snippet = FileUtils.readFileToString(path);
		PartialProgramAnalyzer ppa = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = ppa.retrieveAPICallSequences();
		ArrayList<APISeqItem> seq = seqs.get("foo");
		APICall getAsString = (APICall)seq.get(5);
		assertEquals("JsonElement", getAsString.receiver_type);
		System.out.println(seq);
	}
}
