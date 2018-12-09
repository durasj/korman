package me.duras.korman.controllers;

import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import me.duras.korman.models.Bicycle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import me.duras.korman.DaoFactory;
import me.duras.korman.dao.BicycleDao;
import me.duras.korman.dao.NotificationDao;
import me.duras.korman.models.*;

public class NotifController  {

    private ObservableList<Notification> notifications = FXCollections.observableArrayList();
    private Set<Integer> deleteList = new HashSet<Integer>();
    private NotificationDao dao = DaoFactory.INSTANCE.getNotificationDao();

    @FXML
    private JFXButton sendEmailNotif, deleteNotif;

    @FXML
    private TableView<Notification> notifTablePagin;

    @FXML
    private TableColumn checkBox, notifAgent, notifEmail, notifCategory, notifSize, notifPrice, notifSentEmail;

    @FXML
    private Pagination notifPagin;

    @FXML
    public void deleteNotif() {
     /*   for (int notif : deleteList) {
            dao.delete(notif);
        }
        deleteList.clear();
        deleteNotif.setDisable(true);
        loadList();*/
    }

    public void loadList() {
      /*  notifications.clear();
        List<Notification> list = dao.getAll();

        for (Notification notification : list) {
            notifications.add(notification);
        }*/
    }

    @FXML
    private void sendEmail() {
    }
/*
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (deleteList.size() > 0) {
            deleteNotif.setDisable(false);
        } else {
            deleteNotif.setDisable(true);
        }
        checkBox.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Notification, JFXCheckBox>, ObservableValue<JFXCheckBox>>() {

            @Override
            public ObservableValue<JFXCheckBox> call(
                    TableColumn.CellDataFeatures<Notification, JFXCheckBox> arg0) {
                Notification notification = arg0.getValue();

                JFXCheckBox checkBox = new JFXCheckBox();

                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        if (checkBox.isSelected()) {
                            deleteList.add(notification.getId());
                            deleteNotif.setDisable(false);
                        } else {
                            deleteList.remove(notification.getId());
                            if (deleteList.size() == 0) {
                                deleteNotif.setDisable(true);
                            }
                        }

                    }
                });
                return new SimpleObjectProperty<JFXCheckBox>(checkBox);
            }
        });

        loadList();

        notifTablePagin.setPageCount(notifications.size() / rowsPerPage() + 1);
        notifTablePagin.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex > notifications.size() / rowsPerPage() + 1) {
                    return null;
                } else {
                    return createPage(pageIndex);
                }
            }
        });

        notifTablePagin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
    }*/
}
