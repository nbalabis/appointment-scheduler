package model;

import helper.JDBC;
import helper.SceneSwitcher;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static helper.TimeConversion.convertToDate;
import static helper.TimeConversion.toUTC;

public class Appointment {
    public static void add(String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException, ParseException {
        //convert data to date
        Date start = convertToDate(startDate, startHour, startMinute);
        Date end = convertToDate(endDate, endHour, endMinute);

        //convert date to UTC
        String startUTC = toUTC(start);
        String endUTC = toUTC(end);

        //insert data into database
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setInt(5, contactID);
        ps.setString(6, startUTC);
        ps.setString(7, endUTC);
        ps.setInt(8, customerID);
        ps.setInt(9, userID);
        ps.executeUpdate();
    }

    public static void update(Integer apptID, String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException, ParseException {
        //convert data to date
        Date start = convertToDate(startDate, startHour, startMinute);
        Date end = convertToDate(endDate, endHour, endMinute);

        //convert date to UTC
        String startUTC = toUTC(start);
        String endUTC = toUTC(end);

        //update data in database
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Contact_ID = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = ?;";
        PreparedStatement ps = JDBC.connection. prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setInt(5, contactID);
        ps.setString(6, startUTC);
        ps.setString(7, endUTC);
        ps.setInt(8, customerID);
        ps.setInt(9, userID);
        ps.setInt(10, apptID);
        ps.executeUpdate();
    }

    public static void delete(Integer apptID) throws SQLException {
        //find and delete appt from database
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptID);
        ps.executeUpdate();
    }
}
