package me.duras.korman.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import me.duras.korman.models.Log;

public class SqliteLogDao implements LogDao {
    private JdbcTemplate jdbcTemplate;

    public SqliteLogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Log> getAll() {
        String sql = "SELECT id, content, createdAt FROM log";

        return jdbcTemplate.query(sql, new RowMapper<Log>() {

            @Override
            public Log mapRow(ResultSet rs, int rowNum) throws SQLException {
                Log log = new Log(rs.getString("content"));
                log.setId(rs.getInt("id"));
                log.setCreatedAt(new Date((long) rs.getInt("createdAt") * 1000));
                return log;
            }

        });
    }

    @Override
    public Log save(Log log) {
        if (log.getId() == 0) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("log");
            simpleJdbcInsert.usingGeneratedKeyColumns("id");
            simpleJdbcInsert.usingColumns("content", "createdAt");

            Map<String, Object> values = new HashMap<>();
            values.put("content", log.getContent());
            values.put("createdAt", (int) (log.getCreatedAt().getTime() / 1000));

            int id = simpleJdbcInsert.executeAndReturnKey(values).intValue();
            log.setId((int) id);
        } else {
            throw new Error("Updating logs not allowed");
        }

        return log;
    }

    @Override
    public int clear() {
        String sql = "DELETE FROM setting";
        return jdbcTemplate.update(sql);
    }
}
