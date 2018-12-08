package me.duras.korman.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import me.duras.korman.models.Agent;
import me.duras.korman.models.BicycleCategory;

public class SqliteAgentDao implements AgentDao {
    private JdbcTemplate jdbcTemplate;

    private String columns = "a.id, a.name, a.email, a.category, a.series, a.wmn, a.size, a.minPrice, a.maxPrice, a.minDiff, a.modelYear, a.lastCheck, a.active, c.id AS categoryId, c.name AS categoryName, c.externalUrl AS categoryExternalUrl";
    private RowMapper<Agent> mapper = new RowMapper<Agent>() {

        @Override
        public Agent mapRow(ResultSet rs, int rowNum) throws SQLException {
            BicycleCategory category = new BicycleCategory(rs.getInt("categoryId"), rs.getString("categoryName"),
                    rs.getString("categoryExternalUrl"));

            Agent agent = new Agent(rs.getString("name"), rs.getString("email"), category, rs.getString("series"),
                    rs.getString("size"), rs.getInt("wmn") == 1, rs.getInt("minPrice"), rs.getInt("maxPrice"), rs.getInt("minDiff"), rs.getInt("modelYear"));
            agent.setId(rs.getInt("id"));
            agent.setLastCheck(new Date((long) rs.getInt("lastCheck") * 1000));
            agent.setActive(rs.getInt("active") == 1);

            return agent;
        }

    };
 
    public SqliteAgentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Agent> getAll() {
        String sql = "SELECT " + this.columns + " FROM agent a INNER JOIN bicycle_category c ON a.category = c.id";

        return jdbcTemplate.query(sql, this.mapper);
    }

    @Override
    public Agent getById(int id) {
        String sql = "SELECT " + this.columns
                + " FROM agent a INNER JOIN bicycle_category c ON a.category = c.id WHERE a.id = ?";
        Object[] args = new Object[] { id };
        return jdbcTemplate.queryForObject(sql, args, this.mapper);
    }

    @Override
    public Agent save(Agent agent) {
        if (agent.getId() == 0) {
            // Creating
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("agent");
            simpleJdbcInsert.usingGeneratedKeyColumns("id");
            simpleJdbcInsert.usingColumns("name", "email", "category", "series", "size", "wmn", "minPrice", "maxPrice", "minDiff", "modelYear", "lastCheck", "active");

            Map<String, Object> values = new HashMap<>();
            values.put("name", agent.getName());
            values.put("email", agent.getEmail());
            values.put("category", agent.getCategory().getId());
            values.put("series", agent.getSeries());
            values.put("size", agent.getSize());
            values.put("wmn", agent.getWmn() ? 1 : 0);
            values.put("minPrice", agent.getMinPrice());
            values.put("maxPrice", agent.getMaxPrice());
            values.put("minDiff", agent.getMinDiff());
            values.put("modelYear", agent.getModelYear());
            values.put("lastCheck", 0);
            values.put("active", agent.getActive() ? 1 : 0);

            int id = simpleJdbcInsert.executeAndReturnKey(values).intValue();
            agent.setId((int) id);
        } else {
            String sql = "UPDATE agent SET name = ?, email = ?, category = ?, series = ?, wmn = ?, size = ?, minPrice = ?, maxPrice = ?, minDiff = ?, modelYear = ?, lastCheck = ?, active = ? WHERE id = ?";
            Object[] params = {
                agent.getName(),
                agent.getEmail(),
                agent.getCategory().getId(),
                agent.getSeries(),
                agent.getWmn() ? 1 : 0,
                agent.getSize(),
                agent.getMinPrice(),
                agent.getMaxPrice(),
                agent.getMinDiff(),
                agent.getModelYear(),
                (int) (agent.getLastCheck().getTime() / 1000),
                agent.getActive() ? 1 : 0,
                agent.getId(),
            };
            int[] types = {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
            };

            jdbcTemplate.update(sql, params, types);
        }

        return agent;
    }

    @Override
    public int delete(Agent agent) {
        return delete(agent.getId());
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM agent WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
