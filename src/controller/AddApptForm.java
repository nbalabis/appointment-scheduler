package controller;

import helper.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddApptForm implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set time pickers
        SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
        startHourFactory.setValue(0);
        SpinnerValueFactory<Integer> startMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,60);
        startMinuteFactory.setValue(0);
        startHourPicker.setValueFactory(startHourFactory);
        startMinutePicker.setValueFactory(startMinuteFactory);

        SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
        endHourFactory.setValue(0);
        SpinnerValueFactory<Integer> endMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,60);
        endMinuteFactory.setValue(0);
        endHourPicker.setValueFactory(endHourFactory);
        endMinutePicker.setValueFactory(endMinuteFactory);

        //set contact, customer, and user IDs
        try {
            contactIDPicker.setItems(Contact.getAllIDs());
            customerIDPicker.setItems(Customer.getAllIDs());
            userIDPicker.setItems(User.getAllIDs());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onCreateNewAppt(ActionEvent actionEvent) throws IOException, SQLException {
        //collect all input values
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

        //pass values through to creation function
        Appointment.add(title, description, location, type, contactID, startDate, startHour, startMinute, endDate, endHour, endMinute, customerID, userID);

        //return to appointment page
        SceneSwitcher.toAppts(actionEvent);
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        //return to appointments page
        SceneSwitcher.toAppts(actionEvent);
    }
}
