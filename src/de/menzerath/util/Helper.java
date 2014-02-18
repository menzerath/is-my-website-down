package de.menzerath.util;

import de.menzerath.imwd.Main;

public class Helper {

    /**
     * This method validates the user-input (URL) and checks if it is ok.
     *
     * @param url Given URL to check
     * @return If the input could be validated or not
     */
    public static boolean validateUrlInput(String url) {
        return url.startsWith("http://");
    }

    /**
     * This method validates the user-input (interval) and checks if it is ok.
     *
     * @param interval Given interavl to check
     * @return If the input could be validated or not
     */
    public static boolean validateIntervalInput(String interval) {
        int myInterval = parseInt(interval);
        return !(myInterval < Main.MIN_INTERVAL || myInterval > Main.MAX_INTERVAL);
    }

    /**
     * Parse a String and return an Integer.
     *
     * @param integer String to parse
     * @return Parsed String; 0 on error
     */
    public static int parseInt(String integer) {
        int myInt = 0;
        try {
            myInt = Integer.parseInt(integer);
        } catch (NumberFormatException ignored) {
        }
        return myInt;
    }
}