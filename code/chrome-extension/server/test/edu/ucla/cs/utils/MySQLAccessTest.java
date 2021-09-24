package edu.ucla.cs.utils;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import edu.ucla.cs.maple.server.MySQLAccess;
import edu.ucla.cs.model.Pattern;

public class MySQLAccessTest {

	@Test
	public void testGetPatternsWithAlternativePatterns() {
		MySQLAccess access = new MySQLAccess();
		access.connect();
		HashSet<Pattern> plist = access.getPatterns("createNewFile", "File");
		access.close();
		assertEquals(3, plist.size());
		for(Pattern p : plist) {
			System.out.println(p.pattern);
		}
	}
}
