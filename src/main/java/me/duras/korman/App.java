package me.duras.korman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.fxml.Initializable;

public class App extends Application implements Initializable {

    Database db;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent rootPane = FXMLLoader.load(getClass().getResource("Launcher.fxml"));

        if (Utils.isFirstRun()) {
            try {
                Utils.initUserDataDir();
            } catch (IOException e) {
                System.out.println("Unable to initialize user data directory.");
                e.printStackTrace();
            }
        }

        this.db = new Database();
        this.db.connect();

        System.out.println("Hello, JAVAFX!");

        Scene scene = new Scene(rootPane);
        primaryStage.setTitle("Korman Launcher");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(
                getClass().getResource("icon.png").toString()
        ));
        scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        this.db.close();

        super.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
