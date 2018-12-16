package me.duras.korman.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.duras.korman.DaoFactory;
import me.duras.korman.models.ArchivedBicycle;
import me.duras.korman.models.BicycleCategory;

public class SqliteArchivedBicycleDaoTest extends DaoTestCaseBase {
    private ArchivedBicycleDao dao;

    private BicycleCategory category = new BicycleCategory(1, "MTB", "", "");
    private Date createdAt = new Date(1544968884000L);
    private Date importedAt = new Date(1544968885000L);
    private Date archivedAt = new Date(1544968886000L);
    private ArchivedBicycle bike = new ArchivedBicycle(
        "e123", category, "Exceed", "M", false, 300000, 2018, "https://google.com/q=Exceed", "https://google.com/q=Exceed", createdAt
    );

    @BeforeEach
    public void setupDao() {
        dao = DaoFactory.INSTANCE.getArchivedBicycleDao();
        bike.setId(0);
        bike.setImportedAt(importedAt);
        bike.setArchivedAt(archivedAt);
    }

    @Test
    public void testGetAllSaveManyNew() {
        int oldSize = dao.getAll().size();

        List<ArchivedBicycle> bikes = new ArrayList<ArchivedBicycle>();
        bikes.add(bike);
        dao.saveMany(bikes);
        List<ArchivedBicycle> list = dao.getAll();
        Optional<ArchivedBicycle> sameItem = list.stream()
            .filter((savedItem) -> bikesSame(bike, savedItem))
            .findFirst();

        assertTrue(oldSize + 1 == list.size());
        assertTrue(sameItem.isPresent());
    }

    @Test
    public void testGetByIdSaveNew() {
        List<ArchivedBicycle> bikes = new ArrayList<ArchivedBicycle>();
        bikes.add(bike);
        dao.saveMany(bikes);
        ArchivedBicycle retrieved = dao.getById(1);

        assertTrue(bikesSame(bike, retrieved));
    }

    private boolean bikesSame(ArchivedBicycle first, ArchivedBicycle second) {
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

        if (!first.getArchivedAt().equals(second.getArchivedAt())) return false;

        return true;
    }
}
