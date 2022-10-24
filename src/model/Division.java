package model;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Division model.
 *
 * @author Nicholas Balabis
 */
public class Division {
    /**
     * Get a division's ID.
     *
     * @param division Division name.
     * @return Integer representing Division's ID.
     * @throws SQLException Throws SQLException.
     */
    public static Integer getID(String division) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = \"" + division + "\";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();
        return result.getInt("Division_ID");
    }

    /**
     * Get all divisions within a country.
     *
     * @param countryID Country ID.
     * @return ResultSet containing all divisions included in given country.
     * @throws SQLException Throws SQLException.
     */
    public static ResultSet getAllInCountry(Integer countryID) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = " + countryID;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }
}
