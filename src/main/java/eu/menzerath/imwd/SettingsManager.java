package eu.menzerath.imwd;

import eu.menzerath.util.Logger;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Scanner;

public class SettingsManager {
    private File settingsFile;
    private JSONObject config;

    public SettingsManager() {
        settingsFile = new File(System.getProperty("user.home") + FileSystems.getDefault().getSeparator() + "ismywebsitedown.json");
        Logger.info("Using this config-file: " + settingsFile.getAbsolutePath());

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

        if (!settingsFile.canRead()) {
            Logger.error("Unable to read config-file. Exiting...");
            System.exit(1);
        }

        String settingsFileContent = "";
        try {
            settingsFileContent = new Scanner(settingsFile).useDelimiter("\\A").next();
        } catch (FileNotFoundException e) {
            Logger.error("Unable to read config-file. Exiting...");
            System.exit(1);
        }

        config = new JSONObject(settingsFileContent);
    }

    private void saveSettings() {
        try {
            PrintWriter out = new PrintWriter(settingsFile);
            out.print(config.toString(4));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * BEGIN: Settings-Getter
     */
    public String getUrlFromSettings(int id) {
        JSONObject websites = config.getJSONObject("websites");
        JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
        return websitesId.getString("url");
    }

    public int getIntervalFromSettings(int id) {
        JSONObject websites = config.getJSONObject("websites");
        JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
        return websitesId.getInt("interval");
    }

    public boolean getCheckContentFromSettings(int id) {
        JSONObject websites = config.getJSONObject("websites");
        JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
        return websitesId.getBoolean("checkContent");
    }

    public boolean getCheckPingFromSettings(int id) {
        JSONObject websites = config.getJSONObject("websites");
        JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
        return websitesId.getBoolean("checkPing");
    }

    public int getCheckerCountFromSettings() {
        return config.getInt("activeWebsites");
    }

    public boolean getCreateLogFromSettings() {
        JSONObject logFile = config.getJSONObject("logFile");
        return logFile.getBoolean("create");
    }

    public boolean getCreateValidLogFromSettings() {
        JSONObject logFile = config.getJSONObject("logFile");
        return logFile.getBoolean("valid");
    }

    public boolean getAutorunFromSettings() {
        return config.getBoolean("autorun");
    }

    public boolean getShowBubblesSettings() {
        return config.getBoolean("showBubbles");
    }
    /*
     * END: Settings-Getter
     */

    /*
     * BEGIN: Settings-Setter
     */
    public void setUrlForSettings(int id, String value) {
        JSONObject websites = config.getJSONObject("websites");
        JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
        websitesId.put("url", value);

        saveSettings();
    }

    public void setIntervalForSettings(int id, int value) {
        JSONObject websites = config.getJSONObject("websites");
        JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
        websitesId.put("interval", value);

        saveSettings();
    }

    public void setCheckContentForSettings(int id, boolean value) {
        JSONObject websites = config.getJSONObject("websites");
        JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
        websitesId.put("checkContent", value);

        saveSettings();
    }

    public void setCheckPingForSettings(int id, boolean value) {
        JSONObject websites = config.getJSONObject("websites");
        JSONObject websitesId = websites.getJSONObject(String.valueOf(id + 1));
        websitesId.put("checkPing", value);

        saveSettings();
    }

    public void setCheckerCountForSettings(int value) {
        config.put("activeWebsites", value);

        saveSettings();
    }

    public void setCreateLogForSettings(boolean value) {
        JSONObject logFile = config.getJSONObject("logFile");
        logFile.put("create", value);

        saveSettings();
    }

    public void setCreateValidLogForSettings(boolean value) {
        JSONObject logFile = config.getJSONObject("logFile");
        logFile.put("create", value);

        saveSettings();
    }

    public void setAutorunForSettings(boolean value) {
        config.put("autorun", value);

        saveSettings();
    }

    public void setShowBubblesSettings(boolean value) {
        config.put("showBubbles", value);

        saveSettings();
    }
    /*
     * END: Settings-Setter
     */
}