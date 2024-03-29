package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static helper.TimeConversion.*;
import static helper.Validate.*;

/**
 * Appointment Model.
 *
 * @author Nicholas Balabis
 */
public class Appointment {
    /**
     * Collects and validates input values before creating a new appointment in the database.
     *
     * @param title Appointment title.
     * @param description Appointment description.
     * @param location Appointment location.
     * @param type Appointment type.
     * @param contactID Appointment contactID.
     * @param startDate Appointment start date.
     * @param startHour Appointment start hour.
     * @param startMinute Appointment start minute.
     * @param endDate Appointment end date.
     * @param endHour Appointment end hour.
     * @param endMinute Appointment end minute.
     * @param customerID Appointment customer ID.
     * @param userID Appointment User ID.
     * @return True/False value indicating whether or not the appointment add was successful.
     * @throws SQLException Throws SQLException.
     * @throws ParseException Throws ParseException.
     */
    public static boolean add(String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException, ParseException {
        //convert data to date
        Date start = convertToDate(startDate, startHour, startMinute);
        Date end = convertToDate(endDate, endHour, endMinute);

        //exit function early if doesn't pass validation
        if(isOutsideBusinessHours(start, end) ||
                hasCustomerOverlap(null, customerID, start, end) ||
                isEmpty(title) || isEmpty(description) || isEmpty(location) || isEmpty(type)) {
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

    /**
     * Collects and validates input values before updating an existing appointment in the database.
     *
     * @param aptID Appointment ID.
     * @param title Appointment title.
     * @param description Appointment description.
     * @param location Appointment location.
     * @param type Appointment type.
     * @param contactID Appointment contactID.
     * @param startDate Appointment start date.
     * @param startHour Appointment start hour.
     * @param startMinute Appointment start minute.
     * @param endDate Appointment end date.
     * @param endHour Appointment end hour.
     * @param endMinute Appointment end minute.
     * @param customerID Appointment customer ID.
     * @param userID Appointment User ID.
     * @return True/False value indicating whether or not the appointment update was successful.
     * @throws SQLException Throws SQLException.
     * @throws ParseException Throws ParseException.
     */
    public static boolean update(Integer aptID, String title, String description, String location, String type, Integer contactID, LocalDate startDate, Integer startHour, Integer startMinute, LocalDate endDate, Integer endHour, Integer endMinute, Integer customerID, Integer userID) throws SQLException, ParseException {
        //convert data to date
        Date start = convertToDate(startDate, startHour, startMinute);
        Date end = convertToDate(endDate, endHour, endMinute);

        //exit function early if doesn't pass validation
        if(isOutsideBusinessHours(start, end) ||
                hasCustomerOverlap(aptID, customerID, start, end) ||
                isEmpty(title) || isEmpty(description) || isEmpty(location) || isEmpty(type)) {
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

    /**
     * Deletes an appointment from the database.
     *
     * @param aptID ID of appointment to delete.
     * @throws SQLException Throws SQLException.
     */
    public static void delete(Integer aptID) throws SQLException {
        //find and delete apt from database
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, aptID);
        ps.executeUpdate();
    }

    /**
     * Displays any upcoming appointments for a specified user.
     *
     * @param userID User ID to check appointments for.
     * @throws SQLException Throws SQLException.
     * @throws ParseException Throws ParseException.
     */
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

    /**
     * Gets all distinct appointment types from database.
     *
     * @return An observableList of appointment types.
     * @throws SQLException Throws SQLException.
     */
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

    /**
     * Gets all distinct appointment locations from database.
     *
     * @return An observableList of locations.
     * @throws SQLException Throws SQLException.
     */
    public static ObservableList<String> getLocations() throws SQLException {
        String sql = "SELECT DISTINCT Location FROM appointments;";
        ObservableList<String> locations = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while(result.next()) {
            locations.add(result.getString("Location"));
        }
        return locations;
    }

    /**
     * Gets all appointments in database.
     *
     * @return A ResultSet containing all appointments in the database.
     * @throws SQLException Throws SQLException.
     */
    public static ResultSet getAll() throws SQLException {
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Gets all appointments occurring this month.
     *
     * @param currentYear Integer representing the current year.
     * @param currentMonth Integer representing the current month.
     * @return ResultSet containing all appointments that occur this month.
     * @throws SQLException Throws SQLException.
     */
    public static ResultSet getForMonth(Integer currentYear, Integer currentMonth) throws SQLException {
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments WHERE YEAR(Start) = ? AND MONTH(Start) = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, currentYear);
        ps.setInt(2, currentMonth);
        return  ps.executeQuery();
    }

    /**
     * Gets all appointments that occur within the next week.
     *
     * @param currentDate The current date.
     * @param oneWeekDate The date one week from today.
     * @return ResultSet containing all appointments that occur within the next week.
     * @throws SQLException Throws SQLException.
     */
    public static ResultSet getForWeek(LocalDate currentDate, LocalDate oneWeekDate) throws SQLException {
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments WHERE Start BETWEEN ? AND ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(currentDate));
        ps.setString(2, String.valueOf(oneWeekDate));
        return  ps.executeQuery();
    }

    /**
     * Get a specific appointment.
     *
     * @param aptID Appointment ID to match in database.
     * @return ResultSet containing the appointment.
     * @throws SQLException Throws SQLException.
     */
    public static ResultSet getByID(Integer aptID) throws SQLException {
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID FROM appointments WHERE Appointment_ID = " + aptID;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }
}
