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

public class Validate {
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

    public static boolean hasCustomerOverlap(Integer aptID, Integer customerID, Date start, Date end) throws SQLException {
        //dates are in local time --db dates are in utc
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
}
