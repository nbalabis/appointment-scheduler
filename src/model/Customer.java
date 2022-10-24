package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.Validate.isEmpty;

/**
 * Customer Model.
 *
 * @author Nicholas Balabis
 */
public class Customer {
    /**
     * Deletes a customer and all related appointments matching a given ID from the database.
     *
     * @param customerID ID of customer to delete.
     * @throws SQLException Throws SQLException.
     */
    public static void delete(Integer customerID) throws SQLException {
        //delete all associated appointments
        String sqlDeleteApt = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement psDeleteApt = JDBC.connection.prepareStatement(sqlDeleteApt);
        psDeleteApt.setInt(1, customerID);
        psDeleteApt.executeUpdate();

        //delete customer
        String sqlDeleteCustomer = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement psDeleteCustomer = JDBC.connection.prepareStatement(sqlDeleteCustomer);
        psDeleteCustomer.setInt(1, customerID);
        psDeleteCustomer.executeUpdate();
    }

    /**
     * Returns all customer IDs in database.
     *
     * @return Observable List containing call customer IDs.
     * @throws SQLException Throws SQLException.
     */
    public static ObservableList<Integer> getAllIDs() throws SQLException {
        String sql = "SELECT * FROM customers";
        ObservableList<Integer> IDs = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            IDs.add(result.getInt("Customer_ID"));
        }
        return IDs;
    }

    /**
     * Collects and validates input values before creating a new customer in the database.
     *
     * @param customerName Customer Name.
     * @param address Customer Address.
     * @param postalCode Customer Postal Code.
     * @param phone Customer Phone Number.
     * @param divisionID Customer Division ID.
     * @return True/False value indicating whether or not the customer was added to the database successfully.
     * @throws SQLException Throws SQLException.
     */
    public static boolean add(String customerName, String address, String postalCode, String phone, Integer divisionID) throws SQLException {
        //exit function early if doesn't pass validation
        if(isEmpty(customerName) || isEmpty(address) || isEmpty(postalCode) || isEmpty(phone)) return false;

        //insert data into database
        String sql = "INSERT INTO customers (Customer_name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionID);
        ps.executeUpdate();

        return true;
    }

    /**
     * Collects and validates input values before updating an existing customer in the database.
     *
     * @param customerName Customer Name.
     * @param address Customer Address.
     * @param postalCode Customer Postal Code.
     * @param phone Customer Phone Number.
     * @param divisionID Customer Division ID.
     * @param customerID Customer ID.
     * @return True/False value indicating whether or not the customer was updated successfully.
     * @throws SQLException Throws SQLException.
     */
    public static boolean update(String customerName, String address, String postalCode, String phone, Integer divisionID, int customerID) throws SQLException {
        //exit function early if doesn't pass validation
        if(isEmpty(customerName) || isEmpty(address) || isEmpty(postalCode) || isEmpty(phone)) return false;

        //update data in database
        String sql = "UPDATE customers SET Customer_name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionID);
        ps.setInt(6, customerID);
        ps.executeUpdate();

        return true;
    }

    /**
     * Get all customers from the database.
     *
     * @return ResultSet containing all customers.
     * @throws SQLException Throws SQLException.
     */
    public static ResultSet getAll() throws SQLException {
        String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM customers;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }
}
