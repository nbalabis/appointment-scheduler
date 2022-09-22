package main;

import helper.JDBC;
import helper.TestQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/FirstScreen.fxml"));
        stage.setTitle("First Screen");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        launch(args);
        int rowsAffected = TestQuery.insert("NewUser", "badPassword");
        if(rowsAffected > 0){
            System.out.println("Insert successful!");
        } else {
            System.out.println("Insert failed :(");
        }
        JDBC.closeConnection();
    }
}
