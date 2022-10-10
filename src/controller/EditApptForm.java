package controller;

import helper.JDBC;
import helper.TimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Contact;
import model.Customer;
import model.User;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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

    public void setApptData(Integer apptID) throws SQLException {
        //populate Appt data
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Contact_ID, Start, End, Customer_ID, User_ID FROM appointments WHERE Appointment_ID = " + apptID;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();
        apptIDInput.setText(String.valueOf(result.getInt("Appointment_ID")));
        titleInput.setText(result.getString("Title"));
        descriptionInput.setText(result.getString("Description"));
        locationInput.setText(result.getString("Location"));
        typeInput.setText(result.getString("Type"));
        contactIDPicker.getSelectionModel().select(result.getInt("Contact_ID"));
        startDateSelector.setValue(formatDate(result.getString("Start").substring(0,10)));
        startHourPicker.getValueFactory().setValue(Integer.valueOf(result.getString("Start").substring(11,13)));
        startMinutePicker.getValueFactory().setValue(Integer.valueOf(result.getString("Start").substring(14,16)));
        endDateSelector.setValue(formatDate(result.getString("End").substring(0,10)));
        endHourPicker.getValueFactory().setValue(Integer.valueOf(result.getString("End").substring(11,13)));
        endMinutePicker.getValueFactory().setValue(Integer.valueOf(result.getString("End").substring(14,16)));
        customerIDPicker.getSelectionModel().select(result.getInt("Customer_ID"));
        userIDPicker.getSelectionModel().select(result.getInt("User_ID"));
    }

    public void onSaveAppt(ActionEvent actionEvent) {
    }

    public void onCancel(ActionEvent actionEvent) {
    }


}
