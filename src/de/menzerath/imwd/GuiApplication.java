package de.menzerath.imwd;

import de.menzerath.util.Helper;
import de.menzerath.util.Messages;
import de.menzerath.util.Updater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class GuiApplication extends JFrame {
    // Tray-Icons
    private static final Image iconOk = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/res/ic_ok.png"));
    private static final Image iconWarning = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/res/ic_warning.png"));
    private static final Image iconError = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/res/ic_error.png"));
    private static final Image iconNoConnection = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/res/ic_noConnection.png"));

    // GUI-Elements
    private static JFrame frame;
    private JPanel mainPanel;
    private JPanel websiteSettings2;
    private JPanel websiteSettings3;
    private JMenu mnChecks;
    private JMenu mnChecker;
    private JMenu mnLogs;
    private JTextField urlTextField;
    private JTextField url2TextField;
    private JTextField url3TextField;
    private JTextField intervalTextField;
    private JTextField interval2TextField;
    private JTextField interval3TextField;
    private JButton startButton;
    private JButton stopButton;

    // Other
    private static TrayIcon[] trayIcon = new TrayIcon[4];
    private Checker[] checker = new Checker[4];

    /**
     * Start the GUI!
     * Prepares everything and then shows the form
     */
    public static void startGUI() {
        // For an nicer look according to the used OS.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        frame = new JFrame("GuiApplication");
        GuiApplication gui = new GuiApplication();
        frame.setContentPane(gui.mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle(Main.APPLICATION);
        frame.setIconImage(iconOk);
        frame.setResizable(false);
        if (!SettingsManager.getAutorunFromSettings()) frame.setVisible(true);

        // Run an update-check
        runUpdateCheck(true);

        if (SettingsManager.getAutorunFromSettings()) {
            for (int i = 1; i < SettingsManager.getCheckerCountFromSettings() + 1; i++) {
                gui.addChecker(i);
            }
        }
    }

    /**
     * Gives every button an action and adds an JMenuBar
     */
    public GuiApplication() {
        // How many Checkers will be shown
        int checkerAmount = SettingsManager.getCheckerCountFromSettings();
        if (checkerAmount < 3) websiteSettings3.setVisible(false);
        if (checkerAmount < 2) websiteSettings2.setVisible(false);
        pack();

        // Load saved / default values
        urlTextField.setText(SettingsManager.getUrlFromSettings(1));
        url2TextField.setText(SettingsManager.getUrlFromSettings(2));
        url3TextField.setText(SettingsManager.getUrlFromSettings(3));
        intervalTextField.setText("" + SettingsManager.getIntervalFromSettings(1));
        interval2TextField.setText("" + SettingsManager.getIntervalFromSettings(2));
        interval3TextField.setText("" + SettingsManager.getIntervalFromSettings(3));

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 1; i < SettingsManager.getCheckerCountFromSettings() + 1; i++) {
                    addChecker(i);
                }
            }
        });

        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                stop();
            }
        });

        // START: JMenuBar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // START: File-Menu
        JMenu mnFile = new JMenu("File");
        JMenuItem mntmAbout = new JMenuItem("About");
        JMenuItem mntmExit = new JMenuItem("Exit");
        menuBar.add(mnFile);

        mntmAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null,
                        Main.APPLICATION + " - Version " + Main.VERSION +
                                "\n\n" + Messages.ABOUT_ICONS +
                                "\n" + Messages.ABOUT_SOURCE +
                                "\n" + Messages.ABOUT_AUTHOR, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        mnFile.add(mntmAbout);

        mntmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    checker[1].stopTesting();
                    checker[2].stopTesting();
                    checker[3].stopTesting();
                } catch (NullPointerException ignored) {
                }
                System.exit(0);
            }
        });
        mnFile.add(mntmExit);
        // END: File-Menu

        // START: Checker-Menu
        mnChecker = new JMenu("Checker");
        final JRadioButtonMenuItem rbOne = new JRadioButtonMenuItem("1");
        final JRadioButtonMenuItem rbTwo = new JRadioButtonMenuItem("2");
        final JRadioButtonMenuItem rbThree = new JRadioButtonMenuItem("3");
        menuBar.add(mnChecker);

        ButtonGroup checkerGroup = new ButtonGroup();
        checkerGroup.add(rbOne);
        checkerGroup.add(rbTwo);
        checkerGroup.add(rbThree);

        if (checkerAmount == 1) rbOne.setSelected(true);
        rbOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsManager.setCheckerCountForSettings(1);
                websiteSettings2.setVisible(false);
                websiteSettings3.setVisible(false);
                frame.pack();
            }
        });
        mnChecker.add(rbOne);

        if (checkerAmount == 2) rbTwo.setSelected(true);
        rbTwo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsManager.setCheckerCountForSettings(2);
                websiteSettings2.setVisible(true);
                websiteSettings3.setVisible(false);
                frame.pack();
            }
        });
        mnChecker.add(rbTwo);

        if (checkerAmount == 3) rbThree.setSelected(true);
        rbThree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsManager.setCheckerCountForSettings(3);
                websiteSettings2.setVisible(true);
                websiteSettings3.setVisible(true);
                frame.pack();
            }
        });
        mnChecker.add(rbThree);
        // END: Checker-Menu

        // START: Checks-Menu
        mnChecks = new JMenu("Checks");
        final JCheckBoxMenuItem cbCheckContent = new JCheckBoxMenuItem("Content");
        final JCheckBoxMenuItem cbCheckPing = new JCheckBoxMenuItem("Ping");
        menuBar.add(mnChecks);

        cbCheckContent.setSelected(SettingsManager.getCheckContentFromSettings());
        cbCheckContent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SettingsManager.setCheckContentForSettings(cbCheckContent.isSelected());
            }
        });
        mnChecks.add(cbCheckContent);

        cbCheckPing.setSelected(SettingsManager.getCheckPingFromSettings());
        cbCheckPing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SettingsManager.setCheckPingForSettings(cbCheckPing.isSelected());
            }
        });
        mnChecks.add(cbCheckPing);
        // END: Checks-Menu

        // START: Log-Menu
        mnLogs = new JMenu("Logs");
        final JCheckBoxMenuItem cbLogEnable = new JCheckBoxMenuItem("Enable");
        final JCheckBoxMenuItem cbLogValid = new JCheckBoxMenuItem("Log valid Checks");
        menuBar.add(mnLogs);

        cbLogEnable.setSelected(SettingsManager.getCreateLogFromSettings());
        cbLogEnable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsManager.setCreateLogForSettings(cbLogEnable.isSelected());

                if (!cbLogEnable.isSelected()) {
                    cbLogValid.setEnabled(false);
                    cbLogValid.setSelected(false);
                    SettingsManager.setCreateValidLogForSettigs(false);
                } else {
                    cbLogValid.setEnabled(true);
                }
            }
        });
        mnLogs.add(cbLogEnable);

        cbLogValid.setEnabled(cbLogEnable.isSelected());
        cbLogValid.setSelected(SettingsManager.getCreateValidLogFromSettings());
        cbLogValid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsManager.setCreateValidLogForSettigs(cbLogValid.isSelected());
            }
        });
        mnLogs.add(cbLogValid);
        // END: Log-Menu

        // START: Tools-Menu
        JMenu mnTools = new JMenu("Tools");
        final JCheckBoxMenuItem cbAutorun = new JCheckBoxMenuItem("Start with Windows");
        menuBar.add(mnTools);

        cbAutorun.setSelected(SettingsManager.getAutorunFromSettings());
        if (SettingsManager.getAutorunFromSettings())
            Helper.addToAutorun(); // If there was an update, update file in Autorun!
        cbAutorun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SettingsManager.setAutorunForSettigs(cbAutorun.isSelected());

                if (cbAutorun.isSelected()) {
                    if (!Helper.addToAutorun()) {
                        JOptionPane.showMessageDialog(null, Messages.AUTORUN_ERROR, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Helper.removeFromAutorun();
                }
            }
        });

        // Change the text if the user doesn't use Windows and disable it
        if (!System.getProperty("os.name").startsWith("Windows")) {
            cbAutorun.setEnabled(false);
            cbAutorun.setText("Add to Autorun (Windows only)");
        } else if (System.getProperty("os.name").equals("Windows XP")) {
            cbAutorun.setEnabled(false);
            cbAutorun.setText("Add to Autorun (Vista or higher)");
        }
        mnTools.add(cbAutorun);

        JMenuItem mntmUpdates = new JMenuItem("Check for Updates");
        mntmUpdates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runUpdateCheck(false);
            }
        });
        mnTools.add(mntmUpdates);
        // END: Tools-Menu

        // START: Other-Menu
        JMenu mnOther = new JMenu("Other");
        final JCheckBoxMenuItem cbNotificationBubbles = new JCheckBoxMenuItem("Show Notification-Bubbles");
        menuBar.add(mnOther);

        cbNotificationBubbles.setSelected(SettingsManager.getShowBubblesSettings());
        cbNotificationBubbles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                SettingsManager.setShowBubblesSettigs(cbNotificationBubbles.isSelected());
            }
        });
        mnOther.add(cbNotificationBubbles);
        // END: Other-Menu
        // END: JMenuBar
    }

    /**
     * This will create an TrayIcon and show information about the current check(s)
     */
    private static void createTrayIcon(int checkerId) {
        // Not supported? Bye, Bye!
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported. Exiting...");
            System.exit(1);
        }

        SystemTray tray = SystemTray.getSystemTray();
        trayIcon[checkerId] = new TrayIcon(iconOk, Main.APPLICATION);
        trayIcon[checkerId].setImageAutoSize(true);
        trayIcon[checkerId].setToolTip("Stopped - " + Main.APPLICATION_SHORT);
        trayIcon[checkerId].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
            }
        });

        try {
            tray.add(trayIcon[checkerId]);
        } catch (AWTException e) {
            // Not possible? Bye, Bye!
            System.out.println("TrayIcon could not be added. Exiting...");
            System.exit(1);
        }
    }

    /**
     * Validates the input and starts the process
     * Not the best solution, but it works ;)
     *
     * @param checkerId Currently added Checker (ID)
     */
    private void addChecker(int checkerId) {
        if (checkerId == 1) {
            if (Helper.validateUrlInput(urlTextField.getText().trim()) && Helper.validateIntervalInput(intervalTextField.getText().trim())) {
                start(urlTextField.getText().trim(), Helper.parseInt(intervalTextField.getText().trim()), checkerId, SettingsManager.getCheckerCountFromSettings());
            } else {
                JOptionPane.showMessageDialog(null, Messages.INVALID_PARAMETERS, "Invalid Input (Website 1)", JOptionPane.ERROR_MESSAGE);
            }
        } else if (checkerId == 2) {
            if (Helper.validateUrlInput(url2TextField.getText().trim()) && Helper.validateIntervalInput(interval2TextField.getText().trim())) {
                start(url2TextField.getText().trim(), Helper.parseInt(interval2TextField.getText().trim()), checkerId, SettingsManager.getCheckerCountFromSettings());
            } else {
                JOptionPane.showMessageDialog(null, Messages.INVALID_PARAMETERS, "Invalid Input (Website 2)", JOptionPane.ERROR_MESSAGE);
            }
        } else if (SettingsManager.getCheckerCountFromSettings() == 3) {
            if (Helper.validateUrlInput(url3TextField.getText().trim()) && Helper.validateIntervalInput(interval3TextField.getText().trim())) {
                start(url3TextField.getText().trim(), Helper.parseInt(interval3TextField.getText().trim()), checkerId, SettingsManager.getCheckerCountFromSettings());
            } else {
                JOptionPane.showMessageDialog(null, Messages.INVALID_PARAMETERS, "Invalid Input (Website 3)", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Start testing!
     * Prepares the GUI, the TrayIcon and starts the Checker
     */
    private void start(String url, int interval, int checkerId, int maxChecker) {
        if (!SettingsManager.getCheckContentFromSettings() && !SettingsManager.getCheckPingFromSettings()) {
            // Show message only once (before adding last Checker)
            if (checkerId == maxChecker) {
                JOptionPane.showMessageDialog(null, Messages.NO_CHECK_SELECTED, "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        createTrayIcon(checkerId);
        trayIcon[checkerId].setToolTip("Running - " + Main.APPLICATION_SHORT);

        // Disable/Dispose GUI(-elements)
        frame.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mnChecks.setEnabled(false);
        mnChecker.setEnabled(false);
        mnLogs.setEnabled(false);
        urlTextField.setEditable(false);
        url2TextField.setEditable(false);
        url3TextField.setEditable(false);
        intervalTextField.setEditable(false);
        interval2TextField.setEditable(false);
        interval3TextField.setEditable(false);
        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        // Save the values
        SettingsManager.setUrlForSettings(checkerId, url);
        SettingsManager.setIntervalForSettings(checkerId, interval);

        // Create the Checker
        checker[checkerId] = new Checker(checkerId, url, interval, SettingsManager.getCheckContentFromSettings(), SettingsManager.getCheckPingFromSettings(), SettingsManager.getCreateLogFromSettings(), SettingsManager.getCreateValidLogFromSettings(), true);
        checker[checkerId].startTesting();
    }

    /**
     * Stop testing!
     * Prepares the GUI, the TrayIcon and stops the Checker
     */
    private void stop() {
        SystemTray tray = SystemTray.getSystemTray();
        for (int i = 1; i < 4; i++) {
            try {
                tray.remove(trayIcon[i]);
                checker[i].stopTesting();
            } catch (Exception ignored) {
            }
        }

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Enable GUI-elements
        mnChecks.setEnabled(true);
        mnChecker.setEnabled(true);
        mnLogs.setEnabled(true);
        urlTextField.setEditable(true);
        url2TextField.setEditable(true);
        url3TextField.setEditable(true);
        intervalTextField.setEditable(true);
        interval2TextField.setEditable(true);
        interval3TextField.setEditable(true);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    /**
     * An update-check for the "GuiApplication": If there is an update available, it will show an message and a button to open
     * the website in a browser.
     *
     * @param startup Running this on startup? Then don't show "error"'s or "ok"'s.
     */
    private static void runUpdateCheck(final boolean startup) {
        Thread thread = new Thread() {
            public void run() {
                Updater myUpdater = new Updater();
                if (myUpdater.getServerVersion().equalsIgnoreCase("Error")) {
                    // Show this message if the Updater was created by the user
                    if (!startup) {
                        JOptionPane.showMessageDialog(null, Messages.UPDATE_ERROR, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (myUpdater.isUpdateAvailable()) {
                    int value = JOptionPane.showConfirmDialog(null, Messages.UPDATE_AVAILABLE.replace("%version", myUpdater.getServerVersion()) +
                            "\n" + Messages.UPDATE_AVAILABLE_CHANGES.replace("%changes", myUpdater.getServerChangelog()) +
                            "\n\n" + Messages.UPDATE_NOW, Messages.UPDATE_AVAILABLE_TITLE, JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        try {
                            Desktop.getDesktop().browse(new URI(Main.URL_RELEASE));
                        } catch (Exception ignored) {
                        }
                        System.exit(0);
                    }
                } else {
                    // Show this message if the Updater was created by the user
                    if (!startup) {
                        JOptionPane.showMessageDialog(null, Messages.UPDATE_NO_UPDATE_LONG, Messages.UPDATE_NO_UPDATE, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        };
        thread.start();
    }

    /**
     * Changes TrayIcon (icon, tooltip) and shows a message
     *
     * @param checker     Which Checker updates the TrayIcon
     * @param status      The test-result
     * @param showMessage Display a message
     */
    public static void updateTrayIcon(Checker checker, int status, boolean showMessage) {
        if (status == 1) {
            trayIcon[checker.ID].setImage(iconOk);
            trayIcon[checker.ID].setToolTip(Messages.OK + " - " + Main.APPLICATION_SHORT + "\n" + checker.URL.replace("http://", ""));
        } else if (status == 2) {
            trayIcon[checker.ID].setImage(iconWarning);
            trayIcon[checker.ID].setToolTip(Messages.ERROR_NOT_REACHABLE_TITLE + " - " + Main.APPLICATION_SHORT + "\n" + checker.URL.replace("http://", ""));
            if (showMessage && SettingsManager.getShowBubblesSettings())
                trayIcon[checker.ID].displayMessage(Messages.ERROR_NOT_REACHABLE_TITLE + ": " + checker.URL.replace("http://", ""), Messages.ERROR_NOT_REACHABLE_PING, TrayIcon.MessageType.WARNING);
        } else if (status == 3) {
            trayIcon[checker.ID].setImage(iconError);
            trayIcon[checker.ID].setToolTip(Messages.ERROR_NOT_REACHABLE_TITLE + " - " + Main.APPLICATION_SHORT + "\n" + checker.URL.replace("http://", ""));
            if (showMessage && SettingsManager.getShowBubblesSettings())
                trayIcon[checker.ID].displayMessage(Messages.ERROR_NOT_REACHABLE_TITLE + ": " + checker.URL.replace("http://", ""), Messages.ERROR_NOT_REACHABLE_NO_PING, TrayIcon.MessageType.ERROR);
        } else if (status == 4) {
            trayIcon[checker.ID].setImage(iconNoConnection);
            trayIcon[checker.ID].setToolTip(Messages.ERROR_NO_CONNECTION_TITLE + " - " + Main.APPLICATION_SHORT + "\n" + checker.URL.replace("http://", ""));
            if (showMessage && SettingsManager.getShowBubblesSettings())
                trayIcon[checker.ID].displayMessage(Messages.ERROR_NO_CONNECTION_TITLE, Messages.ERROR_NO_CONNECTION, TrayIcon.MessageType.ERROR);
        }
    }
}