package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import com.connection.SQLConnector;
import com.google.gson.Gson;

@WebServlet("/get-predict-data")
public class getPredictData extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public getPredictData() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The below line is to handle CORS Policy which prevent React which is running on different domain to make a GET or Post request to the Java Servlet. 
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		
		try {
			// Establishing DB Connection.
			SQLConnector newConnection = new SQLConnector();
			Connection connection = newConnection.getConnection();
					
			// Query to Select the rows in DB.
			String queryToSelectDataForPredict = "SELECT doc_id FROM winter_internship WHERE is_deleted = 0 AND sl_no = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(queryToSelectDataForPredict);
			
			//Getting Array of sl_no.
			String SLNOs = request.getParameter("sl_no");
			ArrayList<String> finalList = new ArrayList<String>();
			Collections.addAll(finalList, SLNOs.split(","));
			
			// ArrayList to store doc_id for prediction.
			ArrayList<Long> predictList = new ArrayList<Long>();
					
			// Traversing through the array to delete the row(s).
			for(String s: finalList) {
//				System.out.println(s);
				preparedStatement.setInt(1, Integer.parseInt(s));
				
				// Executing the query.
				ResultSet resultSet = preparedStatement.executeQuery();
				
				// Storing the value in ArrayList.
				resultSet.next();
				predictList.add(resultSet.getLong(1));
			}
						
			
			// Closing the connection to the DB.
			newConnection.closeConnection(connection, preparedStatement);
			
			// Creating JSON to the ArrayList.
			String predictJSON = new Gson().toJson(predictList);
			
//			System.out.println(predictJSON);
			
			// Sending JSON back to front-end.
			response.setStatus(200);
			response.getWriter().print(predictJSON);
					
		} catch (Exception error) {
			error.printStackTrace();
			response.setStatus(500);
			response.getWriter().print("Internal Server Error");
		}
	}
}
