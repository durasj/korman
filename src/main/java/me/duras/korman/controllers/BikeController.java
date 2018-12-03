package me.duras.korman.controllers;

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

import javafx.fxml.Initializable;
import javafx.scene.control.*;

import me.duras.korman.*;
import me.duras.korman.dao.BicycleDao;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;

/**
 * Bike Controller
 */
public class BikeController implements Initializable {

    @FXML
    private Label label1;

    @FXML
    private Button fetchBicyclesButton;

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
                            "https://www.canyon.com/en-sk/factory-outlet/category.html#category=fitness-bikes&id=")
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
