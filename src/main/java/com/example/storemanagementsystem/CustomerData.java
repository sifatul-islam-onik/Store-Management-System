package com.example.storemanagementsystem;

import java.util.Date;

public class CustomerData {
    private int id;
    private int customerID;
    private double total;
    private Date date;
    private String employee_username;

    public CustomerData(int id, int customerID, double total, Date date, String employee_username) {
        this.id = id;
        this.customerID = customerID;
        this.total = total;
        this.date = date;
        this.employee_username = employee_username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmployee_username() {
        return employee_username;
    }

    public void setEmployee_username(String employee_username) {
        this.employee_username = employee_username;
    }
}
