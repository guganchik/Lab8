package dao;

import utils.Const;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataAccessDriver {
    
    private static DataAccessDriver instance;
    private String password;

    private DataAccessDriver() {
    }

    public static DataAccessDriver getInstance() {
        if (instance == null) {
            instance = new DataAccessDriver();
        }
        return instance;
    }            

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Connection getConnection() {
        Connection result = null;
        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            //e.printStackTrace();
            return result;
        }

        try {
            result = DriverManager.getConnection(Const.DB_URL, Const.DB_USER, password);
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return result;
        }

        if (result == null) {
            System.out.println("Failed to make connection to database");
        }
        
        return result;
    }
    
}