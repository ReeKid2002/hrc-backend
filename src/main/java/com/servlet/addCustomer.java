package com.servlet;
// External Imported Packages
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

// User Defined Module
import com.connection.SQLConnector;

@WebServlet("/add")
public class addCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public addCustomer() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The below line is to handle CORS Policy which prevent React which is running on different domain to make a GET or Post request to the Java Servlet. 
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		
		try {
			// Creating New SQL Connection to the DB
			SQLConnector sqlConnector = new SQLConnector(); 
			Connection connection = sqlConnector.getConnection();
			
			// Query to get last/max sl_no.
			Statement statement = connection.createStatement();
			String queryToGetLastSLNO = "SELECT MAX(sl_no) FROM winter_internship"; 
			ResultSet resultSet = statement.executeQuery(queryToGetLastSLNO);
			resultSet.next();
			int maxslno = resultSet.getInt(1);
			statement.close();
			// Query for inserting new customer details in the DB. 
			String insertQuery = "INSERT INTO winter_internship(sl_no, business_code, cust_number, clear_date, buisness_year, doc_id, posting_date, document_create_date, due_in_date, invoice_currency, document_type, posting_id, total_open_amount, baseline_create_date, cust_payment_terms, invoice_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			// Setting Values of the field in the query.
			preparedStatement.setInt(1, maxslno + 1);
			preparedStatement.setString(2, request.getParameter("business_code"));
			preparedStatement.setInt(3, Integer.parseInt(request.getParameter("cust_number")));
			preparedStatement.setDate(4, request.getParameter("clear_date").length() == 0 ? null: java.sql.Date.valueOf((request.getParameter("clear_date"))));
			preparedStatement.setInt(5, Integer.parseInt(request.getParameter("business_year")));
			preparedStatement.setString(6, request.getParameter("doc_id"));
			preparedStatement.setDate(7, java.sql.Date.valueOf((request.getParameter("posting_date"))));
			preparedStatement.setDate(8, java.sql.Date.valueOf((request.getParameter("document_create_date"))));
			preparedStatement.setDate(9, java.sql.Date.valueOf((request.getParameter("due_in_date"))));
			preparedStatement.setString(10, request.getParameter("invoice_currency"));
			preparedStatement.setString(11, request.getParameter("document_type"));
			preparedStatement.setInt(12, Integer.parseInt(request.getParameter("posting_id")));
			preparedStatement.setDouble(13, Double.parseDouble(request.getParameter("total_open_amount")));
			preparedStatement.setDate(14, java.sql.Date.valueOf((request.getParameter("baseline_create_date"))));
			preparedStatement.setString(15, request.getParameter("cust_payment_terms"));
			preparedStatement.setInt(16, Integer.parseInt(request.getParameter("invoice_id")));
			
			// Executing the query.
			int numberOfRowsAffected = preparedStatement.executeUpdate();
			
			// Closing the connection to the DB.
			sqlConnector.closeConnection(connection, preparedStatement);
			
			// Checking is the query is executed correctly and Sending result back to front-end. 
			if(numberOfRowsAffected > 0) { // If Query Executed Successfully
				response.setStatus(200);
				response.getWriter().print("New Customer Succesfully Added");
			} else { // If Query Executed but no change in DB.
				response.setStatus(500);
				response.getWriter().print("New Customer Can't Be Succesfully Added");
			}
		} catch (Exception error) {
			error.printStackTrace();
			response.setStatus(500);
			// To Handle Internal Server Error.
			response.getWriter().print("Internal Server Error");
		}
	}

}
