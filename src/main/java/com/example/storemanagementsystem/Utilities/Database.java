package com.example.storemanagementsystem.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection getConnection() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/store_management","root","");
            return connect;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
