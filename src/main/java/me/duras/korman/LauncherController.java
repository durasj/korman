package me.duras.korman;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 *
 * @author martin
 */
public class LauncherController implements Initializable {
    
    @FXML
    private Button dashboardButton;
    
     @FXML
    private Button bicyclesButton;
     
      @FXML
    private Button notificationsButton;
      
       @FXML
    private Button agentsButton;
       
        @FXML
    private Button requirementsButton;
        
         @FXML
    private Button settingsButton;
    
    @FXML
    private void showDashboard(ActionEvent event) {
        System.out.println("You clicked me!");
        dashboardButton.setText("Hello Dashboard!");
    }
    
     @FXML
    private void showBicycles(ActionEvent event) {
        System.out.println("You clicked me!");
        bicyclesButton.setText("Hello Bike!");
    }
    
     @FXML
    private void showNotifications(ActionEvent event) {
        System.out.println("You clicked me!");
        notificationsButton.setText("Hello Notifications!");
    }
    
     @FXML
    private void showAgents(ActionEvent event) {
        System.out.println("You clicked me!");
        agentsButton.setText("Hello agents!");
    }
    
     @FXML
    private void showRequirements(ActionEvent event) {
        System.out.println("You clicked me!");
        requirementsButton.setText("Hello Requirements!");
    }
    
     @FXML
    private void showSettings(ActionEvent event) {
        System.out.println("You clicked me!");
        settingsButton.setText("Hello Settings!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
