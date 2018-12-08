package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import me.duras.korman.*;
import me.duras.korman.dao.BicycleDao;
import me.duras.korman.models.Agent;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;

/**
 * Bike Controller
 */
public class BikeController implements Initializable {

    private ObservableList<Bicycle> bicycles = FXCollections.observableArrayList();
    private BicycleDao dao = DaoFactory.INSTANCE.getBicycleDao();

    @FXML
    private Label label1;

    @FXML
    private JFXButton fetchBicyclesButton;

    @FXML
    private TableView<Bicycle> bikeTablePagin;

    @FXML
    private TableColumn bikeCategory, bikeSeries, bikeSize, bikeWmn, bikePrice, bikeDiff, bikeYear;

    @FXML
    private Pagination bikePagin;

    @FXML
    void fetchBicycles() {
        System.out.println("Loading" + Instant.now());

        (new BicycleFetching()).fetchAll();
    }

    @FXML
    public void searchBicykle() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bikeTablePagin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // System.out.println(bikeTablePagin.getScene().getHeight());
        loadList();

        bikePagin.setPageCount(bicycles.size() / rowsPerPage() + 1);
        bikePagin.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex > bicycles.size() / rowsPerPage() + 1) {
                    return null;
                } else {
                    return createPage(pageIndex);
                }
            }
        });

        bikeTablePagin.setOnMouseClicked((MouseEvent event) -> {
            showBike();
        });
    }

    private void loadList() {
        bicycles.clear();
        List<Bicycle> list = dao.getAll();

        for (Bicycle bike : list) {
            bicycles.add(bike);
        }
    }

    public int itemsPerPage() {
        return 1;
    }

    public int rowsPerPage() {

        return 6;
    }

    public VBox createPage(int pageIndex) {
        int lastIndex = 0;
        int displace = bicycles.size() % rowsPerPage();
        if (displace > 0) {
            lastIndex = bicycles.size() / rowsPerPage();
        } else {
            lastIndex = bicycles.size() / rowsPerPage() - 1;
        }

        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage();

        bikeCategory.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("category"));
        bikeSeries.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("series"));
        bikeSize.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("size"));
        bikeWmn.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("wmn"));
        bikePrice.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("price"));
        bikeDiff.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("diff"));
        bikeYear.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("modelYear"));

        for (int i = page; i < page + itemsPerPage(); i++) {
            if (lastIndex == pageIndex) {
                bikeTablePagin.setItems(FXCollections.observableArrayList(
                        bicycles.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + displace)));
            } else {
                bikeTablePagin.setItems(FXCollections.observableArrayList(
                        bicycles.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
            }
            box.getChildren().add(bikeTablePagin);
        }
        return box;
    }

    public void showBike() {
        if (bikeTablePagin.getSelectionModel().getSelectedItem() != null) {
            Bicycle selectedBicycle = bikeTablePagin.getSelectionModel().getSelectedItem();
           
            ShowBikeController controller = MenuController.showWindow("Bicycle.fxml", "bicyclesButton", fetchBicyclesButton.getScene())
                    .getController();

            controller.setBicycle(selectedBicycle);
        }
    }
}
