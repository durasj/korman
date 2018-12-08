package me.duras.korman;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.duras.korman.dao.BicycleDao;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;

public class BicycleFetching {
    private int current = 1;

    public void fetchAll() {
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
                        "https://www.canyon.com/en-sk/factory-outlet/ajax" + category.getExternalUrl());

                List<Bicycle> list = new ArrayList<Bicycle>();
                try {
                    // TODO: Fix detail URL
                    list.addAll(fetching.fetchItems("div article", Bicycle.fetchMap,
                            "https://www.canyon.com/en-sk/factory-outlet/category.html#category=fitness-bikes&amp;id=")
                            .stream().map((Bicycle b) -> {
                                b.setCategory(category);
                                return b;
                            }).collect(Collectors.toList()));
                } catch (IOException e) {
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
}
