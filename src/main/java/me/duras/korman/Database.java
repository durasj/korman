package me.duras.korman;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.sqlite.SQLiteConfig;

/**
 * Database abstraction
 */
public class Database {
    public final static String DB_FILE_NAME = "db.sqlite";

    private Connection connection = null;
    private JdbcTemplate jdbcTemplate = null;

    /**
     * Copy initial database
     *
     * @throws IOException
     */
    public static void copyInitial() throws IOException {
        String target = Utils.getUserDataPath() + File.separator + Database.DB_FILE_NAME;
        URL initialUrl = App.class.getResource("initial.sqlite");

        File file = null;
        try {
            file = new File(initialUrl.toURI());
        } catch (URISyntaxException e) {
            file = new File(initialUrl.getPath());
        } finally {
            copyInitial(file.toPath(), (new File(target).toPath()));
        }
    }

    public static void copyInitial(Path initial, Path target) throws IOException {
        Files.copy(initial, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Connect to database
     */
    public Connection connect() {
        return connect(Utils.getUserDataPath() + File.separator + Database.DB_FILE_NAME);
    }

    public Connection connect(String dbPath) {
        try {
            String connectionUrl =
                "jdbc:sqlite:" + dbPath;

                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName("org.sqlite.JDBC");
                dataSource.setUrl(connectionUrl);
                connection = dataSource.getConnection();
                jdbcTemplate = new JdbcTemplate(dataSource);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return this.connection;
    }

    public JdbcTemplate getTemplate() {
        return this.jdbcTemplate;
    }

    /**
     * Close database connection
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
