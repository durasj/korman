package me.duras.korman.controllers;

import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import me.duras.korman.App;
import me.duras.korman.models.*;

public class AgentsController implements Initializable {

    static ObservableList<Agent> agenti = FXCollections.observableArrayList();
    AgentsDao dao = new AgentsDao();

    int pocet = dao.getAllAgents().size();
    int zvysok = pocet % rowsPerPage();
    int mnozstvo = pocet / rowsPerPage();

    @FXML
    private Pagination agentPagination;

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

    public void showWindow(String windowFxml) {
        BorderPane windowResource;
        try {
            windowResource = FXMLLoader.load(App.class.getResource(windowFxml));

            Stage primaryStage = (Stage) newAgentButton.getScene().getWindow();

            Scene scene = new Scene(windowResource, primaryStage.getWidth() - 18, primaryStage.getHeight() - 47);
            primaryStage.setTitle("Korman");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1218);
            primaryStage.setMinHeight(600);

            primaryStage.getIcons().add(new Image(
                    App.class.getResource("icon.png").toString()
            ));
            scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(AgentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {

        agentTablePagin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        agentTablePagin.setOnMouseClicked((MouseEvent event) -> {
            onEdit();
        });

        if (pocet > 0) {
            agenti.clear();
            for (Agent agent : dao.getAllAgents()) {
                agenti.add(agent);
            }

            if (agenti.size() > 0) {

                agentPagination.setPageCount(mnozstvo);

                agentPagination.setPageFactory(new Callback<Integer, Node>() {
                    @Override
                    public Node call(Integer pageIndex) {
                        return createPage(pageIndex);
                    }
                });
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

    public int itemsPerPage() {
        return 1;
    }

    public int rowsPerPage() {
        return 2;
    }

    public VBox createPage(int pageIndex) {
        VBox box = new VBox(5);

        agentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        agentCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        agentSeries.setCellValueFactory(new PropertyValueFactory<>("series"));
        agentSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        agentWmn.setCellValueFactory(new PropertyValueFactory<>("wmn"));
        agentMinPrice.setCellValueFactory(new PropertyValueFactory<>("minPrice"));
        agentMaxPrice.setCellValueFactory(new PropertyValueFactory<>("maxPrice"));
        agentDiff.setCellValueFactory(new PropertyValueFactory<>("difference"));
        agentYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        System.out.println("Prvykoniec: " + (pageIndex * rowsPerPage() + zvysok) + ", druhykoniec: " + (pageIndex * rowsPerPage() + rowsPerPage()));

        if (zvysok > 0) {
            agentTablePagin.setItems(FXCollections.observableArrayList(agenti.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + zvysok)));
        } else {
            agentTablePagin.setItems(FXCollections.observableArrayList(agenti.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
        }
        box.getChildren().add(agentTablePagin);

        return box;
    }

}
