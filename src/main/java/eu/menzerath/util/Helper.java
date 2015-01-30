package eu.menzerath.util;

import eu.menzerath.imwd.Checker;
import eu.menzerath.imwd.GuiApplication;
import eu.menzerath.imwd.Main;
import org.fusesource.jansi.Ansi;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;

public class Helper {

    /**
     * Create a new Checker to check for an existing connection to the internet
     * @return If there is a connection to the internet
     */
    public static boolean testWebConnection() {
        Checker checker = new Checker(0, "http://google.com", 30, true, false, false, false, false);
        return checker.testContent();
    }

    /**
     * This method validates the user-input (URL) and checks if it is ok.
     * One of the allowed protocols is used and there is something after the protocol
     *
     * @param url Given URL to check
     * @return If the input could be validated or not
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
        } catch (URISyntaxException | IOException e) {
            System.out.println(new Ansi().fg(Ansi.Color.RED).bold().a("[ERROR]").reset() + " Unable to add IMWD to Autorun: " + e.getMessage());
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
        } catch (IOException e) {
            System.out.println(new Ansi().fg(Ansi.Color.RED).bold().a("[ERROR]").reset() + " Unable to remove IMWD from Autorun: " + e.getMessage());
            return false;
        }
    }
}