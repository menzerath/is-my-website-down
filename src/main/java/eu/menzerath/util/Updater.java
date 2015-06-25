package eu.menzerath.util;

import eu.menzerath.imwd.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This Updater looks for new versions in a particular text-file in the application's GitHub-project.
 */
public class Updater {
	private static String cacheServerVersion = "Error";
	private static String cacheServerChangelog = "Error";

	/**
	 * Constructor: Refresh data on Object-creation.
	 */
	public Updater() {
		refresh();
	}

	/**
	 * Compares the version of this "IsMyWebsiteDown?"-file and the servers' version.
	 *
	 * @return <code>true</code> if an update is available or <code>false</code> if not
	 */
	public boolean isUpdateAvailable() {
		return !getServerVersion().equalsIgnoreCase(Main.VERSION);
	}

	/**
	 * Get the cached server version.
	 *
	 * @return Server version
	 */
	public String getServerVersion() {
		return cacheServerVersion;
	}

	/**
	 * Get the cached changelog.
	 *
	 * @return Changelog
	 */
	public String getServerChangelog() {
		return cacheServerChangelog;
	}

	/**
	 * Pulls data from the server and caches it for later access (to use with ConsoleApplication) if this is not a SNAPSHOT-version.
	 */
	public void refresh() {
		if (Main.VERSION.contains("SNAPSHOT")) {
			cacheServerVersion = "SNAPSHOT";
			cacheServerChangelog = "SNAPSHOT";
		} else {
			ArrayList<String> fileContent = getUpdateFileFromServer();
			cacheServerVersion = fileContent.get(0);
			cacheServerChangelog = fileContent.get(1);
		}
	}

	/**
	 * Download content from a remote server and return an ArrayList containing every line as a single entry.
	 *
	 * @return Content; each entry = one line
	 */
	private ArrayList<String> getUpdateFileFromServer() {
		ArrayList<String> lines = new ArrayList<>();
		try {
			Scanner sc = new Scanner(new URL("https://raw.githubusercontent.com/MarvinMenzerath/IsMyWebsiteDown/master/VERSION.txt").openConnection().getInputStream());
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