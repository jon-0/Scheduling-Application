package SchedulingApp.DAO;

import static SchedulingApp.DAO.DBConnector.DB_CONN;
import SchedulingApp.Model.Country;
import static SchedulingApp.View_Controller.LoginScreenController.loggedUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBCountry {
    
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        String getAllCountriesSQL = "SELECT * FROM country";
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(getAllCountriesSQL);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Country country = new Country();
                country.setCountryId(rs.getInt("countryId"));
                country.setCountry(rs.getString("country"));
                allCountries.add(country);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allCountries;
    }
    
    public static Country getCountryById(int countryId) {
        String getCountryByIdSQL = "SELECT * FROM country WHERE countryId = ?";
        Country getCountryById = new Country();
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(getCountryByIdSQL);
            stmt.setInt(1, countryId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                getCountryById.setCountryId(rs.getInt("countryId"));
                getCountryById.setCountry(rs.getString("country"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return getCountryById;
    }
    private static int getMaxCountryId() {
        int maxCountryId = 0;
        String maxCountryIdSQL = "SELECT MAX(countryId) FROM country";
        
        try {
            Statement stmt = DB_CONN.createStatement();
            ResultSet rs = stmt.executeQuery(maxCountryIdSQL);
            
            if (rs.next()) {
                maxCountryId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
        }
        return maxCountryId + 1;
    }
    
    public static int addCcountry(Country country) {
        String addCountrySQL = String.join(" ",
                "INSERT INTO country (countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, NOW(), ?, NOW(), ?)");
        
        int countryId = getMaxCountryId();
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(addCountrySQL);
            stmt.setInt(1, countryId);
            stmt.setString(2, country.getCountry());
            stmt.setString(3, loggedUser.getUserName());
            stmt.setString(4, loggedUser.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
        }
        return countryId;
    }
    public static void updateCountry(Country country) {
        String updateCountrySQL = String.join(" ",
                "UPDATE country",
                "SET country=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE countryId=?");
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(updateCountrySQL);
            stmt.setString(1, country.getCountry());
            stmt.setString(2, loggedUser.getUserName());
            stmt.setInt(3, country.getCountryId());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
    public void deleteCountry(Country country) {
        String deleteCountrySQL = "DELETE FROM country WHERE countryId = ?";
        
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(deleteCountrySQL);
            stmt.setInt(1, country.getCountryId());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
}
