package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static model.Division.getID;

/**
 * Controller Logic for Add Customer Form.
 *
 * @author Nicholas Balabis
 */
public class AddCustomerForm implements Initializable {
    public TextField customerNameInput;
    public TextField addressInput;
    public TextField postalCodeInput;
    public TextField phoneInput;
    public ChoiceBox<String> countryPicker;
    public ChoiceBox<String> divisionPicker;

    /**
     * Initializes controller. Includes a lambda expression at the countryPicker event listener.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set country picker
        ObservableList<String> countryList = FXCollections.observableArrayList("Canada", "United Kingdom", "United States");
        countryPicker.setItems(countryList);
        //LAMBDA EXPRESSION - set divisionPicker based on country selection
        countryPicker.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            //get country selection
            String countryChoice = countryPicker.getItems().get((Integer) t1);
            Integer countryID = null;
            switch (countryChoice) {
                case "United States":
                    countryID = 1;
                    break;
                case "United Kingdom":
                    countryID = 2;
                    break;
                case "Canada":
                    countryID = 3;
                    break;
            }

            //set division picker based on selection
            ObservableList<String> divisionList = FXCollections.observableArrayList();
            try {
                ResultSet result = Division.getAllInCountry(countryID);
                while(result.next()) {
                    divisionList.add(result.getString("Division"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            divisionPicker.setItems(divisionList);
            divisionPicker.setDisable(false);
            divisionPicker.getSelectionModel().selectFirst();
        });

        //set initial contact, customer, and user choiceboxes
        countryPicker.getSelectionModel().selectFirst();
        divisionPicker.getSelectionModel().selectFirst();
    }

    /**
     * Passes input values to customer creation function when submit button is clicked.
     *
     * @param actionEvent 'Create New Customer' button clicked.
     * @throws SQLException Throws SQLException.
     * @throws IOException Throws IOException.
     */
    public void onCreateNewCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        //collect all input values
        String customerName = customerNameInput.getText();
        String address = addressInput.getText();
        String postalCode = postalCodeInput.getText();
        String phone = phoneInput.getText();
        Integer divisionID = getID(divisionPicker.getValue());

        //pass values through to function
        boolean successfulAdd = Customer.add(customerName, address, postalCode, phone, divisionID);

        //return to customers page
        if(successfulAdd) SceneSwitcher.toCustomers(actionEvent);
    }

    /**
     * Returns to customers page if cancel button is clicked.
     *
     * @param actionEvent 'Cancel' button clicked.
     * @throws IOException Throws IOEXception.
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }
}
