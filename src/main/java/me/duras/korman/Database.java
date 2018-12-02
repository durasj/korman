package me.duras.korman;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database abstraction
 */
public class Database {
    public final static String DB_FILE_NAME = "db.sqlite";

    private Connection connection = null;

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
            Files.copy(file.toPath(), (new File(target).toPath()));
        }
    }

    /**
     * Connect to database
     */
    public Connection connect() {
        try {
            String connectionUrl =
                "jdbc:sqlite:" + Utils.getUserDataPath() + File.separator + Database.DB_FILE_NAME;
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return this.connection;
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
            System.out.println(ex.getMessage());
        }
    }
}
