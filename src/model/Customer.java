package model;

import controller.Customers;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.Validate.isEmpty;

public class Customer {
    public static void delete(Integer customerID) throws SQLException {
        //delete all associated appointments
        String sqlDeleteAppt = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement psDeleteAppt = JDBC.connection.prepareStatement(sqlDeleteAppt);
        psDeleteAppt.setInt(1, customerID);
        psDeleteAppt.executeUpdate();

        //delete customer
        String sqlDeleteCustomer = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement psDeleteCustomer = JDBC.connection.prepareStatement(sqlDeleteCustomer);
        psDeleteCustomer.setInt(1, customerID);
        psDeleteCustomer.executeUpdate();
    }

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
}
