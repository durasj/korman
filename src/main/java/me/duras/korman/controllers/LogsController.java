package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;

import me.duras.korman.DaoFactory;
import me.duras.korman.dao.LogDao;
import me.duras.korman.models.Log;

public class LogsController implements Initializable {
    LogDao dao = DaoFactory.INSTANCE.getLogDao();

    private ObservableList<String> items = FXCollections.observableArrayList();

    private Timer timer = new Timer();

    private Date lastCheck;

    @FXML
    private JFXListView<String> logsList;

    @FXML
    private JFXButton clearLogsButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logsList.setItems(items);

        loadList();

        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    loadNew();
                });
            }
        };
        timer.schedule(task, 0L, 1000L);
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
        List<Log> list = dao.getAll();

        for (Log log : list) {
            items.add(getLogText(log));
        }

        this.lastCheck = new Date();
    }

    private void loadNew() {
        List<Log> list = dao.getNew(this.lastCheck);

        for (Log log : list) {
            Optional<String> duplicate = items.stream()
                .filter((String l) -> l.equals(getLogText(log)))
                .findFirst();

            if (duplicate.isPresent()) {
                continue;
            }

            items.add("[" + log.getCreatedAt() + "] " + log.getContent());
        }

        this.lastCheck = new Date();
    }

    private String getLogText(Log log) {
        return "[" + log.getCreatedAt() + "] " + log.getContent();
    }

    @FXML
    private void onClearLogs() {
        dao.clear();
        loadList();
    }
}
