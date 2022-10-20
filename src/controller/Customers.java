package controller;

import helper.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.CustomersTable;
import view.CustomerDeletePopup;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for Customers page.
 *
 * @author Nicholas Balabis
 */
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

    /**
     * Initializes controller. Contains a lambda expression at the customersTable event listener.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets customerTable initially
        setCustomerTable();

        //LAMBDA EXPRESSION - customersTable event listener enables edit & delete buttons when a customer is selected.
        customersTable.getSelectionModel().selectedItemProperty().addListener((observableValue, customersTable, t1) -> {
            editCustomerButton.setDisable(false);
            deleteCustomerButton.setDisable(false);
        });
    }

    /**
     * Switches to AddCustomerForm when the add button is clicked.
     *
     * @param actionEvent 'New Customer' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAddCustomer(actionEvent);
    }

    /**
     * Switches to EditCustomerForm and passes selected customer when edit button is clicked.
     *
     * @param actionEvent 'Edit Customer' button clicked.
     * @throws IOException Throws IOException.
     * @throws SQLException Throws SQLException.
     */
    public void onEditCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        //get selected customer
        CustomersTable selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        Integer customerID = selectedCustomer.getCustomerID();

        //switch to EditCustomerForm
        SceneSwitcher.toEditCustomer(actionEvent, customerID);
    }

    /**
     * Passes customer ID to delete function and displays popup when delete button is clicked.
     *
     * @param actionEvent 'Delete Customer' button clicked.
     */
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

    /**
     * Switches to Appointments screen when Appointments button is clicked.
     *
     * @param actionEvent 'Appointments' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onSwitchToApt(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAppts(actionEvent);
    }

    /**
     * Switches to Reports screen when Reports button is clicked.
     *
     * @param actionEvent 'Reports' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onSwitchToReport(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toReports(actionEvent);
    }

    /**
     * Switches to Login screen when Logout button is clicked
     *
     * @param actionEvent 'Logout' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onLogout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toLogin(actionEvent);
    }

    /**
     * Clears customersTable and sets to view all customers.
     */
    private void setCustomerTable() {
        //clear out anything in the table.
        customersTable.getItems().clear();

        //adds all records to observable list
        try {
            ResultSet result = Customer.getAll();
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

        //sets all CellValueFactories in customersTable
        col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        col_customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        //set table with database data
        customersTable.setItems(obList);
    }

    /**
     * Disables edit and delete buttons.
     */
    private void disableButtons() {
        editCustomerButton.setDisable(true);
        deleteCustomerButton.setDisable(true);
    }
}
