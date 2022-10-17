package controller;

import helper.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import static helper.Validate.isValidUser;
import static main.Main.CURRENT_USER_ID;
import static model.Appointment.displayUpcomingApts;

public class Login implements Initializable {
    public TextField userName;
    public PasswordField password;
    public Label userLocationLabel;
    public Label usernameLabel;
    public Label passwordLabel;
    public Button loginButton;

    Locale location = Locale.getDefault();
    String language = location.getLanguage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone = ZoneId.systemDefault();
        userLocationLabel.setText("Location: " + zone);

        if(language.equals("fr")) {
            usernameLabel.setText("Nom d'utilisateur:");
            passwordLabel.setText("Mot de passe:");
            loginButton.setText("Connexion");
            userLocationLabel.setText("Emplacement: " + zone);
        }
    }

    public void onLoginAction(ActionEvent actionEvent) throws SQLException, IOException, ParseException {
        String userNameInput = userName.getText();
        String passwordInput = password.getText();
        boolean successfulLogin = isValidUser(userNameInput, passwordInput, language);
        if(successfulLogin) {
            SceneSwitcher.toAppts(actionEvent);

            //display any upcoming appointments
            displayUpcomingApts(CURRENT_USER_ID);
        }
    }
}
