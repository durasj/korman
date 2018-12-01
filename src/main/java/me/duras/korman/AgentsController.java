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

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        if (dao.getAllAgents().size() > 0) {
            agenti.clear();
            for (Agent agent : dao.getAllAgents()) {
                agenti.add(agent);
            }

            if (agenti.size() > 0) {
                agentId.setCellValueFactory(new PropertyValueFactory<>("idAgenta"));
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

    private void loadData() {
    }

}
