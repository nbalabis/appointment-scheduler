package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contact model.
 *
 * @author Nicholas Balabis
 */
public class Contact {
    /**
     * Get all contact IDs from database.
     *
     * @return Observablelist of all contact IDs.
     * @throws SQLException Throws SQLException.
     */
    public static ObservableList<Integer> getAllIDs() throws SQLException {
        String sql = "SELECT * FROM contacts";
        ObservableList<Integer> IDs = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            IDs.add(result.getInt("Contact_ID"));
        }
        return IDs;
    }

    /**
     * Returns a list of all contact names.
     *
     * @return Observable List containing all contact names.
     * @throws SQLException Throws SQLException.
     */
    public static ObservableList<String> getAllNames() throws SQLException {
        //Get all names from database
        String sql = "SELECT Contact_Name FROM contacts;";
        ObservableList<String> names = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();

        //Add each name to result
        while(result.next()) {
            names.add(result.getString("Contact_Name"));
        }

        return names;
    }
}
