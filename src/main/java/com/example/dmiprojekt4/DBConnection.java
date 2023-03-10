package com.example.dmiprojekt4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        Connection con;  // CONNECTS TO DATABASE

        try {
            con = DriverManager.getConnection("jdbc:sqlserver://LAPTOP-F11OIRMM:1433;databaseName=DMIDB;userName=sa;password=123456;encrypt=true;trustServerCertificate=true");
            return con;
        } catch (SQLException e) {
            System.err.println("cannot create connection  " + e.getMessage());
        }
        return null;
    }
}


