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

public class SqliteBicycleCategoryDaoTest extends DaoTestCaseBase {
    private BicycleCategoryDao dao;

    @BeforeEach
    public void setupDao() {
        dao = DaoFactory.INSTANCE.getBicycleCategoryDao();
    }

    @Test
    public void testGetAll() {
        List<BicycleCategory> list = dao.getAll();
        Optional<BicycleCategory> mtbCategory = list.stream()
            .filter((savedItem) -> savedItem.getName().equals("MTB"))
            .findFirst();

        assertTrue(list.size() >= 5);
        assertTrue(mtbCategory.isPresent());
    }

    @Test
    public void testGetByIdSaveNew() {
        BicycleCategory retrieved = dao.getById(1);

        assertTrue(retrieved.getName().equals("MTB"));
    }
}
