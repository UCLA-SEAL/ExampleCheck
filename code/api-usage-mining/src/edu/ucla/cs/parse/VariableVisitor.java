package edu.ucla.cs.parse;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;

public class VariableVisitor extends ASTVisitor{
	public ArrayList<String> vars;
	
	public VariableVisitor() {
		this.vars = new ArrayList<String>();
	}
	
	@Override
	public boolean visit(SimpleName node) {
		vars.add(node.getIdentifier());
		return false;
	}
}
