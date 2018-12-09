package me.duras.korman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.duras.korman.dao.BicycleDao;
import me.duras.korman.dao.LogDao;
import me.duras.korman.dao.SettingDao;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;
import me.duras.korman.models.Log;

public class BicycleChecking {
    static private boolean inProgress = false;

    private String listUrl, viewUrl;
    private int refreshTime;

    private int categoryProgress = 1;

    public BicycleChecking() {
        SettingDao settingDao = DaoFactory.INSTANCE.getSettingDao();

        listUrl = settingDao.getByKey("listUrl").getValue();
        viewUrl = settingDao.getByKey("viewUrl").getValue();
        refreshTime = (int) Double.parseDouble(settingDao.getByKey("refreshTime").getValue());
    }

    static public boolean isInProgress() {
        return BicycleChecking.inProgress;
    }

    public void fetchAll() {
        // Lock fetching, only one can be done at time
        if (BicycleChecking.inProgress) {
            return;
        }
        BicycleChecking.inProgress = true;

        BicycleDao dao = DaoFactory.INSTANCE.getBicycleDao();
        LogDao logDao = DaoFactory.INSTANCE.getLogDao();

        Set<String> current = dao.getAll().stream()
            .map((Bicycle b) -> b.getExternalId())
            .collect(Collectors.toSet());

        logDao.save(new Log("Fetching bicycles"));
        List<BicycleCategory> categories = DaoFactory.INSTANCE.getBicycleCategoryDao().getAll();
        int total = categories.size();
        for (BicycleCategory category : categories) {
            Supplier<List<Bicycle>> fetch = () -> {
                Fetching fetching = new Fetching(
                        listUrl + category.getExternalUrl());

                List<Bicycle> list = new ArrayList<Bicycle>();
                try {
                    list.addAll(fetching.fetchItems("div article", Bicycle.fetchMap, viewUrl)
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
                try {
                    String categoryName = category.getName();

                    logDao.save(new Log("Saving fetched bicycles for category " + categoryName));
                    // Remove from saving those that we already have
                    List<Bicycle> newBicycles = list.stream()
                        .filter((b) -> !current.contains(b.getExternalId()))
                        .collect(Collectors.toList());
    
                    dao.saveMany(newBicycles);

                    logDao.save(new Log(
                        "Saved " + newBicycles.size() + " new from " + list.size() + " fetched bicycles for category " + categoryName)
                    );
    
                    if (total == categoryProgress) {
                        categoryProgress = 1;
                        BicycleChecking.inProgress = false;
                        logDao.save(new Log(
                            "Finished saving bicycles for all categories")
                        );
                    } else {
                        categoryProgress++;
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            });
        }
    }
}
