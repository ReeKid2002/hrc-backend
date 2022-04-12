package com.servlet;

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
import java.util.HashMap;
import java.util.Map;

import com.connection.SQLConnector;
import com.google.gson.Gson;
import com.pojo.AnalyticsBarGraph;
import com.pojo.AnalyticsPieChart;

@WebServlet("/analtyic")
public class analytics extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public analytics() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The below line is to handle CORS Policy which prevent React which is running on different domain to make a GET or Post request to the Java Servlet. 
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		try {			
			// Creating New Connection to the DB
			SQLConnector newConnection = new SQLConnector();
			Connection connection = newConnection.getConnection();
					
			// Query to get Customer Details for Bar Graph.
			String analytics1 = "SELECT business_code, SUM(total_open_amount), COUNT(cust_number) FROM winter_internship WHERE is_deleted = 0";
			
			// Query to get Customer Details for Pie Chart.
			String analytics2 = "SELECT invoice_currency, COUNT(invoice_currency) FROM winter_internship WHERE is_deleted = 0";
			
			// Changing Query for clear_date 
			if(request.getParameter("clearDateStart").length() == 0 || request.getParameter("clearDateEnd").length() == 0) {
				if(request.getParameter("clearDateStart").length() == 0 && request.getParameter("clearDateEnd").length() == 0) {
					analytics1 += "";
					analytics2 += "";
				} else if(request.getParameter("clearDateStart").length() != 0) {
					analytics1 += " AND clear_date >= '" + request.getParameter("clearDateStart") + "'";
					analytics2 += " AND clear_date >= '" + request.getParameter("clearDateStart") + "'";
				} else {
					analytics1 += " AND clear_date <= '" + request.getParameter("clearDateEnd") + "'";
					analytics2 += " AND clear_date <= '" + request.getParameter("clearDateEnd") + "'";
				}
			} else {
				analytics1 += " AND clear_date BETWEEN '"+ request.getParameter("clearDateStart") + "'" +" AND '"+ request.getParameter("clearDateEnd") + "'";
				analytics2 += " AND clear_date BETWEEN '"+ request.getParameter("clearDateStart") + "'" +" AND '"+ request.getParameter("clearDateEnd") + "'";
			}
			
			// Changing Query for due_in_date
			if(request.getParameter("dueDateStart").length() == 0 || request.getParameter("dueDateEnd").length() == 0) {
				if(request.getParameter("dueDateStart").length() == 0 && request.getParameter("dueDateEnd").length() == 0) {
					analytics1 += "";
					analytics2 += "";
				} else if(request.getParameter("dueDateStart").length() != 0) {
					analytics1 += " AND due_in_date >= '" + request.getParameter("dueDateStart") + "'";
					analytics2 += " AND due_in_date >= '" + request.getParameter("dueDateStart") + "'";
				} else {
					analytics1 += " AND due_in_date <= '" + request.getParameter("dueDateEnd") + "'";
					analytics2 += " AND due_in_date <= '" + request.getParameter("dueDateEnd") + "'";
				}
			} else {
				analytics1 += " AND due_in_date BETWEEN '"+ request.getParameter("dueDateStart") + "'" +" AND '" + request.getParameter("dueDateEnd") + "'";
				analytics2 += " AND due_in_date BETWEEN '"+ request.getParameter("dueDateStart") + "'" +" AND '" + request.getParameter("dueDateEnd") + "'";
			}
			
			// Changing Query for baseline_create_date
			if(request.getParameter("baseDateStart").length() == 0 || request.getParameter("baseDateEnd").length() == 0) {
				if(request.getParameter("baseDateStart").length() == 0 && request.getParameter("baseDateEnd").length() == 0) {
					analytics1 += "";
					analytics2 += "";
				} else if(request.getParameter("baseDateStart").length() != 0) {
					analytics1 += " AND baseline_create_date >= '" + request.getParameter("baseDateStart") + "'";
					analytics2 += " AND baseline_create_date >= '" + request.getParameter("baseDateStart") + "'";
				} else {
					analytics1 += " AND baseline_create_date <= '" + request.getParameter("baseDateEnd") + "'";
					analytics2 += " AND baseline_create_date <= '" + request.getParameter("baseDateEnd") + "'";
				}
			} else {
				analytics1 += " AND baseline_create_date BETWEEN '"+ request.getParameter("baseDateStart") + "'" +" AND '" + request.getParameter("baseDateEnd") + "'";
				analytics2 += " AND baseline_create_date BETWEEN '"+ request.getParameter("baseDateStart") + "'" +" AND '" + request.getParameter("baseDateEnd") + "'";
			}
			
			if(request.getParameter("invoiceCurrency").length() != 0) {
				analytics1 += " AND invoice_currency LIKE '"+request.getParameter("invoiceCurrency")+"%'";
				analytics2 += " AND invoice_currency LIKE '"+request.getParameter("invoiceCurrency")+"%'";
			}
			analytics1 += " GROUP BY business_code;";
			analytics2 += " GROUP BY invoice_currency;";
			
//			System.out.println(analytics1);
//			System.out.println(analytics2);
			Statement statement1 = connection.createStatement();
			Statement statement2 = connection.createStatement();
			
			ResultSet resultSet1 = statement1.executeQuery(analytics1);
			ResultSet resultSet2 = statement2.executeQuery(analytics2);
					
			// ArrayList to Store Details of Bar Graph Data in a single unit.
			ArrayList<AnalyticsBarGraph> barGraph = new ArrayList<AnalyticsBarGraph>();
			
			// ArrayList to Store Details of Bar Graph Data in a single unit.
			ArrayList<AnalyticsPieChart> pieChart = new ArrayList<AnalyticsPieChart>();
					
			// Storing the data from DB into the ArrayList.
			while(resultSet1.next()) {
				AnalyticsBarGraph analyticData = new AnalyticsBarGraph();
				analyticData.setBusiness_code(resultSet1.getString(1));
				analyticData.setTotal_open_amount(resultSet1.getDouble(2));
				analyticData.setTotal_customer(resultSet1.getInt(3));
				barGraph.add(analyticData);
			}
			
			while(resultSet2.next()) {
				AnalyticsPieChart analyticsData = new AnalyticsPieChart();
				analyticsData.setInvoice_currency(resultSet2.getString(1));
				analyticsData.setTotal_invoice_currency(resultSet2.getInt(2));
				pieChart.add(analyticsData);
			}
			
					
			// Closing the connection to the DB.
			newConnection.closeConnection(connection, statement1, statement2);
			
			// HashMap to send Both Data at a time.
			HashMap<String, ArrayList> hashMap = new HashMap<String, ArrayList>();
			hashMap.put("bargraph", barGraph);
			hashMap.put("piechart", pieChart);
			
			// Converting Map to JSON.
			String Graph = new Gson().toJson(hashMap);
//			System.out.println(Graph);

			// Sending JSON back to front-end.
			response.setStatus(200);
			response.getWriter().print(Graph);
					
		} catch (Exception error) {
			error.printStackTrace();
			// Handling Internal Server Error
			response.setStatus(500);
			response.getWriter().print("Internal Server Error");
		}
	}
}
