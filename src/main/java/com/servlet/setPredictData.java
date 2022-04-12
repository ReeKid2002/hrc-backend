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
@WebServlet("/set-predict-data")
public class setPredictData extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public setPredictData() {
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
			String queryToSetDataForPredict = "UPDATE winter_internship SET aging_bucket = ? WHERE doc_id = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(queryToSetDataForPredict);
			
			// Storing Data in ArrayList.
			ArrayList<String> agingBucketList = new ArrayList<String>();
			ArrayList<String> docIdList = new ArrayList<String>();
			Collections.addAll(agingBucketList, request.getParameter("aging_bucket").split(","));
			Collections.addAll(docIdList, request.getParameter("doc_id").split(","));
			System.out.println("AG: "+request.getParameter("aging_bucket")+", DI: "+request.getParameter("doc_id"));
			System.out.println("AG Size: "+agingBucketList.size()+", DI Size: "+docIdList.size());
			
			
			// Looping over the list and executing the query.
			int errorFlag = 0;
			for(int itr=0; itr<agingBucketList.size(); ++itr) {
				System.out.println("AB: "+agingBucketList.get(itr)+", DI: "+docIdList.get(itr));
				// Setting values for the Query.
				preparedStatement.setString(1, agingBucketList.get(itr));
				preparedStatement.setLong(2, (long)Double.parseDouble(docIdList.get(itr)));
				
				System.out.println(preparedStatement);
				// Executing the query.
				int numberOfRowsAffected = preparedStatement.executeUpdate();
				
				// Checking for Error.
				if(numberOfRowsAffected <= 0) {
					errorFlag = 1;
					break;
				}
			}
			
			// Closing the connection to the DB.
			newConnection.closeConnection(connection, preparedStatement);
			
			// Checking is the query is executed correctly and Sending result back to front-end.
			if(errorFlag == 0) { // If Query Executed Successfully
				response.setStatus(200);
				response.getWriter().print("Perdicted Succesfully");
			} else { // If Query Executed but no change in DB.
				response.setStatus(500);
				response.getWriter().print("Perdicted was Unsuccesfully. Please Try Again.");
			}
					
		} catch (Exception error) {
			error.printStackTrace();
			response.setStatus(500);
			response.getWriter().print("Internal Server Error");
		}
	}

}
