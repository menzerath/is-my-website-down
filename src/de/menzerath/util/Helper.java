package de.menzerath.util;

public class Helper {
    private static final int MIN_INTERVAL = 10;
    private static final int MAX_INTERVAL = 300;

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
        return !(myInterval < MIN_INTERVAL || myInterval > MAX_INTERVAL);
    }

    public static int parseInt(String integer) {
        int myInt = 0;
        try {
            myInt = Integer.parseInt(integer);
        } catch (NumberFormatException ignored) {
        }
        return myInt;
    }
}