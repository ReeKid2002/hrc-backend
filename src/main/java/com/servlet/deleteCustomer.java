package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;

import com.connection.SQLConnector;

@WebServlet("/delete")
public class deleteCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public deleteCustomer() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The below line is to handle CORS Policy which prevent React which is running on different domain to make a GET or Post request to the Java Servlet. 
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		
		try {
			// Establishing DB Connection.
			SQLConnector newConnection = new SQLConnector();
			Connection connection = newConnection.getConnection();
			
			// Query to Delete the rows in DB.
			String queryToDeleteRows = "UPDATE winter_internship SET is_deleted=1 WHERE sl_no=?";
			PreparedStatement preparedStatement = connection.prepareStatement(queryToDeleteRows);
			
			//Getting Array of sl_no.
			String SLNOs = request.getParameter("sl_no");
			String subSLNOs = SLNOs.substring(1, SLNOs.length() - 1);
			ArrayList<String> finalList = new ArrayList<String>();
			Collections.addAll(finalList, subSLNOs.split(","));
			
			// Traversing through the array to delete the row(s).
			int errorFlag = 0;
			for(String s: finalList) {
//				System.out.println(s);
				preparedStatement.setInt(1, Integer.parseInt(s));
				
				// Executing the query.
				int numberOfRowsAffected = preparedStatement.executeUpdate();
				
				// Checking is the query is executed correctly and Sending result back to front-end. 
				if(numberOfRowsAffected > 0) { // If Query Executed Successfully
					continue;
				} else { // If Query Executed but no change in DB.
					errorFlag = 1;
					break;
				}
			}
			
			if(errorFlag == 0) {
				response.setStatus(200);
				response.getWriter().print("Invoice(s) Deleted Successfully.");
			} else {
				response.setStatus(500);
				response.getWriter().print("Invoice(s) Deleted Successfully.");
			}
			
			// Closing the connection to the DB.
			newConnection.closeConnection(connection, preparedStatement);
			
		} catch (Exception error) {
			error.printStackTrace();
			response.setStatus(500);
			response.getWriter().print("Internal Server Error");
		}
	}

}
