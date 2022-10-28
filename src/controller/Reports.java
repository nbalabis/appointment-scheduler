package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import static helper.TimeConversion.UTCToLocal;

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
    public RadioButton viewByContactRadioBtn;
    public RadioButton viewByLocationRadioBtn;
    public RadioButton viewByMonthTypeRadioBtn;
    public TableView<MonthTypeTable> monthTypeTable;
    public TableColumn<Object, Object> col_month;
    public TableColumn<Object, Object> col_monthType;
    public TableColumn<Object, Object> col_count;
    public TableView<LocationTable> locationTable;
    public TableColumn<Object, Object> col_uniqueLocation;
    public TableColumn<Object, Object> col_locationCount;

    ObservableList<model.AppointmentsTable> oblist = FXCollections.observableArrayList();
    ObservableList<model.MonthTypeTable> MonthTypeList = FXCollections.observableArrayList();
    ObservableList<model.LocationTable> LocationList = FXCollections.observableArrayList();

    /**
     * Initializes controller. Contains 2 lambda expressions at reportViewGroup radio button listener and choiceBox listener.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set initial view
        viewByMonthTypeRadioBtn.setSelected(true);
        showChoices(false);
        setReportsTableViewByMonthType();

        //set up radio btn group
        final ToggleGroup reportViewGroup = new ToggleGroup();
        viewByMonthTypeRadioBtn.setToggleGroup(reportViewGroup);
        viewByContactRadioBtn.setToggleGroup(reportViewGroup);
        viewByLocationRadioBtn.setToggleGroup(reportViewGroup);

        //LAMBDA EXPRESSION - set choiceBox based on view selected
        reportViewGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton selected = (RadioButton) reportViewGroup.getSelectedToggle();
            //determine which radio button is selected
            switch (selected.getId()) {
                //clear the table, set choicebox/label visibility, then set the choice box
                case "viewByMonthTypeRadioBtn":
                    reportsTable.setVisible(false);
                    locationTable.setVisible(false);
                    monthTypeTable.setVisible(true);
                    monthTypeTable.getItems().clear();
                    showChoices(false);
                    setReportsTableViewByMonthType();
                    break;
                case "viewByContactRadioBtn":
                    reportsTable.setVisible(true);
                    monthTypeTable.setVisible(false);
                    locationTable.setVisible(false);
                    reportsTable.getItems().clear();
                    showChoices(true);
                    break;
                case "viewByLocationRadioBtn":
                    reportsTable.setVisible(false);
                    monthTypeTable.setVisible(false);
                    locationTable.setVisible(true);
                    locationTable.getItems().clear();
                    showChoices(false);
                    setLocationTable();
                    break;
            }
        });

        //LAMBDA EXPRESSION - set table based on choicebox selected filter
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            //clear out anything in table
            reportsTable.getItems().clear();

            //check which choicebox is selected
            String selectedItem = choiceBox.getItems().get((Integer) t1);

            //set table to that filter
            setReportsTableViewByContact(selectedItem);
        });

        //get all contacts from database
        ObservableList<String> contacts = FXCollections.observableArrayList();
        try {
            contacts = Contact.getAllNames();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //set choice box
        choiceBox.setItems(contacts);
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
     * Sets table to month/type view. Contains Lambda expression at types iterator.
     */
    private void setReportsTableViewByMonthType() {
        //get distinct appointment types
        ObservableList<String> types = FXCollections.observableArrayList();
        try{
            types = Appointment.getTypes();
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }

        //Iterate through months
        int month = 1;
        while(month <= 12) {
            //iterate through each type every month
            int finalMonth = month;
            //LAMBDA EXPRESSION - Iterates through each distinct appointment type for every month.
            types.forEach((type) -> {
                String sql = "SELECT COUNT(Appointment_ID) FROM appointments WHERE MONTH(Start) = ? AND TYPE=?;";
                int count = 0;
                try{
                    PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                    ps.setInt(1, finalMonth);
                    ps.setString(2, type);
                    ResultSet result = ps.executeQuery();
                    result.next();
                    count = result.getInt("COUNT(Appointment_ID)");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if(count != 0) {
                    MonthTypeList.add(new MonthTypeTable(
                            convertMonthToString(finalMonth),
                            type,
                            count
                    ));
                }
            });
            month+=1;
        }
        col_month.setCellValueFactory(new PropertyValueFactory<>("month"));
        col_monthType.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_count.setCellValueFactory(new PropertyValueFactory<>("count"));
        monthTypeTable.setItems(MonthTypeList);
    }

    /**
     * Converts integer month to string
     *
     * @param month Integer month
     * @return String month
     */
    private String convertMonthToString(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return null;
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
    private void setLocationTable() {
        //get distinct locations
        ObservableList<String> locations = FXCollections.observableArrayList();
        try{
            locations = Appointment.getLocations();
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }

        //LAMBDA EXPRESSION - Iterates through each distinct appointment location.
        locations.forEach((location) -> {
            //count the number of appointments with each location
            String sql = "SELECT COUNT(Appointment_ID) FROM appointments WHERE Location = ?;";
            int count = 0;
            try{
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setString(1, location);
                ResultSet result = ps.executeQuery();
                result.next();
                count = result.getInt("COUNT(Appointment_ID)");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(count != 0) {
                LocationList.add(new LocationTable(
                        location,
                        count
                ));
            }
        });

        col_uniqueLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        col_locationCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        locationTable.setItems(LocationList);
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
