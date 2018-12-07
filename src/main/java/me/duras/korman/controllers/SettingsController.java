package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SettingsController implements Initializable {

    BikeController bc = new BikeController();

    private String list_Url;
    private String view_Url;
    private int refresh_Time;

    @FXML
    private JFXTextField listUrl, viewUrl, refreshTime;

    @FXML
    private JFXButton addSettings;

    @FXML
    private void addSettings(ActionEvent event) {

        String list_Url = listUrl.getText();
        this.list_Url = list_Url;

        String view_Url = viewUrl.getText();
        this.view_Url = view_Url;

        String refresh_Time = refreshTime.getText();
        if (refresh_Time.equals("")) {
            this.refresh_Time = 300000;
        } else {
            this.refresh_Time = Integer.parseInt(refreshTime.getText()) * 60000;
        }

        bc.setList_Url(list_Url);
        bc.setView_Url(view_Url);
        bc.setRefresh_Time(refresh_Time);

        listUrl.clear();
        viewUrl.clear();
        refreshTime.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listUrl.setText("https://www.canyon.com/en-sk/factory-outlet/ajax");
        viewUrl.setText("https://www.canyon.com/en-sk/factory-outlet/category.html#category=fitness-bikes&amp;id=");
        refreshTime.setText("5");
    }
}
