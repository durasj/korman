package me.duras.korman.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.duras.korman.DaoFactory;
import me.duras.korman.models.Setting;

public class SqliteSettingDaoTest extends DaoTestCaseBase {
    private SettingDao dao;

    private Setting setting = new Setting(1, "listUrl", "https://www.canyon.com/en-sk/factory-outlet/ajax");

    @BeforeEach
    public void setupDao() {
        dao = DaoFactory.INSTANCE.getSettingDao();
        setting.setValue("https://www.canyon.com/en-sk/factory-outlet/ajax");
    }

    @Test
    public void testGetAll() {
        List<Setting> list = dao.getAll();
        Optional<Setting> viewUrl = list.stream()
            .filter((savedItem) -> savedItem.getKey().equals("viewUrl"))
            .findFirst();

        assertTrue(list.size() >= 8);
        assertTrue(viewUrl.isPresent());
    }

    @Test
    public void testGetByKey() {
        Setting saved = dao.getByKey("listUrl");
        assertTrue(settingsSame(setting, saved));
    }

    @Test
    public void testSave() {
        setting.setValue("https://www.canyon.com/en-enk/factory-outlet/ajax");
        dao.save(setting);

        Setting saved = dao.getByKey("listUrl");
        assertTrue(settingsSame(saved, setting));
    }

    private boolean settingsSame(Setting first, Setting second) {
        if (!first.getKey().equals(second.getKey())) return false;
        if (!first.getValue().equals(second.getValue())) return false;

        return true;
    }
}
