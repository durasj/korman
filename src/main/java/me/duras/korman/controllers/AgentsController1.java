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

public class AgentsController1 implements Initializable {

    TableView agentTablePagin = new TableView();

    ObservableList<Agent> agenti = FXCollections.observableArrayList();
    AgentsDao dao = new AgentsDao();

    @FXML
    private Button newAgentButton;

    @FXML
    private AnchorPane agentsTable;

    //  @FXML
    // private TableView<Agent> agentTablePagin;
    @FXML
    private TableColumn<Agent, String> agentId, agentCategory, agentSeries, agentSize, agentWmn, agentMinPrice, agentMaxPrice, agentDiff, agentYear;

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
        agenti.clear();
        for (Agent agent : dao.getAllAgents()) {
            agenti.add(agent);
            System.out.println("Agent id: " + agent.getIdAgenta() + ", Size: " + agent.getSize());
        }
        Stage stage = new Stage();
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(450);
        stage.setHeight(500);

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, agentTablePagin);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
        TableColumn<Agent, String> agentId = new TableColumn<Agent, String>("First Name");
        agentId.setMinWidth(100);
        TableColumn<Agent, String> agentCategory = new TableColumn<Agent, String>("First Name");
        agentCategory.setMinWidth(100);
        TableColumn<Agent, String> agentSeries = new TableColumn<Agent, String>("First Name");
        agentSeries.setMinWidth(100);
        TableColumn<Agent, String> agentSize = new TableColumn<Agent, String>("First Name");
        agentSize.setMinWidth(100);
        TableColumn<Agent, String> agentWmn = new TableColumn<Agent, String>("First Name");
        agentWmn.setMinWidth(100);
        TableColumn<Agent, String> agentMinPrice = new TableColumn<Agent, String>("First Name");
        agentMinPrice.setMinWidth(100);
        TableColumn<Agent, String> agentMaxPrice = new TableColumn<Agent, String>("First Name");
        agentMaxPrice.setMinWidth(100);
        TableColumn<Agent, String> agentDiff = new TableColumn<Agent, String>("First Name");
        agentDiff.setMinWidth(100);
        TableColumn<Agent, String> agentYear = new TableColumn<Agent, String>("First Name");
        agentYear.setMinWidth(100);

        agentId.setCellValueFactory(new PropertyValueFactory<>("idAgenta"));
        agentCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        agentSeries.setCellValueFactory(new PropertyValueFactory<>("series"));
        agentSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        agentWmn.setCellValueFactory(new PropertyValueFactory<>("wmn"));
        agentMinPrice.setCellValueFactory(new PropertyValueFactory<>("minPrice"));
        agentMaxPrice.setCellValueFactory(new PropertyValueFactory<>("maxPrice"));
        agentDiff.setCellValueFactory(new PropertyValueFactory<>("difference"));
        agentYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        System.out.println("velkost OL: " + agenti.size());
        agentTablePagin.setItems(agenti);
        agentTablePagin.getColumns().addAll(agentCategory, agentSeries, agentSize, agentId, agentWmn, agentMinPrice, agentMaxPrice, agentDiff, agentYear);

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
