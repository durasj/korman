package me.duras.korman.controllers;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.duras.korman.App;
import me.duras.korman.models.*;

/**
 *
 * @author mmiskov
 */
public class NewAgentController {

    private String category;
    private String series;
    private String size;
    private int idAgenta;
    private String wmn;
    private int minPrice;
    private int maxPrice;
    private int difference;
    private int year;
    private String name;
    private String timeStamp;
    private static boolean onEdit;

    private static String cat, ser, sz, wm, min, max, diff, yr, nm;

    //after click button Create Agent >> 
    AgentsDao agentsDao = new AgentsDao();

    @FXML
    private TextField setMinPrice, setMaxPrice, setDiff, setYear, setSeries, nameAgent;

    @FXML
    private ComboBox<String> setCategory, setSize, forWoman;

    @FXML
    private Button createAgentButton;

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

        String outputMinPrice = setMinPrice.getText();
        if (outputMinPrice.equals("")) {
            this.minPrice = 0;
        } else {
            this.minPrice = Integer.parseInt(outputMinPrice);
        }

        String outputMaxPrice = setMaxPrice.getText();
        if (outputMaxPrice.equals("")) {
            this.maxPrice = Integer.MAX_VALUE;
        } else {
            this.maxPrice = Integer.parseInt(outputMaxPrice);
        }

        String outputSetDiff = setDiff.getText();
        if (outputSetDiff.equals("")) {
            this.difference = 0;
        } else {
            this.difference = Integer.parseInt(outputSetDiff);
        }

        String outputSetYear = setYear.getText();
        if (outputSetYear.equals("")) {
            this.year = 0;
        } else {
            this.year = Integer.parseInt(outputSetYear);
        }

        String outputSetSeries = setSeries.getText();
        if (outputSetSeries.equals("")) {
            series = null;
        } else {
            this.series = outputSetSeries;
        }

        String outputSetCategory = setCategory.getSelectionModel().getSelectedItem();

        String outputSetSize = setSize.getSelectionModel().getSelectedItem();
        this.size = outputSetSize;

        String outputForWoman = forWoman.getSelectionModel().getSelectedItem();
        this.wmn = outputForWoman;

        if (!onEdit) {
            agentsDao.createAgent(category, series, size, idAgenta, wmn, minPrice, maxPrice, difference, year, name, timeStamp);
        } else {
            agentsDao.editAgent(category, series, size, idAgenta, wmn, minPrice, maxPrice, difference, year, name, timeStamp);
        }

        createAgentButton.setText("Create");
        onEdit = false;

        try {
            BorderPane windowResource = FXMLLoader.load(App.class.getResource("AgentsTable.fxml"));

            Stage primaryStage = (Stage) createAgentButton.getScene().getWindow();

            Scene scene = new Scene(windowResource);
            primaryStage.setTitle("Korman Launcher");
            primaryStage.setScene(scene);

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
            System.out.println("Ahoj som zapnuty");

            System.out.println(minPrice);
            nameAgent.setText(nm);
            setMinPrice.setText(min);
            setMaxPrice.setText(max);
            setDiff.setText(diff);
            setYear.setText(yr);
            setSeries.setText(ser);
            setCategory.getSelectionModel().select(cat);
            setSize.getSelectionModel().select(sz);
            forWoman.getSelectionModel().select(wm);
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
        if (!wm.equals("null")) {
            this.wm = wm;
        } else {
            this.wm = "";
        }
    }

    public void setMin(String min) {
        if (!min.equals("0")) {
            this.min = min;
        } else {
            this.min = "";
        }
    }

    public void setMax(String max) {
        if (!max.equals("2147483647")) {
            this.max = max;
        } else {
            this.max = "";
        }
    }

    public void setDiff(String diff) {
        if (!diff.equals("0")) {
            this.diff = diff;
        } else {
            this.diff = "";
        }
    }

    public void setYr(String yr) {
        if (!yr.equals("0")) {
            this.yr = yr;
        } else {
            this.yr = "";
        }
    }

}
