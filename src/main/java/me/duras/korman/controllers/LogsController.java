package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import me.duras.korman.DaoFactory;
import me.duras.korman.dao.SettingDao;
import me.duras.korman.models.Setting;

public class LogsController implements Initializable {
    SettingDao dao = DaoFactory.INSTANCE.getSettingDao();

    @FXML
    private JFXButton clearLogsButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Setting> list = dao.getAll();
    }

    @FXML
    private void onClearLogs() {
        System.out.println("Clearning");
    }
}
