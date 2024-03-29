package controller;

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
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller logic for Add Appointment Form.
 *
 * @author Nicholas Balabis
 */
public class AddAptForm implements Initializable {
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

    /**
     * Initializes controller.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
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

        //set initial contact, customer, and user choiceboxes
        contactIDPicker.getSelectionModel().selectFirst();
        customerIDPicker.getSelectionModel().selectFirst();
        userIDPicker.getSelectionModel().selectFirst();
    }

    /**
     * Passes input values to appointment creation function when submit button is clicked.
     *
     * @param actionEvent 'Create New Appointment' button clicked.
     * @throws IOException Throws IOException.
     * @throws SQLException Throws SQLException.
     * @throws ParseException Throws ParseException.
     */
    public void onCreateNewAppt(ActionEvent actionEvent) throws IOException, SQLException, ParseException {
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
        boolean successfulAdd = Appointment.add(title, description, location, type, contactID, startDate, startHour, startMinute, endDate, endHour, endMinute, customerID, userID);

        //return to apts page
        if(successfulAdd) SceneSwitcher.toAppts(actionEvent);
    }

    /**
     * Returns to appointments page if cancel button is clicked.
     *
     * @param actionEvent 'Cancel' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAppts(actionEvent);
    }
}
