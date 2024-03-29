package me.duras.korman.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXListView;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import me.duras.korman.DaoFactory;
import me.duras.korman.dao.NotificationDao;
import me.duras.korman.models.Notification;

/**
 * Dashboard Controller
 */
public class DashboardController implements Initializable {
    NotificationDao dao = DaoFactory.INSTANCE.getNotificationDao();

    private ObservableList<String> items = FXCollections.observableArrayList();

    private Timer timer = new Timer();

    private Date lastCheck;

    @FXML
    private JFXListView<String> latestNotificationsList;

    @FXML
    private AnchorPane dashboardCard;

    @FXML
    private Button bicyclesLinkCard, notificationsLinkCard, agentsLinkCard, settingsLinkCard, logsLinkCard;

    @FXML
    private Label latestNotificationsCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Button[] linkCards = {bicyclesLinkCard, notificationsLinkCard, agentsLinkCard, settingsLinkCard, logsLinkCard};
        for (Button linkCard : linkCards) {
            linkCard.getStyleClass().add("link-card");
        }

        latestNotificationsList.setItems(items);

        loadList();

        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    loadNew();
                });
            }
        };
        timer.schedule(task, 0L, 1000L);

        latestNotificationsCount.setVisible(false);
    }

    public Runnable afterInitialize() {
        Runnable onDestroy = () -> {
            timer.cancel();
            timer.purge();
        };

        return onDestroy;
    }

    private void loadList() {
        items.clear();
        List<Notification> list = dao.getAll();

        for (Notification notification : list) {
            items.add(0, getNotificationText(notification));
            if (items.size() > 9) {
                items.remove(9);
            }
        }

        this.lastCheck = new Date();
    }

    private void loadNew() {
        List<Notification> list = dao.getNew(this.lastCheck);

        for (Notification notification : list) {
            Optional<String> duplicate = items.stream()
                .filter((String l) -> l.equals(getNotificationText(notification)))
                .findFirst();

            if (duplicate.isPresent()) {
                list.remove(notification);
                continue;
            }

            items.add(0, getNotificationText(notification));
            if (items.size() > 9) {
                items.remove(9);
            }
        }

        int newCount = list.size();
        if (newCount > 0) {
            String currentCount = latestNotificationsCount.getText();
            latestNotificationsCount.setText(String.valueOf(Integer.parseInt(currentCount) + newCount));
            latestNotificationsCount.setVisible(true);
            this.lastCheck = new Date();
        }
    }

    private String getNotificationText(Notification notification) {
        String categoryName = notification.getBicycle().getCategory().getName();
        String series = notification.getBicycle().getSeries();
        String agentName = notification.getAgent().getName();
        String agentEmail = notification.getAgent().getEmail();
        return categoryName + " " + series + " for " + agentName + " " + agentEmail;
    }

    @FXML
    private void handleCardClick(ActionEvent event) {
        Scene currentScene = bicyclesLinkCard.getScene();
        if (event.getSource() == bicyclesLinkCard) {
            FXMLLoader loader = MenuController.showWindow("BikeTable.fxml", "BicyclesButton", currentScene);
            ((BikeController) loader.getController()).afterInitialize();
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
