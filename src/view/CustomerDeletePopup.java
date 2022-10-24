package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;

import java.sql.SQLException;

/**
 * Popup window to confirm customer deletion.
 *
 * @author Nicholas Balabis
 */
public class CustomerDeletePopup {
    /**
     * Display popup window and delete customer if confirmed. Contains 2 lambda expressions at the action handles for delete and cancel buttons.
     *
     * @param customerID Customer ID to be deleted.
     */
    public static void display(Integer customerID)
    {
        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Confirm Delete");

        Label title= new Label("Are you sure you want to delete this customer?");
        Label subTitle = new Label("This will also delete any associated appointments.");
        Button deleteButton = new Button("Delete");
        Button cancelButton= new Button("Cancel");

        //LAMBDA EXPRESSION - close popup window when 'cancel' is clicked.
        cancelButton.setOnAction(e -> popupwindow.close());
        //LAMBDA EXPRESSION - delete customer if user confirms warning.
        deleteButton.setOnAction(e ->{
            //delete customer
            try {
                Customer.delete(customerID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //close popup
            popupwindow.close();

        });

        VBox layout= new VBox(10);
        layout.getChildren().addAll(title, subTitle, deleteButton, cancelButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();

    }

}
