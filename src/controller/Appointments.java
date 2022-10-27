package controller;

import helper.SceneSwitcher;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static helper.TimeConversion.UTCToLocal;

/**
 * Controller for Appointments page.
 *
 * @author Nicholas Balabis
 */
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
    public Button editAptButton;
    public RadioButton monthlyViewRadioBtn;
    public RadioButton weeklyViewRadioBtn;
    public RadioButton allViewRadioBtn;
    public Button deleteAptButton;

    ObservableList<model.AppointmentsTable> oblist = FXCollections.observableArrayList();

    /**
     * Initializes controller. Contains 2 lambda expressions at the appointmentsTable event listener and radio button group listener.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set Appointment table initially
        setAptTableAll();

        //LAMBDA EXPRESSION - enable editAptButton and deleteAptButton when an apt is selected
        appointmentsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, appointmentsTable, t1) -> {
            editAptButton.setDisable(false);
            deleteAptButton.setDisable(false);
        });

        //set up radio btn group
        final ToggleGroup aptViewGroup = new ToggleGroup();
        monthlyViewRadioBtn.setToggleGroup(aptViewGroup);
        weeklyViewRadioBtn.setToggleGroup(aptViewGroup);
        allViewRadioBtn.setToggleGroup(aptViewGroup);
        allViewRadioBtn.setSelected(true);

        //LAMBDA EXPRESSION - create radio btn group listener
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

    /**
     * Switches to AddAptForm when the add button is clicked.
     *
     * @param actionEvent 'New Appointment' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onAddApt(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toAddApptForm(actionEvent);
    }

    /**
     * Switches to EditAptForm and passes selected appointment when edit button is clicked.
     *
     * @param actionEvent 'Edit Appointment' button clicked.
     * @throws SQLException Throws SQLException.
     * @throws IOException Throws IOException.
     * @throws ParseException Throws ParseException.
     */
    public void onEditApt(ActionEvent actionEvent) throws SQLException, IOException, ParseException {
        //get selected apt
        AppointmentsTable selectedAppt = appointmentsTable.getSelectionModel().getSelectedItem();
        Integer aptID = selectedAppt.getAptID();

        //switch to appt editing screen and pass apt ID
        SceneSwitcher.toEditAppt(actionEvent, aptID);
    }

    /**
     * Passes appointment ID to delete function when delete button is clicked.
     *
     * @param actionEvent 'Delete Appointment' button clicked.
     * @throws SQLException Throws SQLException.
     */
    public void onDeleteApt(ActionEvent actionEvent) throws SQLException {
        //get selected apt
        AppointmentsTable selectedApt = appointmentsTable.getSelectionModel().getSelectedItem();
        Integer aptID = selectedApt.getAptID();
        String aptType = selectedApt.getType();

        //pass apt to delete function
        Appointment.delete(aptID);

        //refresh table and buttons
        setAptTableAll();
        disableButtons();

        //display confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Appointment Deleted");
        alert.setHeaderText("Appointment Successfully Deleted");
        alert.setContentText("ID: " + aptID + "\nType: " + aptType);
        alert.showAndWait();
    }

    /**
     * Clears appointmentsTable and sets to view all appointments.
     */
    public void setAptTableAll(){
        //clear out anything in table
        appointmentsTable.getItems().clear();

        //set table with database data
        try {
            addToObList(Appointment.getAll());
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
        setCellValueFactories();
        appointmentsTable.setItems(oblist);
    }

    /**
     * Clears appointmentsTable and sets to view appointments for this month.
     */
    public void setAptTableMonthly(){
        //clear out anything in table
        appointmentsTable.getItems().clear();

        //get current Month
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        //set table with database data
        try {
            addToObList(Appointment.getForMonth(currentYear, currentMonth));
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
        setCellValueFactories();
        appointmentsTable.setItems(oblist);
    }

    /**
     * Clears appointmentsTable and sets to view appointments for this week.
     */
    public void setAptTableWeekly(){
        //clear out anything in table
        appointmentsTable.getItems().clear();
        LocalDate currentDate = LocalDate.now();
        LocalDate oneWeekDate = LocalDate.now().plusWeeks(1);

        //set table with database data
        try {
            addToObList(Appointment.getForWeek(currentDate, oneWeekDate));
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
        setCellValueFactories();
        appointmentsTable.setItems(oblist);
    }

    /**
     * Switches to Customers screen when Customer button is clicked.
     *
     * @param actionEvent 'Customers' button clicked.
     * @throws IOException Throws IOException.
     */
    public void onSwitchToCustomer(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
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
     * Iterates through SQL result and adds all records to an ObservableList.
     *
     * @param result ResultSet from a SQL query.
     * @throws SQLException Throws SQLException.
     * @throws ParseException Throws ParseException.
     */
    private void addToObList(ResultSet result) throws SQLException, ParseException {
        //convert dates from UTC to Local
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //adds all records to observable list
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
     * Sets all CellValueFactories in appointmentsTable.
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
     * Disables edit and delete buttons.
     */
    private void disableButtons() {
        editAptButton.setDisable(true);
        deleteAptButton.setDisable(true);
    }
}
