package eu.menzerath.imwd;

import eu.menzerath.util.Helper;
import eu.menzerath.util.Messages;
import org.fusesource.jansi.Ansi;

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
        } else {
            System.out.println(new Ansi().bold().fg(Ansi.Color.RED).a("Error: ").fg(Ansi.Color.DEFAULT).a(Messages.INVALID_PARAMETERS).boldOff());
            System.exit(1);
        }
    }

    /**
     * Runs a test and prints a message on the console
     */
    public String run() {
        // Create the Checker and go!
        String result;
        Checker checker = new Checker(1, url, 30, true, true, false, false, false);

        if (checker.testContent()) {
            result = "OK";
        } else {
            if (Helper.testWebConnection()) {
                if (checker.testPing()) {
                    result =  "Ping Only";
                } else {
                    result = "Not Reachable";
                }
            } else {
                result =  "No Connection";
            }
        }
        System.out.println(result);
        return result;
    }
}