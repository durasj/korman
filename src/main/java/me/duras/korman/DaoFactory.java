package me.duras.korman;

import org.springframework.jdbc.core.JdbcTemplate;

import me.duras.korman.dao.SettingDao;
import me.duras.korman.dao.SqliteSettingDao;

public enum DaoFactory {
    INSTANCE;

    private JdbcTemplate jdbcTemplate;
    private SettingDao settingDao;

    public SettingDao getParticipantDao() {
        if (settingDao == null) {
            settingDao = new SqliteSettingDao(getJdbcTemplate());
        }

        return settingDao;
    }

    public void setJdbcTemplate(JdbcTemplate template) {
        jdbcTemplate = template;
    }

    private JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
