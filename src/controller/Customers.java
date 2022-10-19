package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.CustomersTable;
import view.CustomerDeletePopup;

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
    public Button editCustomerButton;
    public Button deleteCustomerButton;

    ObservableList<CustomersTable> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCustomerTable();

        customersTable.getSelectionModel().selectedItemProperty().addListener((observableValue, customersTable, t1) -> {
            editCustomerButton.setDisable(false);
            deleteCustomerButton.setDisable(false);
        });
    }

    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAddCustomer(actionEvent);
    }

    public void onEditCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        CustomersTable selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        Integer customerID = selectedCustomer.getCustomerID();
        SceneSwitcher.toEditCustomer(actionEvent, customerID);
    }

    public void onDeleteCustomer(ActionEvent actionEvent) {
        //get selected customer
        CustomersTable selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        Integer customerID = selectedCustomer.getCustomerID();

        //display confirmation prompt
        CustomerDeletePopup.display(customerID);

        //refresh customer table and buttons
        setCustomerTable();
        disableButtons();
    }

    public void onLogout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toLogin(actionEvent);
    }

    public void onSwitchToAppt(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAppts(actionEvent);
    }

    //Switch to reports page
    public void onSwitchToReport(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toReports(actionEvent);
    }

    private void setCustomerTable() {
        customersTable.getItems().clear();
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

    private void disableButtons() {
        editCustomerButton.setDisable(true);
        deleteCustomerButton.setDisable(true);
    }
}
