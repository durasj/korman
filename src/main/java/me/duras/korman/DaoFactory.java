package me.duras.korman;

import org.springframework.jdbc.core.JdbcTemplate;

import me.duras.korman.dao.BicycleDao;
import me.duras.korman.dao.SettingDao;
import me.duras.korman.dao.SqliteBicycleDao;
import me.duras.korman.dao.SqliteSettingDao;

public enum DaoFactory {
    INSTANCE;

    private JdbcTemplate jdbcTemplate;

    private SettingDao settingDao;
    private BicycleDao bicycleDao;

    public SettingDao getParticipantDao() {
        if (settingDao == null) {
            settingDao = new SqliteSettingDao(getJdbcTemplate());
        }

        return settingDao;
    }

    public BicycleDao getBicycleDao() {
        if (bicycleDao == null) {
            bicycleDao = new SqliteBicycleDao(getJdbcTemplate());
        }

        return bicycleDao;
    }

    public void setJdbcTemplate(JdbcTemplate template) {
        jdbcTemplate = template;
    }

    private JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
