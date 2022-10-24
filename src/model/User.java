package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User model.
 *
 * @author Nicholas Balabis
 */
public class User {
    /**
     * Gets all user IDs.
     *
     * @return Observable List containing all user IDs.
     * @throws SQLException Throws SQLException.
     */
    public static ObservableList<Integer> getAllIDs() throws SQLException {
        String sql = "SELECT * FROM users";
        ObservableList<Integer> IDs = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            IDs.add(result.getInt("User_ID"));
        }
        return IDs;
    }
}
