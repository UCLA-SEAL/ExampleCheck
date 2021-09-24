package edu.ucla.cs.maple.ghlinks;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MatchMethod extends ASTVisitor{
	CompilationUnit cu;
	String methodName;
	int startLine;
	int endLine;
	
	public MatchMethod(CompilationUnit cu, String methodName) {
		this.cu = cu;
		this.methodName = methodName;
		startLine = -1;
		endLine = -1;
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		if (node.getName().toString().equals(methodName)) {
			startLine = cu.getLineNumber(node
					.getStartPosition());
			endLine = cu.getLineNumber(node.getStartPosition()
					+ node.getLength());
		}
		return false;
	}
}
