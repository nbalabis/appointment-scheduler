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
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static helper.Validate.isValidUser;
import static main.Main.CURRENT_USER_ID;
import static model.Appointment.displayUpcomingApts;

/**
 * Controller for Login page.
 *
 * @author Nicholas Balabis
 */
public class Login implements Initializable {
    public TextField userName;
    public PasswordField password;
    public Label userLocationLabel;
    public Label usernameLabel;
    public Label passwordLabel;
    public Button loginButton;

    Locale locale = Locale.getDefault();
    String language = locale.getLanguage();
    ResourceBundle rb = ResourceBundle.getBundle("Nat", locale);

    /**
     * Initializes controller.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //get user's language
        ZoneId zone = ZoneId.systemDefault();
        userLocationLabel.setText("Location: " + zone);

        //set text based on language
        if(language.equals("fr")) {
            usernameLabel.setText(rb.getString("usernameLabel"));
            passwordLabel.setText(rb.getString("passwordLabel"));
            loginButton.setText(rb.getString("loginButton"));
            userLocationLabel.setText(rb.getString("userLocationLabel") + " " + zone);
        }
    }

    /**
     * Validate user, log activity, and switch scenes when login button is clicked.
     *
     * @param actionEvent 'Log In' button clicked.
     * @throws SQLException Throws SQLException.
     * @throws IOException Throws IOException.
     * @throws ParseException Throws ParseException.
     */
    public void onLoginAction(ActionEvent actionEvent) throws SQLException, IOException, ParseException {
        //validate user/password
        String userNameInput = userName.getText();
        String passwordInput = password.getText();
        boolean successfulLogin = isValidUser(userNameInput, passwordInput, language, rb);

        //log login activity
        logger.info("Login successful: " + successfulLogin);

        //switch scenes and display upcoming apts
        if(successfulLogin) {
            SceneSwitcher.toAppts(actionEvent);
            displayUpcomingApts(CURRENT_USER_ID);
        }
    }

    //set up logger
    private static final Logger logger = java.util.logging.Logger.getLogger("LoginActivity");
    static {
        try {
            FileHandler fh;
            logger.setUseParentHandlers(false);
            //configure logger w/ handler and formatter
            fh = new FileHandler("./login_activity.txt", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
