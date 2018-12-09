package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import me.duras.korman.dao.ArchivedBicycleDao;
import me.duras.korman.dao.BicycleDao;
import me.duras.korman.models.ArchivedBicycle;
import me.duras.korman.models.Bicycle;

/**
 * Bike Controller
 */
public class BikeController implements Initializable {

    private ObservableList<Bicycle> bicycles = FXCollections.observableArrayList();
    private BicycleDao dao = DaoFactory.INSTANCE.getBicycleDao();
    private ArchivedBicycleDao archivedDao = DaoFactory.INSTANCE.getArchivedBicycleDao();

    @FXML
    private JFXButton fetchBicyclesButton;

    @FXML
    private JFXToggleButton archivedButton;

    @FXML
    private TableView<Bicycle> bikeTablePagin;

    @FXML
    private TableColumn bikeCategory, bikeSeries, bikeSize, bikeWmn, bikePrice, bikeDiff, bikeYear, bikeArch;

    @FXML
    private Pagination bikePagin;

    @FXML
    void fetchBicycles() {
        (new BicycleChecking()).fetchAll();
    }

    @FXML
    public void searchBicykle() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bikeTablePagin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        archivedButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (!archivedButton.isSelected()) {
                    bikeArch.setVisible(false);
                    loadList(false);
                    System.out.println("vypnuty");
                } else {
                    bikeArch.setVisible(true);
                    loadList(true);
                    System.out.println("zapnuty");
                }
            }
        });

        // System.out.println(bikeTablePagin.getScene().getHeight());
        loadList(false);

        bikeTablePagin.setOnMouseClicked((MouseEvent event) -> {
            showBike();
        });
    }

    private void loadList(boolean isArchived) {
        bicycles.clear();

        if (!isArchived) {
            List<Bicycle> list = dao.getAll();
            for (Bicycle bike : list) {
                bicycles.add(bike);
            }
        } else {
            List<ArchivedBicycle> list = archivedDao.getAll();
            for (ArchivedBicycle bike : list) {
                bicycles.add(bike);
            }
        }

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
    }

    public int itemsPerPage() {
        return 1;
    }

    public int rowsPerPage() {
        int rows = 6;

        return rows;
    }

    public VBox createPage(int pageIndex) {
        int lastIndex = 0;

        int displace = bicycles.size() % rowsPerPage();
        
        if (bicycles.size() != 0) {
            if (displace > 0) {
                lastIndex = bicycles.size() / rowsPerPage();
            } else {
                lastIndex = bicycles.size() / rowsPerPage() - 1;
            }
        } else {
            lastIndex = 0;
        }

        VBox box = new VBox(6);
        int page = pageIndex * itemsPerPage();

        bikeCategory.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("category"));
        bikeSeries.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("series"));
        bikeSize.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("size"));
        bikeWmn.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("wmn"));
        bikePrice.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("price"));
        bikeDiff.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("diff"));
        bikeYear.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("modelYear"));
        bikeArch.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("archivedAt"));

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

            ShowBikeController controller = MenuController.showWindow("Bicycle.fxml", "BicyclesButton", fetchBicyclesButton.getScene())
                    .getController();

            controller.setBicycle(selectedBicycle);
        }
    }
}
