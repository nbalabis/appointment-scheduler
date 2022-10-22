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

/**
 * Controller for EditCustomerForm.
 *
 * @author Nicholas Balabis
 */
public class EditCustomerForm implements Initializable {
    public TextField customerNameInput;
    public TextField addressInput;
    public TextField postalCodeInput;
    public TextField phoneInput;
    public ChoiceBox<String> countryPicker;
    public ChoiceBox<String> divisionPicker;
    public TextField customerIDInput;
    private int customerID;

    /**
     * Initializes controller. Contains a lambda expression at the countryPicker event listener.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set country list
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
                while (result.next()) {
                    divisionList.add(result.getString("Division"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            divisionPicker.setItems(divisionList);
            divisionPicker.setDisable(false);
        });
    }

    /**
     * Sets all input fields to values for selected customer.
     *
     * @param customerID Customer ID for selected customer.
     * @throws SQLException Throws SQLException.
     */
    public void setCustomerData(int customerID) throws SQLException {
        //get customer data from database
        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, first_level_divisions.Division, countries.Country\n" +
                "FROM customers\n" +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID\n" +
                "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID\n" +
                "WHERE customers.Customer_ID = " + customerID;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();

        //set all fields to database values
        customerIDInput.setText(String.valueOf(result.getInt("Customer_ID")));
        customerNameInput.setText(result.getString("Customer_Name"));
        addressInput.setText(result.getString("Address"));
        postalCodeInput.setText(result.getString("Postal_Code"));
        phoneInput.setText(result.getString("Phone"));
        switch (result.getString("Country")) {
            case "U.S":
                countryPicker.getSelectionModel().select("United States");
                break;
            case "UK":
                countryPicker.getSelectionModel().select("United Kingdom");
                break;
            case "Canada":
                countryPicker.getSelectionModel().select("Canada");
                break;
        }
        divisionPicker.getSelectionModel().select(result.getString("Division"));
        this.customerID = result.getInt("Customer_ID");
    }

    /**
     * Passes input values to the Customer update function and switches back to the Customers screen when save button is clicked.
     *
     * @param actionEvent 'Update Customer' button clicked
     * @throws SQLException Throws SQLException
     * @throws IOException Throws IOEXception
     */
    public void onUpdateCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        //collect all input values
        String customerName = customerNameInput.getText();
        String address = addressInput.getText();
        String postalCode = postalCodeInput.getText();
        String phone = phoneInput.getText();
        Integer divisionID = Division.getID(divisionPicker.getValue());

       //pass values through to update function
        boolean successfulUpdate = Customer.update(customerName, address, postalCode, phone, divisionID, customerID);

        //switch back to customers page
        if(successfulUpdate) SceneSwitcher.toCustomers(actionEvent);
    }

    /**
     * Returns to the Customers Page when the cancel button is clicked.
     *
     * @param actionEvent 'Cancel' button clicked.
     * @throws IOException Throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }
}


