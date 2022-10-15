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

public class Main extends Application {
    public static Integer CURRENT_USER_ID = null;

    @Override
    public void start(Stage stage) throws Exception {
//        Locale.setDefault(new Locale("fr"));
        Locale location = Locale.getDefault();
        String language = location.getLanguage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        if(language.equals("fr")) {
            stage.setTitle("Formulaire de Connexion");
        } else {
            stage.setTitle("Login Form");
        }
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
