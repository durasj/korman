package me.duras.korman.dao;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import junit.framework.TestCase;
import me.duras.korman.DaoFactory;
import me.duras.korman.Database;

@TestInstance(Lifecycle.PER_CLASS)
public class DaoTestCaseBase extends TestCase {
    private Database db;

    @BeforeAll
    public void setupDb() {
        String baseDir = System.getProperties().get("basedir").toString();
        String target = baseDir + File.separator + "target" + File.separator + Database.DB_FILE_NAME;
        String initial = baseDir + File.separator + "db" + File.separator + "initial.sqlite";
        try {
            Database.copyInitial((new File(initial).toPath()), (new File(target).toPath()));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException("Unnable to setup test DB");
        }
        db = new Database();
        db.connect(baseDir + File.separator + "target" + File.separator + Database.DB_FILE_NAME);
        DaoFactory.INSTANCE.setJdbcTemplate(db.getTemplate());
    }

    @AfterAll
    public void afterAll() {
        db.close();
    }
}
