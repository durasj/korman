package me.duras.korman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.nodes.Element;

import me.duras.korman.FetchingMap;
import me.duras.korman.dao.ArchivedBicycleDao;
import me.duras.korman.dao.BicycleDao;
import me.duras.korman.dao.LogDao;
import me.duras.korman.dao.SettingDao;
import me.duras.korman.models.ArchivedBicycle;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;
import me.duras.korman.models.Log;

public class BicycleChecking {
    static private boolean inProgress = false;
    static private Timer timer = new Timer();

    private String listUrl, viewUrl;

    private int categoryProgress = 1;

    public BicycleChecking() {
        SettingDao settingDao = DaoFactory.INSTANCE.getSettingDao();

        listUrl = settingDao.getByKey("listUrl").getValue();
        viewUrl = settingDao.getByKey("viewUrl").getValue();
    }

    private static final FetchingMap<Element, Bicycle> fetchMap = (Element el, String detailUrl) -> {
        String externalId = el.attr("data-id");
        String series = el.attr("data-series");
        String size = el.attr("data-size").replaceAll("\\|", "");
        boolean wmn = el.attr("data-wmn") == "1";
        int price = Integer.parseInt(el.attr("data-price"));
        int year = Integer.parseInt(el.attr("data-year"));
        int diff = Integer.parseInt(el.attr("data-diff"));
        String url = detailUrl + externalId;
        String photoUrl = null;
        try {
            photoUrl = el.selectFirst("img").attr("data-srcset").split(" ")[0];
        } catch (Exception e) {
            // Silently ignore, photoUrl is not neccessary
        }

        // Will be filled after the fetching
        BicycleCategory category = null;

        Date created = null;
        try {
            created = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(el.attr("data-date"));
        } catch (ParseException e) {
            System.err.println("Incorrect bicycle date");
        }

        Bicycle bike = new Bicycle(externalId, category, series, size, wmn, price, year, url, photoUrl, created);
        bike.setDiff(diff);

        return bike;
    };

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
        ArchivedBicycleDao archivedDao = DaoFactory.INSTANCE.getArchivedBicycleDao();
        LogDao logDao = DaoFactory.INSTANCE.getLogDao();

        logDao.save(new Log("Fetching bicycles"));
        List<BicycleCategory> categories = DaoFactory.INSTANCE.getBicycleCategoryDao().getAll();
        int total = categories.size();
        for (BicycleCategory category : categories) {
            List<Bicycle> currentList = dao.getAll().stream()
                .filter((Bicycle b) -> b.getCategory().getId() == category.getId())
                .collect(Collectors.toList());

            Set<String> current = currentList.stream()
                .map((Bicycle b) -> b.getExternalId())
                .collect(Collectors.toSet());

            Supplier<List<Bicycle>> fetch = () -> {
                Fetching fetching = new Fetching(
                        listUrl + category.getExternalUrl());

                List<Bicycle> list = new ArrayList<Bicycle>();
                try {
                    list.addAll(fetching.fetchItems("div article", BicycleChecking.fetchMap, viewUrl)
                        .stream().map((Bicycle b) -> {
                            b.setCategory(category);
                            return b;
                        }).collect(Collectors.toList()));
                } catch (IOException e) {
                    logDao.save(new Log(
                        "Fetching error: " + e.getMessage())
                    );
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

                    Set<String> remoteSet = list.stream()
                        .map((Bicycle b) -> b.getExternalId())
                        .collect(Collectors.toSet());

                    // Archive those that we have and were no longer remotely
                    List<ArchivedBicycle> archivedBicycles = currentList.stream()
                        .filter((b) -> !remoteSet.contains(b.getExternalId()))
                        .map((b) -> ArchivedBicycle.fromBicycle(b))
                        .collect(Collectors.toList());

                    archivedDao.saveMany(archivedBicycles);
                    int affected = dao.deleteManyByExternalId(
                        archivedBicycles.stream()
                            .map((b) -> b.getExternalId())
                            .collect(Collectors.toList())
                    );
                    System.out.println(affected);
                    System.out.println(archivedBicycles.stream()
                    .map((b) -> b.getExternalId())
                    .collect(Collectors.toList()));

                    logDao.save(new Log(
                        "Archived " + archivedBicycles.size() + " from " + current.size() + " saved bicycles for category " + categoryName)
                    );
                    

                    // Check if those new bikes fit the criteria


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
                    logDao.save(new Log(
                        "Checking error: " + e.getMessage())
                    );
                }
            });
        }
    }

    static public void startTimer(int refreshTime) {
        BicycleChecking checking = new BicycleChecking();

        timer.cancel();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                checking.fetchAll();
            }
        };
        timer.schedule(task, 0L, refreshTime * 60000);
    }

    static public void stopTimer() {
        timer.cancel();
        timer = new Timer();
    }

    public void checkAllAgentsForBicycles(List<Bicycle> bicycles) {

    }

    public void checkBicyclesForAgent() {
        
    }
}
