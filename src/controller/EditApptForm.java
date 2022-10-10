package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Contact;
import model.Customer;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
        SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
        startHourFactory.setValue(0);
        SpinnerValueFactory<Integer> startMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);
        startMinuteFactory.setValue(0);
        startHourPicker.setValueFactory(startHourFactory);
        startMinutePicker.setValueFactory(startMinuteFactory);

        SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
        endHourFactory.setValue(0);
        SpinnerValueFactory<Integer> endMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);
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

    public void setApptData(Integer apptID) {
    }

    public void onSaveAppt(ActionEvent actionEvent) {
    }

    public void onCancel(ActionEvent actionEvent) {
    }


}
