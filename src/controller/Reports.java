package controller;

import helper.JDBC;
import helper.SceneSwitcher;
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
import static main.Main.CURRENT_USER_ID;

/**
 * Controller for Reports page.
 *
 * @author Nicholas Balabis
 */
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
    public RadioButton viewPersonalRadioBtn;

    ObservableList<model.AppointmentsTable> oblist = FXCollections.observableArrayList();

    /**
     * Initializes controller. Contains 2 lambda expressions at reportViewGroup radio button listener and choiceBox listener.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set initial view
        setChoiceBoxTypes();

        //set up radio btn group
        final ToggleGroup reportViewGroup = new ToggleGroup();
        viewByTypeRadioBtn.setToggleGroup(reportViewGroup);
        viewByMonthRadioBtn.setToggleGroup(reportViewGroup);
        viewByContactRadioBtn.setToggleGroup(reportViewGroup);
        viewPersonalRadioBtn.setToggleGroup(reportViewGroup);
        viewByTypeRadioBtn.setSelected(true);

        //LAMBDA EXPRESSION - set choiceBox based on view selected
        reportViewGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton selected = (RadioButton) reportViewGroup.getSelectedToggle();
            //determine which radio button is selected
            switch (selected.getId()) {
                //clear the table, set choicebox/label visibility, then set the choice box
                case "viewByTypeRadioBtn":
                    reportsTable.getItems().clear();
                    showChoices(true);
                    setChoiceBoxTypes();
                    break;
                case "viewByMonthRadioBtn":
                    reportsTable.getItems().clear();
                    showChoices(true);
                    setChoiceBoxMonths();
                    break;
                case "viewByContactRadioBtn":
                    reportsTable.getItems().clear();
                    showChoices(true);
                    setChoiceBoxContacts();
                    break;
                case "viewPersonalRadioBtn":
                    reportsTable.getItems().clear();
                    showChoices(false);
                    setReportsTableViewPersonal();
                    break;
            }
        });

        //LAMBDA EXPRESSION - set table based on choicebox selected filter
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            //clear out anything in table
            reportsTable.getItems().clear();

            //check which radio button and choicebox is selected
            RadioButton selected = (RadioButton) reportViewGroup.getSelectedToggle();
            String selectedItem = choiceBox.getItems().get((Integer) t1);

            //set table to that filter
            switch (selected.getId()) {
                case "viewByTypeRadioBtn":
                    setReportsTableViewByType(selectedItem);
                    break;
                case "viewByMonthRadioBtn":
                    setReportsTableViewByMonth(selectedItem);
                    break;
                case "viewByContactRadioBtn":
                    setReportsTableViewByContact(selectedItem);
                    break;
            }
        });
    }

    /**
     * Switch to Customers screen when button is clicked.
     *
     * @param actionEvent 'Customers' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onSwitchToCustomer(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }

    /**
     *
     * Switch to Appointments screen when button is clicked.
     * @param actionEvent 'Appointments' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onSwitchToApt(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAppts(actionEvent);
    }

    /**
     *
     * Switch to Login screen when button is clicked.
     * @param actionEvent 'Logout' button clicked.
     * @throws IOException Throws IOException
     */
    public void onLogout(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toLogin(actionEvent);
    }

    /**
     * Set choiceBox to display all appointment types
     */
    private void setChoiceBoxTypes() {
        //get all distinct apt types
        ObservableList<String> types = FXCollections.observableArrayList();
        try {
            types = Appointment.getTypes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        //set choice box items
        choiceBox.setItems(types);
        choiceBoxLabel.setText("Type");
    }

    /**
     * Set choiceBox to display all months
     */
    private void setChoiceBoxMonths() {
        ObservableList<String> months = FXCollections.observableArrayList();
        months.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        choiceBox.setItems(months);
        choiceBoxLabel.setText("Month");
    }

    /**
     * Set choiceBox to display all contacts
     */
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

    /**
     * Set the reports table to View appointments by type
     *
     * @param selectedType Type of appointment selected.
     */
    private void setReportsTableViewByType(String selectedType) {
        //get data from database
        String sql = "SELECT * FROM appointments WHERE Type = ?;";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, selectedType);
            ResultSet result =  ps.executeQuery();
            addToObList(result);
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        //set table
        setCellValueFactories();
        reportsTable.setItems(oblist);
    }

    /**
     * Set the reports table to view appointments by month.
     *
     * @param month Month selected.
     */
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

        //get data from database
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

        //set table with database data
        setCellValueFactories();
        reportsTable.setItems(oblist);
    }

    /**
     * Set the reports table to filter by selected contact name.
     *
     * @param name Selected contact name.
     */
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

        //set table to database data
        setCellValueFactories();
        reportsTable.setItems(oblist);
    }

    /**
     * Set the reports table to view current user's appointments.
     */
    private void setReportsTableViewPersonal() {
        //get data from database
        String sql = "SELECT * FROM appointments WHERE User_ID = ?;";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, CURRENT_USER_ID);
            ResultSet result  = ps.executeQuery();
            addToObList(result);
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }

        //fill table with results
        setCellValueFactories();
        reportsTable.setItems(oblist);
    }

    /**
     * Add ResultSet to observable list.
     *
     * @param result resultSet from database.
     * @throws SQLException Throws SQLException.
     * @throws ParseException Throws ParseException.
     */
    private void addToObList(ResultSet result) throws SQLException, ParseException {
        //convert dates from UTC to Local
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //add each column to new object in observable list
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

    /**
     * Set cellValueFactories for table.
     */
    private void setCellValueFactories() {
        col_apptID.setCellValueFactory(new PropertyValueFactory<>("aptID"));
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

    /**
     * Show/Hide ChoiceBox and ChoiceBoxLabel.
     *
     * @param view True/False value for choiceBox & label display.
     */
    private void showChoices(boolean view) {
        choiceBox.setVisible(view);
        choiceBoxLabel.setVisible(view);
    }
}
