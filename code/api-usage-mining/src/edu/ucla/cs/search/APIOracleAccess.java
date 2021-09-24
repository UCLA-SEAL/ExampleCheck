package edu.ucla.cs.search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class APIOracleAccess {
	final String url = "jdbc:mysql://localhost:3306/apioracle";
	final String username = "root";
	final String password = "5887526";
	public Connection connect = null;
	public ResultSet result = null;
	public PreparedStatement prep = null;

	public void connect() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertAPIOracle(String className, String methodName, ArrayList<String> argTypes) {
		if (connect != null) {
			try {
				String args = "";
				if(!argTypes.isEmpty()) {
					for(int i = 0; i < argTypes.size(); i++) {
						String t = argTypes.get(i);
						args += t;
						if(i < argTypes.size() - 1) {
							args += ",";
						}
					}
				}
				
				if(!oracleExists(className, methodName, args)) {
					prep = connect
							.prepareStatement("insert into methods "
									+ "(class, method, arguments) "
									+ "values (?, ?, ?)");
					prep.setString(1, className);
					prep.setString(2, methodName);
					prep.setString(3, args);
					prep.executeUpdate();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean oracleExists(String className, String methodName, String args) {
		boolean exists = false;
		if (connect != null) {
			try {
				// construct the query
				String query = "select * from methods where class = \"" + className 
						+ "\" and method = \"" + methodName + "\" and arguments = \""
						+ args + "\";";
				prep = connect.prepareStatement(query);
				result = prep.executeQuery();
				exists = result.next();
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return exists;
	}

	public HashSet<String> resolveType(String methodName, ArrayList<String> argTypes) {
		HashSet<String> types = new HashSet<String>();

		if (connect != null) {
			try {
				// construct the query
				String query = "select * from methods where method = " + methodName + ";";
				prep = connect.prepareStatement(query);
				result = prep.executeQuery();
				while (result.next()) {
					String className = result.getString("class");
					String temp = result.getString("arguments");
					ArrayList<String> arguments = new ArrayList<String>();
					for(String s : temp.split(";")) {
						arguments.add(s);
					}
					
					// compare the argument types
					if(arguments.size() == argTypes.size()) {
						boolean flag = true;
						for(int i = 0; i < arguments.size(); i++) {
							String type = argTypes.get(i);
							String oracle = arguments.get(i);
							if(!type.equals("*") && !type.equals(oracle)) {
								flag = false;
								break;
							}
						}
						
						if(flag) {
							types.add(className);
						}
					}
				}

				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return types;
	}

	public void close() {
		try {
			if (result != null)
				result.close();
			if (prep != null)
				prep.close();
			if (connect != null)
				connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
