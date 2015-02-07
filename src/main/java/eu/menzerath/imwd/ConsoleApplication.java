package eu.menzerath.imwd;

import eu.menzerath.imwd.checker.Checker;
import eu.menzerath.util.Helper;
import eu.menzerath.util.Logger;
import eu.menzerath.util.Messages;
import eu.menzerath.util.Updater;
import org.fusesource.jansi.Ansi;

import java.util.Scanner;

public class ConsoleApplication {
    private String url;
    private int interval;
    private boolean createLog;

    /**
     * Validate the values, save them and run the checker
     *
     * @param url       URL to check
     * @param interval  Interval to check
     * @param createLog Create a logfile or not
     */
    public ConsoleApplication(String url, String interval, boolean createLog) {
        Main.sayHello();

        // Run the update-check
        updateCheck();

        boolean checkUrl = Helper.validateUrlInput(url);
        boolean checkInterval = Helper.validateIntervalInput(Helper.parseInt(interval));

        if (!checkUrl && Helper.validateUrlInput("http://" + url)) {
            this.url = "http://" + url;
            Logger.warning(Messages.USING_HTTP);
        } else {
            this.url = url;
        }

        if (checkInterval) {
            this.interval = Helper.parseInt(interval);
            this.createLog = createLog;
            run();
        } else {
            Logger.error(Messages.INVALID_PARAMETERS);
            System.exit(1);
        }
    }

    /**
     * This is the main-method which will take the values from the preferences and start directly.
     */
    private void run() {
        // Display the used values
        System.out.println(new Ansi().bold().a(Messages.CONSOLE_START).boldOff());
        System.out.println("URL:      " + url);
        System.out.println("Interval: " + interval + "s");
        System.out.println("Log-File: " + createLog + "\n");

        // Create the Checker and go!
        Checker checker = new Checker(0, url, interval, true, true, createLog, true);
        checker.startTesting();
    }

    /**
     * An update-check for the "ConsoleApplication": If there is an update available, it will show an url to get the update.
     */
    private void updateCheck() {
        Updater myUpdater = new Updater();
        if (myUpdater.getServerVersion().equalsIgnoreCase("Error")) {
            Logger.error(Messages.UPDATE_ERROR + "\n");
        } else if (myUpdater.getServerVersion().equals("SNAPSHOT")) {
            Logger.warning(Messages.UPDATE_SNAPSHOT + "\n");
        } else if (myUpdater.isUpdateAvailable()) {
            Logger.info(Messages.UPDATE_AVAILABLE.replace("%version", myUpdater.getServerVersion()) + "\n" +
                    Messages.UPDATE_AVAILABLE_CHANGES.replace("%changes", myUpdater.getServerChangelog()) + "\n" +
                    "Press \"y\" to download it now or any other key to delay this update: ");

            Scanner sc = new Scanner(System.in);
            if (sc.next().equalsIgnoreCase("y")) {
                new eu.menzerath.imwd.updater.Updater(false);
            } else {
                System.out.println("");
            }
        }
    }
}