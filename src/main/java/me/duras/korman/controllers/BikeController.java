package me.duras.korman.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import me.duras.korman.*;
import me.duras.korman.dao.ArchivedBicycleDao;
import me.duras.korman.dao.BicycleCategoryDao;
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
    private BicycleCategoryDao categoryDao = DaoFactory.INSTANCE.getBicycleCategoryDao();
    private int rows = 6;
    private double height;
    private boolean archived;

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
    private JFXTextField searchBike;

    private Set<String> possibleCategories;
    private Set<String> possibleSizes;
    private Pattern priceRangePattern = Pattern.compile("(\\d+)-(\\d+)");
    private Pattern modelYearPattern = Pattern.compile("2\\d{3}");

    private final int FILTER_CATEGORY = 1;
    private final int FILTER_SIZE = 2;
    private final int FILTER_GENDER = 3;
    private final int FILTER_RANGE = 4;
    private final int FILTER_MODEL_YEAR = 5;
    private final int FILTER_SERIES = 6;

    @FXML
    void fetchBicycles() {
        (new BicycleChecking()).fetchAll();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        possibleCategories = categoryDao.getAll().stream()
                .map((category) -> category.getName().toLowerCase())
                .collect(Collectors.toSet());

        possibleSizes = Arrays.stream(Bicycle.sizes)
                .map((size) -> size.toLowerCase())
                .collect(Collectors.toSet());

        searchBike.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                loadList(archivedButton.isSelected());
            }
        });

        bikeTablePagin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        archivedButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (!archivedButton.isSelected()) {
                    bikeArch.setVisible(false);
                    archived = false;
                    loadList(archived);
                } else {
                    bikeArch.setVisible(true);
                    archived = true;
                    loadList(archived);
                }
            }
        });

        bikeTablePagin.setOnMouseClicked((MouseEvent event) -> {
            showBike();
        });
    }

    private void loadList(boolean isArchived) {
        bicycles.clear();

        String search = searchBike.getText();

        if (!isArchived) {
            List<Bicycle> list = dao.getAll();
            if (!search.equals("")) {
                list = filterBikes(list, search);
            }
            bicycles.addAll(list);
        } else {
            List<ArchivedBicycle> list = archivedDao.getAll();
            if (!search.equals("")) {
                list = filterBikes(list, search);
            }
            bicycles.addAll(list);
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
        return rows;
    }

    public VBox createPage(int pageIndex) {

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);

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

        VBox box = new VBox();
        int page = pageIndex * itemsPerPage();

        bikeCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bicycle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Bicycle, String> arg0) {
                Bicycle bike = arg0.getValue();

                return new SimpleStringProperty(bike.getCategory().getName());
            }
        });

        bikeWmn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bicycle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Bicycle, String> arg0) {
                Bicycle bike = arg0.getValue();

                return new SimpleStringProperty(bike.isWmn() ? "Yes" : "No");
            }
        });

        bikePrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bicycle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Bicycle, String> arg0) {
                Bicycle bike = arg0.getValue();

                return new SimpleStringProperty(format.format(bike.getPrice() / 100));
            }
        });

        bikeDiff.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bicycle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Bicycle, String> arg0) {
                Bicycle bike = arg0.getValue();

                return new SimpleStringProperty(format.format(bike.getDiff() / 100));
            }
        });

        bikeSeries.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("series"));
        bikeSize.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("size"));
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

    public void onSearch(ActionEvent event) {
        System.out.println("test");
    }

    public void afterInitialize() {
        Stage stage = (Stage) bikeTablePagin.getScene().getWindow();
        height = stage.getHeight();

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {

            double oldValue = ((Number) oldVal).doubleValue();
            double newValue = ((Number) newVal).doubleValue();
            System.out.println("archivovane " + archived);
            if ((newValue - height) > 100) {
                height = newValue;
                rows = (int) height / 70;
                loadList(archived);
                System.out.println("+ " + archived);
            } else if ((height - newValue) > 80) {
                height = newValue;
                rows = (int) height / 100;
                loadList(archived);
                System.out.println("-" + archived);
            }
        });
        loadList(archived);
    }

    private <T extends Bicycle> List<T> filterBikes(List<T> bicycles, String search) {
        // Process conditions
        List<Object[]> conditions = Arrays.stream(search.split(","))
                .map((token) -> {
                    token = token.trim().toLowerCase();
                    // Check for one the categories
                    if (possibleCategories.contains(token)) {
                        Object[] condition = {FILTER_CATEGORY, token};
                        return condition;
                    }

                    // Check for Size
                    if (possibleSizes.contains(token)) {
                        Object[] condition = {FILTER_SIZE, token};
                        return condition;
                    }

                    // Check for woman/man
                    if (token.equals("man") || token.equals("woman")) {
                        Object[] condition = {FILTER_GENDER, token};
                        return condition;
                    }

                    // Check for price range
                    Matcher rangeMatcher = priceRangePattern.matcher(token);
                    if (rangeMatcher.find()) {
                        Object[] condition = {FILTER_RANGE, rangeMatcher.group(1), rangeMatcher.group(2)};
                        return condition;
                    }

                    // Check for model year
                    Matcher yearMatcher = modelYearPattern.matcher(token);
                    if (yearMatcher.find()) {
                        Object[] condition = {FILTER_MODEL_YEAR, token};
                        return condition;
                    }

                    // Else it's series
                    Object[] condition = {FILTER_SERIES, token};
                    return condition;
                })
                .collect(Collectors.toList());

        // Filter bicycles
        return bicycles.stream()
                .filter((bicycle) -> {
                    for (Object[] condition : conditions) {
                        switch ((int) condition[0]) {
                            case FILTER_CATEGORY:
                                if (!bicycle.getCategory().getName().toLowerCase().equals(condition[1])) {
                                    return false;
                                }
                                break;

                            case FILTER_SIZE:
                                if (!bicycle.getSize().toLowerCase().equals(condition[1])) {
                                    return false;
                                }
                                break;

                            case FILTER_GENDER:
                                if (!(bicycle.isWmn() ? "woman" : "man").equals(condition[1])) {
                                    return false;
                                }
                                break;

                            case FILTER_RANGE:
                                int price = Math.round(bicycle.getPrice() / 100);
                                int minPrice = Integer.parseInt((String) condition[1]);
                                int maxPrice = Integer.parseInt((String) condition[2]);
                                if (price < minPrice || price > maxPrice) {
                                    return false;
                                }
                                break;

                            case FILTER_MODEL_YEAR:
                                if (!String.valueOf(bicycle.getModelYear()).equals(condition[1])) {
                                    return false;
                                }
                                break;

                            case FILTER_SERIES:
                                String series = bicycle.getSeries();
                                if (series != null && !series.toLowerCase().contains((String) condition[1])) {
                                    return false;
                                }
                                break;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }
}
