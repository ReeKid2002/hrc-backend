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

import com.connection.SQLConnector;
import com.google.gson.Gson;
import com.pojo.Predict;

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
			String queryToSelectDataForPredict = "SELECT w.business_code, w.cust_number, c.name_customer, w.clear_date, w.buisness_year, w.doc_id, w.posting_date, w.due_in_date, w.baseline_create_date, w.cust_payment_terms, CASE w.invoice_currency WHEN 'CAD' THEN (w.total_open_amount*0.7) ELSE w.total_open_amount END AS converted_usd FROM winter_internship AS w INNER JOIN customer AS c ON c.cust_number = w.cust_number WHERE w.is_deleted = 0 AND w.sl_no = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(queryToSelectDataForPredict);
					
			// Setting sl_no in Query.
			preparedStatement.setInt(1, Integer.parseInt(request.getParameter("sl_no")));
			
			// Object to store data for prediction.
			Predict predict = new Predict();
						
			// Executing the query.
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
						
			predict.setBusiness_code(resultSet.getString(1));
			predict.setCust_number(resultSet.getInt(2));
			predict.setName_customer(resultSet.getString(3));
			predict.setClear_date(resultSet.getString(4));
			predict.setBuisness_year(resultSet.getInt(5));
			predict.setDoc_id(resultSet.getString(6));
			predict.setPosting_date(resultSet.getDate(7));
			predict.setDue_in_date(resultSet.getDate(8));
			predict.setBaseline_create_date(resultSet.getDate(9));
			predict.setCust_payment_terms(resultSet.getString(10));
			predict.setConverted_usd(resultSet.getDouble(11));
			
			// Closing the connection to the DB.
			newConnection.closeConnection(connection, preparedStatement);
			
			// Creating JSON to the ArrayList.
			String predictJSON = new Gson().toJson(predict);
			
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
