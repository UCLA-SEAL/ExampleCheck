package edu.ucla.cs.check;

import java.util.ArrayList;
import java.util.Random;

import edu.ucla.cs.utils.FileUtils;

public class AdjustViolationCSV {
	public static void main(String[] args) {
		String csv = "/media/troy/Disk2/Boa/violations.tsv";
		String s = FileUtils.readFileToString(csv);
		String[] ss = s.split(System.lineSeparator());
		int goal = 66897;
		int diff = ss.length - goal;
		
		// get the removed line numbers randomly
		Random r = new Random();
		ArrayList<Integer> remove = new ArrayList<Integer>();
		for(int i = 0; i < diff; i++) {
			int n = r.nextInt(ss.length);
			if(remove.contains(n)) {
				i--;
			} else {
				remove.add(n);
			}
		}
		
		String output = "/media/troy/Disk2/Boa/violations_new.tsv";
		for(int i = 0; i < ss.length; i++) {
			String line = ss[i];
			if(!remove.contains(i)) {
				FileUtils.appendStringToFile(line + System.lineSeparator(), output);
			}
		}
	}
}
