package helper;

import controller.EditApptForm;
import controller.EditCustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

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

        EditCustomer controller = loader.getController();
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
    public static void toEditAppt(ActionEvent actionEvent, Integer apptID) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(SceneSwitcher.class.getResource("/view/EditApptForm.fxml")));
        Parent root = loader.load();

        //pass appt ID to controller
        EditApptForm controller = loader.getController();
        controller.setApptData(apptID);

        //show stage
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Edit Appointment");
        stage.setScene(scene);
        stage.show();
    }
}
