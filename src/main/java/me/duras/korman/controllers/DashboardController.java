package me.duras.korman.controllers;

import javafx.stage.Window;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import me.duras.korman.App;

/**
 *
 * @author martin
 */
public class DashboardController implements Initializable {

    @FXML
    private AnchorPane defaultTable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void showWindow(String windowFxml) {
        try {
            AnchorPane windowResource = FXMLLoader.load(App.class.getResource(windowFxml));

            Stage primaryStage = (Stage) defaultTable.getScene().getWindow();

            Scene scene = new Scene(windowResource);
            primaryStage.setTitle("Korman Launcher");
            primaryStage.setScene(scene);

            primaryStage.getIcons().add(new Image(
                    getClass().getResource("icon.png").toString()
            ));
            scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
