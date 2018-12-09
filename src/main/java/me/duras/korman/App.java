package me.duras.korman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.duras.korman.controllers.MenuController;
import me.duras.korman.dao.SettingDao;
import javafx.scene.image.Image;

public class App extends Application implements Initializable {

    Database db;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent rootPane = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));

        if (Utils.isFirstRun()) {
            try {
                Utils.initUserDataDir();
                Database.copyInitial();
            } catch (IOException e) {
                System.out.println("Unable to initialize user data directory.");
                e.printStackTrace();
            }
        }

        this.db = new Database();
        this.db.connect();
        DaoFactory.INSTANCE.setJdbcTemplate(this.db.getTemplate());

        Scene scene = new Scene(rootPane);
        primaryStage.setTitle("Korman");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(
            App.class.getResource("icon.png").toString()
        ));
        scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
        MenuController.setActive("DashboardButton", scene);
        primaryStage.show();

        // Start bicycle checking timer
        SettingDao settingDao = DaoFactory.INSTANCE.getSettingDao();
        int refreshTime = (int) Math.round(Double.parseDouble(settingDao.getByKey("refreshTime").getValue()));
        if (refreshTime > 0) {
            BicycleChecking.startTimer(refreshTime);
        }
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
