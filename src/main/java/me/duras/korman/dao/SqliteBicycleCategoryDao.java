package me.duras.korman.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import me.duras.korman.models.BicycleCategory;

public class SqliteBicycleCategoryDao implements BicycleCategoryDao {
    private JdbcTemplate jdbcTemplate;

    public SqliteBicycleCategoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BicycleCategory> getAll() {
        String sql = "SELECT id, name, externalUrl, externalDetailUrl FROM bicycle_category";

        return jdbcTemplate.query(sql, new RowMapper<BicycleCategory>() {

            @Override
            public BicycleCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
                BicycleCategory category = new BicycleCategory(rs.getInt("id"), rs.getString("name"), rs.getString("externalUrl"), rs.getString("externalDetailUrl"));
                return category;
            }

        });
    }

    @Override
    public BicycleCategory getById(int id) {
        String sql = "SELECT id, name, externalUrl FROM bicycle_category WHERE id = ?";
        Object[] args = new Object[] { id };
        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<>(BicycleCategory.class));
    }
}
