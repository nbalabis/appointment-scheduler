package helper;

import controller.EditAptForm;
import controller.EditCustomerForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Objects;

import static main.Main.CURRENT_USER_ID;

public class SceneSwitcher {

    @FXML
    public static void toAppts(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/Appointments.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("All Appointments");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toLogin(ActionEvent actionEvent) throws IOException {
        CURRENT_USER_ID = null;
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/Login.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Login Form");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toCustomers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/Customers.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("All Customers");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/AddCustomer.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toEditCustomer(ActionEvent actionEvent, Integer customerID) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/EditCustomer.fxml")));
        Parent root = loader.load();

        EditCustomerForm controller = loader.getController();
        controller.setCustomerData(customerID);

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Edit Customer");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toAddApptForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/AddApptForm.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toEditAppt(ActionEvent actionEvent, Integer apptID) throws IOException, SQLException, ParseException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/EditApptForm.fxml")));
        Parent root = loader.load();

        //pass appt ID to controller
        EditAptForm controller = loader.getController();
        controller.setAptData(apptID);

        //show stage
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Edit Appointment");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void toReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/Reports.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }
}
