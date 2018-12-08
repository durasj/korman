package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import me.duras.korman.models.*;

public class AgentsController implements Initializable {

    private ObservableList<Agent> agenti = FXCollections.observableArrayList();
    private List<Integer> checkBoxZoznam = new ArrayList<Integer>();
    AgentsDao dao = new AgentsDao();

    private int pocet = dao.getAllAgents().size();

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
        if (checkBoxZoznam.size() > 0) {
            delete.setDisable(false);
        } else {
            delete.setDisable(true);
        }
        checkBox.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Agent, JFXCheckBox>, ObservableValue<JFXCheckBox>>() {

            @Override
            public ObservableValue<JFXCheckBox> call(
                    TableColumn.CellDataFeatures<Agent, JFXCheckBox> arg0) {
                Agent user = arg0.getValue();

                JFXCheckBox checkBox = new JFXCheckBox();

                /* for (int i = 0; i < checkBoxZoznam.size(); i++) {
                    if (checkBoxZoznam.get(i).equals(pocet));
                }*/
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                            Boolean old_val, Boolean new_val) {
                        //user.setSelected(new_val);
                        if (checkBox.isSelected()) {
                            checkBoxZoznam.add(user.getIdAgenta());
                            delete.setDisable(false);
                            System.out.println("klik " + checkBoxZoznam.size());
                        } else {
                            checkBoxZoznam.remove(user.getIdAgenta());
                            if (checkBoxZoznam.size() == 0) {
                                delete.setDisable(true);
                            }
                            System.out.println("pokliku  " + checkBoxZoznam.size());
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

        if (pocet > 0) {
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
            MenuController.showWindow("newAgent.fxml", "agentsButton", newAgentButton.getScene());

        }
    }

    @FXML
    public void deleteAgent(ActionEvent event) {
        for (int i = 0; i < checkBoxZoznam.size(); i++) {
            System.out.println("agenti size: " + agenti.size());
            System.out.println("checkbox size: " + checkBoxZoznam.size());
            agenti.remove(agenti.get(i).getName());
            System.out.println(agenti.get(i).getName());
            dao.deleteAgent(checkBoxZoznam.get(i));
        }
        /* BorderPane windowResource;
        try {
            windowResource = FXMLLoader.load(App.class.getResource("AgentsTable.fxml"));

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
        }*/

    }
}
