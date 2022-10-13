package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.AppointmentsTable;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static helper.TimeConversion.UTCToLocal;

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
    public Button editApptButton;
    public RadioButton monthlyViewRadioBtn;
    public RadioButton weeklyViewRadioBtn;
    public RadioButton allViewRadioBtn;

    ObservableList<model.AppointmentsTable> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set Appointment table initially
        setAptTableAll();

        //enable editApptButton when an appt is selected
        appointmentsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, appointmentsTable, t1) -> editApptButton.setDisable(false));

        //set up radio btn group
        final ToggleGroup aptViewGroup = new ToggleGroup();
        monthlyViewRadioBtn.setToggleGroup(aptViewGroup);
        weeklyViewRadioBtn.setToggleGroup(aptViewGroup);
        allViewRadioBtn.setToggleGroup(aptViewGroup);
        allViewRadioBtn.setSelected(true);

        //create radio btn group listener
        aptViewGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton selected = (RadioButton) aptViewGroup.getSelectedToggle();
            switch (selected.getId()) {
                case "monthlyViewRadioBtn":
                    setAptTableMonthly();
                    break;
                case "weeklyViewRadioBtn":
                    setAptTableWeekly();
                    break;
                case "allViewRadioBtn":
                    setAptTableAll();
                    break;
            }
        });
    }

    public void onAddAppt(ActionEvent actionEvent) throws IOException {
        //switch to Add Appt Form
        SceneSwitcher.toAddApptForm(actionEvent);
    }

    public void onEditAppt(ActionEvent actionEvent) throws SQLException, IOException {
        //get selected appt
        AppointmentsTable selectedAppt = appointmentsTable.getSelectionModel().getSelectedItem();
        Integer apptID = selectedAppt.getApptID();

        //switch to appt editing screen and pass appt ID
        SceneSwitcher.toEditAppt(actionEvent, apptID);
    }

    public void onDeleteAppt(ActionEvent actionEvent) throws SQLException {
        //get selected appt
        AppointmentsTable selectedAppt = appointmentsTable.getSelectionModel().getSelectedItem();
        Integer apptID = selectedAppt.getApptID();

        //delete from database
        Appointment.delete(apptID);

        //refresh table
        setAptTableAll();
    }

    public void onLogout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toLogin(actionEvent);
    }

    public void onSwitchToCustomer(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }

    public void setAptTableAll(){
        //clear out anything in table
        appointmentsTable.getItems().clear();

        //set table with database data
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet result =  ps.executeQuery();
            addToObList(result);
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        setCellValueFactories();
        appointmentsTable.setItems(oblist);
    }

    public void setAptTableMonthly(){
        //clear out anything in table
        appointmentsTable.getItems().clear();
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        //set table with database data
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments WHERE YEAR(Start) = ? AND MONTH(Start) = ?;";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, currentYear);
            ps.setInt(2, currentMonth);
            ResultSet result =  ps.executeQuery();
            addToObList(result);
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        setCellValueFactories();
        appointmentsTable.setItems(oblist);
    }

    public void setAptTableWeekly(){
        //clear out anything in table
        appointmentsTable.getItems().clear();
        LocalDate currentDate = LocalDate.now();
        LocalDate oneWeekDate = LocalDate.now().plusWeeks(1);

        //set table with database data
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments WHERE Start BETWEEN ? AND ?;";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, String.valueOf(currentDate));
            ps.setString(2, String.valueOf(oneWeekDate));
            ResultSet result =  ps.executeQuery();
            addToObList(result);
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        setCellValueFactories();
        appointmentsTable.setItems(oblist);
    }

    private void addToObList(ResultSet result) throws SQLException, ParseException {
        //convert dates from UTC to Local
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        while(result.next()) {
            oblist.add(new AppointmentsTable(
                    result.getString("Title"),
                    result.getString("Description"),
                    result.getString("Location"),
                    result.getString("Type"),
                    UTCToLocal(dateFormat.parse(result.getString("Start"))),
                    UTCToLocal(dateFormat.parse(result.getString("End"))),
                    result.getInt("Appointment_ID"),
                    result.getInt("Customer_ID"),
                    result.getInt("User_ID"),
                    result.getInt("Contact_ID")
            ));
        }
    }

    private void setCellValueFactories() {
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
    }
}
