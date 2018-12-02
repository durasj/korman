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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.duras.korman.App;

/**
 *
 * @author martin
 */
public class MenuController implements Initializable {

    DashboardController dc = new DashboardController();

    @FXML
    private Button dashboardButton, bicyclesButton, notificationsButton, agentsButton, settingsButton;

    @FXML
    private AnchorPane defaultTable;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == dashboardButton) {
            dc.showWindow("Dashnoard.fxml");
        } else if (event.getSource() == bicyclesButton) {
            dc.showWindow("BikeTable.fxml");
        } else if (event.getSource() == notificationsButton) {

        } else if (event.getSource() == agentsButton) {
            dc.showWindow("AgentsTable.fxml");
        } else if (event.getSource() == settingsButton) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
