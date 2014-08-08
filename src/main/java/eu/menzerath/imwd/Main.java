package eu.menzerath.imwd;

import eu.menzerath.util.Messages;

import java.awt.*;

public class Main {
    public static final String APPLICATION = "Is My Website Down?";
    public static final String APPLICATION_SHORT = "IMWD";
    public static final String VERSION = "2.2-SNAPSHOT";
    public static final String URL = "https://github.com/MarvinMenzerath/IsMyWebsiteDown";
    public static final String URL_RELEASE = "https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases";
    public static final String[] PROTOCOLS = {"http://", "https://"};
    public static final int MIN_INTERVAL = 10;
    public static final int MAX_INTERVAL = 300;

    /**
     * The Beginning!
     * If arguments were passed they will be handled, otherwise it will start a ConsoleApplication OR the GuiApplication.
     *
     * @param args Passed arguments for start
     */
    public static void main(String[] args) {
        if (args.length == 0) { // Running on a setup without graphical desktop and no arguments passed: show the needed arguments
            if (GraphicsEnvironment.isHeadless()) {
                System.out.println(Messages.CONSOLE_HELP);
                System.exit(1);
            } else { // Otherwise: Open the GUI
                GuiApplication.startGUI();
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("--help")) { // Give the user some help
            sayHello();
            System.out.println(Messages.CONSOLE_HELP);
        } else if (args.length == 2) { // Start a ConsoleApplication and create a Log-File
            new ConsoleApplication(args[0].trim(), args[1].trim(), true);
        } else if (args.length == 3 && args[2].equalsIgnoreCase("--nolog")) { // Start a ConsoleApplication but do not create a Log-File
            new ConsoleApplication(args[0].trim(), args[1].trim(), false);
        } else if (args.length == 3 && args[2].equalsIgnoreCase("--once")) { // Run a QuickTest
            new QuickTest(args[0].trim()).run();
        } else { // Something went wrong (blame the user!)
            sayHello();
            System.out.println(Messages.CONSOLE_HELP);
        }
    }

    /**
     * Shows an informative message about "Is My Website Down?" on start
     */
    public static void sayHello() {
        String lineVersion = APPLICATION + " v" + VERSION;
        for (int i = lineVersion.length(); i < 42; i++) lineVersion += " ";

        System.out.println("##################################################");
        System.out.println("### " + lineVersion + " ###");
        System.out.println("###                                            ###");
        System.out.println("### " + Messages.COPYRIGHT + "              ###");
        System.out.println("### " + URL.substring(8) + " ###");
        System.out.println("##################################################\n");
    }
}