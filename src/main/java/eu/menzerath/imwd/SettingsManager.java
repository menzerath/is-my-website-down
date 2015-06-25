package eu.menzerath.imwd;

import eu.menzerath.util.Logger;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Scanner;

/**
 * The SettingsManager manages the configuration of the whole application in a single JSON-file in the user's home-directory.
 * At the moment it is only used by the GuiApplication.
 */
public class SettingsManager {
	private File settingsFile;
	private JSONObject config;

	/**
	 * Constructor: Opens or creates the config-file from a default-file in the application's resources.
	 */
	public SettingsManager() {
		settingsFile = new File(System.getProperty("user.home") + FileSystems.getDefault().getSeparator() + "ismywebsitedown.json");
		Logger.info("Using this config-file: " + settingsFile.getAbsolutePath());

		// File does not exist: copy a default-configuration from the resources
		if (!settingsFile.exists()) {
			InputStream ddlStream = SettingsManager.class.getClassLoader().getResourceAsStream("default_config.json");
			try (FileOutputStream fos = new FileOutputStream(settingsFile)) {
				byte[] buf = new byte[2048];
				int r = ddlStream.read(buf);
				while (r != -1) {
					fos.write(buf, 0, r);
					r = ddlStream.read(buf);
				}
			} catch (IOException e) {
				Logger.error("Unable to create a new config-file: " + e.getMessage() + "\nExiting...");
				System.exit(1);
			}
		}

		// Unable to read the config-file
		if (!settingsFile.canRead()) {
			Logger.error("Unable to read config-file. Exiting...");
			System.exit(1);
		}

		// Read config-file into a String
		String settingsFileContent = "";
		try {
			settingsFileContent = new Scanner(settingsFile).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			Logger.error("Unable to read config-file. Exiting...");
			System.exit(1);
		}

		// Create a JSONObject from the config-file's String
		config = new JSONObject(settingsFileContent);
	}

