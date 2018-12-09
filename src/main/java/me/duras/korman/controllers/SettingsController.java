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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import me.duras.korman.BicycleChecking;
import me.duras.korman.DaoFactory;
import me.duras.korman.dao.SettingDao;
import me.duras.korman.models.Setting;

public class SettingsController implements Initializable {

    SettingDao dao = DaoFactory.INSTANCE.getSettingDao();

    private Setting listUrlSetting, viewUrlSetting, refreshTimeSetting, smtpHostSetting, smtpPortSetting, smtpUserSetting, smtpPasswordSetting, emailFromSetting;

    @FXML
    private JFXTextField listUrl, viewUrl, smtpHost, smtpPort, smtpUser, smtpPassword, emailFrom;

    @FXML
    private JFXSlider refreshTime;

    @FXML
    private JFXButton addSettings;

    @FXML
    private void addSettings(ActionEvent event) {
        if (listUrl.getText().isEmpty() || viewUrl.getText().isEmpty()) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Both URLs are required");
            errorAlert.showAndWait();
            return;
        }

        if (!smtpHost.getText().isEmpty() && (smtpPort.getText().isEmpty() || emailFrom.getText().isEmpty())) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Port and Email From address are required if SMTP host is filled");
            errorAlert.showAndWait();
            return;
        }

        int smtpPortValue = 0;
        try {
            smtpPortValue = Integer.parseInt(smtpPort.getText());
        } catch (NumberFormatException e) {}
        if (smtpPortValue > 65535 || smtpPortValue < 1) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Port is integer from range 1 to 65535");
            errorAlert.showAndWait();
            return;
        }

        listUrlSetting.setValue(listUrl.getText());
        viewUrlSetting.setValue(viewUrl.getText());
        refreshTimeSetting.setValue(String.valueOf(refreshTime.getValue()));

        smtpHostSetting.setValue(smtpHost.getText());
        smtpPortSetting.setValue(smtpPort.getText());
        smtpUserSetting.setValue(smtpUser.getText());
        smtpPasswordSetting.setValue(smtpPassword.getText());
        emailFromSetting.setValue(emailFrom.getText());

        dao.save(listUrlSetting);
        dao.save(viewUrlSetting);
        dao.save(refreshTimeSetting);

        dao.save(smtpHostSetting);
        dao.save(smtpPortSetting);
        dao.save(smtpUserSetting);
        dao.save(smtpPasswordSetting);
        dao.save(emailFromSetting);

        int refreshTimeValue = (int) Math.round(refreshTime.getValue());
        if (refreshTimeValue > 0) {
            BicycleChecking.startTimer(refreshTimeValue);
        } else {
            BicycleChecking.stopTimer();
        }
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

            if (setting.getKey().equals("smtpHost")) {
                smtpHostSetting = setting;
                smtpHost.setText(setting.getValue() != null ? setting.getValue() : "");
            }

            if (setting.getKey().equals("smtpPort")) {
                smtpPortSetting = setting;
                smtpPort.setText(setting.getValue() != null ? setting.getValue() : "");
            }

            if (setting.getKey().equals("smtpUser")) {
                smtpUserSetting = setting;
                smtpUser.setText(setting.getValue() != null ? setting.getValue() : "");
            }

            if (setting.getKey().equals("smtpPassword")) {
                smtpPasswordSetting = setting;
                smtpPassword.setText(setting.getValue() != null ? setting.getValue() : "");
            }

            if (setting.getKey().equals("emailFrom")) {
                emailFromSetting = setting;
                emailFrom.setText(setting.getValue() != null ? setting.getValue() : "");
            }
        }
    }
}
