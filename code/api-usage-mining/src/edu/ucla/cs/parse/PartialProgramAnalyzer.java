package edu.ucla.cs.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.ucla.cs.model.APICall;
import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.utils.FileUtils;

public class PartialProgramAnalyzer {
	CompilationUnit cu;
	public boolean isIncomplete = false;
	
	public PartialProgramAnalyzer(String snippet) throws Exception {
		PartialProgramParser parser = new PartialProgramParser();
		// unescape html special characters, e.g., &amp;&amp; will become &&
		String code = StringEscapeUtils.unescapeHtml4(snippet);
		this.cu = parser.getCompilationUnitFromString(code);
		this.isIncomplete = parser.cutype == 0 ? false : true;
		if(this.cu == null) {
			throw new Exception("Partial Program Parse Error!");
		} else {
			IProblem[] errors = this.cu.getProblems();
			if(errors != null && errors.length != 0) {
				throw new Exception("Partial Program Parse Error!");
			}
		}
	}
	
	public HashMap<String, ArrayList<APISeqItem>> retrieveAPICallSequencesClassLevel() {
		if (this.cu == null) {
			return null;
		}
		
		APICallVisitorInClassLevel v = new APICallVisitorInClassLevel();
		this.cu.accept(v);
		
		HashMap<String, ArrayList<APISeqItem>> seqs = new HashMap<String, ArrayList<APISeqItem>>();
		
		seqs.put("class", v.seq);
		return seqs;
	}
	
	public HashMap<String, ArrayList<APISeqItem>> retrieveAPICallSequencesMethodLevel() {
		if (this.cu == null) {
			return null;
		}
		
		MethodVisitor mv = new MethodVisitor();
		this.cu.accept(mv);
		return mv.seqs;
	}
	
	public ArrayList<ArrayList<APISeqItem>> retrieveAPICallSequencesMethodLevel(HashSet<String> apis) {
		if (this.cu == null) {
			return null;
		}
		
		MethodVisitor mv = new MethodVisitor();
		this.cu.accept(mv);
		ArrayList<ArrayList<APISeqItem>> seqs = new ArrayList<ArrayList<APISeqItem>>();
		for(String method : mv.seqs.keySet()) {
			ArrayList<APISeqItem> seq = mv.seqs.get(method);
			ArrayList<String> calls = new ArrayList<String>();
			for(APISeqItem item : seq) {
				if(item instanceof APICall) {
					calls.add(((APICall)item).name);
				}
			}
			
			if(calls.containsAll(apis)) {
				seqs.add(seq);
			}
		}
		
		return seqs;
	}
	
	public APITypeVisitor resolveTypes() {
		if (this.cu == null) {
			return null;
		}
		
		APITypeVisitor visitor = new APITypeVisitor();
		this.cu.accept(visitor);
		return visitor;
	}
	
	public static void main(String[] args) throws Exception {
		String sample = "/home/troy/research/BOA/Maple/example/sample.txt";
		String snippet = FileUtils.readFileToString(sample);
		PartialProgramAnalyzer a = new PartialProgramAnalyzer(snippet);
		System.out.println(a.retrieveAPICallSequencesMethodLevel());	
	}
}
