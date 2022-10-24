package helper;

import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

import static helper.TimeConversion.localToEST;
import static helper.TimeConversion.localToUTC;
import static main.Main.CURRENT_USER_ID;

/**
 * Helpers for validating user submitted data.
 *
 * @author Nicholas Balabis
 */
public class Validate {
    /**
     * Checks if the username and password entered match database values
     *
     * @param userName Inputted userName.
     * @param password Inputted password.
     * @param language Current user's language.
     * @param rb resourceBundle.
     * @return True/False value indicating if the login was successful.
     * @throws SQLException Throws SQLException.
     */
    public static boolean isValidUser(String userName, String password, String language, ResourceBundle rb) throws SQLException {
        //search database for matching username and password
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet result =  ps.executeQuery();

        //check if user/pass matches database
        boolean isValid = result.next();

        if(isValid) {
            //set current user ID
            CURRENT_USER_ID = result.getInt("User_ID");
        } else {
            //display alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            if(language.equals("fr")) {
                alert.setTitle(rb.getString("alertTitle"));
                alert.setContentText(rb.getString("alertContentText"));
            } else {
                alert.setTitle("User Not Found");
                alert.setContentText("Invalid Username/Password. Please try again.");
            }
            alert.showAndWait();
        }
        return isValid;
    }

    /**
     * Checks if a given start and end date are outside of business hours.
     *
     * @param start Appointment start date/time.
     * @param end Appointment end date/time.
     * @return True/False value indicating if an appointment occurs outside of business hours.
     * @throws ParseException Throws ParseException.
     */
    public static boolean isOutsideBusinessHours(Date start, Date end) throws ParseException {
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

    /**
     * Determines if a customer's appointment overlaps with another appointment for the same customer.
     *
     * @param aptID The ID of the selected appointment.
     * @param customerID The customer's ID.
     * @param start The appointment start time.
     * @param end The appointment end time.
     * @return True/False value indicating whether or not there is an overlap.
     * @throws SQLException Throws SQLException.
     */
    public static boolean hasCustomerOverlap(Integer aptID, Integer customerID, Date start, Date end) throws SQLException {
        //convert dates to UTC
        String startUTC = localToUTC(start);
        String endUTC = localToUTC(end);

        //get all apts from database that match customer and fall between start and end
        String sql = "SELECT * FROM Appointments WHERE Customer_ID = ? AND Start BETWEEN ? AND ? OR End BETWEEN ? AND ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.setString(2, startUTC);
        ps.setString(3, endUTC);
        ps.setString(4, startUTC);
        ps.setString(5, endUTC);
        ResultSet result =  ps.executeQuery();

        //check if overlap exists
        boolean hasOverlap = result.next();
        //exclude overlaps with itself
        if(hasOverlap && aptID != null && result.getInt("Appointment_ID") == aptID) hasOverlap = false;

        //display error
        if(hasOverlap) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Appointment Time");
            alert.setHeaderText(null);
            alert.setContentText("Appointment overlaps with another appointment for this customer.");
            alert.showAndWait();
        }

        return hasOverlap;
    }

    /**
     * Checks if a given field has been left empty and displays relevant alert.
     *
     * @param field Input field to check.
     * @return True/False value indicating if the field has been left empty.
     */
    public static boolean isEmpty(String field) {
        //check if string is empty
        boolean emptyField = field.equals("");

        if(emptyField) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText(null);
            alert.setContentText("There are one or more empty fields. Please fill out each one before submitting.");
            alert.showAndWait();
        }

        return emptyField;
    }
}
