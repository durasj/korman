package me.duras.korman.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.duras.korman.DaoFactory;
import me.duras.korman.models.Agent;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;
import me.duras.korman.models.Notification;

public class SqliteNotificationDaoTest extends DaoTestCaseBase {
    private NotificationDao dao;

    private BicycleCategory category = new BicycleCategory(1, "MTB", "", "");
    private Date createdAt = new Date(1544968884000L);
    private Date createdAtAlternative = new Date(1544968874000L);
    private Agent agent = new Agent("Unnamed", "email@email.com", category, "Exceed", "M", false, 1000, 3000, 0, 2018);
    private Bicycle bike = new Bicycle(
        "e123", category, "Exceed", "M", false, 300000, 2018, "https://google.com/q=Exceed", "https://google.com/q=Exceed", createdAt
    );
    private Notification notification = new Notification(agent, bike, createdAt, false);

    @BeforeAll
    @Override
    public void setupDb() {
        super.setupDb();
        DaoFactory.INSTANCE.getAgentDao().save(agent);
        DaoFactory.INSTANCE.getBicycleDao().save(bike);
    }

    @BeforeEach
    public void setupDao() {
        dao = DaoFactory.INSTANCE.getNotificationDao();
        notification.setId(0);
    }

    @Test
    public void testGetAllSaveNew() {
        int oldSize = dao.getAll().size();

        dao.save(notification);
        List<Notification> list = dao.getAll();
        Optional<Notification> sameItem = list.stream()
            .filter((savedItem) -> notificationsSame(notification, savedItem))
            .findFirst();

        assertTrue(oldSize + 1 == list.size());
        assertTrue(sameItem.isPresent());
    }

    @Test
    public void testGetByIdSaveNew() {
        Notification saved = dao.save(notification);
        Notification retrieved = dao.getById(saved.getId());

        assertTrue(notificationsSame(saved, retrieved));
    }

    @Test
    public void testSaveExisting() {
        Notification original = dao.save(notification);
        Notification cloned = dao.getById(original.getId());
        cloned.setAgent(agent);
        cloned.setBicycle(bike);
        cloned.setCreatedAt(createdAtAlternative);
        cloned.setEmailSent(true);

        Notification saved = dao.save(cloned);

        assertTrue(notificationsSame(cloned, saved));
    }

    @Test
    public void testGetNewSaveNew() {
        dao.save(notification);

        Date createdAtNew = new Date(createdAt.getTime() + 1000L);
        List<Notification> firstList = dao.getNew(createdAtNew);

        notification.setId(0);
        notification.setCreatedAt(createdAtNew);
        dao.save(notification);
        List<Notification> secondList = dao.getNew(createdAtNew);

        assertTrue(firstList.size() == 0);
        assertTrue(secondList.size() == 1);
    }

    @Test
    public void testSaveManyGetAll() {
        int oldSize = dao.getAll().size();

        List<Notification> notifications = new ArrayList<Notification>();
        notifications.add(notification);
        notifications.add(notification);
        dao.saveMany(notifications);
        List<Notification> list = dao.getAll();
        Optional<Notification> sameItem = list.stream()
            .filter((savedItem) -> notificationsSame(notification, savedItem))
            .findFirst();

        assertTrue(oldSize + 1 == list.size());
        assertTrue(sameItem.isPresent());
    }

    @Test
    public void testDeleting() {
        int listSize = dao.getAll().size();

        dao.save(notification);
        Notification cloned = dao.getById(notification.getId());
        cloned.setId(0);
        dao.save(cloned);

        dao.delete(notification);
        dao.delete(cloned.getId());

        int newListSize = dao.getAll().size();
        assertTrue(listSize == newListSize);
    }

    private boolean notificationsSame(Notification first, Notification second) {
        if (first.getAgent().getId() != second.getAgent().getId()) return false;
        if (first.getBicycle().getId() != second.getBicycle().getId()) return false;
        if (!first.getCreatedAt().equals(second.getCreatedAt())) return false;
        if (first.getEmailSent() != second.getEmailSent()) return false;

        return true;
    }
}
