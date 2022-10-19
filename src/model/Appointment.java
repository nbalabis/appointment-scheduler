package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static helper.TimeConversion.*;
import static helper.Validate.hasCustomerOverlap;
import static helper.Validate.isOutsideBusinessHours;

public class Appointment {
    public static boolean add(String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException, ParseException {
        //convert data to date
        Date start = convertToDate(startDate, startHour, startMinute);
        Date end = convertToDate(endDate, endHour, endMinute);

        //exit function early if doesn't pass validation
        if(isOutsideBusinessHours(start, end) || hasCustomerOverlap(null, customerID, start, end)) {
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

    public static boolean update(Integer aptID, String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException, ParseException {
        //convert data to date
        Date start = convertToDate(startDate, startHour, startMinute);
        Date end = convertToDate(endDate, endHour, endMinute);

        //exit function early if doesn't pass validation
        if(isOutsideBusinessHours(start, end) || hasCustomerOverlap(aptID, customerID, start, end)) {
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
        ps.setInt(10, aptID);
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

    public static void displayUpcomingApts(Integer userID) throws SQLException, ParseException {
        //get current time and current time + 15
        Date currentTime = new Date();
        Date timeIn15 = new Date(currentTime.getTime() + (15 * 60 * 1000));

        //convert to UTC
        String currentTimeUTC = localToUTC(currentTime);
        String timeIn15UTC = localToUTC(timeIn15);

        //check database to see if any apts for current user start within 15 min
        String sql = "SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN ? AND ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ps.setString(2, currentTimeUTC);
        ps.setString(3, timeIn15UTC);
        ResultSet result =  ps.executeQuery();

        //display appropriate alert
        if(result.next()) {
            //convert start/end to Local
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            //Upcoming Apt Alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upcoming Appointment");
            alert.setHeaderText(null);
            alert.setContentText("You have scheduled appointment in less than 15 minutes. \n " +
                    "Appointment ID: " + result.getInt("Appointment_ID") +
                    "\n Start: " + UTCToLocal(dateFormat.parse(result.getString("Start"))) +
                    "\n End: " + UTCToLocal(dateFormat.parse(result.getString("End")))
            );
            alert.showAndWait();
        } else {
            //No Upcoming Apts Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Upcoming Appointments");
            alert.setHeaderText(null);
            alert.setContentText("You do not have any appointments scheduled in the next 15 minutes.");
            alert.showAndWait();
        }
    }

    public static ObservableList<String> getTypes() throws SQLException {
        String sql = "SELECT DISTINCT Type FROM appointments;";
        ObservableList<String> types = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            types.add(result.getString("Type"));
        }
        return types;
    }
}
