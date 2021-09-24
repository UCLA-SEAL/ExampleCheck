package edu.ucla.cs.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.ucla.cs.model.Answer;
import edu.ucla.cs.utils.FileUtils;

public class RankSOExampleByLength {
	public static void main(String[] args) {
		HashSet<String> typeQuery = new HashSet<String>();
		typeQuery.add("SQLiteDatabase");
		HashSet<ArrayList<String>> apiQueries = new HashSet<ArrayList<String>>();
		ArrayList<String> q1 = new ArrayList<String>();
		q1.add("query(7)");
		apiQueries.add(q1);
		Search search = new Search();
		HashSet<Answer> answers = search.search(typeQuery, apiQueries);
		ArrayList<Answer> sortedList = new ArrayList<Answer>();
		sortedList.addAll(answers);
		Collections.sort(sortedList, new Comparator<Answer>() {

			@Override
			public int compare(Answer o1, Answer o2) {
				String code1 = "";
				for(String s : o1.snippets) {
					code1 += s + System.lineSeparator();
					code1 += System.lineSeparator();
				}
				
				String code2 = "";
				for(String s : o2.snippets) {
					code2 += s + System.lineSeparator();
					code2 += System.lineSeparator();
				}
				
				if(code1.length() > code2.length()) {
					return 1;
				} else if (code1.length() < code2.length()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		// log to file
		String logFile = "/media/troy/Disk2/Boa/apis/SQLiteDatabase.query/so.txt";
		if(new File(logFile).exists()) {
			new File(logFile).delete();
		}
		for(Answer answer : sortedList) {
			FileUtils.appendStringToFile("https://stackoverflow.com/questions/" + answer.id + System.lineSeparator(), logFile);
			for(String s : answer.snippets) {
				String snippet = StringEscapeUtils.unescapeHtml4(s);
				FileUtils.appendStringToFile(snippet + System.lineSeparator(), logFile);
			}
		}
	}
}
