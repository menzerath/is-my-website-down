package eu.menzerath.util;

import eu.menzerath.imwd.GuiApplication;
import eu.menzerath.imwd.Main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;

public class Helper {

    /**
     * This method validates the user-input (URL) and checks if it is ok.
     *
     * @param url Given URL to check
     * @return If the input could be validated or not
     */
    public static boolean validateUrlInput(String url) {
        return url.trim().startsWith("http://");
    }

    /**
     * This method validates the user-input (interval) and checks if it is ok.
     *
     * @param interval Given interavl to check
     * @return If the input could be validated or not
     */
    public static boolean validateIntervalInput(int interval) {
        return !(interval < Main.MIN_INTERVAL || interval > Main.MAX_INTERVAL);
    }

    /**
     * Parse a String and return an Integer.
     *
     * @param integer String to parse
     * @return Parsed String; 0 on error
     */
    public static int parseInt(String integer) {
        int myInt = 0;
        try {
            myInt = Integer.parseInt(integer.trim());
        } catch (NumberFormatException ignored) {
        }
        return myInt;
    }

    /**
     * Copy "Is My Website Down" into the Autorun-folder (works with Windows Vista, 7, 8 and 8.1).
     */
    public static boolean addToAutorun() {
        try {
            CodeSource cSource = GuiApplication.class.getProtectionDomain().getCodeSource();
            File sourceFile = new File(cSource.getLocation().toURI().getPath());
            Path source = Paths.get(sourceFile.getParentFile().getPath() + File.separator + sourceFile.getName());
            Path dest = Paths.get("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\IMWD.jar");
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove "Is My Website Down" from the Autorun-folder (works with Windows Vista, 7, 8 and 8.1).
     */
    public static boolean removeFromAutorun() {
        try {
            Path dest = Paths.get("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\IMWD.jar");
            Files.delete(dest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}