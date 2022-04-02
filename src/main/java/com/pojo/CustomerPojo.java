package com.pojo;

import java.sql.Date;

public class CustomerPojo {
	// Data Members.
	private int id;
	private int sl_no;
	private String business_code;
	private int cust_number;
	private String clear_date;
	private int business_year;
	private String doc_id;
	private Date posting_date;
	private Date document_create_date;
	private Date due_in_date;
	private String invoice_currency;
	private String document_type;
	private int posting_id;
	private double total_open_amount;
	private Date baseline_create_date;
	private String cust_payment_terms;
	private int invoice_id;
	
	// Constructor
	public CustomerPojo() {
		this.id = 0;
		this.sl_no = 0;
		this.business_code = "";
		this.cust_number = 0;
		this.clear_date = "";
		this.business_year = 0;
		this.doc_id = "";
		this.posting_date = null;
		this.document_create_date = null;
		this.due_in_date = null;
		this.invoice_currency = "";
		this.document_type = "";
		this.posting_id = 0;
		this.total_open_amount = 0;
		this.baseline_create_date = null;
		this.cust_payment_terms = "";
		this.invoice_id = 0;
	}
	
	// Getter and Setter Method for the Data Members.
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSl_no() {
		return sl_no;
	}
	public void setSl_no(int sl_no) {
		this.sl_no = sl_no;
	}
	public String getBusiness_code() {
		return business_code;
	}
	public void setBusiness_code(String business_code) {
		this.business_code = business_code;
	}
	public int getCust_number() {
		return cust_number;
	}
	public void setCust_number(int cust_number) {
		this.cust_number = cust_number;
	}
	public String getClear_date() {
		return clear_date;
	}
	public void setClear_date(String clear_date) {
		this.clear_date = ""+clear_date;
	}
	public int getBusiness_year() {
		return business_year;
	}
	public void setBusiness_year(int business_year) {
		this.business_year = business_year;
	}
	public String getDoc_id() {
		return doc_id;
	}
	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}
	public Date getPosting_date() {
		return posting_date;
	}
	public void setPosting_date(Date posting_date) {
		this.posting_date = posting_date;
	}
	public Date getDocument_create_date() {
		return document_create_date;
	}
	public void setDocument_create_date(Date document_create_date) {
		this.document_create_date = document_create_date;
	}
	public Date getDue_in_date() {
		return due_in_date;
	}
	public void setDue_in_date(Date due_in_date) {
		this.due_in_date = due_in_date;
	}
	public String getInvoice_currency() {
		return invoice_currency;
	}
	public void setInvoice_currency(String invoice_currency) {
		this.invoice_currency = invoice_currency;
	}
	public String getDocument_type() {
		return document_type;
	}
	public void setDocument_type(String document_type) {
		this.document_type = document_type;
	}
	public int getPosting_id() {
		return posting_id;
	}
	public void setPosting_id(int posting_id) {
		this.posting_id = posting_id;
	}
	public double getTotal_open_amount() {
		return total_open_amount;
	}
	public void setTotal_open_amount(double total_open_amount) {
		this.total_open_amount = total_open_amount;
	}
	public Date getBaseline_create_date() {
		return baseline_create_date;
	}
	public void setBaseline_create_date(Date baseline_create_date) {
		this.baseline_create_date = baseline_create_date;
	}
	public String getCust_payment_terms() {
		return cust_payment_terms;
	}
	public void setCust_payment_terms(String cust_payment_terms) {
		this.cust_payment_terms = cust_payment_terms;
	}
	public int getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(int invoice_id) {
		this.invoice_id = invoice_id;
	}
	
}
