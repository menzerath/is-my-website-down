package eu.menzerath.imwd;

import eu.menzerath.util.Helper;
import eu.menzerath.util.Messages;

public class QuickTest {
    private String url;

    /**
     * Validate the url and run the checker
     *
     * @param url URL to check
     */
    public QuickTest(String url) {
        if (Helper.validateUrlInput(url)) {
            this.url = url;
            run();
        } else {
            System.out.println(Messages.INVALID_PARAMETERS);
            System.exit(1);
        }
    }

    /**
     * Runs a test and prints a message on the console
     */
    private void run() {
        // Create the Checker and go!
        Checker checker = new Checker(1, url, 30, true, true, false, false, false);

        if (checker.testContent(this.url)) {
            System.out.println("OK");
        } else {
            if (checker.testConnection()) {
                if (checker.testPing(this.url)) {
                    System.out.println("Ping Only");
                } else {
                    System.out.println("Not Reachable");
                }
            } else {
                System.out.println("No Connection");
            }
        }
    }
}