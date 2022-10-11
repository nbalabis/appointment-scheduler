package model;

import helper.JDBC;
import helper.SceneSwitcher;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Appointment {
    public static void add(String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException {
        //format dates
        String start = startDate.toString() + " " + startHour + ":" + startMinute;
        String end = endDate.toString() + " " + endHour + ":" + endMinute;

        //insert data into database
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setInt(5, contactID);
        ps.setString(6, start);
        ps.setString(7, end);
        ps.setInt(8, customerID);
        ps.setInt(9, userID);
        ps.executeUpdate();
    }

    public static void update(Integer apptID, String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException {
        //format dates
        String start = startDate.toString() + " " + startHour + ":" + startMinute;
        String end = endDate.toString() + " " + endHour + ":" + endMinute;

        //update data in database
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Contact_ID = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = ?;";

        PreparedStatement ps = JDBC.connection. prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setInt(5, contactID);
        ps.setString(6, start);
        ps.setString(7, end);
        ps.setInt(8, customerID);
        ps.setInt(9, userID);
        ps.setInt(10, apptID);
        ps.executeUpdate();
    }
}
