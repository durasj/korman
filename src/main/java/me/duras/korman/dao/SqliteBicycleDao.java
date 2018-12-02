package me.duras.korman.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import me.duras.korman.models.Bicycle;

public class SqliteBicycleDao implements BicycleDao {
    private JdbcTemplate jdbcTemplate;

    public SqliteBicycleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Bicycle> getAll() {
        String sql = "SELECT id, externalId, category, series, size, wmn, price, diff, url, photoUrl, createdAt, importedAt FROM setting";

        /*return jdbcTemplate.query(sql, new RowMapper<Bicycle>() {

            @Override
            public Bicycle mapRow(ResultSet rs, int rowNum) throws SQLException {
                Bicycle workShop = new Bicycle(rs.getInt("id"), rs.getString("key"), rs.getString("value"));
                return workShop;
            }

        });*/

        return null;
    }

    @Override
    public Bicycle getById(int id) {
        String sql = "SELECT id, externalId, category, series, size, wmn, price, diff, url, photoUrl, createdAt, importedAt FROM setting WHERE id = ?";
        Object[] args = new Object[] { id };
        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(Bicycle.class));
    }

    @Override
    public int save(Bicycle setting) {
        String sql = "UPDATE setting SET value = ? WHERE id = ?";
        //return jdbcTemplate.update(sql, setting.getValue(), setting.getId());
        return 0;
    }

    @Override
    public int delete(Bicycle bicycle) {
        return 0;
    }
}
