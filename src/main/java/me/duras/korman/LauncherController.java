package me.duras.korman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author martin
 */
public class LauncherController implements Initializable {

    Agents agents = new Agents();

    @FXML
    private Button dashboardButton, bicyclesButton, notificationsButton, agentsButton, settingsButton, newAgentButton;

    @FXML
    private AnchorPane bicyclesTable, notiffTable, agentsTable, settingsTable, dashboardTable;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == dashboardButton) {
            dashboardTable.toFront();
        } else if (event.getSource() == bicyclesButton) {
            bicyclesTable.toFront();
        } else if (event.getSource() == notificationsButton) {
            notiffTable.toFront();
        } else if (event.getSource() == agentsButton) {
            agentsTable.toFront();
            agents.showAgent();
        } else if (event.getSource() == settingsButton) {
            settingsTable.toFront();
        } else if (event.getSource() == newAgentButton) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("newAgent.fxml"));

                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Korman - New Agent");
                stage.getIcons().add(new Image(
                        getClass().getResource("icon.png").toString()
                ));
                stage.show();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
