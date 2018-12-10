package me.duras.korman.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Dashboard Controller
 */
public class DashboardController implements Initializable {
    @FXML
    private AnchorPane dashboardCard;

    @FXML
    private Button bicyclesLinkCard, notificationsLinkCard, agentsLinkCard, settingsLinkCard, logsLinkCard;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Button[] linkCards = {bicyclesLinkCard, notificationsLinkCard, agentsLinkCard, settingsLinkCard, logsLinkCard};
        for (Button linkCard : linkCards) {
            linkCard.getStyleClass().add("link-card");
        }

        // TODO: Add loading of latest notifications and timer for live checking
    }

    @FXML
    private void handleCardClick(ActionEvent event) {
        Scene currentScene = bicyclesLinkCard.getScene();
        if (event.getSource() == bicyclesLinkCard) {
            MenuController.showWindow("BikeTable.fxml", "BicyclesButton", currentScene);
        } else if (event.getSource() == notificationsLinkCard) {
            MenuController.showWindow("Notifications.fxml", "NotificationsButton", currentScene);
        } else if (event.getSource() == agentsLinkCard) {
            MenuController.showWindow("AgentsTable.fxml", "AgentsButton", currentScene);
        } else if (event.getSource() == settingsLinkCard) {
            MenuController.showWindow("Settings.fxml", "SettingsButton", currentScene);
        } else if (event.getSource() == logsLinkCard) {
            FXMLLoader loader = MenuController.showWindow("Logs.fxml", "LogsButton", currentScene);
            MenuController.onDestroy = ((LogsController) loader.getController()).afterInitialize();
        }
    }
}
