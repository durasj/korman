package me.duras.korman;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

@DisplayName("Utils")
public class UtilsTest extends TestCase {

    @Test
    @DisplayName("Give correct user data path for each OS")
    public void testUserDataPath() {
        String windowsDataPath = Utils.getUserDataPath("windows");
        assertTrue(windowsDataPath.matches(".*\\\\Korman"));

        String macDataPath = Utils.getUserDataPath("mac");
        assertTrue(macDataPath.matches(".*\\/Library\\/Application Support\\/me.duras.korman"));

        String linuxDataPath = Utils.getUserDataPath("linux");
        assertTrue(linuxDataPath.matches(".*\\/.korman"));
    }

}
