package model;

import controller.Customers;
import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
