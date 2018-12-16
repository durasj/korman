package me.duras.korman.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.duras.korman.DaoFactory;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;

public class SqliteBicycleDaoTest extends DaoTestCaseBase {
    private BicycleDao dao;

    private BicycleCategory category = new BicycleCategory(1, "MTB", "", "");
    private BicycleCategory categoryRoad = new BicycleCategory(2, "Road", "", "");
    private Date createdAt = new Date(1544968884000L);
    private Date importedAt = new Date(1544968885000L);
    private Date createdAtAlternative = new Date(1544968894000L);
    private Date importedAtAlternative = new Date(1544968895000L);
    private Bicycle bike = new Bicycle(
        "e123", category, "Exceed", "M", false, 300000, 2018, "https://google.com/q=Exceed", "https://google.com/q=Exceed", createdAt
    );

    @BeforeEach
    public void setupDao() {
        dao = DaoFactory.INSTANCE.getBicycleDao();
        bike.setId(0);
        bike.setExternalId("e123");
        bike.setImportedAt(importedAt);
    }

    @Test
    public void testGetAllSaveNew() {
        int oldSize = dao.getAll().size();

        dao.save(bike);
        List<Bicycle> list = dao.getAll();
        Optional<Bicycle> sameItem = list.stream()
            .filter((savedItem) -> bikesSame(bike, savedItem))
            .findFirst();

        assertTrue(oldSize + 1 == list.size());
        assertTrue(sameItem.isPresent());
    }

    @Test
    public void testGetByIdSaveNew() {
        bike.setExternalId("e987");
        Bicycle saved = dao.save(bike);
        Bicycle retrieved = dao.getById(saved.getId());

        assertTrue(bikesSame(saved, retrieved));
    }

    @Test
    public void testGetByExternalIdSaveNew() {
        bike.setExternalId("e159");
        Bicycle saved = dao.save(bike);
        Bicycle retrieved = dao.getByExternalId(bike.getExternalId());

        assertTrue(bikesSame(saved, retrieved));
    }

    @Test
    public void testSaveManyGetAll() {
        int oldSize = dao.getAll().size();

        List<Bicycle> bikes = new ArrayList<Bicycle>();
        bike.setExternalId("654");
        bikes.add(bike);
        bike.setExternalId("e459");
        bikes.add(bike);
        dao.saveMany(bikes);
        List<Bicycle> list = dao.getAll();
        Optional<Bicycle> sameItem = list.stream()
            .filter((savedItem) -> bikesSame(bike, savedItem))
            .findFirst();

        assertTrue(oldSize + 2 == list.size());
        assertTrue(sameItem.isPresent());
    }

    @Test
    public void testDeleting() {
        int listSize = dao.getAll().size();

        bike.setExternalId("e999");
        dao.save(bike);
        Bicycle cloned = dao.getById(bike.getId());
        cloned.setId(0);
        cloned.setExternalId("e555");
        dao.save(cloned);

        dao.delete(bike);
        List<String> list = new ArrayList<String>();
        list.add(cloned.getExternalId());
        dao.deleteManyByExternalId(list);

        int newListSize = dao.getAll().size();
        assertTrue(listSize == newListSize);
    }

    private boolean bikesSame(Bicycle first, Bicycle second) {
        if (!first.getExternalId().equals(second.getExternalId())) return false;
        if (first.getCategory().getId() != second.getCategory().getId()) return false;
        if (!first.getSeries().equals(second.getSeries())) return false;
        if (!first.getSize().equals(second.getSize())) return false;
        if (first.isWmn() != second.isWmn()) return false;
        if (first.getPrice() != second.getPrice()) return false;
        if (first.getDiff() != second.getDiff()) return false;
        if (first.getModelYear() != second.getModelYear()) return false;
        if (!first.getUrl().equals(second.getUrl())) return false;
        if (!first.getPhotoUrl().equals(second.getPhotoUrl())) return false;
        if (!first.getCreatedAt().equals(second.getCreatedAt())) return false;
        if (!first.getImportedAt().equals(second.getImportedAt())) return false;

        return true;
    }
}
