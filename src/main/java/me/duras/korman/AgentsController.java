package me.duras.korman;

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
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AgentsController implements Initializable {

    ObservableList<Agent> agenti = FXCollections.observableArrayList();
    AgentsDao dao = new AgentsDao();

    @FXML
    private Button newAgentButton;

    @FXML
    private AnchorPane agentsTable;

    @FXML
    private Pagination agentPagin;

    @FXML
    private TableView<Agent> agentTablePagin;

    @FXML
    private TableColumn<?,?> agentId, agentCategory, agentSeries, agentSize, agentWmn, agentMinPrice, agentMaxPrice, agentDiff, agentYear;

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

    public void showAgents() {
        for (Agent agent : dao.getAllAgents()) {
            agenti.add(agent);
            System.out.println("Agent maxPrice: " + agent.getMaxPrice() + ", Size: " + agent.getSize());
        }
        System.out.println();
        /*agentId.setText("Ahoj");
        agentCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        agentSeries.setCellValueFactory(new PropertyValueFactory<>("series"));
        agentSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        agentWmn.setCellValueFactory(new PropertyValueFactory<>("wmn"));
        agentMinPrice.setCellValueFactory(new PropertyValueFactory<>("minPrice"));
        agentMaxPrice.setCellValueFactory(new PropertyValueFactory<>("maxPrice"));
        agentDiff.setCellValueFactory(new PropertyValueFactory<>("difference"));
        agentYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        agentTablePagin.setItems(agenti);*/
        
      //  agentTablePagin.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {

    }

    private void setCellTable() {

    }

    private void loadData() {
    }

}
