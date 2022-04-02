package com.servlet;
// External Imported Module
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.connection.SQLConnector;
import com.google.gson.Gson;
import com.pojo.CustomerPojo;

@WebServlet("/get-customer")
public class getAllCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public getAllCustomer() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The below line is to handle CORS Policy which prevent React which is running on different domain to make a GET or Post request to the Java Servlet. 
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		
		try {			
			// Creating New Connection to the DB
			SQLConnector newConnection = new SQLConnector();
			Connection connection = newConnection.getConnection();
			
			// Query to get All Customer Details from the DB.
			String toGetAllCustomer = "SELECT sl_no, business_code, cust_number, clear_date, buisness_year, doc_id, posting_date, document_create_date, due_in_date, invoice_currency, document_type, posting_id, total_open_amount, baseline_create_date, cust_payment_terms, invoice_id FROM winter_internship WHERE is_deleted=0;";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(toGetAllCustomer);
			
			// ArrayList to Store Details of All Customer in a single unit.
			ArrayList<CustomerPojo> allCustomer = new ArrayList<CustomerPojo>();
			
			// Storing the data of Customer from DB into the ArrayList.
			while(resultSet.next()) {
				CustomerPojo customer = new CustomerPojo();
				customer.setId(resultSet.getInt(1));
				customer.setSl_no(resultSet.getInt(1));
				customer.setBusiness_code(resultSet.getString(2));
				customer.setCust_number(resultSet.getInt(3));
				customer.setClear_date(resultSet.getString(4));
				customer.setBusiness_year(resultSet.getInt(5));
				customer.setDoc_id(resultSet.getString(6));
				customer.setPosting_date(resultSet.getDate(7));
				customer.setDocument_create_date(resultSet.getDate(8));
				customer.setDue_in_date(resultSet.getDate(9));
				customer.setInvoice_currency(resultSet.getString(10));
				customer.setDocument_type(resultSet.getString(11));
				customer.setPosting_id(resultSet.getInt(12));
				customer.setTotal_open_amount(resultSet.getDouble(13));
				customer.setBaseline_create_date(resultSet.getDate(14));
				customer.setCust_payment_terms(resultSet.getString(15));
				customer.setInvoice_id(resultSet.getInt(16));
				allCustomer.add(customer);
			}
			
			// Closing the connection to the DB.
			newConnection.closeConnection(connection, statement);
			
			// Creating JSON to the ArrayList.
			String allCustomerJSON = new Gson().toJson(allCustomer);
			
			// Sending JSON back to front-end.
			response.setStatus(200);
			response.getWriter().print(allCustomerJSON);
			
		} catch (Exception error) {
			error.printStackTrace();
			// Handling Internal Server Error
			response.setStatus(500);
			response.getWriter().print("Internal Server Error");
		}
	}
}
