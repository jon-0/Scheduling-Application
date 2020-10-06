package SchedulingApp.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String DB_NAME = "U05yHQ";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + DB_NAME;
    private static final String USERNAME = "U05yHQ";
    private static final String PASSWORD = "53688645162";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
            
    static Connection DB_CONN;
    
    public static void openConnection() throws ClassNotFoundException, SQLException, Exception
    {
        Class.forName(DRIVER);
        DB_CONN = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        System.out.println("Connection Opened Successfully");
    }
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception
    {
        DB_CONN.close();
        System.out.println("Connection Closed Successfully");
    }
}
