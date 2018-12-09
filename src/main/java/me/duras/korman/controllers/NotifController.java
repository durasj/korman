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

public class NotifController implements Initializable {

    private ObservableList<Notification> notifications = FXCollections.observableArrayList();
    private Set<Notification> deleteList = new HashSet<Notification>();
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
           for (Notification notif : deleteList) {
            dao.delete(notif);
        }
        deleteList.clear();
        deleteNotif.setDisable(true);
        loadList();
    }

    public void loadList() {
        notifications.clear();
        List<Notification> list = dao.getAll();

        for (Notification notification : list) {
            notifications.add(notification);
        }
    }

    @FXML
    public void sendEmail() {
    }

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
                            deleteList.add(notification);
                            deleteNotif.setDisable(false);
                        } else {
                            deleteList.remove(notification);
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

        notifPagin.setPageCount(notifications.size() / rowsPerPage() + 1);
        notifPagin.setPageFactory(new Callback<Integer, Node>() {
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

        int displace = notifications.size() % rowsPerPage();

        if (notifications.size() != 0) {
            if (displace > 0) {
                lastIndex = notifications.size() / rowsPerPage();
            } else {
                lastIndex = notifications.size() / rowsPerPage() - 1;
            }
        } else {
            lastIndex = 0;
        }

        VBox box = new VBox();
        int page = pageIndex * itemsPerPage();

        notifAgent.setCellValueFactory(new PropertyValueFactory<Notification, String>("agent"));
        notifEmail.setCellValueFactory(new PropertyValueFactory<Notification, String>("bicycle"));
        notifCategory.setCellValueFactory(new PropertyValueFactory<Notification, String>("bicycle"));
        notifSize.setCellValueFactory(new PropertyValueFactory<Notification, String>("bicycle"));
        notifPrice.setCellValueFactory(new PropertyValueFactory<Notification, String>("bicycle"));
        notifSentEmail.setCellValueFactory(new PropertyValueFactory<Notification, String>("emailSent"));

        for (int i = page; i < page + itemsPerPage(); i++) {
            if (lastIndex == pageIndex) {
                notifTablePagin.setItems(FXCollections.observableArrayList(
                        notifications.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + displace)));
            } else {
                notifTablePagin.setItems(FXCollections.observableArrayList(
                        notifications.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
            }
            box.getChildren().add(notifTablePagin);
        }
        return box;
    }
}
