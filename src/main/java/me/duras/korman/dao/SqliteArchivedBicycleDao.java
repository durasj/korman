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

import me.duras.korman.models.ArchivedBicycle;
import me.duras.korman.models.BicycleCategory;;

public class SqliteArchivedBicycleDao implements ArchivedBicycleDao {
    protected JdbcTemplate jdbcTemplate;

    protected String columns = SqliteBicycleDao.columns + ", b.archivedAt";
    protected RowMapper<ArchivedBicycle> mapper = new RowMapper<ArchivedBicycle>() {

        @Override
        public ArchivedBicycle mapRow(ResultSet rs, int rowNum) throws SQLException {
            BicycleCategory category = new BicycleCategory(rs.getInt("categoryId"), rs.getString("categoryName"),
                    rs.getString("categoryExternalUrl"), rs.getString("categoryExternalDetailUrl"));

            ArchivedBicycle bicycle = new ArchivedBicycle(rs.getString("externalId"), category, rs.getString("series"),
                    rs.getString("size"), rs.getInt("wmn") == 1, rs.getInt("price"), rs.getInt("modelYear"),
                    rs.getString("url"), rs.getString("photoUrl"), new Date((long) rs.getInt("createdAt") * 1000));
            bicycle.setId(rs.getInt("id"));
            bicycle.setDiff(rs.getInt("diff"));
            bicycle.setImportedAt(new Date((long) rs.getInt("importedAt") * 1000));
            bicycle.setArchivedAt(new Date((long) rs.getInt("archivedAt") * 1000));

            return bicycle;
        }

    };

    public SqliteArchivedBicycleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ArchivedBicycle> getAll() {
        String sql = "SELECT " + this.columns + " FROM archived_bicycle b INNER JOIN bicycle_category c ON b.category = c.id";

        return jdbcTemplate.query(sql, this.mapper);
    }

    @Override
    public ArchivedBicycle getById(int id) {
        String sql = "SELECT " + this.columns
                + " FROM archived_bicycle b INNER JOIN bicycle_category c ON b.category = c.id WHERE b.id = ?";
        Object[] args = new Object[] { id };
        return jdbcTemplate.queryForObject(sql, args, this.mapper);
    }

    @Override
    public void saveMany(List<ArchivedBicycle> bicycles) {
        ArchivedBicycle[] list = new ArchivedBicycle[bicycles.size()];
        bicycles.toArray(list);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("archived_bicycle");
        simpleJdbcInsert.usingGeneratedKeyColumns("id");
        simpleJdbcInsert.usingColumns("externalId", "category", "series", "size", "wmn", "price", "diff", "modelYear", "url", "photoUrl", "createdAt", "importedAt", "archivedAt");

        Map<String, Object>[] rows = new HashMap[bicycles.size()];
        int i = 0;
        for (ArchivedBicycle bicycle : list) {
            if (bicycle.getId() == 0) {
                Map<String, Object> values = new HashMap<>();
                values.put("externalId", bicycle.getExternalId());
                values.put("category", bicycle.getCategory().getId());
                values.put("series", bicycle.getSeries());
                values.put("size", bicycle.getSize());
                values.put("wmn", bicycle.isWmn() ? 1 : 0);
                values.put("price", bicycle.getPrice());
                values.put("diff", bicycle.getDiff());
                values.put("modelYear", bicycle.getModelYear());
                values.put("url", bicycle.getUrl());
                values.put("photoUrl", bicycle.getPhotoUrl());
                values.put("createdAt", bicycle.getCreatedAt());
                values.put("importedAt", bicycle.getImportedAt());
                values.put("archivedAt", (int) Math.round((new Date()).getTime() / 1000));
                rows[i] = values;
            } else {
                throw new RuntimeException("Updating is not supported for archived bicycles");
            }
            i++;
        }

        simpleJdbcInsert.executeBatch(rows);
    }
}
