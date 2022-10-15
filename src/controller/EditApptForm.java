package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import helper.TimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static helper.TimeConversion.UTCToLocal;
import static helper.TimeConversion.formatDate;

public class EditApptForm implements Initializable {
    public TextField titleInput;
    public TextField descriptionInput;
    public TextField locationInput;
    public TextField typeInput;
    public ChoiceBox<Integer> contactIDPicker;
    public DatePicker startDateSelector;
    public Spinner<Integer> startHourPicker;
    public Spinner<Integer> startMinutePicker;
    public DatePicker endDateSelector;
    public Spinner<Integer> endHourPicker;
    public Spinner<Integer> endMinutePicker;
    public ChoiceBox<Integer> customerIDPicker;
    public ChoiceBox<Integer> userIDPicker;
    public TextField apptIDInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set time pickers
        startHourPicker.setValueFactory(TimePicker.setHours());
        startMinutePicker.setValueFactory(TimePicker.setMinutes());
        endHourPicker.setValueFactory(TimePicker.setHours());
        endMinutePicker.setValueFactory(TimePicker.setMinutes());

        //set contact, customer, and user IDs
        try {
            contactIDPicker.setItems(Contact.getAllIDs());
            customerIDPicker.setItems(Customer.getAllIDs());
            userIDPicker.setItems(User.getAllIDs());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setApptData(Integer apptID) throws SQLException, ParseException {
        //populate Appt data
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID FROM appointments WHERE Appointment_ID = " + apptID;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();

        //convert dates from UTC to Local
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String start = UTCToLocal(dateFormat.parse(result.getString("Start")));
        String end = UTCToLocal(dateFormat.parse(result.getString("End")));

        apptIDInput.setText(String.valueOf(result.getInt("Appointment_ID")));
        titleInput.setText(result.getString("Title"));
        descriptionInput.setText(result.getString("Description"));
        locationInput.setText(result.getString("Location"));
        typeInput.setText(result.getString("Type"));
        contactIDPicker.getSelectionModel().select(Integer.valueOf(result.getString("Contact_ID")));
        startDateSelector.setValue(formatDate(start.substring(0,10)));
        startHourPicker.getValueFactory().setValue(Integer.valueOf(start.substring(11,13)));
        startMinutePicker.getValueFactory().setValue(Integer.valueOf(start.substring(14,16)));
        endDateSelector.setValue(formatDate(end.substring(0,10)));
        endHourPicker.getValueFactory().setValue(Integer.valueOf(end.substring(11,13)));
        endMinutePicker.getValueFactory().setValue(Integer.valueOf(end.substring(14,16)));
        customerIDPicker.getSelectionModel().select(Integer.valueOf(result.getString("Customer_ID")));
        userIDPicker.getSelectionModel().select(Integer.valueOf(result.getString("User_ID")));
    }

    public void onUpdateAppt(ActionEvent actionEvent) throws SQLException, IOException, ParseException {
        //collect all input values
        Integer apptID = Integer.valueOf(apptIDInput.getText());
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        String location = locationInput.getText();
        String type = typeInput.getText();
        Integer contactID = contactIDPicker.getValue();
        LocalDate startDate =  startDateSelector.getValue();
        Integer startHour = startHourPicker.getValue();
        Integer startMinute = startMinutePicker.getValue();
        LocalDate endDate = endDateSelector.getValue();
        Integer endHour = endHourPicker.getValue();
        Integer endMinute = endMinutePicker.getValue();
        Integer customerID = customerIDPicker.getValue();
        Integer userID = userIDPicker.getValue();

//      pass values through to update function
        boolean successfulUpdate = Appointment.update(apptID, title, description, location, type, contactID, startDate, startHour, startMinute, endDate, endHour, endMinute, customerID, userID);

        if(successfulUpdate) {
            //switch back to appts page
            SceneSwitcher.toAppts(actionEvent);
        }
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        //return to appointments page
        SceneSwitcher.toAppts(actionEvent);
    }


}
