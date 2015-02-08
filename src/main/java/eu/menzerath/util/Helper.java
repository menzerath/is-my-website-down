package eu.menzerath.util;

import eu.menzerath.imwd.GuiApplication;
import eu.menzerath.imwd.Main;
import eu.menzerath.imwd.checker.Checker;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;

/**
 * The Helper-Class contains multiple validators and often-used methods
 */
public class Helper {

    /**
     * Create a new Checker to check for an existing connection to the internet
     * @return <code>true</code> if there is a response from the Google-webservers or <code>false</code> if not
     */
    public static boolean testWebConnection() {
        Checker checker = new Checker(0, "http://google.com", 30, true, false, false, false);
        return checker.testContent();
    }

    /**
     * This method validates the user-input (URL) and checks if it is ok.
     * One of the allowed protocols is used and there is something after the protocol
     *
     * @param url Given URL to check
     * @return <code>true</code> if the input has been successfully validated or <code>false</code> if not
     */
    public static boolean validateUrlInput(String url) {
        boolean success = false;
        for (String p : Main.PROTOCOLS) {
            if (url.startsWith(p) && !url.substring(p.length()).equals("")) success = true;
        }
        return success;
    }

    /**
     * This method validates the user-input (interval) and checks if it is ok.
     *
     * @param interval Given interval to check
     * @return <code>true</code> if the input has been successfully validated or <code>false</code> if not
     */
    public static boolean validateIntervalInput(int interval) {
        return !(interval < Main.MIN_INTERVAL || interval > Main.MAX_INTERVAL);
    }

    /**
     * Parse a String and return an Integer.
     *
     * @param integer String to parse
     * @return Given String as Integer or 0 on error
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
     * Removes the protocol from a url.
     * @param url Url to remove protocol from
     * @return Given url without a protocol
     */
    public static String getUrlWithoutProtocol(String url) {
        for (String p : Main.PROTOCOLS) {
            url = url.replace(p, "");
        }
        return url;
    }

    /**
     * Copy "Is My Website Down" into the Autorun-folder (works with Windows Vista, 7, 8 and 8.1).
     * @return <code>true</code> if the application has been copied into the autorun or <code>false</code> if not
     */
    public static boolean addToAutorun() {
        try {
            CodeSource cSource = GuiApplication.class.getProtectionDomain().getCodeSource();
            File sourceFile = new File(cSource.getLocation().toURI().getPath());
            Path source = Paths.get(sourceFile.getParentFile().getPath() + File.separator + sourceFile.getName());
            Path dest = Paths.get("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\IMWD.jar");
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (URISyntaxException | IOException e) {
            Logger.error("Unable to add IMWD to Autorun: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove "Is My Website Down" from the Autorun-folder (works with Windows Vista, 7, 8 and 8.1).
     * @return <code>true</code> if the application has been removed from the autorun or <code>false</code> if not
     */
    public static boolean removeFromAutorun() {
        try {
            Path dest = Paths.get("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\IMWD.jar");
            Files.delete(dest);
            return true;
        } catch (IOException e) {
            Logger.error("Unable to remove IMWD from Autorun: " + e.getMessage());
            return false;
        }
    }
}