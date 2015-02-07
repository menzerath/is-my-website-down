package eu.menzerath.imwd;

import eu.menzerath.util.Helper;
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
            System.out.println(new Ansi().bold().fg(Ansi.Color.YELLOW).a("[WARNING]").reset() + " " + Messages.USING_HTTP);
        } else {
            this.url = url;
        }

        if (checkInterval) {
            this.interval = Helper.parseInt(interval);
            this.createLog = createLog;
            run();
        } else {
            System.out.println(new Ansi().bold().fg(Ansi.Color.RED).a("[ERROR]").reset() + " " + Messages.INVALID_PARAMETERS);
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
        Ansi a = new Ansi();

        Updater myUpdater = new Updater();
        if (myUpdater.getServerVersion().equalsIgnoreCase("Error")) {
            System.out.println(a.fg(Ansi.Color.RED).bold().a("[ERROR]").reset() + " " + Messages.UPDATE_ERROR + "\n");
        } else if (myUpdater.getServerVersion().equals("SNAPSHOT")) {
            System.out.println(a.fg(Ansi.Color.YELLOW).bold().a("[WARNING]").reset() + " " + Messages.UPDATE_SNAPSHOT + "\n");
        } else if (myUpdater.isUpdateAvailable()) {
            System.out.println(a.fg(Ansi.Color.CYAN).bold().a("[INFO]").reset() + " " + Messages.UPDATE_AVAILABLE.replace("%version", myUpdater.getServerVersion()));
            System.out.println(Messages.UPDATE_AVAILABLE_CHANGES.replace("%changes", myUpdater.getServerChangelog()));

            System.out.println("Press \"y\" to download it now or any other key to delay this update: ");
            Scanner sc = new Scanner(System.in);
            if (sc.next().equalsIgnoreCase("y")) {
                new eu.menzerath.imwd.updater.Updater(false);
            } else {
                System.out.println("");
            }
        }
    }
}