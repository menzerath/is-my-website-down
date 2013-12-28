package de.menzerath.util;

import de.menzerath.imwd.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Updater {
    private final String UPDATE_URL = "https://raw.github.com/MarvinMenzerath/IsMyWebsiteDown/master/VERSION.txt";

    private static String cacheServerVersion = "Error";
    private static String cacheServerChangelog = "Error";

    /**
     * Refresh data on Object-creation
     */
    public Updater() {
        refresh();
    }

    /**
     * Compares the version of this "IsMyWebsiteDown?"-file and the servers' version
     *
     * @return Availability of an update
     */
    public boolean isUpdateAvailable() {
        return !getServerVersion().equalsIgnoreCase(Main.getVersion());
    }

    /**
     * Get the cached server version
     *
     * @return Server version
     */
    public String getServerVersion() {
        return cacheServerVersion;
    }

    /**
     * Get the cached changelog
     *
     * @return Changelog
     */
    public String getServerChangelog() {
        return cacheServerChangelog;
    }

    /**
     * Pulls data from the server and caches it for later access (to use with ConsoleApplication)
     */
    public void refresh() {
        cacheServerVersion = getFileFromServer(UPDATE_URL).get(0);
        cacheServerChangelog = getFileFromServer(UPDATE_URL).get(1);
    }

    /**
     * Helper which gets content from a remote server and returns an ArrayList containing every line as a single entry
     *
     * @param url URL to access
     * @return Content; each entry = one line
     */
    private ArrayList<String> getFileFromServer(String url) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new URL(url).openConnection().getInputStream());
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine().trim());
            }
            sc.close();
        } catch (Exception e) {
            lines.add("Error"); // Version
            lines.add("Error"); // Changelog
        }
        return lines;
    }
}