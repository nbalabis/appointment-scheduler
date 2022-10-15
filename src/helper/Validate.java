package helper;

import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import static helper.TimeConversion.localToEST;
import static helper.TimeConversion.localToUTC;

public class Validate {
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

    public static boolean hasCustomerOverlap(Integer customerID, Date start, Date end) throws SQLException {
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
