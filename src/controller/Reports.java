package controller;

import helper.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Reports implements Initializable {
    public Label choiceBoxLabel;
    public ChoiceBox choiceBox;
    public TableView reportsTable;
    public TableColumn col_apptID;
    public TableColumn col_title;
    public TableColumn col_description;
    public TableColumn col_location;
    public TableColumn col_type;
    public TableColumn col_start;
    public TableColumn col_end;
    public TableColumn col_customerID;
    public TableColumn col_userID;
    public TableColumn col_contactID;
    public RadioButton viewByTypeRadioBtn;
    public RadioButton viewByMonthRadioBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set up radio btn group
        final ToggleGroup reportViewGroup = new ToggleGroup();
        viewByTypeRadioBtn.setToggleGroup(reportViewGroup);
        viewByMonthRadioBtn.setToggleGroup(reportViewGroup);
        viewByTypeRadioBtn.setSelected(true);

        //create radio btn group listener
        reportViewGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton selected = (RadioButton) reportViewGroup.getSelectedToggle();
            switch (selected.getId()) {
                case "viewByTypeRadioBtn":
                    setReportsTableViewByType();
                    break;
                case "viewByMonthRadioBtn":
                    setReportsTableViewByMonth();
                    break;
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

    //Set the reports table to View appointments by type
    private void setReportsTableViewByType() {
        System.out.println("View apts by type");
    }

    //Set the reports table to view appointments by month
    private void setReportsTableViewByMonth() {
        System.out.println("View apts by month");
    }
}
