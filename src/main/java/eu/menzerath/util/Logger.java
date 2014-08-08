package eu.menzerath.util;

import eu.menzerath.imwd.Checker;
import eu.menzerath.imwd.GuiApplication;

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
     * @param checker        the Logger's "parent"
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
     * The Checker started it's work
     */
    public void start() {
        write(getLogHead() + "[INFO] " + Messages.LOG_START.replace("%url", CHECKER.URL).replace("%interval", "" + CHECKER.INTERVAL));
    }

    /**
     * This was a successful check
     */
    public void ok() {
        if (LOG_VALID_CHECKS) write(getLogHead() + "[OK] " + Messages.LOG_OK);
        updateGui(1);
    }

    /**
     * This is only a warning - it might get worse
     */
    public void warning() {
        write(getLogHead() + "[WARNING] " + Messages.LOG_PING_ONLY);
        updateGui(2);
    }

    /**
     * Website is gone
     */
    public void error() {
        write(getLogHead() + "[ERROR] " + Messages.LOG_ERROR);
        updateGui(3);
    }

    /**
     * There is no connection to the internet
     */
    public void noConnection() {
        // Print this only once, but update the GUI!
        if (CHECKER.ID != 0) {
            updateGui(4);
            return;
        }
        write(getLogHead().replace("[0]", "[A]") + "[ERROR] " + Messages.LOG_NO_CONNECTION);
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
            File file = new File("imwd_" + CHECKER.getUrlWithoutProtocol() + ".txt");
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream(file, true));
                out.append(message.replace("[" + CHECKER.ID + "] ", "")).append("\r\n");
                out.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void updateGui(int status) {
        if (UPDATE_GUI) {
            if (status == 4 && CHECKER.ID != 1) {
                GuiApplication.updateTrayIcon(CHECKER, status, false);
            } else {
                GuiApplication.updateTrayIcon(CHECKER, status, true);
            }
        }
    }

    private String getLogHead() {
        // +1 to be more user-friendly
        return "[" + (CHECKER.ID + 1) + "] [" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] ";
    }
}