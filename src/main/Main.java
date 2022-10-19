package main;

import helper.JDBC;
import helper.TestQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main extends Application {
    public static Integer CURRENT_USER_ID = null;



    @Override
    public void start(Stage stage) throws Exception {
        //get locale and language
//        Locale.setDefault(new Locale("fr"));
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String stageTitle;
        ResourceBundle rb = ResourceBundle.getBundle("Nat", locale);
        if (language.equals("fr")) {
            stageTitle = rb.getString("LoginStageTitle");
        } else {
            stageTitle = "Login Form";
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        stage.setTitle(stageTitle);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
