package me.duras.korman.controllers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import me.duras.korman.App;

public class MenuController {

    @FXML
    private ToggleButton dashboardButton, bicyclesButton, notificationsButton, agentsButton, settingsButton, logsButton;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Scene currentScene = dashboardButton.getScene();
        if (event.getSource() == dashboardButton) {
            MenuController.showWindow("Dashboard.fxml", dashboardButton.getId(), currentScene);
        } else if (event.getSource() == bicyclesButton) {
            MenuController.showWindow("BikeTable.fxml", bicyclesButton.getId(), currentScene);
        } else if (event.getSource() == notificationsButton) {
            MenuController.showWindow("Notifications.fxml", notificationsButton.getId(), currentScene);
        } else if (event.getSource() == agentsButton) {
            MenuController.showWindow("AgentsTable.fxml", agentsButton.getId(), currentScene);
        } else if (event.getSource() == settingsButton) {
            MenuController.showWindow("Settings.fxml", settingsButton.getId(), currentScene);
        } else if (event.getSource() == logsButton) {
            MenuController.showWindow("Logs.fxml", logsButton.getId(), currentScene);
        }
    }

    public static FXMLLoader showWindow(String windowFxml, String buttonId, Scene currentScene) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(windowFxml));
        BorderPane windowResource;
        try {
            windowResource = loader.load();
            MenuController.loadWindow(windowFxml, buttonId, currentScene, windowResource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    private static void loadWindow(String windowFxml, String buttonId, Scene currentScene, BorderPane windowResource) {
        Stage primaryStage = (Stage) currentScene.getWindow();

        Scene scene = new Scene(
            windowResource,
            currentScene.getWidth(),
            currentScene.getHeight()
        );
        scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
        primaryStage.setScene(scene);
        setActive(buttonId, scene);
    }

    public static void setActive(String buttonId, Scene currentScene) {
        ToggleButton activeButton = (ToggleButton) currentScene.lookup("#" + buttonId);
        activeButton.setSelected(true);
        ImageView activeButtonIcon = (ImageView) currentScene.lookup("#" + buttonId + "Icon");

        // Get window name from the button id
        Pattern pattern = Pattern.compile("([A-Z][a-z]+)");
        Matcher matcher = pattern.matcher(buttonId);
        if (matcher.find()) {
            String windowName = matcher.group(1).toLowerCase();
            String activeIconPath = App.class.getResource("icons/" + windowName + "-active.png").toExternalForm();
            activeButtonIcon.setImage(new Image(activeIconPath));
        }
    }
}
