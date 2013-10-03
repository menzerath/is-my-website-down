package de.menzerath.imwd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;

// Needs rewrite! - Someone? Please? :)
public class ConsoleApplication {
	/**
	 * Starting an "ConsoleApplication" - "Is My Website Down?" will run only on the commandline!
	 * @param startWithSettings Use saved settings or ask for values
	 */
	public ConsoleApplication(boolean startWithSettings) {
		if (startWithSettings) {
			runWithSettings();
		} else {
			run();
		}
	}

	/**
	 * This is the main-method, which will ask for every value and start the checks after this.
	 * You can stop it by typing "stop" on the console.
	 */
	private void run() {
		// Declare and init our variables
		String url = "";
		int interval;
		boolean createLog = false;
		boolean createValidLog = false;

		// Run the update-check
		runUpdateCheck();

		// Create a BufferedReaders
		BufferedReader buf = new BufferedReader (new InputStreamReader(System.in));

		// ####################
		// ### Enter an url ###
		// ####################
		System.out.print("Enter the URL to check [" + Main.getUrlFromSettings() + "]: http://");
		try {
			url = buf.readLine();
		} catch (IOException ignored) {}

		if (url.equals("")) {
			url = Main.getUrlFromSettings();
		} else {
			url = "http://" + url;
		}

		// #########################
		// ### Enter an interval ###
		// #########################
		System.out.print("Enter an interval (in seconds) [" + Main.getIntervalFromSettings() + "]: ");
		try {
			interval = Integer.parseInt(buf.readLine());
		} catch (NumberFormatException | InputMismatchException | IOException ignored) {
			interval = Main.getIntervalFromSettings();
		}

		// #####################
		// ### Create a log? ###
		// #####################
		// Get value from settings and convert it to "yes" or "no"
		boolean preCreateLog = Main.getCreateLogFromSettings();
		String preCreateLogString;
		if (preCreateLog) {
			preCreateLogString = "yes";
		} else {
			preCreateLogString = "no";
		}

		// Actual enter
		System.out.print("Create a log-file? [" + preCreateLogString + "]: ");
		String createLogString = "";
		try {
			createLogString = buf.readLine();
		} catch (IOException ignored) {}

		if (createLogString.equalsIgnoreCase("yes") || createLogString.equalsIgnoreCase("y")) {
			createLog = true;
		} else if (createLogString.equalsIgnoreCase("no") || createLogString.equalsIgnoreCase("n")) {
			createLog = false;
		} else if (createLogString.equals("")) {
			createLog = Main.getCreateLogFromSettings();
		}

		// Only if the previous question was answered with "yes"
		if (createLog) {
			// ##############################
			// ### Log successful checks? ###
			// ##############################
			// Get value from settings and convert it to "yes" or "no"
			boolean preCreateValidLog = Main.getCreateValidLogFromSettings();
			String preCreateValidLogString;
			if (preCreateValidLog) {
				preCreateValidLogString = "yes";
			} else {
				preCreateValidLogString = "no";
			}

			// Actual enter
			System.out.print("Log valid checks? [" + preCreateValidLogString+ "]: ");
			String createValidLogString = "";
			try {
				createValidLogString = buf.readLine();
			} catch (IOException ignored) {}

			if (createValidLogString.equalsIgnoreCase("yes") || createValidLogString.equalsIgnoreCase("y")) {
				createValidLog = true;
			} else if (createValidLogString.equalsIgnoreCase("no") || createValidLogString.equalsIgnoreCase("n")) {
				createValidLog = false;
			} else if (createValidLogString.equals("")) {
				createValidLog = Main.getCreateValidLogFromSettings();
			}
		}

		// ######################################
		// ### Revision of the entered values ###
		// ######################################
		System.out.println("\nYour settings:");
		System.out.println("URL: " + url);
		System.out.println("Interval: " + interval + " seconds");
		System.out.println("Log-File: " + Boolean.toString(createLog));
		System.out.println("Log valid checks: " + Boolean.toString(createValidLog) + "\n");
		System.out.print("Can we start? (Type \"stop\" to exit IMWD) [yes]: ");

		String settingsOKString = "";
		try {
			settingsOKString = buf.readLine();
		} catch (IOException ignored) {}

		// Not ok? Exit!
		if (settingsOKString.equalsIgnoreCase("no") || settingsOKString.equalsIgnoreCase("n")) {
			System.out.println("Let's start again...");
			System.exit(0);
		}

		// Save the entered values
		Main.setUrlForSettings(url);
		Main.setIntervalForSettings(interval);
		Main.setCreateLogForSettings(createLog);
		Main.setCreateValidLogForSettigs(createValidLog);

		System.out.println("");

		// Create the Checker and go!
		Checker checker = new Checker(url, interval, createLog, createValidLog, false);
		checker.startTesting();

		// Add option to exit "Is My Website Down?" by typing "stop"
		boolean working = true;
		while (working) { // Not good! :(
			try {
				if (buf.readLine().equalsIgnoreCase("stop")) {
					checker.stopTesting();
					working = false;

					try {
						buf.close();
					} catch (IOException ignored) {}
				}
			} catch (IOException ignored) {}
		}

		System.exit(0);
	}

	/**
	 * This is the second main-method which will take the values from the preferences and start directly.
	 */
	private void runWithSettings() {
		// Declare and init our variables
		String url = Main.getUrlFromSettings();
		int interval = Main.getIntervalFromSettings();
		boolean createLog = Main.getCreateLogFromSettings();
		boolean createValidLog = Main.getCreateValidLogFromSettings();

		// Run the update-check
		runUpdateCheck();

		// ############################
		// ### Show the used values ###
		// ############################
		System.out.println("\nStarting with the following settings:");
		System.out.println("URL: " + url);
		System.out.println("Interval: " + interval + " seconds");
		System.out.println("Log-File: " + Boolean.toString(createLog));
		System.out.println("Log valid checks: " + Boolean.toString(createValidLog) + "\n");

		// Create the Checker and go!
		Checker checker = new Checker(url, interval, createLog, createValidLog, false);
		checker.startTesting();

		// Add option to exit "Is My Website Down?" by typing "stop"
		BufferedReader buf = new BufferedReader (new InputStreamReader(System.in));

		boolean working = true;
		while (working) { // Not good! :(
			try {
				if (buf.readLine().equalsIgnoreCase("stop")) {
					checker.stopTesting();
					working = false;

					try {
						buf.close();
					} catch (IOException ignored) {}
				}
			} catch (IOException ignored) {}
		}

		System.exit(0);
	}

	/**
	 * An update-check for the "ConsoleApplication": If there is an update available, it will stop and show an url to get the update.
	 */
	private void runUpdateCheck() {
		if (Main.getServerVersion().equalsIgnoreCase("Error")) {
			System.out.println("Unable to search for Updates. Please visit \"http://marvin-menzerath.de/software/imwd/\"." + "\n");
		} else if (Main.isUpdateAvailable()) {
			System.out.println("There is an update to version " + Main.getServerVersion() + " available.");
			System.out.println("Changelog: " + Main.getServerChangelog());
			System.out.println("Please download it now by using \"wget http://marvin-menzerath.de/download/imwd.jar\"." + "\n");

			System.exit(0);
		}
	}
}