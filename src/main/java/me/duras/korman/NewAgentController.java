package me.duras.korman;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author mmiskov
 */
public class NewAgentController {

    private String category;
    private String series;
    private String size;
    private int wmn;
    private int minPrice;
    private int maxPrice;
    private int difference;
    private int year;

    //after click button Create Agent >> 
    AgentsDao agentsDao = new AgentsDao();
    Agents agents = new Agents();

    @FXML
    private TextField setMinPrice, setMaxPrice, setDiff, setYear, setSeries;

    @FXML
    private ComboBox<String> setCategory, setSize, forWoman;

    @FXML
    private Button createAgentButton;

    @FXML
    private void addAgent(ActionEvent event) {

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
        }

        String outputSetCategory = setCategory.getSelectionModel().getSelectedItem();

        String outputSetSize = setSize.getSelectionModel().getSelectedItem();
        this.size = outputSetSize;

        String outputForWoman = forWoman.getSelectionModel().getSelectedItem();

        if (outputForWoman != null && outputForWoman.equals("Yes")) {
            this.wmn = 1;
        } else {
            this.wmn = 0;
        }

        agentsDao.createAgent(category, series, size, wmn, minPrice, maxPrice, difference, year);
        agents.showAgent();

        Stage stage = (Stage) createAgentButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void initialize() {

    }

}
