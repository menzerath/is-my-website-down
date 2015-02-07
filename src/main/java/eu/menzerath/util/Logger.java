package eu.menzerath.util;

import org.fusesource.jansi.Ansi;

public class Logger {

    public static void info(String message) {
        System.out.println(new Ansi().fg(Ansi.Color.CYAN).bold().a("[INFO]").reset() + " " + message);
    }

    public static void warning(String message) {
        System.out.println(new Ansi().fg(Ansi.Color.YELLOW).bold().a("[WARNING]").reset() + " " + message);
    }

    public static void error(String message) {
        System.out.println(new Ansi().fg(Ansi.Color.RED).bold().a("[ERROR]").reset() + " " + message);
    }
}