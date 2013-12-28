package de.menzerath.imwd;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Checker {
    private final int ID;
    private final String URL;
    private final int INTERVAL;
    private final boolean CHECK_CONTENT;
    private final boolean CHECK_PING;
    private final boolean LOG_ENABLED;
    private final boolean LOG_VALID_CHECKS;
    private final boolean GUI;
    private Timer timer;

    /**
     * Put the specified values in our own parameters
     *
     * @param pId             Unique ID of this Checker
     * @param pUrl            Which url will get checked
     * @param pInterval       How often will this url get checked (in seconds)
     * @param pLogEnabled     Create an log-file?
     * @param pLogValidChecks Log even successful checks?
     * @param pUsingGui       Do we have to update a GUI?
     */
    public Checker(int pId, String pUrl, int pInterval, boolean checkContent, boolean checkPing, boolean pLogEnabled, boolean pLogValidChecks, boolean pUsingGui) {
        this.ID = pId;
        this.URL = pUrl;
        this.INTERVAL = pInterval;
        this.CHECK_CONTENT = checkContent;
        this.CHECK_PING = checkPing;
        this.LOG_ENABLED = pLogEnabled;
        this.LOG_VALID_CHECKS = pLogValidChecks;
        this.GUI = pUsingGui;
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

        log(0);
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
                log(1);
            } else {
                if (testContent("http://google.com")) {
                    if (testPing(this.URL)) {
                        log(2);
                    } else {
                        log(3);
                    }
                } else {
                    log(4);
                }
            }
        } else if (CHECK_CONTENT) {
            if (testContent(this.URL)) {
                log(1);
            } else {
                if (testContent("http://google.com")) {
                    log(3);
                } else {
                    log(4);
                }
            }
        } else if (CHECK_PING) {
            if (testPing(this.URL)) {
                log(1);
            } else {
                if (testContent("http://google.com")) {
                    log(3);
                } else {
                    log(4);
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
     * This is the logger: It prints the information according to the status-integer as text on the console
     * and changes the notification-icon if running with an GUI
     *
     * @param status Specifies the test-result OR an current event
     */
    private void log(int status) {
        // If running with an GUI, set the notification-icon
        if (GUI) {
            GuiApplication.setNotification(this.ID, this.URL.replace("http://", ""), status);
        }

        // Build a message-string
        SimpleDateFormat df = new SimpleDateFormat(getDateFormat());
        String toLog = "[" + this.ID + "]";
        toLog += " [" + df.format(new Date()) + "]";
        if (status == 0) {
            toLog += " [INFO] Checking " + URL + " every " + INTERVAL + " seconds.";
        } else if (status == 1) {
            toLog += " [OK] Everything OK.";
        } else if (status == 2) {
            toLog += " [WARNING] Only a Ping was successful.";
        } else if (status == 3) {
            toLog += " [ERROR] Unable to reach the website.";
        } else if (status == 4) {
            toLog += " [ERROR] No connection to the internet.";
        }

        // Print the message on the console
        if (status == 1 && LOG_VALID_CHECKS) {
            System.out.println(toLog);
        } else if (status != 1) {
            System.out.println(toLog);
        }

        // Save the message in an file (if enabled)
        if (LOG_ENABLED && (status != 1 || LOG_VALID_CHECKS)) {
            File file = new File(getLogFileName());
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream(file, true));
                out.append(toLog).append("\r\n");
                out.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * This is the name of the created log file
     *
     * @return Log-file name
     */
    private String getLogFileName() {
        return "imwd_" + URL.replace("http://", "") + ".txt";
    }

    /**
     * This is the date-format which will be used in the logs
     *
     * @return Date-format
     */
    private String getDateFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}