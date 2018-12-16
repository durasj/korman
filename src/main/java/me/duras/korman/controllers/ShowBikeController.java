package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.duras.korman.models.ArchivedBicycle;
import me.duras.korman.models.Bicycle;

public class ShowBikeController implements Initializable {

    private Bicycle bicycle = null;
    private ArchivedBicycle archivedBicycle = null;

    @FXML
    private JFXButton closeBike;

    @FXML
    private Label category, series, size, wmn, price, difference, year, archivedAt;

    @FXML
    private ImageView bikePic;

    @FXML
    private void showDetails() throws URISyntaxException, IOException {
        Desktop d = Desktop.getDesktop();
        d.browse(new URI(bicycle.getUrl()));
    }

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
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);

        this.bicycle = bicycle;

        if (bicycle.getPhotoUrl() != null) {
            Image image = new Image(bicycle.getPhotoUrl());
            bikePic.setImage(image);
        }

        category.setText(bicycle.getCategory().getName());
        series.setText(bicycle.getSeries());
        size.setText(bicycle.getSize());
        wmn.setText(bicycle.isWmn() ? "Yes" : "No");
        price.setText(String.valueOf(format.format(bicycle.getPrice() / 100)));
        difference.setText(String.valueOf(format.format(bicycle.getDiff() / 100)));
        year.setText(String.valueOf(bicycle.getModelYear()));
    }

    public void setArchivedBicycle(ArchivedBicycle bicycle) {
        archivedAt.setVisible(true);
        archivedAt.setText(String.valueOf(archivedBicycle.getArchivedAt()));
    }
}
