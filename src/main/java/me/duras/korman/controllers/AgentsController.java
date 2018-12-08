package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import me.duras.korman.DaoFactory;
import me.duras.korman.dao.AgentDao;
import me.duras.korman.models.*;

public class AgentsController implements Initializable {

    private ObservableList<Agent> agents = FXCollections.observableArrayList();
    private AgentDao dao = DaoFactory.INSTANCE.getAgentDao();
    private Set<Integer> deleteList = new HashSet<Integer>();

    @FXML
    private JFXButton newAgentButton, delete;

    @FXML
    private TableView<Agent> agentTablePagin;

    @FXML
    private TableColumn<Agent, String> agentName, agentCategory, agentSeries, agentSize, agentWmn, agentMinPrice, agentMaxPrice, agentDiff, agentYear;

    @FXML
    private TableColumn<Agent, JFXCheckBox> checkBox;

    @FXML
    private void agentsButtonAction(ActionEvent event) {

        if (event.getSource() == newAgentButton) {
            MenuController.showWindow("newAgent.fxml", "AgentsButton", newAgentButton.getScene());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        if (deleteList.size() > 0) {
            delete.setDisable(false);
        } else {
            delete.setDisable(true);
        }
        checkBox.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Agent, JFXCheckBox>, ObservableValue<JFXCheckBox>>() {

            @Override
            public ObservableValue<JFXCheckBox> call(
                    TableColumn.CellDataFeatures<Agent, JFXCheckBox> arg0) {
                Agent agent = arg0.getValue();

                JFXCheckBox checkBox = new JFXCheckBox();

                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                         if (checkBox.isSelected()) {
                            deleteList.add(agent.getId());
                            delete.setDisable(false);
                        } else {
                            deleteList.remove(agent.getId());
                            if (deleteList.size() == 0) {
                                delete.setDisable(true);
                            }
                        }

                    }
                });
                return new SimpleObjectProperty<JFXCheckBox>(checkBox);
            }
        });

        agentTablePagin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        agentTablePagin.setOnMouseClicked((MouseEvent event) -> {
            onEdit();
        });

        loadList();
    }

    public void onEdit() {
        if (agentTablePagin.getSelectionModel().getSelectedItem() != null) {
            Agent selectedAgent = agentTablePagin.getSelectionModel().getSelectedItem();

            NewAgentController controller = MenuController.showWindow("newAgent.fxml", "AgentsButton", newAgentButton.getScene())
                .getController();

            controller.setAgent(selectedAgent);
        }
    }

    @FXML
    public void deleteAgent(ActionEvent event) {
        for (int agentId : deleteList) {
            dao.delete(agentId);
        }
        deleteList.clear();
        delete.setDisable(true);
        loadList();
    }

    private void loadList() {
        agents.clear();
        List<Agent> list = dao.getAll();

        if (list.size() > 0) {
            for (Agent agent : list) {
                agents.add(agent);
            }

            if (agents.size() > 0) {
                agentName.setCellValueFactory(new PropertyValueFactory<>("name"));
                agentCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
                agentSeries.setCellValueFactory(new PropertyValueFactory<>("series"));
                agentSize.setCellValueFactory(new PropertyValueFactory<>("size"));
                agentWmn.setCellValueFactory(new PropertyValueFactory<>("wmn"));
                agentMinPrice.setCellValueFactory(new PropertyValueFactory<>("minPrice"));
                agentMaxPrice.setCellValueFactory(new PropertyValueFactory<>("maxPrice"));
                agentDiff.setCellValueFactory(new PropertyValueFactory<>("minDiff"));
                agentYear.setCellValueFactory(new PropertyValueFactory<>("modelYear"));

                agentTablePagin.setItems(agents);
            }
        }
    }
}
