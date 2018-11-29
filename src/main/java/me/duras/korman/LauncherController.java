package me.duras.korman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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

/**
 *
 * @author martin
 */
public class LauncherController implements Initializable {

    Agents agents = new Agents();
    ObservableList<Agent> agenti = FXCollections.observableArrayList();
    AgentsDao dao = new AgentsDao();

    @FXML
    private Pagination agentPagin;

    @FXML
    private TableView<Agent> agentTablePagin;

    @FXML
    private TableColumn<Agent, String> agentCategory, agentSeries, agentSize, agentWmn, agentMinPrice, agentMaxPrice, agentDiff, agentYear;
    @FXML
    private Button dashboardButton, bicyclesButton, notificationsButton, agentsButton, settingsButton, newAgentButton;

    @FXML
    private AnchorPane bicyclesTable, notiffTable, agentsTable, settingsTable, dashboardTable;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == dashboardButton) {
            dashboardTable.toFront();
        } else if (event.getSource() == bicyclesButton) {
            bicyclesTable.toFront();
        } else if (event.getSource() == notificationsButton) {
            notiffTable.toFront();
        } else if (event.getSource() == agentsButton) {
            agentsTable.toFront();
            showAgents();

        } else if (event.getSource() == settingsButton) {
            settingsTable.toFront();
        } else if (event.getSource() == newAgentButton) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("newAgent.fxml"));

                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Korman - New Agent");
                stage.getIcons().add(new Image(
                        getClass().getResource("icon.png").toString()
                ));
                stage.show();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void showAgents() {
        for (Agent agent : dao.getAllAgents()) {
            agenti.add(agent);
            System.out.println("Agent maxPrice: " + agent.getMaxPrice() + ", Size: " + agent.getSize());
        }

        agentCategory.setCellValueFactory(new PropertyValueFactory<Agent, String>("category"));
        agentSeries.setCellValueFactory(new PropertyValueFactory<Agent, String>("series"));
        agentSize.setCellValueFactory(new PropertyValueFactory<Agent, String>("size"));
        agentWmn.setCellValueFactory(new PropertyValueFactory<Agent, String>("wmn"));
        agentMinPrice.setCellValueFactory(new PropertyValueFactory<Agent, String>("minPrice"));
        agentMaxPrice.setCellValueFactory(new PropertyValueFactory<Agent, String>("maxPrice"));
        agentDiff.setCellValueFactory(new PropertyValueFactory<Agent, String>("difference"));
        agentYear.setCellValueFactory(new PropertyValueFactory<Agent, String>("year"));

        agentTablePagin.setItems(agenti);
    }

}
