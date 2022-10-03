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

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        Locale.setDefault(new Locale("fr"));
        Locale location = Locale.getDefault();
        String language = location.getLanguage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        if(language.equals("fr")) {
            stage.setTitle("Formulaire de Connexion");
        } else {
            stage.setTitle("Login Form");
        }
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
