package me.duras.korman;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    Database db;

    @Override
    public void start(Stage primaryStage) throws Exception {
        LauncherController mainController = new LauncherController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Launcher.fxml"));
        fxmlLoader.setController(mainController);
        Parent rootPane = fxmlLoader.load();

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

        System.out.println( "Hello, JAVAFX!" );

        Scene scene = new Scene(rootPane);
        primaryStage.setTitle("Korman Launcher");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        this.db.close();

        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}