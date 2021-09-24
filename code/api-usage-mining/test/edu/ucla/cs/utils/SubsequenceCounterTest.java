package edu.ucla.cs.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class SubsequenceCounterTest {

	@Test
	public void testOneSubSequence() {
		ArrayList<String> seq1 = new ArrayList<String>();
		seq1.add("a");
		seq1.add("b");
		seq1.add("c");
		seq1.add("d");
		
		ArrayList<String> seq2 = new ArrayList<String>();
		seq2.add("b");
		seq2.add("c");
		
		SubsequenceCounter counter = new SubsequenceCounter(seq1, seq2);
		assertEquals(1, counter.countMatches());
	}
	
	@Test
	public void testTwoSubSequences() {
		ArrayList<String> seq1 = new ArrayList<String>();
		seq1.add("a");
		seq1.add("b");
		seq1.add("b");
		seq1.add("c");
		seq1.add("d");
		
		ArrayList<String> seq2 = new ArrayList<String>();
		seq2.add("b");
		seq2.add("c");
		
		SubsequenceCounter counter = new SubsequenceCounter(seq1, seq2);
		assertEquals(2, counter.countMatches());
	}
	
	@Test
	public void testThreeSubSequences() {
		ArrayList<String> seq1 = new ArrayList<String>();
		seq1.add("a");
		seq1.add("b");
		seq1.add("c");
		seq1.add("b");
		seq1.add("d");
		seq1.add("c");
		
		ArrayList<String> seq2 = new ArrayList<String>();
		seq2.add("b");
		seq2.add("c");
		
		SubsequenceCounter counter = new SubsequenceCounter(seq1, seq2);
		assertEquals(3, counter.countMatches());
	}
	
	@Test
	public void testRealSubSequences() {
		// [IF {, }, findViewById(1), IF {, }]
		ArrayList<String> seq1 = new ArrayList<String>();
		seq1.add("IF {");
		seq1.add("}");
		seq1.add("findViewById(1)");
		seq1.add("IF {");
		seq1.add("}");
		
		// [findViewById(1), IF {, }]
		ArrayList<String> seq2 = new ArrayList<String>();
		seq2.add("findViewById(1)");
		seq2.add("IF {");
		seq2.add("}");
		
		SubsequenceCounter counter = new SubsequenceCounter(seq1, seq2);
		assertEquals(1, counter.countMatches());
	}
}
