package edu.ucla.cs.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ControlConstruct;

public class MethodVisitor extends ASTVisitor{
	public HashMap<String, ArrayList<APISeqItem>> seqs = new HashMap<String, ArrayList<APISeqItem>>();
	
	public boolean visit(MethodDeclaration node) {
		String method = node.getName().toString();
		
		List excpts = node.thrownExceptionTypes();
		boolean flag = false;
		if(excpts != null && !excpts.isEmpty()) {
			flag = true;
		}
		
		APICallVisitor cv = new APICallVisitor();
		node.accept(cv);
		if(flag) {
			// this method throws exception
			cv.seq.add(0, ControlConstruct.TRY);
			cv.seq.add(ControlConstruct.END_BLOCK);
			cv.seq.add(ControlConstruct.CATCH);
			cv.seq.add(ControlConstruct.END_BLOCK);
		}
		seqs.put(method, cv.seq);
		
		return false;
	}
}
