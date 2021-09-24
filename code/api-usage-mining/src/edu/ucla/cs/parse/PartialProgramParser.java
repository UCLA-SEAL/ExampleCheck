package edu.ucla.cs.parse;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class PartialProgramParser {
	public int cutype;
	private int flag = 0;

	public CompilationUnit getCompilationUnitFromString(String code)
			throws IOException, NullPointerException, ClassNotFoundException {
		ASTParser parser = getASTParser(code);
		ASTNode cu = (CompilationUnit) parser.createAST(null);
		// System.out.println(cu);
		cutype = 0;
		if (((CompilationUnit) cu).types().isEmpty()) {
			flag = 1;
			cutype = 1;
			// no class header, try to parse
			String s1 = "public class sample{\n" + code + "\n}";
			parser = getASTParser(s1);
			try {
				cu = parser.createAST(null);
			} catch(Exception e) {
				// parse error
				return null;
			}
			cu.accept(new ASTVisitor() {
				public boolean visit(MethodDeclaration node) {
					// find the method header
					flag = 2;
					return false;
				}
			});
			
			if (flag == 1) {
				// this code snippet has no method header nor class header
				s1 = "public class sample{\n public void foo(){\n" + code
						+ "\n}\n}";
				cutype = 2;
				parser = getASTParser(s1);
				try {
					cu = parser.createAST(null);
				} catch(Exception e) {
					// parse error
					return null;
				}
				
			}
			
			if (flag == 2) {
				// this code snippet has a method header but not class header
				s1 = "public class sample{\n" + code + "\n}";
				cutype = 1;
				parser = getASTParser(s1);
				try {
					cu = parser.createAST(null);
				} catch(Exception e) {
					// parse error
					return null;
				}
			}
		} else {
			// this code snippet has both class header and method header
			cutype = 0;
			parser = getASTParser(code);
			try {
				cu = parser.createAST(null);
			} catch(Exception e) {
				// parse error
				return null;
			}
		}
		return (CompilationUnit) cu;
	}

	private ASTParser getASTParser(String sourceCode) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setStatementsRecovery(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(sourceCode.toCharArray());
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
		parser.setCompilerOptions(options);
		return parser;
	}
}
