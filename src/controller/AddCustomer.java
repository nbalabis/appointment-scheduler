package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCustomer {
    public TextField customerNameInput;
    public TextField addressInput;
    public TextField postalCodeInput;
    public TextField phoneInput;

    public void onCancel(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }

    public void onCreateNewCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        String customerName = customerNameInput.getText();
        String address = addressInput.getText();
        String postalCode = postalCodeInput.getText();
        String phone = phoneInput.getText();

        String sql = "INSERT INTO customers (Customer_name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, 12);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.executeUpdate();
        SceneSwitcher.toCustomers(actionEvent);
    }

}
