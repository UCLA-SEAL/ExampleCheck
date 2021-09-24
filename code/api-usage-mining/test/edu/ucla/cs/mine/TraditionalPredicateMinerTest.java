package edu.ucla.cs.mine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import edu.ucla.cs.utils.ProcessUtils;

public class TraditionalPredicateMinerTest {
	@Test
	public void testExtractArguments() {
		String args = "getMyPath(a,c,d)+File.separator+SAVE_FILE_NAME,File.separator(d,)+SAVE_FILE_NAME,";
		ArrayList<String> list = ProcessUtils.getArguments(args);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("getMyPath(a,c,d)+File.separator+SAVE_FILE_NAME");
		expected.add("File.separator(d,)+SAVE_FILE_NAME");
		assertEquals(expected, list);
	}
	
	@Test
	public void testExtractArguments2() {
		String args = "\"index=\"+curIndexthis+\"\\n\",";
		ArrayList<String> list = ProcessUtils.getArguments(args);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("\"index=\"+curIndexthis+\"\\n\"");
		assertEquals(expected, list);
	}
	
	@Test
	public void testPropagatePredicateSimple() {
		String expr = "file.createNewFile()";
		String predicate = "!file.exists()";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("createNewFile(0)");
		String path = "/home/troy/research/BOA/Maple/example/File.createNewFile/small-sequence.txt";
		String sequence_path = "/home/troy/research/BOA/Maple/example/File.createNewFile/small-output.txt";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, path, sequence_path);
		HashMap<String, ArrayList<String>> map = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> predicates = map.get("createNewFile(0)");
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("!rcv.exists()");
		assertEquals(expected, predicates);
	}
	
	@Test
	public void testPropagatePredicateWithChainedMethodCalls() {
		String expr = "indexEntry=new String(\"index=\"+curIndexthis+\"\\n\",).getBytes()";
		String predicate = "\"index=\"+curIndexthis+\"\\n\".length() > 0 && !new String(\"index=\"+curIndexthis+\"\\n\",).exists()";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("new String(1)");
		pattern.add("getBytes(0)");
		String path = "/home/troy/research/BOA/Maple/example/File.createNewFile/small-sequence.txt";
		String sequence_path = "/home/troy/research/BOA/Maple/example/File.createNewFile/small-output.txt";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, path, sequence_path);
		HashMap<String, ArrayList<String>> map = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> predicates1 = map.get("new String(1)");
		ArrayList<String> expected1 = new ArrayList<String>();
		expected1.add("arg0.length() > 0 && !new String(arg0,).exists()");
		assertEquals(expected1, predicates1);
		ArrayList<String> predicates2 = map.get("getBytes(0)");
		ArrayList<String> expected2 = new ArrayList<String>();
		expected2.add("!rcv.exists()");
		assertEquals(expected2, predicates2);
	}
	
	@Test
	public void testPropagatePredicateWithNestedMethodCalls() {
		String expr = "saveFile=file.createNewFile(getMyPath(file,)+File.separator+SAVE_FILE_NAME,getMyPath()+File.separator+SAVE_FILE_NAME,)";
		String predicate = "!file.exists()";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("getMyPath(1)");
		pattern.add("getMyPath(0)");
		pattern.add("createNewFile(2)");
		String path = "/home/troy/research/BOA/Maple/example/File.createNewFile/small-sequence.txt";
		String sequence_path = "/home/troy/research/BOA/Maple/example/File.createNewFile/small-output.txt";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, path, sequence_path);
		HashMap<String, ArrayList<String>> map = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> predicates1 = map.get("createNewFile(2)");
		ArrayList<String> expected1 = new ArrayList<String>();
		expected1.add("!rcv.exists()");
		assertEquals(expected1, predicates1);
		ArrayList<String> predicates2 = map.get("getMyPath(1)");
		ArrayList<String> expected2 = new ArrayList<String>();
		expected2.add("!arg0.exists()");
		assertEquals(expected2, predicates2);
		ArrayList<String> predicates3 = map.get("getMyPath(0)");
		ArrayList<String> expected3 = new ArrayList<String>();
		expected3.add("true");
		assertEquals(expected3, predicates3);
	}
	
	@Test
	public void testPropagatePredicateWithNestedMethodCallsWhereTheFirstMatchIsNotInPattern() {
		String expr = "System.out.println(st.nextToken(),)";
		String predicate = "st.hasMoreTokens() && docComment!=null";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("nextToken(0)");
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, "", "");
		HashMap<String, ArrayList<String>> predicates = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> expected1 = new ArrayList<String>();
		expected1.add("rcv.hasMoreTokens()");
		assertEquals(expected1, predicates.get("nextToken(0)"));
		assertEquals(null, predicates.get("println"));
	}
	
	@Test
	public void testPropagatePredicateWithNestedMethodCallsWhereTheFirstMatchIsNotInPattern2() {
		String expr = "new TL1Line(command.nextToken().trim(),)";
		String predicate = "i<pllines && termCode==';'||termCode=='>' && command.hasMoreTokens() && !(rawOutput==null) && !(msgType.charAt(0,)=='M')";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("nextToken(0)");
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, "", "");
		HashMap<String, ArrayList<String>> predicates = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> expected1 = new ArrayList<String>();
		expected1.add("rcv.hasMoreTokens()");
		assertEquals(expected1, predicates.get("nextToken(0)"));
		assertEquals(null, predicates.get("TL1Line(1)"));
		assertEquals(null, predicates.get("trim(0)"));
	}
	
	@Test
	public void testPropagatePredicateWithChainedCall() {
		String expr = "otherRealIn=(String) shortTtToFullTt.foo(in,).get(in,)";
		String predicate = "prncvs[sex.toInteger()].containsKey(in,)";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("get(1)");
		pattern.add("foo(1)");
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, "", "");
		HashMap<String, ArrayList<String>> predicates = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> expected1 = new ArrayList<String>();
		expected1.add("prncvs[sex.toInteger()].containsKey(arg0,)");
		assertEquals(expected1, predicates.get("get(1)"));
		assertEquals(null, predicates.get("toInteger(0)"));
		assertEquals(expected1, predicates.get("foo(1)"));
	}
	
	@Test
	public void testPropagatePredicateWithAssignment() {
		String expr = "tok=t.nextToken()";
		String predicate = "t.hasMoreTokens()";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("nextToken(0)");
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, "", "");
		HashMap<String, ArrayList<String>> predicates = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("rcv.hasMoreTokens()");
		assertEquals(expected, predicates.get("nextToken(0)"));
	}
	
	@Test
	public void testPropagatePredicateWithAssignmentAndNested() {
		String expr = "line=new StringBuilder(lines.nextToken(),)";
		String predicate = "lines.hasMoreTokens()";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("nextToken(0)");
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, "", "");
		HashMap<String, ArrayList<String>> predicates = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("rcv.hasMoreTokens()");
		assertEquals(expected, predicates.get("nextToken(0)"));
		assertEquals(null, predicates.get("new StringBuilder"));
	}
	
	@Test
	public void testPropagatePredicateWithAssignmentAndNestedAndChained() {
		String expr = "Util.copyDocFiles(configuration,pathTokens.nextToken()+File.separator,DocletConstants.DOC_FILES_DIR_NAME,first,)";
		String predicate = "!(root.classes().length==0) && pathTokens.hasMoreTokens()";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("nextToken(0)");
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, "", "");
		HashMap<String, ArrayList<String>> predicates = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("rcv.hasMoreTokens()");
		assertEquals(expected, predicates.get("nextToken(0)"));
		assertEquals(null, predicates.get("copyDocFiles"));
	}
	
	@Test
	public void testPropagatePredicateWithDeeplyNested() {
		String expr = "url=fileToURL(new File(st.nextToken(),),)";
		String predicate = "st.hasMoreTokens()";
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("nextToken(0)");
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(pattern, "", "");
		HashMap<String, ArrayList<String>> predicates = pm.propagatePredicates(expr, expr, predicate);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("rcv.hasMoreTokens()");
		assertEquals(expected, predicates.get("nextToken(0)"));
		assertEquals(null, predicates.get("new File(1)"));
		assertEquals(null, predicates.get("fileToURL(1)"));
	}
	
	@Test
	public void testExtractReceiverAfterTypeCastingOfReturnValue() {
		String path = "/home/troy/research/BOA/Maple/example/Iterator.next/small-sequence.txt";
		String sequence_path = "/home/troy/research/BOA/Maple/example/Iterator.next/small-output.txt";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(
				new ArrayList<String>(), path, sequence_path);
		String expr = "e=(Map.Entry) i.next()";
		String apiName = "next";
		assertEquals("i", pm.getReceiver(expr, apiName));
	}
	
	@Test
	public void testExtractReceiverWithExtraParenthesis() {
		String expr = "action=(session.get(\"feedbackAction\",)!=null&&session.get(\"feedbackAction\",).toString().equals(\"save\",))?\"save\":\"cancel\"";
		String api = "get";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(new ArrayList<String>(), "", "");
		String rcv = pm.getReceiver(expr, api);
		assertEquals("session", rcv);
	}
	
	@Test
	public void testExtractReceiverWithTypeCastingInReceiver() {
		String expr = "((File) value).mkdirs()";
		String api = "mkdir";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(new ArrayList<String>(), "", "");
		String rcv = pm.getReceiver(expr, api);
		assertEquals("((File) value)", rcv);
	}
	
	@Test
	public void testExtractReceiverOfMethodCallInArgumentList() {
		String expr = "color=new Color(Display.getCurrent(),rgb,)";
		String api = "getCurrent";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(new ArrayList<String>(), "", "");
		String rcv = pm.getReceiver(expr, api);
		assertEquals("Display", rcv);
	}
	
	@Test
	public void testExtractArgumentWithCommaInQuote() {
		String args = "strVal,\"[,]\",";
		ArrayList<String> argList = ProcessUtils.getArguments(args);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("strVal");
		expected.add("\"[,]\"");
		assertEquals(expected, argList);
	}
	
	@Test
	public void testExtractPredicateWithAt() {
		String s = "tokenizera=new StringTokenizer(token,\"//\",)@!(currentLine.contains(\"@\",))";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(new ArrayList<String>(), "", "");
		String[] ss = ProcessUtils.splitByAt(s);
		assertEquals("tokenizera=new StringTokenizer(token,\"//\",)", ss[0]);
		assertEquals("!(currentLine.contains(\"@\",))", ss[1]);
	}
	
	@Test(timeout = 1000)
	public void testExtractPredicateWithAt2() {
		String s = "jid=JID.escapeNode(node,)+\"@\"+jid.substring(atIndex+1,)@!(tokens.countTokens()!=2)";
		TraditionalPredicateMiner pm = new TraditionalPredicateMiner(new ArrayList<String>(), "", "");
		String[] ss = ProcessUtils.splitByAt(s);
		assertEquals("jid=JID.escapeNode(node,)+\"@\"+jid.substring(atIndex+1,)", ss[0]);
		assertEquals("!(tokens.countTokens()!=2)", ss[1]);
	}
	
	@Test
	public void splitLineByArrow() {
		String s = "-> _log.info()@ -> _log.info(\"->\")@ -> _log.info()@";
		ArrayList<String> ss = ProcessUtils.splitByArrow(s);
		assertEquals(3, ss.size());
	}
}
