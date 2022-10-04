package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.CustomersTable;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Customers implements Initializable {
    public TableView<CustomersTable> customersTable;
    public TableColumn<CustomersTable, Integer> col_customerID;
    public TableColumn<CustomersTable, String> col_customerName;
    public TableColumn<CustomersTable, String> col_address;
    public TableColumn<CustomersTable, String> col_postalCode;
    public TableColumn<CustomersTable, String> col_phone;
    public TableColumn<CustomersTable, Integer> col_divisionID;

    ObservableList<CustomersTable> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM customers;";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while(result.next()) {
                obList.add(new CustomersTable(
                        result.getString("Customer_Name"),
                        result.getString("Address"),
                        result.getString("Postal_Code"),
                        result.getString("Phone"),
                        result.getInt("Customer_ID"),
                        result.getInt("Division_ID")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        col_customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        customersTable.setItems(obList);
    }

    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAddCustomer(actionEvent);
    }

    public void onEditCustomer(ActionEvent actionEvent) {
    }

    public void onDeleteCustomer(ActionEvent actionEvent) {
    }

    public void onLogout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toLogin(actionEvent);
    }

    public void onSwitchToAppt(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAppts(actionEvent);
    }
}
