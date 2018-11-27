package me.duras.korman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

/**
 *
 * @author martin
 */
public class LauncherController implements Initializable {

    @FXML
    private Button dashboardButton;

    @FXML
    private Button bicyclesButton;

    @FXML
    private Button notificationsButton;

    @FXML
    private Button agentsButton;

    @FXML
    private Button requirementsButton;

    @FXML
    private Button settingsButton;

    @FXML
    private AnchorPane defaultTable;

    @FXML
    private void showDashboard(ActionEvent event) {
        System.out.println("You clicked Dashboard!");
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("dashTable.fxml"));
            defaultTable.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBicycles(ActionEvent event) {
        System.out.println("You clicked Bike!");
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("bikeTable.fxml"));
            defaultTable.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showNotifications(ActionEvent event) {
        System.out.println("You clicked Notifications!");
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("notifTable.fxml"));
            defaultTable.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showAgents(ActionEvent event) {
        System.out.println("You clicked agents!");
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("agentsTable.fxml"));
            defaultTable.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showRequirements(ActionEvent event) {
        System.out.println("You clicked Requirements!");
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("reqTable.fxml"));
            defaultTable.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showSettings(ActionEvent event) {
        System.out.println("You clicked Settings!");
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("settTable.fxml"));
            defaultTable.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
