package de.menzerath.imwd;

import javax.swing.*;

public class Helper {
    private static final int MIN_INTERVAL = 10;
    private static final int MAX_INTERVAL = 300;

    /**
     * This method validates the user-input (URL) and checks if it is ok. If not the user will see an message according the problem.
     *
     * @param url Given URL to check
     * @param gui If this method was started from the GUI or the Console
     * @return If the input could be validated or not
     */
    public static boolean validateUrlInput(String url, boolean gui) {
        if (!url.startsWith("http://")) {
            if (gui) {
                JOptionPane.showMessageDialog(null, "Please enter a valid URL!\nValid: Starts with \"http://\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("Please enter a valid URL: Starts with \"http://\"");
            }
            return false;
        }
        return true;
    }

    /**
     * This method validates the user-input (interval) and checks if it is ok. If not the user will see an message according the problem.
     *
     * @param interval Given interavl to check
     * @param gui      If this method was started from the GUI or the Console
     * @return 0 if not valid or any other (parsed and validated) interval
     */
    public static boolean validateIntervalInput(String interval, boolean gui) {
        int myInterval = parseInt(interval);
        if (myInterval < MIN_INTERVAL || myInterval > MAX_INTERVAL) {
            if (gui) {
                JOptionPane.showMessageDialog(null, "Please enter a valid interval!\nValid: Only Numbers, between 10 and 600", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("Please enter a valid interval: Only Numbers; between 10 and 600");
            }
            return false;
        }
        return true;
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