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
        } else {
            System.out.println(Messages.INVALID_PARAMETERS);
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

        if (checker.testContent(this.url)) {
            result = "OK";
        } else {
            if (checker.testConnection()) {
                if (checker.testPing(this.url)) {
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