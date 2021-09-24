package edu.ucla.cs.process.traditional;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.utils.ProcessUtils;

public class ProcessTest {
	
	@Test
	public void testExtractChainedMethodCall() {
		String expr = "indexEntry=new String(\"index=\"+curIndexthis+\"\\n\",).getBytes()@";
		SequenceProcessor sp = new SequenceProcessor();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("new String(1)");
		expected.add("getBytes(0)");
		assertEquals(expected, sp.extractItems(expr));
	}
	
	@Test
	public void testExtractNestedMethodCall() {
		String expr = "saveFile=new File(getMyPath()+File.separator+SAVE_FILE_NAME,getMyPath()+File.separator+SAVE_FILE_NAME,)@ ";
		SequenceProcessor sp = new SequenceProcessor();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("getMyPath(0)");
		expected.add("getMyPath(0)");
		expected.add("new File(2)");
		assertEquals(expected, sp.extractItems(expr));
	}
	
	@Test
	public void testExtractChainedMethodCallWithArgs() {
		String expr = "domainService=(DomainService) ApplicationContextHolder.getContext().getBean(\"domainService\",)";
		SequenceProcessor sp = new SequenceProcessor();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("getContext(0)");
		expected.add("getBean(1)");
		assertEquals(expected, sp.extractItems(expr));
	}
	
	@Test
	public void testExtractCallChainAsArgs() {
		String expr = "domainService=(DomainService) ApplicationContextHolder.getContext(getBean().getArgs(a,),).getBean(\"domainService\",)";
		SequenceProcessor sp = new SequenceProcessor();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("getBean(0)");
		expected.add("getArgs(1)");
		expected.add("getContext(1)");
		expected.add("getBean(1)");
		assertEquals(expected, sp.extractItems(expr));
	}
	
	@Test
	public void testMethodCallWithPrecondition() {
		String expr = "file.createNewFile()@!file.exists()";
		SequenceProcessor sp = new SequenceProcessor();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("createNewFile(0)");
		assertEquals(expected, sp.extractItems(expr));
	}
	
	@Test
	public void testExtractClassConstructorOfParameterizedType() {
		String expr = "individus=new ArrayList<>()";
		SequenceProcessor sp = new SequenceProcessor();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("new ArrayList<>(0)");
		assertEquals(expected, sp.extractItems(expr));
	}
	
	@Test(timeout = 5000)
	public void testSpecificLineHangsInCreateNewFile() {
		String path = "/home/troy/research/BOA/Maple/test/edu/ucla/cs/process/traditional/testHangs.txt";
		try {
			Process p = new Process();
			p.s = new CatastrophicBacktrackingProcessor();
			p.processByLine(path);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testIsBalanced() {
		String expr1 = "adwdw)adwdw(dwkok)((()))";
		assertFalse(ProcessUtils.isBalanced(expr1));
		String expr2 = "adwo(aadkwo)kopadwko(())";
		assertTrue(ProcessUtils.isBalanced(expr2));
		String expr3 = "adwdw(\"ajiodw)jaidw\")";
		assertTrue(ProcessUtils.isBalanced(expr3));
		String expr4 = "adwdw(\"aji\"odw)jaidw)";
		assertFalse(ProcessUtils.isBalanced(expr4));
		String expr5 = "adwdw(\"aji\\\"odw)j\\\"ai\"dw)";
		assertTrue(ProcessUtils.isBalanced(expr5));
	}
	
	@Test
	public void testGetFirstUnbalancedCloseParenthesis() {
		String expr1 = "adwdw)adwdw(dwkok)((()))";
		assertEquals(5, ProcessUtils.findFirstUnbalancedCloseParenthesis(expr1));
		String expr2 = "adwo(aadkwo)kopadwko(())";
		assertEquals(-1, ProcessUtils.findFirstUnbalancedCloseParenthesis(expr2));
		String expr3 = "adwdw(\"ajiodw)jaidw\")";
		assertEquals(-1, ProcessUtils.findFirstUnbalancedCloseParenthesis(expr3));
		String expr4 = "adwdw(\"aji\"odw)jaidw)";
		assertEquals(20, ProcessUtils.findFirstUnbalancedCloseParenthesis(expr4));
		String expr5 = "adwdw(\"aji\\\"odw)j\\\"ai\"dw)";
		assertEquals(-1, ProcessUtils.findFirstUnbalancedCloseParenthesis(expr5));
	}
	
	@Test
	public void testCloseParenthesisInTheMiddle() {
		Method mock = new Method("", "", "", "");
		String expr = "results[key][SEQ] = -> foo()@ } -> foo().bar(\"{}{}{}\",)@ } } ELSE { ";
		SequenceProcessor sp = new SequenceProcessor();
		sp.buildSequenceMap(mock, expr);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("foo(0)");
		expected.add("}");
		expected.add("foo(0)");
		expected.add("bar(1)");
		expected.add("}");
		expected.add("}");
		expected.add("ELSE {");
		assertEquals(expected, mock.seq);
	}
	
	
	@Test
	public void testTwoEmptyBlocksAtTheEnd() {
		Method mock = new Method("", "", "", "");
		String expr = "results[key][SEQ] = -> TRY { -> foo().bar(\"{}{}{}\",)@ } CATCH { } CATCH { } FINALLY { }";
		SequenceProcessor sp = new SequenceProcessor();
		sp.buildSequenceMap(mock, expr);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("TRY {");
		expected.add("foo(0)");
		expected.add("bar(1)");
		expected.add("}");
		expected.add("CATCH {");
		expected.add("}");
		expected.add("CATCH {");
		expected.add("}");
		expected.add("FINALLY {");
		expected.add("}");
		assertEquals(expected, mock.seq);
	}
}
