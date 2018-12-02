/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.duras.korman.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.duras.korman.App;

/**
 *
 * @author martin
 */
public class MenuController implements Initializable {

    @FXML
    private Button dashboardButton, bicyclesButton, notificationsButton, agentsButton, settingsButton;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == dashboardButton) {
            this.showWindow("Dashnoard.fxml");
        } else if (event.getSource() == bicyclesButton) {
            this.showWindow("BikeTable.fxml");
        } else if (event.getSource() == notificationsButton) {

        } else if (event.getSource() == agentsButton) {
            this.showWindow("AgentsTable.fxml");
        } else if (event.getSource() == settingsButton) {

        }
    }

    private void showWindow(String windowFxml) {
        try {
            BorderPane windowResource = FXMLLoader.load(App.class.getResource(windowFxml));

            Stage primaryStage = (Stage) dashboardButton.getScene().getWindow();

            Scene scene = new Scene(windowResource);
            primaryStage.setTitle("Korman Launcher");
            primaryStage.setScene(scene);

            primaryStage.getIcons().add(new Image(
                App.class.getResource("icon.png").toString()
            ));
            scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
