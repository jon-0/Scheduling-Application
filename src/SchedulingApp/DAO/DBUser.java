package SchedulingApp.DAO;

import SchedulingApp.Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBUser {
    
    private final static Connection DB_CONN = DBConnector.DB_CONN;
    
    public DBUser () {
    }
    
    public static ObservableList<User> getActiveUsers() {
        ObservableList<User> activeUsers = FXCollections.observableArrayList();
        String getActiveUsers = "SELECT * FROM user WHERE active = 1";
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(getActiveUsers);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                User activeUser = new User();
                activeUser.setUserId(rs.getInt("userId"));
                activeUser.setUserName(rs.getString("userName"));
                activeUser.setPassword(rs.getString("password"));
                activeUser.setActive(rs.getBoolean("active"));
                
                activeUsers.add(activeUser);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return activeUsers;
    }
    
    public static User getUserById(int userId) {
        String getUserByIdSQL = "SELECT * FROM user WHERE userId=?";
        User user = new User();
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(getUserByIdSQL);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
