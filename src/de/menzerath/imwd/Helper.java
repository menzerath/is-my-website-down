package de.menzerath.imwd;

import javax.swing.*;

public class Helper {
    private static final int MIN_INTERVAL = 10;
    private static final int MAX_INTERVAL = 300;

    /**
     * This method validates the user-input and checks if it is ok. If not the user will see an message according the problem.
     *
     * @param url      Given URL to check
     * @param interval Given interval to check (as String, needs to be validated)
     * @param gui      If this method was started from the GUI or the Console
     * @return If the input could be validated or not
     */
    public static boolean validateInput(String url, String interval, boolean gui) {
        if (!url.startsWith("http://")) {
            if (gui) {
                JOptionPane.showMessageDialog(null, "Please enter a valid URL!\nValid: Starts with \"http://\"", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("Please enter a valid URL: Starts with \"http://\"");
            }
            return false;
        }

        boolean invalidInterval = false;
        int myInterval = 0;
        try {
            myInterval = Integer.parseInt(interval);
        } catch (NumberFormatException e) {
            invalidInterval = true;
        }

        if (myInterval < MIN_INTERVAL || myInterval > MAX_INTERVAL) {
            invalidInterval = true;
        }

        if (invalidInterval) {
            if (gui) {
                JOptionPane.showMessageDialog(null, "Please enter a valid interval!\nValid: Only Numbers, between 10 and 600", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("Please enter a valid interval: Only Numbers; between 10 and 600");
            }
            return false;
        }
        return true;
    }
}