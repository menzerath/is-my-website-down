package eu.menzerath.util;

import org.fusesource.jansi.Ansi;

/**
 * This is the Logger-class to be used by any other class for outputting informative messages, warnings and errors.
 * It also utilizes Ansi for a colored output.
 */
public class Logger {

    /**
     * New log-entry: an informative message - nothing to worry about
     *
     * @param message Message to log
     */
    public static void info(String message) {
        System.out.println(new Ansi().fg(Ansi.Color.CYAN).bold().a("[INFO]").reset() + " " + message);
    }

    /**
     * New log-entry: a warning - something may be broken
     * @param message Message to log
     */
    public static void warning(String message) {
        System.out.println(new Ansi().fg(Ansi.Color.YELLOW).bold().a("[WARNING]").reset() + " " + message);
    }

    /**
     * New log-entry: an error - there is clearly something wrong
     * @param message Message to log
     */
    public static void error(String message) {
        System.out.println(new Ansi().fg(Ansi.Color.RED).bold().a("[ERROR]").reset() + " " + message);
    }
}