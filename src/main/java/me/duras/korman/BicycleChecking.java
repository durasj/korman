package me.duras.korman;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.nodes.Element;

import me.duras.korman.FetchingMap;
import me.duras.korman.dao.ArchivedBicycleDao;
import me.duras.korman.dao.BicycleDao;
import me.duras.korman.dao.LogDao;
import me.duras.korman.dao.NotificationDao;
import me.duras.korman.dao.SettingDao;
import me.duras.korman.models.Agent;
import me.duras.korman.models.ArchivedBicycle;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;
import me.duras.korman.models.Log;
import me.duras.korman.models.Notification;

public class BicycleChecking {
    static private boolean inProgress = false;
    static private Timer timer = new Timer();

    private String listUrl, viewUrl;
    private int categoryProgress = 1;
    private LogDao logDao = DaoFactory.INSTANCE.getLogDao();
    private NotificationDao notificationDao = DaoFactory.INSTANCE.getNotificationDao();
    private BicycleDao dao = DaoFactory.INSTANCE.getBicycleDao();

    public BicycleChecking() {}

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

        ArchivedBicycleDao archivedDao = DaoFactory.INSTANCE.getArchivedBicycleDao();
        SettingDao settingDao = DaoFactory.INSTANCE.getSettingDao();

        listUrl = settingDao.getByKey("listUrl").getValue();
        viewUrl = settingDao.getByKey("viewUrl").getValue();

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
                    dao.deleteManyByExternalId(
                        archivedBicycles.stream()
                            .map((b) -> b.getExternalId())
                            .collect(Collectors.toList())
                    );

                    logDao.save(new Log(
                        "Archived " + archivedBicycles.size() + " from " + current.size() + " saved bicycles for category " + categoryName)
                    );

                    // Check if those new bikes fit the criteria
                    if (newBicycles.size() > 0) {
                        checkAllAgentsForBicycles(newBicycles);
                    }

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
        List<Agent> agents = DaoFactory.INSTANCE.getAgentDao().getAll();

        agents.stream()
            .forEach((agent) -> {
                List<Notification> notifications = bicycles.stream()
                    .filter((bicycle) -> {
                        if (agent.getCategory().getId() != bicycle.getCategory().getId()) {
                            return false;
                        }

                        if (agent.getSeries() != null && !agent.getSeries().equals("") &&
                            (bicycle.getSeries().toLowerCase().contains(agent.getSeries().toLowerCase()))) {
                            return false;
                        }

                        if (!bicycle.getSize().equals(agent.getSize())) {
                            return false;
                        }

                        if (bicycle.isWmn() != agent.getWmn()) {
                            return false;
                        }

                        int price = bicycle.getPrice() / 100;
                        if (price > agent.getMaxPrice() || price < agent.getMinPrice()) {
                            return false;
                        }

                        if ((bicycle.getDiff() / 100) < agent.getMinDiff()) {
                            return false;
                        }

                        if (bicycle.getModelYear() != agent.getModelYear()) {
                            return false;
                        }

                        return true;
                    })
                    .map((bicycle) -> new Notification(
                        agent,
                        dao.getByExternalId(bicycle.getExternalId()),
                        new Date(),
                        false
                    ))
                    .collect(Collectors.toList());

                notificationDao.saveMany(notifications);
                String email = agent.getEmail();
                if (email != null && !email.equals("")) {
                    sendEmail(notifications, email);
                }
            });
    }

    public void sendEmail(List<Notification> notifications, String email) {
        // Do sending asynchronously since we rely on external services
        CompletableFuture.runAsync(() -> {
            Emailing emailing = new Emailing();
            if (!emailing.isInitialized()) {
                return;
            }

            logDao.save(new Log("Sending email to " + email));

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
            String emailTitle = notifications.size() + " new matching bikes";

            List<Map<String, String>> bicycles = notifications.stream()
                .map((notification) -> notification.getBicycle())
                .map((bicycle) -> {
                    HashMap<String, String> prepared = new HashMap<String, String>();
                    prepared.put("url", bicycle.getUrl());
                    prepared.put("series", bicycle.getSeries());
                    prepared.put("price", format.format(bicycle.getPrice() / 100));
                    prepared.put("diff", format.format(bicycle.getDiff() / 100));
                    prepared.put("modelYear", String.valueOf(bicycle.getModelYear()));
                    prepared.put("size", bicycle.getSize());
                    return prepared;
                })
                .collect(Collectors.toList());

            Map<String, Object> templateData = new HashMap<String, Object>();
            templateData.put("title", emailTitle);
            templateData.put("bikes", bicycles);

            String emailContent;
            try {
                InputStream templateStream = App.class.getResourceAsStream("email.hbs");
                emailContent = emailing.prepareTemplate(templateStream, templateData);
            } catch (Exception e) {
                logDao.save(new Log("Preparing email to " + email + " failed with: " + e.getMessage()));
                e.printStackTrace(System.err);
                return;
            }

            try {
                emailing.sendEmail(email, emailTitle, emailContent);
                // TODO: Sending emails duplicates records
                for (Notification notification : notifications) {
                    notification.setEmailSent(true);
                    notificationDao.save(notification);
                }
                logDao.save(new Log("Sent email to " + email));
            } catch (Exception e) {
                logDao.save(new Log("Sending email failed to " + email + " with: " + e.getMessage()));
            }
        });
    }
}
