package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.duras.korman.App;
import me.duras.korman.DaoFactory;
import me.duras.korman.dao.AgentDao;
import me.duras.korman.models.*;

public class NewAgentController {

    private Agent agent = null;

    AgentDao dao = DaoFactory.INSTANCE.getAgentDao();

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
        List<BicycleCategory> categories = DaoFactory.INSTANCE.getBicycleCategoryDao().getAll();
        BicycleCategory mtbCategory = null;
        BicycleCategory roadCategory = null;
        BicycleCategory triathlonCategory = null;
        BicycleCategory urbanCategory = null;
        BicycleCategory fitnessCategory = null;

        for (BicycleCategory category : categories) {
            if (category.getName().equals("MTB")) {
                mtbCategory = category;
            }
            if (category.getName().equals("Road")) {
                roadCategory = category;
            }
            if (category.getName().equals("Triathlon")) {
                triathlonCategory = category;
            }
            if (category.getName().equals("Urban")) {
                urbanCategory = category;
            }
            if (category.getName().equals("Fitness")) {
                fitnessCategory = category;
            }
        }

        String name = nameAgent.getText();
        if (name.equals("")) {
            name = "Unnamed";
        }

        int minPrice = (int) setMinPrice.getValue();
        int maxPrice = (int) setMaxPrice.getValue();
        int minDiff = (int) setDiff.getValue();
        int modelYear = (int) setYear.getValue();
        String series = setSeries.getText();

        BicycleCategory category = null;
        String chosenCategoryName = setCategory.getSelectionModel().getSelectedItem();
        if (chosenCategoryName.equals("MTB")) {
            category = mtbCategory;
        }
        if (chosenCategoryName.equals("Road")) {
            category = roadCategory;
        }
        if (chosenCategoryName.equals("Triathlon")) {
            category = triathlonCategory;
        }
        if (chosenCategoryName.equals("Urban")) {
            category = urbanCategory;
        }
        if (chosenCategoryName.equals("Fitness")) {
            category = fitnessCategory;
        }

        String size = setSize.getSelectionModel().getSelectedItem();
        boolean forWmn = setForWoman.isSelected();

        Agent savedAgent = null;
        if (this.agent != null) {
            agent.setName(name);
            agent.setMinPrice(minPrice);
            agent.setMaxPrice(maxPrice);
            agent.setMinDiff(minDiff);
            agent.setModelYear(modelYear);
            agent.setSeries(series);
            agent.setCategory(category);
            agent.setSize(size);
            agent.setWmn(forWmn);
            savedAgent = agent;
        } else {
            savedAgent = new Agent(name, "TODO", category, series, size, forWmn, minPrice, maxPrice, minDiff, modelYear);
        }

        dao.save(savedAgent);
        if (agent == null) {
            createAgentButton.setText("Create");
        }

        MenuController.showWindow("AgentsTable.fxml", "AgentsButton", createAgentButton.getScene());
    }

    @FXML
    private void initialize() {
        nameAgent.setText("Unnamed");
        setCategory.getSelectionModel().select("MTB");
        setSize.getSelectionModel().select("M");
        setForWoman.setSelected(false);
    }

    public void setAgent(Agent agent) {
        this.agent = agent;

        createAgentButton.setText("Save");

        nameAgent.setText(String.valueOf(agent.getName()));
        setMinPrice.setValue((double) (agent.getMinPrice()));
        setMaxPrice.setValue((double) (agent.getMaxPrice()));
        setDiff.setValue((double) (agent.getMinDiff()));
        setYear.setValue((double) (agent.getModelYear()));
        setSeries.setText(agent.getSeries());
        setCategory.getSelectionModel().select(agent.getCategory().getName());
        setSize.getSelectionModel().select(agent.getSize());
        if (agent.getWmn()) {
            setForWoman.setSelected(true);
        }
    }
}
