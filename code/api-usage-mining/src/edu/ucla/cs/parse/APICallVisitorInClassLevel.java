package edu.ucla.cs.parse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.ucla.cs.model.APISeqItem;
import edu.ucla.cs.model.ControlConstruct;

public class APICallVisitorInClassLevel extends ASTVisitor{
	ArrayList<APISeqItem> seq = new ArrayList<APISeqItem>();
	
	public boolean visit(FieldDeclaration node) {
		APICallVisitor cv = new APICallVisitor();
		node.accept(cv);
		seq.addAll(cv.seq);
		return false;
	}
	
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
		seq.addAll(cv.seq);
		
		return false;
	}
}
