package de.menzerath.imwd;

import de.menzerath.util.Logger;

import java.awt.*;
import java.util.prefs.Preferences;

public class Main {
    private static final String IMWD_VERSION = "2.0.2";
    private static final String PREF_URL = "url";
    private static final String PREF_INTERVAL = "interval";
    private static final String PREF_CHECKER_COUNT = "checkerCount";
    private static final String PREF_CHECK_CONTENT = "checkContent";
    private static final String PREF_CHECK_PING = "checkPing";
    private static final String PREF_CREATELOG = "createLog";
    private static final String PREF_CREATEVALIDLOG = "createValidLog";

    /**
     * The Beginning!
     * If arguments were passed they will be handled, otherwise it will start a ConsoleApplication OR the GuiApplication.
     *
     * @param args Passed arguments for start
     */
    public static void main(String[] args) {
        Logger.sayHello();
        if (args.length == 2) {
            // 2 Arguments passed, if they are the correct ones create a new ConsoleApplication, otherwise show the user the needed arguments
            if (args[0].trim().startsWith("--url=") && args[1].trim().startsWith("--interval=")) {
                new ConsoleApplication(args[0].trim().substring(6), args[1].trim().substring(11));
            } else {
                System.out.println("You have to specify the following arguments in this order:");
                System.out.println("--url=http://website.com --interval=30");
                System.exit(0);
            }
        } else {
            // If this is running on a setup without an graphical desktop and no arguments were passed show the needed arguments
            if (GraphicsEnvironment.isHeadless()) {
                System.out.println("You have to specify the following arguments in this order:");
                System.out.println("--url=http://website.com --interval=30");
                System.exit(0);
            } else {
                GuiApplication.startGUI();
            }
        }
    }

    /**
     * Displays the version of this "Is My Website Down?"-file
     *
     * @return The version of this file
     */
    public static String getVersion() {
        return IMWD_VERSION;
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
        return getPreferences().getBoolean(PREF_CREATELOG, false);
    }

    /**
     * Get the last used setting OR the default value
     *
     * @return Log successful checks OR default value
     */
    public static boolean getCreateValidLogFromSettings() {
        return getPreferences().getBoolean(PREF_CREATEVALIDLOG, false);
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
        getPreferences().putBoolean(PREF_CREATELOG, value);
    }

    /**
     * Save the specified value in the settings
     *
     * @param value "log successful checks"-setting to save
     */
    public static void setCreateValidLogForSettigs(boolean value) {
        getPreferences().putBoolean(PREF_CREATEVALIDLOG, value);
    }
}