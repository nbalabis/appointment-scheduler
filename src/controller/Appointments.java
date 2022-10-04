package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AppointmentsTable;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Appointments implements Initializable {
    public TableView<model.AppointmentsTable> appointmentsTable;
    public TableColumn<model.AppointmentsTable, Integer> col_apptID;
    public TableColumn<model.AppointmentsTable, String> col_title;
    public TableColumn<model.AppointmentsTable, String> col_description;
    public TableColumn<model.AppointmentsTable, String> col_location;
    public TableColumn<model.AppointmentsTable, String> col_type;
    public TableColumn<model.AppointmentsTable, String> col_start;
    public TableColumn<model.AppointmentsTable, String> col_end;
    public TableColumn<model.AppointmentsTable, Integer> col_customerID;
    public TableColumn<model.AppointmentsTable, Integer> col_userID;
    public TableColumn<model.AppointmentsTable, Integer> col_contactID;

    ObservableList<model.AppointmentsTable> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet result =  ps.executeQuery();
            while(result.next()) {
                oblist.add(new AppointmentsTable(
                        result.getString("Title"),
                        result.getString("Description"),
                        result.getString("Location"),
                        result.getString("Type"),
                        result.getString("Start"),
                        result.getString("End"),
                        result.getInt("Appointment_ID"),
                        result.getInt("Customer_ID"),
                        result.getInt("User_ID"),
                        result.getInt("Contact_ID")
                        ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        col_apptID.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_start.setCellValueFactory(new PropertyValueFactory<>("start"));
        col_end.setCellValueFactory(new PropertyValueFactory<>("end"));
        col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        col_userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        col_contactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        appointmentsTable.setItems(oblist);
    }

    public void onAddAppt(ActionEvent actionEvent) {
    }

    public void onEditAppt(ActionEvent actionEvent) {
    }

    public void onDeleteAppt(ActionEvent actionEvent) {
    }

    public void onLogout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toLogin(actionEvent);
    }

    public void onSwitchToCustomer(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }
}
