package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Division {
    public static Integer getID(String division) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = \"" + division + "\";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();
        return result.getInt("Division_ID");
    }

    public static ResultSet getAllInCountry(Integer countryID) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = " + countryID;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }
}
