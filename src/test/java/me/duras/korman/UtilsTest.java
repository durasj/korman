package me.duras.korman;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Utils")
class UtilsTest {

    @Test
    @DisplayName("Give correct user data path for each OS")
    void testUserDataPath() {
        String windowsDataPath = Utils.getUserDataPath("windows");
        assertTrue(windowsDataPath.matches(".*\\\\Korman"));

        String macDataPath = Utils.getUserDataPath("mac");
        assertTrue(macDataPath.matches(".*\\/Library\\/Application Support\\/me.duras.korman"));

        String linuxDataPath = Utils.getUserDataPath("linux");
        assertTrue(linuxDataPath.matches(".*\\/.korman"));
    }

}