	/**
	 * Saves the changed configuration in the opened JSON-file.
	 */
	private void saveSettings() {
		try {
			PrintWriter out = new PrintWriter(settingsFile);
			out.print(config.toString(4));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the url of a website according to it's id.
	 *
	 * @param id The website's id
	 * @return The website's url
	 */
	public String getUrlFromSettings(int id) {
		JSONObject websites = config.getJSONObject("websites");
		JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
		return websitesId.getString("url");
	}

	/**
	 * Get the check-interval of a website according to it's id.
	 *
	 * @param id The website's id
	 * @return The website's interval
	 */
	public int getIntervalFromSettings(int id) {
		JSONObject websites = config.getJSONObject("websites");
		JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
		return websitesId.getInt("interval");
	}

	/**
	 * Get to know whether the content-check of a website is enabled or not according to the website's id.
	 *
	 * @param id The website's id
	 * @return <code>true</code> if the content-check is enabled or <code>false</code> if it is not.
	 */
	public boolean getCheckContentFromSettings(int id) {
		JSONObject websites = config.getJSONObject("websites");
		JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
		return websitesId.getBoolean("checkContent");
	}

	/**
	 * Get to know whether the ping-check of a website is enabled or not according to the website's id.
	 *
	 * @param id The website's id
	 * @return <code>true</code> if the ping-check is enabled or <code>false</code> if it is not.
	 */
	public boolean getCheckPingFromSettings(int id) {
		JSONObject websites = config.getJSONObject("websites");
		JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
		return websitesId.getBoolean("checkPing");
	}

	/**
	 * Get the number of active website-checks.
	 *
	 * @return Number of active website-checks
	 */
	public int getCheckerCountFromSettings() {
		return config.getInt("activeWebsites");
	}

	/**
	 * Get to know whether log-file-creation is enabled or not.
	 *
	 * @return <code>true</code> if log-file-creation is enabled or <code>false</code> if it is not.
	 */
	public boolean getCreateLogFromSettings() {
		JSONObject logFile = config.getJSONObject("logFile");
		return logFile.getBoolean("create");
	}

	/**
	 * Get to know whether logging of successful checks is enabled or not.
	 *
	 * @return <code>true</code> if logging of successful checks is enabled or <code>false</code> if it is not.
	 */
	public boolean getCreateValidLogFromSettings() {
		JSONObject logFile = config.getJSONObject("logFile");
		return logFile.getBoolean("valid");
	}

	/**
	 * Get to know whether autorun is enabled or not.
	 *
	 * @return <code>true</code> if autorun is enabled or <code>false</code> if it is not.
	 */
	public boolean getAutorunFromSettings() {
		return config.getBoolean("autorun");
	}

	/**
	 * Get to know whether the GUI has to show message-bubbles or not.
	 *
	 * @return <code>true</code> if the GUI has to show message-bubbles or <code>false</code> if not.
	 */
	public boolean getShowBubblesSettings() {
		return config.getBoolean("showBubbles");
	}

	/**
	 * Set the url of a website according to it's id.
	 *
	 * @param id    The website's id
	 * @param value The website's url
	 */
	public void setUrlForSettings(int id, String value) {
		JSONObject websites = config.getJSONObject("websites");
		JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
		websitesId.put("url", value);

		saveSettings();
	}

	/**
	 * Set the check-interval of a website according to it's id.
	 *
	 * @param id    The website's id
	 * @param value The website's interval
	 */
	public void setIntervalForSettings(int id, int value) {
		JSONObject websites = config.getJSONObject("websites");
		JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
		websitesId.put("interval", value);

		saveSettings();
	}

	/**
	 * Set whether the content-check of a website is enabled or not according to the website's id.
	 *
	 * @param id    The website's id
	 * @param value <code>true</code> to enable and <code>false</code> to disable the content-check.
	 */
	public void setCheckContentForSettings(int id, boolean value) {
		JSONObject websites = config.getJSONObject("websites");
		JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
		websitesId.put("checkContent", value);

		saveSettings();
	}

	/**
	 * Set whether the ping-check of a website is enabled or not according to the website's id.
	 *
	 * @param id    The website's id
	 * @param value <code>true</code> to enable and <code>false</code> to disable the ping-check.
	 */
	public void setCheckPingForSettings(int id, boolean value) {
		JSONObject websites = config.getJSONObject("websites");
		JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
		websitesId.put("checkPing", value);

		saveSettings();
	}

	/**
	 * Set the number of active website-checks.
	 *
	 * @param value Number of active website-checks
	 */
	public void setCheckerCountForSettings(int value) {
		config.put("activeWebsites", value);

		saveSettings();
	}

	/**
	 * Set whether log-file-creation is enabled or not.
	 *
	 * @param value <code>true</code> to enable and <code>false</code> to disable log-file-creation.
	 */
	public void setCreateLogForSettings(boolean value) {
		JSONObject logFile = config.getJSONObject("logFile");
		logFile.put("create", value);

		saveSettings();
	}

	/**
	 * Set whether logging of successful checks is enabled or not.
	 *
	 * @param value <code>true</code> to enable and <code>false</code> to disable logging of successful checks.
	 */
	public void setCreateValidLogForSettings(boolean value) {
		JSONObject logFile = config.getJSONObject("logFile");
		logFile.put("create", value);

		saveSettings();
	}

	/**
	 * Set whether autorun is enabled or not.
	 *
	 * @param value <code>true</code> to enable and <code>false</code> to disable autorun.
	 */
	public void setAutorunForSettings(boolean value) {
		config.put("autorun", value);

		saveSettings();
	}

	/**
	 * Set whether the GUI has to show message-bubbles or not.
	 *
	 * @param value <code>true</code> to enable and <code>false</code> to disable message-bubbles.
	 */
	public void setShowBubblesSettings(boolean value) {
		config.put("showBubbles", value);

		saveSettings();
	}
}