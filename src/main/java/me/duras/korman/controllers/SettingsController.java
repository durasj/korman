package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import me.duras.korman.DaoFactory;
import me.duras.korman.dao.SettingDao;
import me.duras.korman.models.Setting;

public class SettingsController implements Initializable {

    SettingDao dao = DaoFactory.INSTANCE.getSettingDao();

    private Setting listUrlSetting, viewUrlSetting, refreshTimeSetting;

    @FXML
    private JFXTextField listUrl, viewUrl;

    @FXML
    private JFXSlider refreshTime;

    @FXML
    private JFXButton addSettings;

    @FXML
    private void addSettings(ActionEvent event) {
        listUrlSetting.setValue(listUrl.getText());
        viewUrlSetting.setValue(viewUrl.getText());
        refreshTimeSetting.setValue(String.valueOf(refreshTime.getValue()));

        dao.save(listUrlSetting);
        dao.save(viewUrlSetting);
        dao.save(refreshTimeSetting);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Setting> list = dao.getAll();

        for (Setting setting : list) {
            if (setting.getKey().equals("listUrl")) {
                listUrlSetting = setting;
                listUrl.setText(setting.getValue());
            }

            if (setting.getKey().equals("viewUrl")) {
                viewUrlSetting = setting;
                viewUrl.setText(setting.getValue());
            }

            if (setting.getKey().equals("refreshTime")) {
                refreshTimeSetting = setting;
                refreshTime.setValue(Double.parseDouble(setting.getValue()));
            }
        }
    }
}
