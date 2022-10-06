package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    public ChoiceBox<String> divisionPicker;
    public TextField customerIDInput;
    private int customerID;

    public void onUpdateCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        String customerName = customerNameInput.getText();
        String address = addressInput.getText();
        String postalCode = postalCodeInput.getText();
        String phone = phoneInput.getText();
        Integer divisionID = getDivisionID(divisionPicker.getValue());

        String sql = "UPDATE customers SET Customer_name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionID);
        ps.setInt(6, customerID);
        ps.executeUpdate();

        SceneSwitcher.toCustomers(actionEvent);
    }

    public Integer getDivisionID(String division) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = \"" + division + "\";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();
        return result.getInt("Division_ID");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> countryList = FXCollections.observableArrayList("Canada", "United Kingdom", "United States");
        countryPicker.setItems(countryList);

        countryPicker.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
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

                String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = " + countryID;
                ObservableList<String> divisionList = FXCollections.observableArrayList();
                try {
                    PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                    ResultSet result = ps.executeQuery();
                    while (result.next()) {
                        divisionList.add(result.getString("Division"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                divisionPicker.setItems(divisionList);
                divisionPicker.setDisable(false);
            }
        });
    }
}


