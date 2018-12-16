package me.duras.korman.controllers;

import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import me.duras.korman.BicycleChecking;
import me.duras.korman.DaoFactory;
import me.duras.korman.dao.NotificationDao;
import me.duras.korman.models.*;

public class NotifController implements Initializable {

    private ObservableList<Notification> notifications = FXCollections.observableArrayList();
    private Set<Notification> deleteList = new HashSet<Notification>();
    private List<Notification> sendEmailList = new ArrayList<Notification>();
    private NotificationDao dao = DaoFactory.INSTANCE.getNotificationDao();
    private int rows = 6;
    private double height;

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

    @FXML
    public void sendEmail() {
        BicycleChecking checking = new BicycleChecking();

        Map<Integer, List<Notification>> groups =
            new HashMap<Integer, List<Notification>>();

        for (Notification notif : sendEmailList) {
            if (notif.getAgent().getEmail() == null ||
                notif.getAgent().getEmail().equals("")
            ) {
                continue;
            }

            Integer agentId = notif.getAgent().getId();
            if (!groups.containsKey(agentId)) {
                groups.put(agentId, new ArrayList<Notification>());
            }
            groups.get(agentId).add(notif);
        }

        for (List<Notification> grouped : groups.values()) {
            checking.sendEmail(grouped, grouped.get(1).getAgent().getEmail());
        }

        sendEmailList.clear();
        loadList();
    }

    public void loadList() {
        notifications.clear();
        List<Notification> list = dao.getAll();

        for (Notification notification : list) {
            notifications.add(notification);
        }

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
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (deleteList.size() > 0) {
            deleteNotif.setDisable(false);
        } else {
            deleteNotif.setDisable(true);
        }

        notifTablePagin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        checkBox.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Notification, JFXCheckBox>, ObservableValue<JFXCheckBox>>() {

            @Override
            public ObservableValue<JFXCheckBox> call(
                    TableColumn.CellDataFeatures<Notification, JFXCheckBox> arg0) {
                Notification notification = arg0.getValue();

                JFXCheckBox checkBox = new JFXCheckBox();

                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                        if (checkBox.isSelected()) {
                            sendEmailList.add(notification);
                            deleteList.add(notification);
                            deleteNotif.setDisable(false);
                        } else {
                            sendEmailList.remove(notification);
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

        notifAgent.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Notification, String> arg0) {
                Notification notif = arg0.getValue();

                return new SimpleStringProperty(notif.getAgent().getName());
            }
        });

        notifEmail.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Notification, String> arg0) {
                Notification notif = arg0.getValue();

                return new SimpleStringProperty(notif.getAgent().getEmail());
            }
        });

        notifCategory.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Notification, String> arg0) {
                Notification notif = arg0.getValue();

                return new SimpleStringProperty(notif.getBicycle().getCategory().getName());
            }
        });

        notifSize.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Notification, String> arg0) {
                Notification notif = arg0.getValue();

                return new SimpleStringProperty(notif.getBicycle().getSize());
            }
        });

        notifPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Notification, String> arg0) {
                Notification notif = arg0.getValue();

                return new SimpleStringProperty(format.format(notif.getBicycle().getPrice() / 100));
            }
        });

        notifSentEmail.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Notification, String> arg0) {
                Notification notif = arg0.getValue();

                return new SimpleStringProperty(notif.getEmailSent() ? "Yes" : "No");
            }
        });

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

    public void afterInitialize() {
        Stage stage = (Stage) notifTablePagin.getScene().getWindow();
        height = stage.getHeight();

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {

            double oldValue = ((Number) oldVal).doubleValue();
            double newValue = ((Number) newVal).doubleValue();

            if ((newValue - height) > 100) {
                height = newValue;
                rows = (int) height / 70;
                loadList();
                System.out.println("Tu Som");

            } else if ((height - newValue) > 80) {
                height = newValue;
                rows = (int) height / 100;
                loadList();

            }
        });
        loadList();
    }
}
