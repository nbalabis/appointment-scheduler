package helper;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidateLogin {
    //could this be non static?
    public static void validate(String userName, String password, ActionEvent event) throws SQLException, IOException {
        String sql = "SELECT 1 FROM users WHERE User_Name = ? AND Password = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet result =  ps.executeQuery();
        if(result.next()) {
            SceneSwitcher.toMain(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User Not Found");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Username/Password. Please try again.");
            alert.showAndWait();
        }
    }
}
