package edu.ucla.cs.process.lightweight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Multiset;

import edu.ucla.cs.model.Assignment;
import edu.ucla.cs.model.MethodCall;
import edu.ucla.cs.model.Predicate;
import edu.ucla.cs.model.Receiver;
import edu.ucla.cs.process.lightweight.ArgumentProcessor;
import edu.ucla.cs.process.lightweight.AssignmentProcessor;
import edu.ucla.cs.process.lightweight.PredicateProcessor;
import edu.ucla.cs.process.lightweight.Process;
import edu.ucla.cs.process.lightweight.ReceiverProcessor;
import edu.ucla.cs.process.lightweight.SequenceProcessor;
import edu.ucla.cs.process.lightweight.TypeProcessor;
import edu.ucla.cs.slice.Slicer;

public class ProcessDriver {
	final String mkey = "https://github.com/fywb251/bsl_impc_android ** cube-android/src/com/foreveross/chameleon/pad/fragment/ChatRoomFragment.java ** ChatRoomFragment ** initValues";
	final String ckey = "https://github.com/fywb251/bsl_impc_android ** cube-android/src/com/foreveross/chameleon/pad/fragment/ChatRoomFragment.java ** ChatRoomFragment";
	
	Process proc;
	
	@Before
	public void setup(){
		proc = new Process();
	}
	
	@Test
	public void test1(){
		try {
			proc.s = new TypeProcessor(); 
			proc.processByLine("/home/troy/research/BOA/Slicer/example/type.txt");
			
			String type = Slicer.methods.get(mkey).locals.get("dir");
			assertEquals("File", type);
			
			Set<String> vars = Slicer.methods.get(mkey).rev_locals.get("File");
			assertEquals(1, vars.size());
			assertTrue(vars.contains("dir"));
			
			vars = Slicer.methods.get(mkey).rev_locals.get("String");
			assertEquals(6, vars.size());
			
			vars = Slicer.classes.get(ckey).rev_fields.get("File");
			assertEquals(3, vars.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test2(){
		try {
			proc.s = new SequenceProcessor();
			proc.processByLine("/home/troy/research/BOA/Slicer/example/sequence.txt");
			String m1 = Slicer.methods.get(mkey).seq.get(0);
			assertEquals("IF {", m1);
			boolean b1 = Slicer.methods.get(mkey).seq.contains("createNewFile");
			
			assertTrue(b1);
			boolean b2 = Slicer.methods.get(mkey).seq.contains("mkdirs");
			
			assertTrue(b2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3() {
		try {
			proc.s = new ArgumentProcessor();
			proc.processByLine("/home/troy/research/BOA/Slicer/example/argument.txt");
			Multiset<MethodCall> mset = Slicer.methods.get(mkey).args.get("new File");
			
			assertEquals(3, mset.size());
			assertEquals(2, mset.elementSet().size());
			
			// mock objects
			ArrayList<String> list = new ArrayList<String>();
			list.add("v::path");
			MethodCall mock = new MethodCall("new File", list);
			assertEquals(2, mset.count(mock));
			
			// test the reverse argument map
			Multiset<MethodCall> rev_mset = Slicer.methods.get(mkey).rev_args.get("v::path");
			HashSet<String> apis = new HashSet<String>();
			rev_mset.forEach(mc -> {apis.add(mc.name);});
			assertEquals(2, rev_mset.size());
			assertEquals(1, apis.size());
			assertTrue(apis.contains("new File"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4() {
		try {
			proc.s = new AssignmentProcessor();
			proc.processByLine("/home/troy/research/BOA/Slicer/example/assignment.txt");
			Multiset<Assignment> mset = Slicer.methods.get(mkey).assigns.get("dir");
			
			assertEquals(1, mset.size());
			
			// mock objects
			ArrayList<String> uses = new ArrayList<String>();
			uses.add("m::new File");
			uses.add("v::path");
			Assignment mock = new Assignment("dir", uses);
			assertEquals(1, mset.count(mock));
			
			// test reverse assignment map
			Multiset<Assignment> rev_mset = Slicer.methods.get(mkey).rev_assigns.get("v::path");
			HashSet<String> vars = new HashSet<String>();
			rev_mset.forEach(assign -> {vars.add(assign.lhs);});
			assertEquals(2, vars.size());
			assertTrue(vars.contains("dir"));
			assertTrue(vars.contains("mFileTemp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test5() {
		try {
			proc.s = new ReceiverProcessor();
			proc.processByLine("/home/troy/research/BOA/Slicer/example/receiver.txt");
			Multiset<Receiver> mset = Slicer.methods.get(mkey).receivers.get("exists");
			
			assertEquals(3, mset.size());
			
			// mock objects
			Receiver mock = new Receiver("v::dir", "exists");
			assertEquals(1, mset.count(mock));
			
			// test reverse receiver map
			Multiset<Receiver> rev_mset = Slicer.methods.get(mkey).rev_receivers.get("v::dir");
			HashSet<String> apis = new HashSet<String>();
			rev_mset.forEach(rcv -> {apis.add(rcv.method);});
			assertEquals(2, apis.size());
			assertTrue(apis.contains("exists"));
			assertTrue(apis.contains("mkdirs"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test6() {
		try {
			proc.s = new PredicateProcessor();
			proc.processByLine("/home/troy/research/BOA/Slicer/example/predicate.txt");
			Multiset<Predicate> mset = Slicer.methods.get(mkey).predicates.get("createNewFile");
			
			assertEquals(1, mset.size());
			
			// mock objects
			Predicate mock = new Predicate("createNewFile", "!exists");
			assertEquals(1, mset.count(mock));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
