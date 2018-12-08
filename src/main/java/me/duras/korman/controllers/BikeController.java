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
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.stage.Stage;

import me.duras.korman.*;
import me.duras.korman.dao.AgentDao;
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

    private String list_Url;
    private String view_Url;
    private int refresh_Time;
    private int vyskaOkna;

    @FXML
    private Label label1;

    @FXML
    private JFXButton fetchBicyclesButton;

    @FXML
    private TableView bikeTablePagin;

    @FXML
    private TableColumn bikeCategory, bikeSeries, bikeSize, bikeWmn, bikePrice, bikeDiff, bikeYear;

    @FXML
    private Pagination bikePagin;

    private int current = 1;

    @FXML
    void fetchBicycles() {
        System.out.println("Loading" + Instant.now());
        BicycleDao dao = DaoFactory.INSTANCE.getBicycleDao();

        Consumer<List<Bicycle>> onCompleted = (List<Bicycle> bicycles) -> {
            try {
                System.out.println("Saving" + Instant.now());
                dao.saveMany(bicycles);
                System.out.println("Saved" + Instant.now());
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        };

        List<BicycleCategory> categories = DaoFactory.INSTANCE.getBicycleCategoryDao().getAll();
        List<Bicycle> bicycles = new ArrayList<Bicycle>();
        int total = categories.size();
        for (BicycleCategory category : categories) {
            Supplier<List<Bicycle>> fetch = () -> {
                Fetching fetching = new Fetching(
                        // TODO Implement dynamic loading of the URL and cssSelector
                        "https://www.canyon.com/en-sk/factory-outlet/ajax" + category.getExternalUrl()
                );

                List<Bicycle> list = new ArrayList<Bicycle>();
                try {
                    // TODO: Fix detail URL
                    list.addAll(
                            fetching.fetchItems("div article", Bicycle.fetchMap,
                                    "https://www.canyon.com/en-sk/factory-outlet/category.html#category=fitness-bikes&amp;id=")
                                    .stream().map((Bicycle b) -> {
                                        b.setCategory(category);
                                        return b;
                                    }).collect(Collectors.toList())
                    );
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return list;
            };

            CompletableFuture.supplyAsync(fetch).thenAccept((list) -> {
                bicycles.addAll(list);
                current++;
                if (total == current) {
                    onCompleted.accept(bicycles);
                }
            });
        }
    }

    public void setList_Url(String list_Url) {
        this.list_Url = list_Url;
    }

    public void setView_Url(String view_Url) {
        this.view_Url = view_Url;
    }

    public void setRefresh_Time(int refresh_Time) {
        this.refresh_Time = refresh_Time;
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

        for (int i = page; i < page + itemsPerPage(); i++) {

            bikeCategory.setCellValueFactory(
                    new PropertyValueFactory<Bicycle, String>("category"));

            bikeSeries.setCellValueFactory(
                    new PropertyValueFactory<Bicycle, String>("series"));

            bikeSize.setCellValueFactory(
                    new PropertyValueFactory<Bicycle, String>("size"));

            bikeWmn.setCellValueFactory(
                    new PropertyValueFactory<Bicycle, String>("wmn"));

            bikePrice.setCellValueFactory(
                    new PropertyValueFactory<Bicycle, String>("price"));

            bikeDiff.setCellValueFactory(
                    new PropertyValueFactory<Bicycle, String>("diff"));

            bikeYear.setCellValueFactory(
                    new PropertyValueFactory<Bicycle, String>("modelYear"));

            if (lastIndex == pageIndex) {
                bikeTablePagin.setItems(FXCollections.observableArrayList(bicycles.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + displace)));
            } else {
                bikeTablePagin.setItems(FXCollections.observableArrayList(bicycles.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
            }
            box.getChildren().add(bikeTablePagin);
        }
        return box;
    }
}
