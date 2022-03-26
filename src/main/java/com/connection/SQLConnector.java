package com.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnector {
	static final String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost:3306/grey_goose";
	static final String username = "root";
	static final String password = "root";
	public Connection getConnection() throws Exception{
		try {
			Class.forName(jdbcDriver); // Load the Class jdbcDriver without creating the instance and the static block of the class is executed.
			Connection connection = DriverManager.getConnection(url, username, password); // Creating Connection to the DB.
			return connection; // Returning the Connection object.
		} catch(Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	public void closeConnection(Connection connection, Statement statement) throws SQLException { // Method to close the connection. 
		try {
			connection.close();
			statement.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	public void closeConnection(Connection connection, PreparedStatement preparedStatement) throws SQLException { // Method to close the connection.
		try {
			connection.close();
			preparedStatement.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
}
