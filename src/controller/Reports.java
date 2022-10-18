package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.beans.Observable;
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
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import static helper.TimeConversion.UTCToLocal;

public class Reports implements Initializable {
    public Label choiceBoxLabel;
    public ChoiceBox<String> choiceBox;
    public TableView<AppointmentsTable> reportsTable;
    public TableColumn<Object, Object> col_apptID;
    public TableColumn<Object, Object> col_title;
    public TableColumn<Object, Object> col_description;
    public TableColumn<Object, Object> col_location;
    public TableColumn<Object, Object> col_type;
    public TableColumn<Object, Object> col_start;
    public TableColumn<Object, Object> col_end;
    public TableColumn<Object, Object> col_customerID;
    public TableColumn<Object, Object> col_userID;
    public TableColumn<Object, Object> col_contactID;
    public RadioButton viewByTypeRadioBtn;
    public RadioButton viewByMonthRadioBtn;
    public RadioButton viewByContactRadioBtn;

    ObservableList<model.AppointmentsTable> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set initial view
        setChoiceBoxTypes();

        //set up radio btn group
        final ToggleGroup reportViewGroup = new ToggleGroup();
        viewByTypeRadioBtn.setToggleGroup(reportViewGroup);
        viewByMonthRadioBtn.setToggleGroup(reportViewGroup);
        viewByContactRadioBtn.setToggleGroup(reportViewGroup);
        viewByTypeRadioBtn.setSelected(true);

        //create radio btn group listener
        reportViewGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton selected = (RadioButton) reportViewGroup.getSelectedToggle();
            //determine which radio button is selected
            switch (selected.getId()) {
                //clear the table, then set the choice box
                case "viewByTypeRadioBtn":
                    reportsTable.getItems().clear();
                    setChoiceBoxTypes();
                    break;
                case "viewByMonthRadioBtn":
                    reportsTable.getItems().clear();
                    setChoiceBoxMonths();
                    break;
                case "viewByContactRadioBtn":
                    reportsTable.getItems().clear();
                    setChoiceBoxContacts();
                    break;
            }
        });

        //create choicebox listener
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            //clear out anything in table
            reportsTable.getItems().clear();

            //check which radio button and choicebox is selected
            RadioButton selected = (RadioButton) reportViewGroup.getSelectedToggle();
            String selectedItem = choiceBox.getItems().get((Integer) t1);

            switch (selected.getId()) {
                //set table to that filter
                case "viewByTypeRadioBtn":
                    setReportsTableViewByType(selectedItem);
                    break;
                case "viewByMonthRadioBtn":
                    setReportsTableViewByMonth(selectedItem);
                    break;
                case "viewByContactRadioBtn":
                    setReportsTableViewByContact(selectedItem);
            }
        });
    }

    //Switch to Customers Screen
    public void onSwitchToCustomer(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }

    //Switch to Appointments Screen
    public void onSwitchToApt(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAppts(actionEvent);
    }

    //Log user out and return to login screen
    public void onLogout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toLogin(actionEvent);
    }

    private void setChoiceBoxTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        
        //get all distinct apt types
        try {
            types = Appointment.getTypes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        //set choice box items
        choiceBox.setItems(types);
        choiceBoxLabel.setText("Type");
    }

    private void setChoiceBoxMonths() {
        ObservableList<String> months = FXCollections.observableArrayList();
        months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        choiceBox.setItems(months);
        choiceBoxLabel.setText("Month");
    }

    //Set the choice box to display all contacts
    private void setChoiceBoxContacts() {
        //get all contacts from database
        ObservableList<String> contacts = FXCollections.observableArrayList();
        try {
            contacts = Contact.getAllNames();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //set choice box and label
        choiceBox.setItems(contacts);
        choiceBoxLabel.setText("Contact Name");
    }

    //Set the reports table to View appointments by type
    private void setReportsTableViewByType(String selectedType) {
        //set table with database data
        String sql = "SELECT * FROM appointments WHERE Type = ?;";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, selectedType);
            ResultSet result =  ps.executeQuery();
            addToObList(result);
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        setCellValueFactories();
        reportsTable.setItems(oblist);
    }

    //Set the reports table to view appointments by month
    private void setReportsTableViewByMonth(String month) {
        //convert Month to integer
        int selectedMonth = 0;
        switch (month) {
            case "January":
                selectedMonth = 1;
                break;
            case "February":
                selectedMonth = 2;
                break;
            case "March":
                selectedMonth = 3;
                break;
            case "April":
                selectedMonth = 4;
                break;
            case "May":
                selectedMonth = 5;
                break;
            case "June":
                selectedMonth = 6;
                break;
            case "July":
                selectedMonth = 7;
                break;
            case "August":
                selectedMonth = 8;
                break;
            case "September":
                selectedMonth = 9;
                break;
            case "October":
                selectedMonth = 10;
                break;
            case "November":
                selectedMonth = 11;
                break;
            case "December":
                selectedMonth = 12;
                break;
        }

        //get current year
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int currentYear = calendar.get(Calendar.YEAR);

        //set table with database data
        String sql = "SELECT * FROM appointments WHERE MONTH(Start) = ? AND YEAR(START) = ?;";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, selectedMonth);
            ps.setInt(2, currentYear);
            ResultSet result =  ps.executeQuery();
            addToObList(result);
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        setCellValueFactories();
        reportsTable.setItems(oblist);
    }

    //Set the reports table to filter by selected contact name
    private void setReportsTableViewByContact(String name) {
        //get data from database
        String sql = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE contacts.Contact_Name = ?;";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet result = ps.executeQuery();
            addToObList(result);
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        setCellValueFactories();
        reportsTable.setItems(oblist);
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
