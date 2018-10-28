package me.duras.korman;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database abstraction
 *
 * @author sqlitetutorial.net
 */
public class Database {
    public final static String DB_FILE_NAME = "db.sqlite";

    private Connection connection = null;

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
