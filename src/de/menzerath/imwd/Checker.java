package de.menzerath.imwd;

import de.menzerath.util.Logger;

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
     * Successful? Log this and we're done!
     * No Success? Try to reach Google to see if there are problems with the internet connection
     * Successful? Ping the url to see, if the webserver is down
     * Successful? Webserver is down. Log this and we're done!
     * No Success? Whole server is down. Log this and we're done!
     * No Success? No connection to the internet! Log this and we're done!
     * Every exception will be ignored!
     */
    public void runTest() {
        if (CHECK_CONTENT && CHECK_PING) {
            if (testContent(this.URL)) {
                logger.ok();
            } else {
                if (testConnection()) {
                    if (testPing(this.URL)) {
                        logger.warning();
                    } else {
                        logger.error();
                    }
                } else {
                    logger.noConnection();
                }
            }
        } else if (CHECK_CONTENT) {
            if (testContent(this.URL)) {
                logger.ok();
            } else {
                if (testConnection()) {
                    logger.error();
                } else {
                    logger.noConnection();
                }
            }
        } else if (CHECK_PING) {
            if (testPing(this.URL)) {
                logger.ok();
            } else {
                if (testConnection()) {
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
     * @param url URL to check
     * @return Is there any content at the url?
     */
    private boolean testContent(String url) {
        try {
            URLConnection myConnection = new URL(url).openConnection();
            myConnection.setRequestProperty("User-Agent", "IsMyWebsiteDown-Checker/" + Main.getVersion());

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
     * @param url URL to check
     * @return Is a ping successful?
     */
    private boolean testPing(String url) {
        try {
            String cmd;
            if (System.getProperty("os.name").startsWith("Windows")) {
                cmd = "ping -n 1 -w 3000 " + url.replace("http://", "");
            } else {
                cmd = "ping -c 1 " + url.replace("http://", "");
            }

            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();
            return myProcess.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Third (optional) test: Check if there is a connection to the internet
     *
     * @return Is a connection to the internet available?
     */
    private boolean testConnection() {
        return testContent("http://google.com");
    }
}