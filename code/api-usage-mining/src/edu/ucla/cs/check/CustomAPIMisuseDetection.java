package edu.ucla.cs.check;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ControlConstruct;

public class CustomAPIMisuseDetection {
	@Test
	public void checkThreadSleep() {
		String output = "/media/troy/Disk2/Boa/apis/Thread.sleep/violations.txt";
		File outputFile = new File(output);
		if(outputFile.exists()) outputFile.delete();
		
		HashSet<String> typeQuery = new HashSet<String>();
		typeQuery.add("Thread");
		HashSet<ArrayList<String>> apiQueries = new HashSet<ArrayList<String>>();
		ArrayList<String> apiQuery = new ArrayList<String>();
		apiQuery.add("sleep(1)");
		apiQueries.add(apiQuery);
		// TRY, sleep(int)@true, END_BLOCK, CATCH(Exception), END_BLOCK
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		HashSet<ArrayList<APISeqItem>> ps = new HashSet<ArrayList<APISeqItem>>();
		ArrayList<APISeqItem> p = new ArrayList<APISeqItem>();
		p.add(ControlConstruct.TRY);
		p.add(new APICall("sleep", "true",1));
		p.add(ControlConstruct.END_BLOCK);
		p.add(ControlConstruct.CATCH);
		p.add(ControlConstruct.END_BLOCK);
		ps.add(p);
		pset.add(ps);
		ExtendedAPIMisuseDetection ead = new ExtendedAPIMisuseDetection(typeQuery, apiQueries, pset);
		ead.run(output);
	}
	
	@Test
	public void checkActivitySuperOnCreate() {
		String output = "/media/troy/Disk2/Boa/apis/Activity.super.onCreate/violations.txt";
		File outputFile = new File(output);
		if(outputFile.exists()) outputFile.delete();
		
		HashSet<String> typeQuery = new HashSet<String>();
		HashSet<ArrayList<String>> apiQueries = new HashSet<ArrayList<String>>();
		ArrayList<String> apiQuery = new ArrayList<String>();
		apiQuery.add("super.onCreate(1)");
		apiQueries.add(apiQuery);
		// TRY, sleep(int)@true, END_BLOCK, CATCH(Exception), END_BLOCK
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		HashSet<ArrayList<APISeqItem>> ps = new HashSet<ArrayList<APISeqItem>>();
		ArrayList<APISeqItem> p = new ArrayList<APISeqItem>();
		p.add(new APICall("super.onCreate", "true",1));
		p.add(new APICall("setContentView", "true",1));
		ps.add(p);
		pset.add(ps);
		ExtendedAPIMisuseDetection ead = new ExtendedAPIMisuseDetection(typeQuery, apiQueries, pset);
		ead.run(output);
	}
	
	@Test
	public void checkActivitySetContentView() {
		String output = "/media/troy/Disk2/Boa/apis/Activity.setContentView/violations.txt";
		File outputFile = new File(output);
		if(outputFile.exists()) outputFile.delete();
		
		HashSet<String> typeQuery = new HashSet<String>();
		HashSet<ArrayList<String>> apiQueries = new HashSet<ArrayList<String>>();
		ArrayList<String> apiQuery = new ArrayList<String>();
		apiQuery.add("setContentView(1)");
		apiQueries.add(apiQuery);
		// TRY, sleep(int)@true, END_BLOCK, CATCH(Exception), END_BLOCK
		HashSet<HashSet<ArrayList<APISeqItem>>> pset = new HashSet<HashSet<ArrayList<APISeqItem>>>();
		HashSet<ArrayList<APISeqItem>> ps = new HashSet<ArrayList<APISeqItem>>();
		ArrayList<APISeqItem> p = new ArrayList<APISeqItem>();
		p.add(new APICall("super.onCreate", "true",1));
		p.add(new APICall("setContentView", "true",1));
		ps.add(p);
		pset.add(ps);
		ExtendedAPIMisuseDetection ead = new ExtendedAPIMisuseDetection(typeQuery, apiQueries, pset);
		ead.run(output);
	}
}
