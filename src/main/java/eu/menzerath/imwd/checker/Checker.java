package eu.menzerath.imwd.checker;

import eu.menzerath.imwd.GuiApplication;
import eu.menzerath.imwd.Main;
import eu.menzerath.util.Helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The heart of this application. A Checker manages a single url including and all it's checks and a Logger-instance.
 */
public class Checker {
    public final int ID;
    public final String URL;
    public final int INTERVAL;
    public final boolean CHECK_CONTENT;
    public final boolean CHECK_PING;

    private Logger logger;
    private Timer timer;

    /**
     * Constructor: Put the specified values in our own parameters
     *
     * @param id             Unique ID of this Checker
     * @param url            Which url will get checked
     * @param interval       How often will this url get checked (in seconds)
     * @param checkContent   If this Checker checks content
     * @param checkPing      If this Checker checks ping
     * @param logEnabled     Create a log-file?
     * @param logValidChecks Log even successful checks?
     */
    public Checker(int id, String url, int interval, boolean checkContent, boolean checkPing, boolean logEnabled, boolean logValidChecks) {
        this.ID = id;
        this.URL = url;
        this.INTERVAL = interval;
        this.CHECK_CONTENT = checkContent;
        this.CHECK_PING = checkPing;

        this.logger = new Logger(this, logEnabled, logValidChecks);
    }

    /**
     * Constructor: Put the specified values in our own parameters
     *
     * @param id             Unique ID of this Checker
     * @param url            Which url will get checked
     * @param interval       How often will this url get checked (in seconds)
     * @param checkContent   If this Checker checks content
     * @param checkPing      If this Checker checks ping
     * @param logEnabled     Create a log-file?
     * @param logValidChecks Log even successful checks?
     * @param gui            the GUI that needs to be updated
     */
    public Checker(int id, String url, int interval, boolean checkContent, boolean checkPing, boolean logEnabled, boolean logValidChecks, GuiApplication gui) {
        this.ID = id;
        this.URL = url;
        this.INTERVAL = interval;
        this.CHECK_CONTENT = checkContent;
        this.CHECK_PING = checkPing;

        this.logger = new Logger(this, logEnabled, logValidChecks, gui);
    }

    /**
     * Start testing: Create a timer and schedule it
     */
    public void startTesting() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runTest();
            }
        }, 0, (INTERVAL * 1000));

        logger.start();
    }

    /**
     * Stop testing: Cancel and purge the timer
     */
    public void stopTesting() {
        timer.cancel();
        timer.purge();
    }

    /**
     * The actual testing-method:
     * 1. Try to get content from the site
     *  1.1 Success: Log this and we're done!
     *  1.2: No Success: Try to reach Google to see if there are problems with the internet connection
     *   1.2.1: Success: Ping the url to see, if the webserver is down
     *    1.2.1.1: Success: Webserver is down. Log this and we're done!
     *    1.2.1.2: No Success: Whole server is down. Log this and we're done!
     *   1.2.2: No Success: No connection to the internet! Log this and we're done!
     */
    public void runTest() {
        if (CHECK_CONTENT && CHECK_PING) {
            if (testContent()) {
                logger.ok();
            } else {
                if (Helper.testWebConnection()) {
                    if (testPing()) {
                        logger.warning();
                    } else {
                        logger.error();
                    }
                } else {
                    logger.noConnection();
                }
            }
        } else if (CHECK_CONTENT) {
            if (testContent()) {
                logger.ok();
            } else {
                if (Helper.testWebConnection()) {
                    logger.error();
                } else {
                    logger.noConnection();
                }
            }
        } else if (CHECK_PING) {
            if (testPing()) {
                logger.ok();
            } else {
                if (Helper.testWebConnection()) {
                    logger.error();
                } else {
                    logger.noConnection();
                }
            }
        } else {
            logger.error();
        }
    }

    /**
     * Content-Test: Check if there is any content at the url (or more generally a response from the webserver)
     *
     * @return <code>true</code> if there was a response and <code>false</code> if not
     */
    public boolean testContent() {
        try {
            URLConnection myConnection = new URL(this.URL).openConnection();
            myConnection.setRequestProperty("User-Agent", Main.USER_AGENT);
            myConnection.setRequestProperty("Connection", "close");

            BufferedReader in = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
            if (in.readLine() != null) {
                in.close();
                return true;
            } else {
                in.close();
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Ping-Test: Check if a ping is successful
     *
     * @return <code>true</code> if there was a response and <code>false</code> if not
     */
    public boolean testPing() {
        try {
            String cmd;
            if (System.getProperty("os.name").startsWith("Windows")) {
                cmd = "ping -n 1 -w 3000 " + Helper.getUrlWithoutProtocol(URL);
            } else {
                cmd = "ping -c 1 " + Helper.getUrlWithoutProtocol(URL);
            }

            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();
            return myProcess.exitValue() == 0;
        } catch (Exception ignored) {
        }
        return false;
    }
}