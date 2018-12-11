package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.duras.korman.models.Bicycle;

public class ShowBikeController implements Initializable {

    private Bicycle bicycle = null;

    @FXML
    private JFXButton closeBike;

    @FXML
    private Label category, series, size, wmn, price, difference, year;

    @FXML
    private ImageView bikePic;

    @FXML
    private void close() {
        BikeController controller = MenuController.showWindow("BikeTable.fxml", "BicyclesButton", closeBike.getScene())
            .getController();
        controller.afterInitialize();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setBicycle(Bicycle bicycle) {
        this.bicycle = bicycle;
        if (bicycle.getPhotoUrl() != null) {
            try {
                Image image = new Image(new FileInputStream(bicycle.getPhotoUrl()));
                bikePic.setImage(image);
            } catch (FileNotFoundException e) {
            }
        }
        category.setText(bicycle.getSeries());
        series.setText(bicycle.getSeries());
        size.setText(bicycle.getSize());
        wmn.setText(bicycle.getSize());
        price.setText(String.valueOf(bicycle.getPrice()));
        difference.setText(String.valueOf(bicycle.getDiff()));
        year.setText(String.valueOf(bicycle.getModelYear()));
    }
}
