package edu.ucla.cs.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import edu.ucla.cs.parse.PartialProgramParser;

public class APICount {
	final String query = "select * from answers;";
	final String url = "jdbc:mysql://localhost:3306/stackoverflow";
	final String username = "root";
	final String password = "5887526";

	HashMap<String, Integer> map;

	public APICount() {
		this.map = new HashMap<String, Integer>();
	}

	public void count() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			Connection connect = DriverManager.getConnection(url, username,
					password);
			PreparedStatement prep = connect.prepareStatement(query);
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				String body = result.getString("Body");
				ArrayList<String> snippets = getCode(body);
				for (String snippet : snippets) {
					snippet = StringEscapeUtils.unescapeHtml4(snippet);
					if (!snippet.contains(System.lineSeparator())
							|| !snippet.contains(";")) {
						// skip those trivial snippets with a single line of
						// code
						continue;
					}

					PartialProgramParser parser = new PartialProgramParser();
					try {
						CompilationUnit cu = parser
								.getCompilationUnitFromString(snippet);
						if (cu != null
								&& (cu.getProblems() == null || cu
										.getProblems().length == 0)) {
							// reasonable AST
							MethodCallCollector c = new MethodCallCollector();
							cu.accept(c);
							for (String mc : c.count.keySet()) {
								if (map.containsKey(mc)) {
									map.put(mc, map.get(mc) + c.count.get(mc));
								} else {
									map.put(mc, c.count.get(mc));
								}
							}
						}
					} catch (Exception e) {
						// suppress the exception
						continue;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getCode(String body) {
		ArrayList<String> codes = new ArrayList<>();
		String start = "<code>", end = "</code>";
		int s = 0;
		while (true) {
			s = body.indexOf(start, s);
			if (s == -1)
				break;
			s += start.length();
			int e = body.indexOf(end, s);
			if (e == -1)
				break;
			codes.add(body.substring(s, e).trim());
			s = e + end.length();
		}
		return codes;
	}

	public static void main(String[] args) {
		APICount counter = new APICount();
		counter.count();
		LinkedList<Entry<String, Integer>> sortedList = new LinkedList<Entry<String, Integer>>(
				counter.map.entrySet());
		Collections.sort(sortedList, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				if (o1.getValue() > o2.getValue()) {
					return -1;
				} else if (o1.getValue() == o2.getValue()) {
					return 0;
				} else {
					return 1;
				}
			}

		});

		String output = "/home/troy/research/BOA/api_count_no_constructor.txt";
		for (Entry<String, Integer> entry : sortedList) {
			FileUtils.appendStringToFile(
					entry.getKey() + "," + entry.getValue() + System.lineSeparator(), output);
		}
	}
}

class MethodCallCollector extends ASTVisitor {
	String superClass;
	HashMap<String, String> fields;
	HashMap<String, String> locals;
	HashMap<String, Integer> count;

	public MethodCallCollector() {
		fields = new HashMap<String, String>();
		locals = new HashMap<String, String>();
		count = new HashMap<String, Integer>();
		superClass = null;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		Type superType = node.getSuperclassType();
		if (superType != null) {
			superClass = getType(superType);
		}

		// clean the symbol table for the fields
		fields.clear();
		FieldDeclaration[] fields = node.getFields();
		if (fields != null) {
			for (FieldDeclaration fd : fields) {
				fd.accept(this);
			}
		}

		MethodDeclaration[] methods = node.getMethods();
		if (methods != null) {
			for (MethodDeclaration md : methods) {
				md.accept(this);
			}
		}
		return false;
	}

	// build the symbol table for class fields
	@Override
	public boolean visit(FieldDeclaration node) {
		Type t = node.getType();
		List<VariableDeclarationFragment> frags = node.fragments();
		String type = getType(t);
		for (VariableDeclarationFragment frag : frags) {
			String var = frag.getName().toString();
			fields.put(var, type);
		}
		return false;
	}

	// build the symbol table for local variables
	@Override
	public boolean visit(SingleVariableDeclaration node) {
		Type t = node.getType();
		String var = node.getName().toString();
		String type = getType(t);
		locals.put(var, type);
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		Type t = node.getType();
		List<VariableDeclarationFragment> frags = node.fragments();
		String type = getType(t);
		for (VariableDeclarationFragment frag : frags) {
			String var = frag.getName().toString();
			locals.put(var, type);
		}
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		Type t = node.getType();
		List<VariableDeclarationFragment> frags = node.fragments();
		String type = getType(t);
		for (VariableDeclarationFragment frag : frags) {
			String var = frag.getName().toString();
			locals.put(var, type);
		}
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// clean the symbol table for the local variables
		locals.clear();
		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		Expression expr = node.getExpression();
		if (expr != null) {
			String receiver = expr.toString();
			String type = null;
			if (locals.containsKey(receiver)) {
				type = locals.get(receiver);
			} else if (fields.containsKey(receiver)) {
				type = fields.get(receiver);
			}

			String methodCall = null;
			if (type != null) {
				// type is resolved
				methodCall = type + "." + node.getName().toString();
			} else {
				if(Character.isUpperCase(receiver.charAt(0))) {
					// it is likely to be a call to a static method
					methodCall = receiver + "." + node.getName().toString();
				}
			}
			
			if(methodCall != null) {
				if (count.containsKey(methodCall)) {
					count.put(methodCall, count.get(methodCall) + 1);
				} else {
					count.put(methodCall, 1);
				}
			}
		}

		return true;
	}

//	@Override
//	public boolean visit(ClassInstanceCreation node) {
//		Type t = node.getType();
//		if (t.isParameterizedType()) {
//			ParameterizedType pt = (ParameterizedType) t;
//			t = pt.getType();
//		}
//
//		String methodCall = "new " + t;
//		if (count.containsKey(methodCall)) {
//			count.put(methodCall, count.get(methodCall) + 1);
//		} else {
//			count.put(methodCall, 1);
//		}
//
//		return true;
//	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		if (superClass != null) {
			String methodCall = superClass + "." + node.getName().toString();
			if (count.containsKey(methodCall)) {
				count.put(methodCall, count.get(methodCall) + 1);
			} else {
				count.put(methodCall, 1);
			}
		}
		return true;
	}

	private String getType(Type node) {
		String type = node.toString();
		if (node.isNameQualifiedType()) {
			QualifiedType qtype = (QualifiedType) node;
			type = qtype.getName().toString();
		} else if (node.isParameterizedType()) {
			ParameterizedType ptype = (ParameterizedType) node;
			type = ptype.getType().toString();
			// List<Type> params = ptype.typeArguments();
			// for(Type param : params) {
			// getType(param);
			// }
		}

		return type;
	}
}
