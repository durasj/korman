package me.duras.korman.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author martin
 */
public class LauncherController implements Initializable {

    @FXML
    private Button dashboardButton, bicyclesButton, notificationsButton, agentsButton, settingsButton;

    @FXML
    private AnchorPane defaultTable;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == dashboardButton) {

        } else if (event.getSource() == bicyclesButton) {
            showWindow("BikeTable.fxml");
        } else if (event.getSource() == notificationsButton) {

        } else if (event.getSource() == agentsButton) {
            showWindow("AgentsTable.fxml");
        } else if (event.getSource() == settingsButton) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void showWindow(String window) {
        String newWindow = window;
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(newWindow));
            defaultTable.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
