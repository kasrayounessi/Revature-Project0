package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionFactory {

    private static Connection connection = null;

    private ConnectionFactory(){}
    public static Connection getConnection(){
        if (connection == null){
            ResourceBundle bundle = ResourceBundle.getBundle("com/company/dbConfig");
            String url = bundle.getString("url");
            String userName = bundle.getString("username");
            String password = bundle.getString("password");
            try {
                connection = DriverManager.getConnection(url, userName, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}

