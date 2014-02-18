package de.menzerath.imwd;

import de.menzerath.util.Messages;

import java.awt.*;
import java.util.prefs.Preferences;

public class Main {
    public static final String APPLICATION = "Is My Website Down?";
    public static final String APPLICATION_SHORT = "IMWD";
    public static final String VERSION = "2.0.2";
    public static final String URL = "https://github.com/MarvinMenzerath/IsMyWebsiteDown";
    public static final String URL_RELEASE = "https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases";
    public static final int MIN_INTERVAL = 10;
    public static final int MAX_INTERVAL = 300;

    private static final String PREF_URL = "url";
    private static final String PREF_INTERVAL = "interval";
    private static final String PREF_CHECKER_COUNT = "checkerCount";
    private static final String PREF_CHECK_CONTENT = "checkContent";
    private static final String PREF_CHECK_PING = "checkPing";
    private static final String PREF_CREATE_LOG = "createLog";
    private static final String PREF_LOG_VALID_CHECKS = "createValidLog";

    /**
     * The Beginning!
     * If arguments were passed they will be handled, otherwise it will start a ConsoleApplication OR the GuiApplication.
     *
     * @param args Passed arguments for start
     */
    public static void main(String[] args) {
        sayHello();

        boolean createLog = true;
        if (args.length == 3 && args[2].equalsIgnoreCase("--nolog")) createLog = false;
        if (args.length > 1 && args.length < 4) {
            // Arguments passed, if they are the correct ones create a new ConsoleApplication, otherwise show the user the needed arguments
            if (args[0].trim().startsWith("--url=") && args[1].trim().startsWith("--interval=")) {
                new ConsoleApplication(args[0].trim().substring(6), args[1].trim().substring(11), createLog);
            } else {
                System.out.println(Messages.INVALID_ARGUMENTS);
                System.exit(1);
            }
        } else {
            // If this is running on a setup without an graphical desktop and no arguments were passed show the needed arguments
            if (GraphicsEnvironment.isHeadless()) {
                System.out.println(Messages.INVALID_ARGUMENTS);
                System.exit(1);
            } else {
                GuiApplication.startGUI();
            }
        }
    }

    /**
     * Shows an informative message about "Is My Website Down?" on start
     */
    public static void sayHello() {
        System.out.println("##################################################");
        System.out.println("### " + APPLICATION + " v" + VERSION + "                 ###");
        System.out.println("###                                            ###");
        System.out.println("### © 2012-2014: Marvin Menzerath              ###");
        System.out.println("### " + URL.substring(8) + " ###");
        System.out.println("##################################################\n");
    }

    /**
     * Get the Preferences from the user-node AND the "Main"-class
     *
     * @return Preferences
     */
    public static Preferences getPreferences() {
        return Preferences.userNodeForPackage(Main.class);
    }

    /**
     * Get the last used setting OR the default value
     *
     * @return Last used url OR default value
     */
    public static String getUrlFromSettings(int id) {
        return getPreferences().get(PREF_URL + id, "http://google.com");
    }

    /**
     * Get the last used setting OR the default value
     *
     * @return Last used interval OR default value
     */
    public static int getIntervalFromSettings(int id) {
        return getPreferences().getInt(PREF_INTERVAL + id, 30);
    }

    /**
     * Get the last used setting OR the default value
     *
     * @return Last amount of checkers OR default value
     */
    public static int getCheckerCountFromSettings() {
        return getPreferences().getInt(PREF_CHECKER_COUNT, 1);
    }

    /**
     * Get the last used setting OR the default value
     *
     * @return Check content OR default value
     */
    public static boolean getCheckContentFromSettings() {
        return getPreferences().getBoolean(PREF_CHECK_CONTENT, true);
    }

    /**
     * Get the last used setting OR the default value
     *
     * @return Check ping OR default value
     */
    public static boolean getCheckPingFromSettings() {
        return getPreferences().getBoolean(PREF_CHECK_PING, true);
    }

    /**
     * Get the last used setting OR the default value
     *
     * @return Create logfile OR default value
     */
    public static boolean getCreateLogFromSettings() {
        return getPreferences().getBoolean(PREF_CREATE_LOG, false);
    }

    /**
     * Get the last used setting OR the default value
     *
     * @return Log successful checks OR default value
     */
    public static boolean getCreateValidLogFromSettings() {
        return getPreferences().getBoolean(PREF_LOG_VALID_CHECKS, false);
    }

    /**
     * Save the specified value in the settings
     *
     * @param value The url to save
     */
    public static void setUrlForSettings(int id, String value) {
        getPreferences().put(PREF_URL + id, value);
    }

    /**
     * Save the specified value in the settings
     *
     * @param value The interval to save
     */
    public static void setIntervalForSettings(int id, int value) {
        getPreferences().putInt(PREF_INTERVAL + id, value);
    }

    /**
     * Save the specified value in the settings
     *
     * @param value The count of used checkers to save
     */
    public static void setCheckerCountForSettings(int value) {
        getPreferences().putInt(PREF_CHECKER_COUNT, value);
    }

    /**
     * Save the specified value in the settings
     *
     * @param value "create a log"-setting to save
     */
    public static void setCheckContentForSettings(boolean value) {
        getPreferences().putBoolean(PREF_CHECK_CONTENT, value);
    }

    /**
     * Save the specified value in the settings
     *
     * @param value "create a log"-setting to save
     */
    public static void setCheckPingForSettings(boolean value) {
        getPreferences().putBoolean(PREF_CHECK_PING, value);
    }

    /**
     * Save the specified value in the settings
     *
     * @param value "create a log"-setting to save
     */
    public static void setCreateLogForSettings(boolean value) {
        getPreferences().putBoolean(PREF_CREATE_LOG, value);
    }

    /**
     * Save the specified value in the settings
     *
     * @param value "log successful checks"-setting to save
     */
    public static void setCreateValidLogForSettigs(boolean value) {
        getPreferences().putBoolean(PREF_LOG_VALID_CHECKS, value);
    }
}