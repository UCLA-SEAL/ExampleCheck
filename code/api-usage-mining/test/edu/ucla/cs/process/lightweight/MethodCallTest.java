package edu.ucla.cs.process.lightweight;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import edu.ucla.cs.model.MethodCall;

public class MethodCallTest {
	
	@Test
	public void testEquals(){
		ArrayList<String> l1 = new ArrayList<String>();
		l1.add("v::path");
		ArrayList<String> l2 = new ArrayList<String>();
		l2.add("v::path");
		MethodCall mock1 = new MethodCall("new File", l1);
		MethodCall mock2 = new MethodCall("new File", l2);
		assertEquals(true, mock1.equals(mock2));
	}
	
	@Test
	public void testWithMultiset(){
		ArrayList<String> l1 = new ArrayList<String>();
		l1.add("v::path");
		ArrayList<String> l2 = new ArrayList<String>();
		l2.add("v::path");
		MethodCall mock1 = new MethodCall("new File", l1);
		MethodCall mock2 = new MethodCall("new File", l2);
		Multiset<MethodCall> mset = HashMultiset.create();
		mset.add(mock1);
		assertEquals(true, mset.contains(mock1));
		assertEquals(true, mset.contains(mock2));
	}
}
