package edu.ucla.cs.parse;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ControlConstruct;
import edu.ucla.cs.utils.FileUtils;

public class PartialProgramAnalyzerTest {
	@Test
	public void testMultipleMethodsInASnippet() throws Exception {
		String sample = "/home/troy/research/BOA/Maple/test/edu/ucla/cs/parse/snippet_with_multiple_method.txt";
		String snippet = FileUtils.readFileToString(sample);
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashSet<String> apis = new HashSet<String>();
		apis.add("next(0)");
		ArrayList<ArrayList<APISeqItem>> seqs = new ArrayList<ArrayList<APISeqItem>>();
		ArrayList<APISeqItem> seq = new ArrayList<APISeqItem>();
		seq.add(ControlConstruct.IF);
		seq.add(new APICall("new NoSuchElementException", "!hasNext()", 0));
		seq.add(ControlConstruct.END_BLOCK);
		seq.add(new APICall("next", "true && !(!hasNext())", 0));
		seqs.add(seq);
		assertEquals(seqs, analyzer.retrieveAPICallSequencesMethodLevel(apis));
	}
	
	@Test
	public void testSnippet28300736() throws Exception {
		String sample = "/home/troy/research/BOA/Maple/test/edu/ucla/cs/parse/snippet_28300736.txt";
		String snippet = FileUtils.readFileToString(sample);
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashSet<String> apis = new HashSet<String>();
		apis.add("firstKey(0)");
		ArrayList<ArrayList<APISeqItem>> seqs = analyzer.retrieveAPICallSequencesMethodLevel(apis);
		assertEquals(1, seqs.size());
		ArrayList<APISeqItem> seq = seqs.get(0);
		APICall call = (APICall) seq.get(2);
		assertEquals("map1", call.receiver);
	}
	
	@Test
	public void testSnippetWithException() throws Exception {
		String sample = "/home/troy/research/BOA/Maple/test/edu/ucla/cs/parse/snippet_37551851.txt";
		String snippet = FileUtils.readFileToString(sample);
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashSet<String> apis = new HashSet<String>();
		apis.add("new FileInputStream(1)");
		ArrayList<ArrayList<APISeqItem>> seqs = analyzer.retrieveAPICallSequencesMethodLevel(apis);
		ArrayList<APISeqItem> seq = seqs.get(0);
		assertEquals(ControlConstruct.TRY, seq.get(0));
		assertEquals(ControlConstruct.CATCH, seq.get(seq.size() - 2));
	}
	
	@Test
	public void testParameterizedType() throws Exception {
		String sample = "/home/troy/research/BOA/Maple/test/edu/ucla/cs/parse/snippet_parameterized.txt";
		String snippet = FileUtils.readFileToString(sample);
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashSet<String> apis = new HashSet<String>();
		apis.add("get(1)");
		ArrayList<ArrayList<APISeqItem>> seqs = analyzer.retrieveAPICallSequencesMethodLevel(apis);
		ArrayList<APISeqItem> seq = seqs.get(0);
		assertEquals("new HashMap(0)@true", seq.get(0).toString());
	}
	
	@Test
	public void testExtractAPICallsInAnonymousClass() throws Exception {
		String sample = "/home/troy/research/BOA/Maple/test/edu/ucla/cs/parse/snippet_3707662.txt";
		String snippet = FileUtils.readFileToString(sample);
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = analyzer.retrieveAPICallSequencesClassLevel();
		ArrayList<APISeqItem> seq = seqs.get("class");
		System.out.println(seq);
	}
	
	@Test
	public void testExtractAPICallsInClassLevel() throws Exception {
		String sample = "/home/troy/research/BOA/Maple/test/edu/ucla/cs/parse/snippet_9942662.txt";
		String snippet = FileUtils.readFileToString(sample);
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = analyzer.retrieveAPICallSequencesClassLevel();
		ArrayList<APISeqItem> seq = seqs.get("class");
		System.out.println(seq);
	}
	
	@Test
	public void testSnippet5701826() throws Exception {
		String sample = "/home/troy/research/BOA/Maple/test/edu/ucla/cs/parse/snippet_5701826.txt";
		String snippet = FileUtils.readFileToString(sample);
		PartialProgramAnalyzer analyzer = new PartialProgramAnalyzer(snippet);
		HashMap<String, ArrayList<APISeqItem>> seqs = analyzer.retrieveAPICallSequencesClassLevel();
		ArrayList<APISeqItem> seq = seqs.get("class");
		System.out.println(seq);
	}
}
