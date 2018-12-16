package me.duras.korman.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.duras.korman.DaoFactory;
import me.duras.korman.models.Log;

public class SqliteLogDaoTest extends DaoTestCaseBase {
    private LogDao dao;

    private Date createdAt = new Date(1544968884000L);
    private Log log = new Log("Message");

    @BeforeEach
    public void setupDao() {
        dao = DaoFactory.INSTANCE.getLogDao();
        log.setId(0);
        log.setCreatedAt(createdAt);
    }

    @Test
    public void testGetAllSaveNew() {
        int oldSize = dao.getAll().size();

        dao.save(log);
        List<Log> list = dao.getAll();
        Optional<Log> sameItem = list.stream()
            .filter((savedItem) -> logsSame(log, savedItem))
            .findFirst();

        assertTrue(oldSize + 1 == list.size());
        assertTrue(sameItem.isPresent());
    }

    @Test
    public void testGetNewSaveNew() {
        dao.save(log);

        Date createdAtNew = new Date(createdAt.getTime() + 1000L);
        List<Log> firstList = dao.getNew(createdAtNew);

        log.setId(0);
        log.setCreatedAt(createdAtNew);
        dao.save(log);
        List<Log> secondList = dao.getNew(createdAtNew);

        assertTrue(firstList.size() == 0);
        assertTrue(secondList.size() == 1);
    }

    @Test
    public void testClearing() {
        dao.save(log);
        dao.clear();

        int newListSize = dao.getAll().size();
        assertTrue(newListSize == 0);
    }

    private boolean logsSame(Log first, Log second) {
        if (!first.getContent().equals(second.getContent())) return false;
        if (!first.getCreatedAt().equals(second.getCreatedAt())) return false;

        return true;
    }
}
