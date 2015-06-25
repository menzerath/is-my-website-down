package eu.menzerath.imwd;

import eu.menzerath.imwd.checker.Checker;
import eu.menzerath.util.Helper;
import eu.menzerath.util.Logger;
import eu.menzerath.util.Messages;

/**
 * A single and quick test which outputs the result directly on the console - without anything else.
 */
public class QuickTest {
	private String url;

	/**
	 * Constructor: Validate the url and run the checker
	 *
	 * @param url URL to check
	 */
	public QuickTest(String url) {
		if (Helper.validateUrlInput(url)) {
			this.url = url;
		} else if (Helper.validateUrlInput("http://" + url)) {
			this.url = "http://" + url;
		} else {
			Logger.error(Messages.INVALID_PARAMETERS);
			System.exit(1);
		}
	}

	/**
	 * Runs a test, prints a message on the console and returns the result (to a calling application)
	 *
	 * @return Check-result
	 */
	public String run() {
		// Create the Checker and go!
		String result;
		Checker checker = new Checker(0, url, 30, true, true, false, false);

		if (checker.testContent()) {
			result = "OK";
		} else {
			if (Helper.testWebConnection()) {
				if (checker.testPing()) {
					result = "Ping Only";
				} else {
					result = "Not Reachable";
				}
			} else {
				result = "No Connection";
			}
		}
		System.out.println(result);
		return result;
	}
}