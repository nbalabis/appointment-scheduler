package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main extends Application {
    public static Integer CURRENT_USER_ID = null;

    /**
     * Loads and displays starting window in local language.
     *
     * @param stage Stage.
     * @throws Exception Throws IO Exception.
     */
    @Override
    public void start(Stage stage) throws Exception {
        //get locale and language
//        Locale.setDefault(new Locale("fr"));
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String stageTitle;
        ResourceBundle rb = ResourceBundle.getBundle("Nat", locale);


        //set stage title based on local language
        if (language.equals("fr")) {
            stageTitle = rb.getString("LoginStageTitle");
        } else {
            stageTitle = "Login Form";
        }

        //load login screen
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        stage.setTitle(stageTitle);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /**
     * Main method to start application and open database connection.
     *
     * @param args optional args.
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
