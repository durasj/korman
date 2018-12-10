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

import me.duras.korman.DaoFactory;
import me.duras.korman.models.Agent;
import me.duras.korman.models.Bicycle;
import me.duras.korman.models.BicycleCategory;
import me.duras.korman.models.Notification;

public class SqliteNotificationDao implements NotificationDao {
    protected JdbcTemplate jdbcTemplate;

    final static String columns = "n.id, n.agent, n.bicycle, n.createdAt, n.emailSent";
    protected RowMapper<Notification> mapper = new RowMapper<Notification>() {

        @Override
        public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
            Agent agent = DaoFactory.INSTANCE.getAgentDao().getById(rs.getInt("agent"));
            Bicycle bicycle = DaoFactory.INSTANCE.getBicycleDao().getById(rs.getInt("bicycle"));

            Notification notification = new Notification(agent, bicycle, new Date((long) rs.getInt("createdAt") * 1000), rs.getInt("emailSent") == 1);
            notification.setId(rs.getInt("id"));

            return notification;
        }

    };

    public SqliteNotificationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Notification> getAll() {
        String sql = "SELECT " + SqliteNotificationDao.columns + " FROM notification n";

        return jdbcTemplate.query(sql, this.mapper);
    }

    @Override
    public Notification getById(int id) {
        String sql = "SELECT " + SqliteNotificationDao.columns
                + " FROM notification n";
        Object[] args = new Object[] { id };
        return jdbcTemplate.queryForObject(sql, args, this.mapper);
    }

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == 0) {
            // Creating
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("notification");
            simpleJdbcInsert.usingGeneratedKeyColumns("id");
            simpleJdbcInsert.usingColumns("agent", "bicycle", "createdAt", "emailSent");

            Map<String, Object> values = new HashMap<>();
            values.put("agent", notification.getAgent().getId());
            values.put("bicycle", notification.getBicycle().getId());
            values.put("createdAt", (int) Math.round((new Date()).getTime() / 1000));
            values.put("emailSent", notification.getEmailSent() ? 1 : 0);

            int id = simpleJdbcInsert.executeAndReturnKey(values).intValue();
            notification.setId((int) id);
        } else {
            String sql = "UPDATE notification SET agent = ?, bicycle = ?, createdAt = ?, emailSent = ? WHERE id = ?";
            Object[] params = {
                notification.getAgent().getId(),
                notification.getBicycle().getId(),
                (int) (notification.getCreatedAt().getTime() / 1000),
                notification.getEmailSent() ? 1 : 0,
                notification.getId(),
            };
            int[] types = {
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
            };

            jdbcTemplate.update(sql, params, types);
        }

        return notification;
    }

    @Override
    public List<Notification> saveMany(List<Notification> notifications) {
        for (Notification notification : notifications) {
            this.save(notification);
        }

        return notifications;
    }

    @Override
    public int delete(Notification notification) {
        return delete(notification.getId());
    }

    @Override
    public int delete(int notificationId) {
        String sql = "DELETE FROM notification WHERE id = ?";
        return jdbcTemplate.update(sql, notificationId);
    }
}
