package me.duras.korman.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import me.duras.korman.models.*;

public class AgentsController implements Initializable {

    static ObservableList<Agent> agenti = FXCollections.observableArrayList();
    AgentsDao dao = new AgentsDao();

    @FXML
    private Button newAgentButton;

    @FXML
    private AnchorPane agentsTable, agentsTableIn;

    @FXML
    private TableView<Agent> agentTablePagin;

    @FXML
    private TableColumn<Agent, String> agentName, agentCategory, agentSeries, agentSize, agentWmn, agentMinPrice, agentMaxPrice, agentDiff, agentYear;

    @FXML
    private void agentsButtonAction(ActionEvent event) {

        if (event.getSource() == newAgentButton) {
            showWindow("newAgent.fxml");

        }
    }

    public void showWindow(String window) {
        String newWindow = window;
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(newWindow));
            agentsTable.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        agentTablePagin.setOnMouseClicked((MouseEvent event) -> {
            onEdit();
        });

        if (dao.getAllAgents().size() > 0) {
            agenti.clear();
            for (Agent agent : dao.getAllAgents()) {
                agenti.add(agent);
            }

            if (agenti.size() > 0) {
                agentName.setCellValueFactory(new PropertyValueFactory<>("name"));
                agentCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
                agentSeries.setCellValueFactory(new PropertyValueFactory<>("series"));
                agentSize.setCellValueFactory(new PropertyValueFactory<>("size"));
                agentWmn.setCellValueFactory(new PropertyValueFactory<>("wmn"));
                agentMinPrice.setCellValueFactory(new PropertyValueFactory<>("minPrice"));
                agentMaxPrice.setCellValueFactory(new PropertyValueFactory<>("maxPrice"));
                agentDiff.setCellValueFactory(new PropertyValueFactory<>("difference"));
                agentYear.setCellValueFactory(new PropertyValueFactory<>("year"));

                agentTablePagin.setItems(agenti);

            }
        }
    }

    public void onEdit() {

        if (agentTablePagin.getSelectionModel().getSelectedItem() != null) {
            Agent selectedAgent = agentTablePagin.getSelectionModel().getSelectedItem();

            NewAgentController newAgentController = new NewAgentController();
            newAgentController.onEdit(true);

            String name = String.valueOf(selectedAgent.getName());
            String minPrice = String.valueOf(selectedAgent.getMinPrice());
            String maxPrice = String.valueOf(selectedAgent.getMaxPrice());
            String minDiff = String.valueOf(selectedAgent.getDifference());
            String modelYear = String.valueOf(selectedAgent.getYear());
            String series = String.valueOf(selectedAgent.getSeries());
            String category = String.valueOf(selectedAgent.getCategory());
            String size = String.valueOf(selectedAgent.getSize());
            String forWomen = String.valueOf(selectedAgent.getWmn());

            newAgentController.setName(name);
            newAgentController.setSer(series);
            newAgentController.setYr(modelYear);
            newAgentController.setDiff(minDiff);
            newAgentController.setMin(minPrice);
            newAgentController.setMax(maxPrice);
            newAgentController.setCat(category);
            newAgentController.setSz(size);
            newAgentController.setWm(forWomen);
            showWindow("newAgent.fxml");

        }
    }

    private void loadData() {
    }

}
