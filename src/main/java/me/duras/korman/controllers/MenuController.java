package me.duras.korman.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.duras.korman.App;

public class MenuController {

    @FXML
    private Button dashboardButton, bicyclesButton, notificationsButton, agentsButton, settingsButton;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == dashboardButton) {
            this.showWindow("Dashnoard.fxml");
        } else if (event.getSource() == bicyclesButton) {
            this.showWindow("BikeTable.fxml");
        } else if (event.getSource() == notificationsButton) {
            this.showWindow("Notifications.fxml");
        } else if (event.getSource() == agentsButton) {
            this.showWindow("AgentsTable.fxml");
        } else if (event.getSource() == settingsButton) {
            this.showWindow("Settings.fxml");
        }
    }

    private void showWindow(String windowFxml) {
        try {
            BorderPane windowResource = FXMLLoader.load(App.class.getResource(windowFxml));

            Stage primaryStage = (Stage) dashboardButton.getScene().getWindow();

            Scene scene = new Scene(windowResource, primaryStage.getWidth() - 18, primaryStage.getHeight() - 47);
            primaryStage.setTitle("Korman");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1218);
            primaryStage.setMinHeight(600);

            primaryStage.getIcons().add(new Image(
                    App.class.getResource("icon.png").toString()
            ));
            scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
