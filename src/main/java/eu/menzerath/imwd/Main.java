package eu.menzerath.imwd;

import eu.menzerath.util.Messages;

import java.awt.*;

public class Main {
    public static final String APPLICATION = "Is My Website Down?";
    public static final String APPLICATION_SHORT = "IMWD";
    public static final String VERSION = "2.1.2-SNAPSHOT";
    public static final String URL = "https://github.com/MarvinMenzerath/IsMyWebsiteDown";
    public static final String URL_RELEASE = "https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases";
    public static final int MIN_INTERVAL = 10;
    public static final int MAX_INTERVAL = 300;

    /**
     * The Beginning!
     * If arguments were passed they will be handled, otherwise it will start a ConsoleApplication OR the GuiApplication.
     *
     * @param args Passed arguments for start
     */
    public static void main(String[] args) {
        sayHello();

        boolean createLog = true;
        if (args.length == 3 && args[2].equalsIgnoreCase("--nolog")) createLog = false;
        if (args.length > 1 && args.length < 4) {
            // Arguments passed, if they are the correct ones: create a new ConsoleApplication, otherwise show the user the needed arguments
            if (args[0].trim().startsWith("--url=") && args[1].trim().startsWith("--interval=")) {
                new ConsoleApplication(args[0].trim().substring(6), args[1].trim().substring(11), createLog);
            } else {
                System.out.println(Messages.INVALID_ARGUMENTS);
                System.exit(1);
            }
        } else {
            // Running on a setup without graphical desktop and no arguments passed: show the needed arguments
            if (GraphicsEnvironment.isHeadless()) {
                System.out.println(Messages.INVALID_ARGUMENTS);
                System.exit(1);
            } else {
                GuiApplication.startGUI();
            }
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