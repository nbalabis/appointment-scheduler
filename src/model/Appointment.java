package model;

import helper.JDBC;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

import static helper.TimeConversion.*;

public class Appointment {
    public static boolean add(String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException, ParseException {
        //convert data to date
        Date start = convertToDate(startDate, startHour, startMinute);
        Date end = convertToDate(endDate, endHour, endMinute);

        //exit function early if doesn't pass validation
        if(isOutsideBusinessHours(start, end)) {
            return false;
        }

        //convert date to UTC
        String startUTC = localToUTC(start);
        String endUTC = localToUTC(end);

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

        return true;
    }

    public static boolean update(Integer apptID, String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException, ParseException {
        //convert data to date
        Date start = convertToDate(startDate, startHour, startMinute);
        Date end = convertToDate(endDate, endHour, endMinute);

        //exit function early if doesn't pass validation
        if(isOutsideBusinessHours(start, end)) {
            return false;
        }

        //convert date to UTC
        String startUTC = localToUTC(start);
        String endUTC = localToUTC(end);

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

        return true;
    }

    public static void delete(Integer apptID) throws SQLException {
        //find and delete appt from database
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptID);
        ps.executeUpdate();
    }

    private static boolean isOutsideBusinessHours(Date start, Date end) throws ParseException {
        //convert to EST
        String startEST = localToEST(start);
        String endEST = localToEST(end);

        //check if start or end is outside business hours
        int startHour = Integer.parseInt(startEST.substring(11, 13));
        int endHour = Integer.parseInt(endEST.substring(11, 13));
        boolean isOutsideHours = startHour < 8 || startHour > 21 || endHour < 8 || endHour > 21;

        //display error
        if(isOutsideHours) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Appointment Time");
            alert.setHeaderText(null);
            alert.setContentText("Appointment start and end times must fall within business hours (8AM - 10PM EST).");
            alert.showAndWait();
        }

        return isOutsideHours;
    }
}
