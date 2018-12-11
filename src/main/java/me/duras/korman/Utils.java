package me.duras.korman;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Utils {
    /**
     * Gets path to the application data path for the currently used user
     *
     * Inspired by the StackOverflow answer from Mike Warren
     * https://stackoverflow.com/a/16660314/5919589
     *
     * @param osName Current operating system, can be "windows", "mac", "linux"
     * @return application user data directory path
     */
    public static String getUserDataPath(String osName) {
        String workingDirectory;

        if (osName.equals("windows")) {
            workingDirectory = System.getenv("AppData");

            return workingDirectory + "\\Korman";
        }

        if (osName.equals("mac")) {
            workingDirectory = System.getProperty("user.home") + "/Library/Application Support";

            return workingDirectory + "/me.duras.korman";
        }

        if (osName.equals("linux")) {
            workingDirectory = System.getProperty("user.home");

            return workingDirectory + "/.korman";
        }

        throw new RuntimeException(
            "Unsupported OS name: " + osName + ". Supported: \"windows\", \"mac\", \"linux\"."
        );
    }

    /**
     * Gets path to the application data path for the currently used user and OS
     *
     * Inspired by the StackOverflow answer from Mike Warren
     * https://stackoverflow.com/a/16660314/5919589
     *
     * @return application user data directory path
     */
    public static String getUserDataPath() {
        String OS = System.getProperty("os.name").toUpperCase();

        String osName;
        if (OS.contains("WIN")) {
            osName = "windows";
        } else if (OS.contains("LINUX")) {
            osName = "linux";
        } else if (OS.contains("MAC OS")) {
            osName = "mac";
        } else {
            throw new RuntimeException("Unsupported OS: " + OS);
        }

        return Utils.getUserDataPath(osName);
    }

    /**
     * Finds if this application is started for the first time
     *
     * @return TRUE if it is first run, FALSE otherwise
     */
    public static boolean isFirstRun() {
        File dataDir = new File(Utils.getUserDataPath());

        return !(dataDir.exists() && dataDir.isDirectory());
    }

    /**
     * Initializes user data dir
     * 
     * @throws IOException
     */
    public static void initUserDataDir() throws IOException {
        Files.createDirectories(Paths.get(Utils.getUserDataPath()));
    }
}
