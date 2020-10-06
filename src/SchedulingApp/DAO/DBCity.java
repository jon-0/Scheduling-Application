package SchedulingApp.DAO;

import static SchedulingApp.DAO.DBConnector.DB_CONN;
import SchedulingApp.Model.City;
import static SchedulingApp.View_Controller.LoginScreenController.loggedUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCity {
    
    public static int getCityId(String city) {
        String getCitySQL = "SELECT cityId FROM city WHERE city = ?";
        int cityId = 0;
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(getCitySQL);
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                cityId = rs.getInt("cityId");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return cityId;
    }
    
    public static City getCityById(int cityId) {
        String getCityByIdSQL = "SELECT * FROM city WHERE cityId = ?";
        City getCityById = new City();
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(getCityByIdSQL);
            stmt.setInt(1, cityId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                getCityById.setCityId(rs.getInt("cityId"));
                getCityById.setCity(rs.getString("city"));
                getCityById.setCountryId(rs.getInt("countryId"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return getCityById;
    }
    
    private static int getMaxCityId() {
        int maxCityId = 0;
        String maxCityIdSQL = "SELECT MAX(cityId) FROM city";
        
        try {
            Statement stmt = DB_CONN.createStatement();
            ResultSet rs = stmt.executeQuery(maxCityIdSQL);
            
            if (rs.next()) {
                maxCityId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return maxCityId + 1;
    }

    public static City addCity(City city) {
        String addCitySQL = String.join(" ",
                "INSERT INTO city (cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, NOW(), ?, NOW(), ?)");
        
        int cityId = getMaxCityId();
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(addCitySQL);
            stmt.setInt(1, cityId);
            stmt.setString(2, city.getCity());
            stmt.setInt(3, city.getCountryId());
            stmt.setString(4, loggedUser.getUserName());
            stmt.setString(5, loggedUser.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return city;
    }
    public static void updateCity(City city) {
        String updateCitySQL = String.join(" ",
                "UPDATE city",
                "SET city=?, countryId=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE cityId=?");
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(updateCitySQL);
            stmt.setString(1, city.getCity());
            stmt.setInt(2, city.getCountryId());
            stmt.setString(3, loggedUser.getUserName());
            stmt.setInt(4, city.getCityId());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCity(City city) {
        String deleteCitySQL = "DELETE FROM city WHERE cityId = ?";
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(deleteCitySQL);
            stmt.setInt(1, city.getCityId());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
