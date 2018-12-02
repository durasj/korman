package me.duras.korman.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import me.duras.korman.models.Setting;

public class SqliteSettingDao implements SettingDao {
    private JdbcTemplate jdbcTemplate;

    public SqliteSettingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Setting> getAll() {
        String sql = "SELECT id, key, value FROM setting";

        return jdbcTemplate.query(sql, new RowMapper<Setting>() {

            @Override
            public Setting mapRow(ResultSet rs, int rowNum) throws SQLException {
                Setting workShop = new Setting(rs.getInt("id"), rs.getString("key"), rs.getString("value"));
                return workShop;
            }

        });
    }

    @Override
    public Setting getByKey(String key) {
        String sql = "SELECT id, key, value FROM setting WHERE key = ?";
        Object[] args = new Object[] { key };
        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(Setting.class));
    }

    @Override
    public int save(Setting setting) {
        String sql = "UPDATE setting SET value = ? WHERE id = ?";
        return jdbcTemplate.update(sql, setting.getValue(), setting.getId());
    }
}
