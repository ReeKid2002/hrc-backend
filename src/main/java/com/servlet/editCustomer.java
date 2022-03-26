package com.servlet;
// External Imported Module
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.connection.SQLConnector;

@WebServlet("/edit")
public class editCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public editCustomer() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The below line is to handle CORS Policy which prevent React which is running on different domain to make a GET or Post request to the Java Servlet. 
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
					
		try {
			// Establishing DB Connection.
			SQLConnector newConnection = new SQLConnector();
			Connection connection = newConnection.getConnection();
			
			// Query to Edit the Invoice Currency and Customer Payment Terms in DB.
			String queryToUpdateICandCPT = "UPDATE winter_internship SET invoice_currency=?, cust_payment_terms=? WHERE sl_no=?";
			PreparedStatement preparedStatement = connection.prepareStatement(queryToUpdateICandCPT);
			
			// Setting Parameters in the SQL Query.
			preparedStatement.setString(1, request.getParameter("invoice_currency"));
			preparedStatement.setString(2, request.getParameter("cust_payment_terms"));
			preparedStatement.setInt(3, Integer.parseInt(request.getParameter("sl_no")));
			
			// Executing the query.
			int numberOfRowsAffected = preparedStatement.executeUpdate();
			
			// Closing the connection to the DB.
			newConnection.closeConnection(connection, preparedStatement);
						
			// Checking is the query is executed correctly and Sending result back to front-end.
			if(numberOfRowsAffected > 0) { // If Query Executed Successfully
				response.getWriter().print("Customer Succesfully Editted");
			} else { // If Query Executed but no change in DB.
				response.getWriter().print("Customer Can't Be Succesfully Editted");
			}
		} catch (Exception error) {
			error.printStackTrace();
			// To Handle Internal Server Error.
			response.getWriter().print("Internal Server Error");
		}
	}

}
