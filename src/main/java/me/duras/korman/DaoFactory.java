package me.duras.korman;

import org.springframework.jdbc.core.JdbcTemplate;

import me.duras.korman.dao.AgentDao;
import me.duras.korman.dao.BicycleCategoryDao;
import me.duras.korman.dao.BicycleDao;
import me.duras.korman.dao.LogDao;
import me.duras.korman.dao.SettingDao;
import me.duras.korman.dao.SqliteAgentDao;
import me.duras.korman.dao.SqliteBicycleCategoryDao;
import me.duras.korman.dao.SqliteBicycleDao;
import me.duras.korman.dao.SqliteLogDao;
import me.duras.korman.dao.SqliteSettingDao;

public enum DaoFactory {
    INSTANCE;

    private JdbcTemplate jdbcTemplate;

    private AgentDao agentDao;
    private SettingDao settingDao;
    private BicycleDao bicycleDao;
    private BicycleCategoryDao bicycleCategoryDao;
    private LogDao logDao;

    public AgentDao getAgentDao() {
        if (agentDao == null) {
            agentDao = new SqliteAgentDao(getJdbcTemplate());
        }

        return agentDao;
    }

    public SettingDao getSettingDao() {
        if (settingDao == null) {
            settingDao = new SqliteSettingDao(getJdbcTemplate());
        }

        return settingDao;
    }

    public LogDao getLogDao() {
        if (logDao == null) {
            logDao = new SqliteLogDao(getJdbcTemplate());
        }

        return logDao;
    }

    public BicycleDao getBicycleDao() {
        if (bicycleDao == null) {
            bicycleDao = new SqliteBicycleDao(getJdbcTemplate());
        }

        return bicycleDao;
    }

    public BicycleCategoryDao getBicycleCategoryDao() {
        if (bicycleCategoryDao == null) {
            bicycleCategoryDao = new SqliteBicycleCategoryDao(getJdbcTemplate());
        }

        return bicycleCategoryDao;
    }

    public void setJdbcTemplate(JdbcTemplate template) {
        jdbcTemplate = template;
    }

    private JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
