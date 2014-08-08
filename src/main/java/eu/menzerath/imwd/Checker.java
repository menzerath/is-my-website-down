package eu.menzerath.imwd;

import eu.menzerath.util.Helper;
import eu.menzerath.util.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class Checker {
    public final int ID;
    public final String URL;
    public final int INTERVAL;
    public final boolean CHECK_CONTENT;
    public final boolean CHECK_PING;

    private Logger logger;
    private Timer timer;

    /**
     * Put the specified values in our own parameters
     *
     * @param id             Unique ID of this Checker
     * @param url            Which url will get checked
     * @param interval       How often will this url get checked (in seconds)
     * @param checkContent   If this Checker checks content
     * @param checkPing      If this Checker checks ping
     * @param logEnabled     Create a log-file?
     * @param logValidChecks Log even successful checks?
     * @param updateGui      Do we have to update a GUI?
     */
    public Checker(int id, String url, int interval, boolean checkContent, boolean checkPing, boolean logEnabled, boolean logValidChecks, boolean updateGui) {
        this.ID = id;
        this.URL = url;
        this.INTERVAL = interval;
        this.CHECK_CONTENT = checkContent;
        this.CHECK_PING = checkPing;

        this.logger = new Logger(this, logEnabled, logValidChecks, updateGui);
    }

    /**
     * Start the testing:
     * Create a timer and schedule it
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
     * Stop the testing:
     * Cancel and purge the timer
     */
    public void stopTesting() {
        timer.cancel();
        timer.purge();
    }

    /**
     * The actual testing-method:
     * First, try to get content from the site
     *   Successful? Log this and we're done!
     *   No Success? Try to reach Google to see if there are problems with the internet connection
     *     Successful? Ping the url to see, if the webserver is down
     *       Successful? Webserver is down. Log this and we're done!
     *       No Success? Whole server is down. Log this and we're done!
     *     No Success? No connection to the internet! Log this and we're done!
     * Every exception will be ignored!
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
        }
    }

    /**
     * First test: Check if there is any content at the url
     *
     * @return Is there any content at the url?
     */
    public boolean testContent() {
        try {
            URLConnection myConnection = new URL(this.URL).openConnection();
            myConnection.setRequestProperty("User-Agent", "IsMyWebsiteDown/" + Main.VERSION + " (" + Main.URL + ")");
            myConnection.setRequestProperty("Connection", "close");

            BufferedReader in = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
            if (in.readLine() != null) {
                in.close();
                return true;
            } else {
                in.close();
                return false;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Second test: Check if a ping is successful
     *
     * @return Is a ping successful?
     */
    public boolean testPing() {
        try {
            String cmd;
            if (System.getProperty("os.name").startsWith("Windows")) {
                cmd = "ping -n 1 -w 3000 " + getUrlWithoutProtocol();
            } else {
                cmd = "ping -c 1 " + getUrlWithoutProtocol();
            }

            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();
            return myProcess.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUrlWithoutProtocol() {
        String myUrl = this.URL;
        for (String p : Main.PROTOCOLS) {
            myUrl = myUrl.replace(p, "");
        }
        return myUrl;
    }
}