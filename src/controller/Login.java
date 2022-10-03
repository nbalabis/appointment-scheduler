package controller;

import helper.ValidateLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public TextField userName;
    public PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login initialized");
    }

    public void onLoginAction(ActionEvent actionEvent) throws SQLException, IOException {
        String userNameInput = userName.getText();
        String passwordInput = password.getText();
        ValidateLogin.validate(userNameInput, passwordInput, actionEvent);
    }
}
