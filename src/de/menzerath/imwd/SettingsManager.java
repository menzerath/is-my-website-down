package de.menzerath.imwd;

import java.util.prefs.Preferences;

public class SettingsManager {
    private static final String PREF_URL = "url";
    private static final String PREF_INTERVAL = "interval";
    private static final String PREF_CHECKER_COUNT = "checkerCount";
    private static final String PREF_CHECK_CONTENT = "checkContent";
    private static final String PREF_CHECK_PING = "checkPing";
    private static final String PREF_CREATE_LOG = "createLog";
    private static final String PREF_LOG_VALID_CHECKS = "createValidLog";
    private static final String PREF_AUTORUN = "autorun";
    private static final String PREF_SHOW_BUBBLES = "showBubbles";

    /**
     * Get the Preferences from the user-node AND the "Main"-class
     *
     * @return Preferences
     */
    public static Preferences getPreferences() {
        return Preferences.userNodeForPackage(Main.class);
    }

    /*
     * BEGIN: Settings-Getter including their default values
     */
    public static String getUrlFromSettings(int id) {
        return getPreferences().get(PREF_URL + id, "http://google.com");
    }

    public static int getIntervalFromSettings(int id) {
        return getPreferences().getInt(PREF_INTERVAL + id, 30);
    }

    public static int getCheckerCountFromSettings() {
        return getPreferences().getInt(PREF_CHECKER_COUNT, 1);
    }

    public static boolean getCheckContentFromSettings() {
        return getPreferences().getBoolean(PREF_CHECK_CONTENT, true);
    }

    public static boolean getCheckPingFromSettings() {
        return getPreferences().getBoolean(PREF_CHECK_PING, true);
    }

    public static boolean getCreateLogFromSettings() {
        return getPreferences().getBoolean(PREF_CREATE_LOG, false);
    }

    public static boolean getCreateValidLogFromSettings() {
        return getPreferences().getBoolean(PREF_LOG_VALID_CHECKS, false);
    }

    public static boolean getAutorunFromSettings() {
        return getPreferences().getBoolean(PREF_AUTORUN, false);
    }

    public static boolean getShowBubblesSettings() {
        return getPreferences().getBoolean(PREF_SHOW_BUBBLES, true);
    }
    /*
     * END: Settings-Getter including their default values
     */

    /*
     * BEGIN: SettingsManager-Setter
     */
    public static void setUrlForSettings(int id, String value) {
        getPreferences().put(PREF_URL + id, value);
    }

    public static void setIntervalForSettings(int id, int value) {
        getPreferences().putInt(PREF_INTERVAL + id, value);
    }

    public static void setCheckerCountForSettings(int value) {
        getPreferences().putInt(PREF_CHECKER_COUNT, value);
    }

    public static void setCheckContentForSettings(boolean value) {
        getPreferences().putBoolean(PREF_CHECK_CONTENT, value);
    }

    public static void setCheckPingForSettings(boolean value) {
        getPreferences().putBoolean(PREF_CHECK_PING, value);
    }

    public static void setCreateLogForSettings(boolean value) {
        getPreferences().putBoolean(PREF_CREATE_LOG, value);
    }

    public static void setCreateValidLogForSettigs(boolean value) {
        getPreferences().putBoolean(PREF_LOG_VALID_CHECKS, value);
    }

    public static void setAutorunForSettigs(boolean value) {
        getPreferences().putBoolean(PREF_AUTORUN, value);
    }

    public static void setShowBubblesSettigs(boolean value) {
        getPreferences().putBoolean(PREF_SHOW_BUBBLES, value);
    }
    /*
     * END: Settings-Setter
     */
}
