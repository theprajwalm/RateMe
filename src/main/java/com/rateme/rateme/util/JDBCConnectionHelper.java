package com.rateme.rateme.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectionHelper {

    private static JDBCConnectionHelper instance;

    // --- YOUR DATABASE CREDENTIALS ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/swtp";
    private static final String USER = "root";
    private static final String PASS = "";

    // Private constructor for Singleton pattern
    private JDBCConnectionHelper() {
        try {
            // Loads the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found! Check your pom.xml", e);
        }
    }

    public static JDBCConnectionHelper getInstance() {
        if (instance == null) {
            instance = new JDBCConnectionHelper();
        }
        return instance;
    }

    // This is the method your tests call to get the active connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}