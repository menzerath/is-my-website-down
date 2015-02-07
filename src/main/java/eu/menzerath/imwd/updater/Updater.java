package eu.menzerath.imwd.updater;

import eu.menzerath.imwd.Main;
import eu.menzerath.util.Logger;
import eu.menzerath.util.Messages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.CodeSource;

public class Updater {

    /**
     * The Updater: Downloads the new jar-file and starts it's cleaner
     */
    public Updater(boolean startNewVersion) {
        Logger.info("Starting Updater...");

        // get the current version
        String newVersion = new eu.menzerath.util.Updater().getServerVersion();
        if (newVersion.equalsIgnoreCase("Error") || newVersion.equalsIgnoreCase("SNAPSHOT")) {
            Logger.error(Messages.UPDATE_ERROR);
            System.exit(1);
        }

        // build paths
        String newFileUrl = Main.URL_RELEASE + "/download/v" + newVersion + "/" + Main.APPLICATION_SHORT + ".jar";
        String newFileName = Main.APPLICATION.replace(" ", "").replace("?", "") + "-" + newVersion + ".jar";

        // download the update
        Logger.info("Downloading " + newFileUrl + " to " + newFileName);
        try {
            ReadableByteChannel in = Channels.newChannel(new URL(newFileUrl).openStream());
            FileChannel out = new FileOutputStream(newFileName).getChannel();
            out.transferFrom(in, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            Logger.error("Unable to download " + newFileUrl + ": " + e.getMessage());
            System.exit(1);
        }

        // get this file's name
        String oldVersionName = "";
        try {
            CodeSource cSource = Updater.class.getProtectionDomain().getCodeSource();
            File oldVersion = new File(cSource.getLocation().toURI().getPath());
            oldVersionName = oldVersion.getName();
        } catch (URISyntaxException e) {
            Logger.error("Unable to get this file's name: " + e.getMessage());
        }

        // give other answers according to startNewVersion-boolean
        if (startNewVersion) {
            Logger.info("Starting " + newFileName);
        } else {
            Logger.info("Download successful. Exiting now.");
        }

        // start the new version's cleaner
        try {
            Runtime.getRuntime().exec("java -cp " + newFileName + " eu.menzerath.imwd.updater.Cleaner " + oldVersionName + " " + startNewVersion);
        } catch (IOException e) {
            Logger.error("Unable to start " + newFileName + ": " + e.getMessage());
        }

        System.exit(0);
    }
}