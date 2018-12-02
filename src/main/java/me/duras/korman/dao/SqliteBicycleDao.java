package me.duras.korman.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;

public class SqliteBicycleDao implements BicycleDao {
    private JdbcTemplate jdbcTemplate;

    private String columns = "b.id, b.externalId, b.category, b.series, b.size, b.wmn, b.price, b.diff, b.modelYear, b.url, b.photoUrl, b.createdAt, b.importedAt, c.id, c.name, c.externalUrl";
    private RowMapper<Bicycle> mapper = new RowMapper<Bicycle>() {

        @Override
        public Bicycle mapRow(ResultSet rs, int rowNum) throws SQLException {
            BicycleCategory category = new BicycleCategory(rs.getInt("c.id"), rs.getString("c.name"),
                    rs.getString("c.externalUrl"));

            Bicycle bicycle = new Bicycle(rs.getInt("b.externalId"), category, rs.getString("b.series"),
                    rs.getString("b.size"), rs.getInt("b.wmn") == 1, rs.getInt("b.price"), rs.getInt("b.modelYear"),
                    rs.getString("b.url"), rs.getString("b.photoUrl"), new Date((long) rs.getInt("importedAt") * 1000));
            bicycle.setId(rs.getInt("b.id"));
            bicycle.setDiff(rs.getInt("b.diff"));

            return bicycle;
        }

    };

    public SqliteBicycleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Bicycle> getAll() {
        String sql = "SELECT " + this.columns + " FROM bicycle AS b JOIN bicycle_category AS c ON b.category = c.id";

        return jdbcTemplate.query(sql, this.mapper);
    }

    @Override
    public Bicycle getById(int id) {
        String sql = "SELECT " + this.columns
                + " FROM bicycle AS b JOIN bicycle_category AS c ON b.category = c.id WHERE b.id = ?";
        Object[] args = new Object[] { id };
        return jdbcTemplate.queryForObject(sql, args, this.mapper);
    }

    @Override
    public Bicycle save(Bicycle bicycle) {
        if (bicycle.getId() == 0) {
            // Creating
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("bicycle");
            simpleJdbcInsert.usingGeneratedKeyColumns("id");
            simpleJdbcInsert.usingColumns("id", "externalId", "category", "series", "size", "wmn", "price", "diff", "modelYear", "url", "photoUrl", "createdAt", "importedAt");

            Map<String, Object> values = new HashMap<>();
            values.put("externalId", bicycle.getExternalId());
            values.put("category", bicycle.getCategory().getId());
            values.put("series", bicycle.getSeries());
            values.put("size", bicycle.getSize());
            values.put("wmn", bicycle.isWmn() ? 1 : 0);
            values.put("price", bicycle.getPrice());
            values.put("diff", bicycle.getDiff());
            values.put("modelYear", bicycle.getModelYear());
            values.put("photoUrl", bicycle.getPhotoUrl());
            values.put("createdAt", bicycle.getCreatedAt());
            values.put("importedAt", (int) (new Date()).getTime() / 1000);

            int id = (int) simpleJdbcInsert.executeAndReturnKey(values).longValue();
            bicycle.setId(id);
        } else {
            throw new RuntimeException("Updating is not supported for bicycles");
        }

        return bicycle;
    }

    @Override
    public int delete(Bicycle bicycle) {
        String sql = "DELETE FROM bicycle WHERE id = ?";
        return jdbcTemplate.update(sql, bicycle.getId());
    }
}
