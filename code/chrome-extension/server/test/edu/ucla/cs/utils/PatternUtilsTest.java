package edu.ucla.cs.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.CATCH;
import edu.ucla.cs.model.ControlConstruct;

public class PatternUtilsTest {
	@Test
	public void testConvertPatternFromStringToArrayList() {
		String pattern = "TRY, new FileInputStream(File)@true, END_BLOCK, CATCH(Exception), END_BLOCK";
		ArrayList<APISeqItem> pArray = PatternUtils.convert(pattern);
		assertEquals(ControlConstruct.TRY, pArray.get(0));
		ArrayList<String> argList = new ArrayList<String>();
		argList.add("File");
		APICall expectedCall = new APICall("new FileInputStream", "true", null, null, argList);
		assertEquals(expectedCall, pArray.get(1));
		assertEquals(ControlConstruct.END_BLOCK, pArray.get(2));
		assertEquals(new CATCH("Exception"), pArray.get(3));
		assertEquals(ControlConstruct.END_BLOCK, pArray.get(4));
	}
}
