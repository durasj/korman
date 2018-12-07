package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.duras.korman.App;
import me.duras.korman.models.*;

public class NewAgentController {

    private String category;
    private String series;
    private String size;
    private int idAgenta;
    private boolean wmn;
    private int minPrice;
    private int maxPrice;
    private int difference;
    private int year;
    private String name;
    private String timeStamp;
    private static boolean onEdit;

    private static String cat, ser, sz, wm, min, max, diff, yr, nm;

    AgentsDao agentsDao = new AgentsDao();

    @FXML
    private JFXSlider setMinPrice, setMaxPrice, setDiff, setYear;

    @FXML
    private JFXTextField setSeries, nameAgent;

    @FXML
    private JFXComboBox<String> setCategory, setSize;

    @FXML
    private JFXCheckBox setForWoman;

    @FXML
    private JFXButton createAgentButton;

    @FXML
    private AnchorPane newAgent;

    @FXML
    private void addAgent(ActionEvent event) {

        String outputAgentName = nameAgent.getText();
        if (outputAgentName.equals("")) {
            this.name = "default";
        } else {
            this.name = outputAgentName;
        }

        int outputMinPrice = (int) setMinPrice.getValue();
        this.minPrice = outputMinPrice;

        int outputMaxPrice = (int) setMaxPrice.getValue();
        if (outputMaxPrice == 0) {
            this.maxPrice = 10000;
        } else {
            this.maxPrice = outputMaxPrice;
        }

        int outputSetDiff = (int) setDiff.getValue();
        this.difference = outputSetDiff;

        int outputSetYear = (int) setYear.getValue();
        this.year = outputSetYear;

        String outputSetSeries = setSeries.getText();
        if (outputSetSeries.equals("")) {
            series = null;
        } else {
            this.series = outputSetSeries;
        }

        String outputSetCategory = setCategory.getSelectionModel().getSelectedItem();
        this.category = outputSetCategory;

        String outputSetSize = setSize.getSelectionModel().getSelectedItem();
        this.size = outputSetSize;

        boolean outputSetForWoman = setForWoman.isSelected();
        if (outputSetForWoman == false) {
            this.wmn = false;
        } else {
            this.wmn = true;
        }

        if (!onEdit) {
            agentsDao.createAgent(category, series, size, idAgenta, wmn, minPrice, maxPrice, difference, year, name, timeStamp);
        } else {
            agentsDao.editAgent(category, series, size, idAgenta, wmn, minPrice, maxPrice, difference, year, name, timeStamp);
            createAgentButton.setText("Create");
            onEdit = false;
        }

        try {
            BorderPane windowResource = FXMLLoader.load(App.class.getResource("AgentsTable.fxml"));

            Stage primaryStage = (Stage) createAgentButton.getScene().getWindow();

            Scene scene = new Scene(windowResource, primaryStage.getWidth() - 18, primaryStage.getHeight() - 47);
            primaryStage.setTitle("Korman Launcher");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1218);
            primaryStage.setMinHeight(600);

            primaryStage.getIcons().add(new Image(
                    App.class.getResource("icon.png").toString()
            ));
            scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEdit(boolean onEdit) {
        this.onEdit = onEdit;
    }

    @FXML
    private void initialize() {
        if (onEdit) {
            createAgentButton.setText("Edit");
            System.out.println("onEdit zapnuty");

            nameAgent.setText(nm);
            setMinPrice.setValue(Double.parseDouble(min));
            setMaxPrice.setValue(Double.parseDouble(max));
            setDiff.setValue(Double.parseDouble(diff));
            setYear.setValue(Double.parseDouble(yr));
            setSeries.setText(ser);
            setCategory.getSelectionModel().select(cat);
            setSize.getSelectionModel().select(sz);
            if (wm.equals("true")) {
                setForWoman.setSelected(true);
            }
        } else {
            setCategory.getSelectionModel().select("MTB/GRAVITY");
            setSize.getSelectionModel().select("M");
            setForWoman.setSelected(false);

        }
    }

    public void setName(String nm) {
        this.nm = nm;
    }

    public void setCat(String cat) {
        if (!cat.equals("null")) {
            this.cat = cat;
        } else {
            this.cat = "";
        }
    }

    public void setSer(String ser) {
        if (!ser.equals("null")) {
            this.ser = ser;
        } else {
            this.ser = "";
        }
    }

    public void setSz(String sz) {
        if (!sz.equals("null")) {
            this.sz = sz;
        } else {
            this.sz = "";
        }
    }

    public void setWm(String wm) {
        this.wm = wm;

    }

    public void setMin(String min) {
        this.min = min;

    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public void setYr(String yr) {
        this.yr = yr;
    }

}
