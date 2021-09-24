package edu.ucla.cs.search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import edu.ucla.cs.model.Answer;

public class MySQLAccess {
	final String url = "jdbc:mysql://localhost:3306/stackoverflow";
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

	public HashSet<Answer> searchCodeSnippets(HashSet<HashSet<String>> queries) {
		HashSet<Answer> answers = new HashSet<Answer>();

		if (connect != null) {
			for (HashSet<String> keywords : queries) {
				try {
					// construct the query
					String query = "select * from answers";
					if (!keywords.isEmpty()) {
						query += " where";
						for (String keyword : keywords) {
							query += " body like \"%" + keyword + "%\" and";
						}
						query = query.substring(0, query.length() - 4);
					}

					query += ";";

					prep = connect.prepareStatement(query);
					result = prep.executeQuery();
					while (result.next()) {
						String id = result.getString("Id");
						String parentId = result.getString("ParentId");
						String body = result.getString("Body");
						String score = result.getString("Score");
						String isAccepted = result.getString("IsAccepted");
						String tags = result.getString("Tags");
						String viewCount = result.getString("ViewCount");
						Answer answer = new Answer(id, parentId, body, score,
								isAccepted, tags, viewCount);
						answers.add(answer);
					}

					result.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return answers;
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
