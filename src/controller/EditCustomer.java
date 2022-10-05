package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditCustomer implements Initializable {
    public TextField customerNameInput;
    public TextField addressInput;
    public TextField postalCodeInput;
    public TextField phoneInput;
    public ChoiceBox<String> countryPicker;
    public ChoiceBox divisionPicker;
    public TextField customerIDInput;

    public void onUpdateCustomer(ActionEvent actionEvent) {
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }

    public void setCustomerData(int customerID) throws SQLException {
        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, first_level_divisions.Division, countries.Country\n" +
                "FROM customers\n" +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID\n" +
                "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID\n" +
                "WHERE customers.Customer_ID = " + customerID;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();
        customerIDInput.setText(String.valueOf(result.getInt("Customer_ID")));
        customerNameInput.setText(result.getString("Customer_Name"));
        addressInput.setText(result.getString("Address"));
        postalCodeInput.setText(result.getString("Postal_Code"));
        phoneInput.setText(result.getString("Phone"));
        countryPicker.getSelectionModel().select(result.getString("Country"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> countryList = FXCollections.observableArrayList("Canada", "United Kingdom", "United States");
        countryPicker.setItems(countryList);

    }
}


