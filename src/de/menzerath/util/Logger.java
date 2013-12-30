package de.menzerath.util;

import de.menzerath.imwd.Checker;
import de.menzerath.imwd.GuiApplication;
import de.menzerath.imwd.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private final Checker CHECKER;
    private final boolean CREATE_FILE;
    private final boolean LOG_VALID_CHECKS;
    private final boolean UPDATE_GUI;

    /**
     * Put the specified values in our own parameters
     *
     * @param checker        "Logger's parent"
     * @param createFile     Create a log-file?
     * @param logValidChecks Log even successful checks?
     * @param updateGui      Do we have to update a GUI?
     */
    public Logger(Checker checker, boolean createFile, boolean logValidChecks, boolean updateGui) {
        this.CHECKER = checker;
        this.CREATE_FILE = createFile;
        this.LOG_VALID_CHECKS = logValidChecks;
        this.UPDATE_GUI = updateGui;
    }

    /**
     * Shows an informative message about "Is My Website Down?" on start
     */
    public static void sayHello() {
        System.out.println("##################################################");
        System.out.println("### Is My Website Down? v" + Main.getVersion() + "                 ###");
        System.out.println("###                                            ###");
        System.out.println("### © 2012-2014: Marvin Menzerath              ###");
        System.out.println("### github.com/MarvinMenzerath/IsMyWebsiteDown ###");
        System.out.println("##################################################\n");
    }

    /**
     * The Checker started its work
     */
    public void start() {
        write(getLogHead() + "[INFO] Checking " + CHECKER.URL + " every " + CHECKER.INTERVAL + " seconds.");
    }

    /**
     * This was a successful check
     */
    public void ok() {
        if (LOG_VALID_CHECKS) write(getLogHead() + "[OK] Everything OK.");
        updateGui(1);
    }

    /**
     * This is only a warning - it might get worse
     */
    public void warning() {
        write(getLogHead() + "[WARNING] Only a Ping was successful.");
        updateGui(2);
    }

    /**
     * Website is gone
     */
    public void error() {
        write(getLogHead() + "[ERROR] Unable to reach the website.");
        updateGui(3);
    }

    /**
     * There is no connection to the internet
     */
    public void noConnection() {
        // Print this only once, but update the GUI!
        if (CHECKER.ID != 1) {
            updateGui(4);
            return;
        }

        write(getLogHead().replace("[1]", "[X]") + "[ERROR] No connection to the internet.");
        updateGui(4);
    }

    /**
     * Print this message to the console and add it to the log-file
     *
     * @param message Message to print/output
     */
    private void write(String message) {
        System.out.println(message);

        if (CREATE_FILE) {
            File file = new File("imwd_" + CHECKER.URL.replace("http://", "") + ".txt");
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream(file, true));
                out.append(message).append("\r\n");
                out.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void updateGui(int status) {
        if (UPDATE_GUI) GuiApplication.setNotification(CHECKER.ID, CHECKER.URL.replace("http://", ""), status);
    }

    private String getLogHead() {
        return "[" + CHECKER.ID + "] [" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] ";
    }
}