package controller;

import helper.JDBC;
import helper.SceneSwitcher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {
    public TextField customerNameInput;
    public TextField addressInput;
    public TextField postalCodeInput;
    public TextField phoneInput;
    public ChoiceBox<String> countryPicker;
    public ChoiceBox<String> divisionPicker;

    public void onCancel(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.toCustomers(actionEvent);
    }

    public void onCreateNewCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        String customerName = customerNameInput.getText();
        String address = addressInput.getText();
        String postalCode = postalCodeInput.getText();
        String phone = phoneInput.getText();
        Integer divisionID = getDivisionID(divisionPicker.getValue());

        String sql = "INSERT INTO customers (Customer_name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionID);
        ps.executeUpdate();

        SceneSwitcher.toCustomers(actionEvent);
    }

    public Integer getDivisionID(String division) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = \"" + division + "\";";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();
        return result.getInt("Division_ID");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> countryList = FXCollections.observableArrayList("Canada", "United Kingdom", "United States");
        countryPicker.setItems(countryList);
        countryPicker.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                String countryChoice = countryPicker.getItems().get((Integer) t1);
                Integer countryID = null;
                switch (countryChoice) {
                    case "United States":
                        countryID = 1;
                        break;
                    case "United Kingdom":
                        countryID = 2;
                        break;
                    case "Canada":
                        countryID = 3;
                        break;
                }

                String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = " + countryID;
                ObservableList<String> divisionList = FXCollections.observableArrayList();
                try {
                    PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                    ResultSet result = ps.executeQuery();
                    while(result.next()) {
                        divisionList.add(result.getString("Division"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                divisionPicker.setItems(divisionList);
                divisionPicker.setDisable(false);
            }
        });
    }
}
