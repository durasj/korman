package me.duras.korman.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.duras.korman.App;

public class MenuController {

    @FXML
    private ToggleButton dashboardButton, bicyclesButton, notificationsButton, agentsButton, settingsButton;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == dashboardButton) {
            this.showWindow("Dashboard.fxml", dashboardButton.getId());
        } else if (event.getSource() == bicyclesButton) {
            this.showWindow("BikeTable.fxml", bicyclesButton.getId());
        } else if (event.getSource() == notificationsButton) {
            this.showWindow("Notifications.fxml", notificationsButton.getId());
        } else if (event.getSource() == agentsButton) {
            this.showWindow("AgentsTable.fxml", agentsButton.getId());
        } else if (event.getSource() == settingsButton) {
            this.showWindow("Settings.fxml", settingsButton.getId());
        }
    }

    private void showWindow(String windowFxml, String buttonId) {
        try {
            BorderPane windowResource = FXMLLoader.load(App.class.getResource(windowFxml));

            Scene currentScene = dashboardButton.getScene();
            Stage primaryStage = (Stage) currentScene.getWindow();

            Scene scene = new Scene(
                windowResource,
                currentScene.getWidth(),
                currentScene.getHeight()
            );
            scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
            primaryStage.setScene(scene);
            ToggleButton activeButton = (ToggleButton) scene.lookup("#" + buttonId);
            activeButton.setSelected(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
