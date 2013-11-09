package de.menzerath.imwd;

public class ConsoleApplication {
    private String url;
    private int interval;

    /**
     * Validate the values, save them and run the checker
     *
     * @param url      URL to check
     * @param interval Interval to check
     */
    public ConsoleApplication(String url, String interval) {
        if (Helper.validateUrlInput(url, false) && Helper.validateIntervalInput(interval, false)) {
            this.url = url;
            this.interval = Helper.parseInt(interval);
            run();
        } else {
            System.exit(1);
        }
    }

    /**
     * This is the main-method which will take the values from the preferences and start directly.
     */
    private void run() {
        // Run the update-check
        runUpdateCheck();

        // Display the used values
        System.out.println("\nStarting with the following settings:");
        System.out.println("URL: " + url);
        System.out.println("Interval: " + interval + " seconds");
        System.out.println("Log-File: Yes");
        System.out.println("Log valid checks: Yes\n");

        // Create the Checker and go!
        Checker checker = new Checker(1, url, interval, true, true, true, true, false);
        checker.startTesting();
    }

    /**
     * An update-check for the "ConsoleApplication": If there is an update available, it will stop and show an url to get the update.
     */
    private void runUpdateCheck() {
        System.out.println("Checking for updates, please wait...");
        Updater myUpdater = new Updater();
        if (myUpdater.getServerVersion().equalsIgnoreCase("Error")) {
            System.out.println("Unable to search for Updates. Please visit \"https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases/\"." + "\n");
        } else if (myUpdater.isUpdateAvailable()) {
            System.out.println("There is an update to version " + myUpdater.getServerVersion() + " available.");
            System.out.println("Changes: " + myUpdater.getServerChangelog());
            System.out.println("Please download it now by using \"wget https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases/download/v" + myUpdater.getServerVersion() + "/IMWD.jar\".");
            System.exit(0);
        } else {
            System.out.println("Congrats, no update found!");
        }
    }
}