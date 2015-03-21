package eu.menzerath.imwd;

import eu.menzerath.imwd.checker.Checker;
import eu.menzerath.util.Helper;
import eu.menzerath.util.Logger;
import eu.menzerath.util.Messages;
import eu.menzerath.util.Updater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The GuiApplication is used to maintain multiple Checker-objects in a single and easy-to-use GUI.
 * It also contains many extra features and uses the SettingsManager to maintain it's configuration.
 */
public class GuiApplication extends JFrame {
    // Tray-Icons
    private static final Image iconOk = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/icons/ok.png"));
    private static final Image iconWarning = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/icons/warning.png"));
    private static final Image iconError = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/icons/error.png"));
    private static final Image iconNoConnection = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/icons/noConnection.png"));

    // GUI-Elements
    private static JFrame frame;
    private JPanel mainPanel;
    private JMenu mnChecker;
    private JMenu mnLogs;
    private JPanel websiteSettings1;
    private JPanel websiteSettings2;
    private JPanel websiteSettings3;
    private JPanel websiteSettings4;
    private JPanel websiteSettings5;
    private JPanel websiteSettings6;
    private JPanel websiteSettings7;
    private JPanel websiteSettings8;
    private JTextField url1;
    private JTextField url2;
    private JTextField url3;
    private JTextField url4;
    private JTextField url5;
    private JTextField url6;
    private JTextField url7;
    private JTextField url8;
    private JTextField interval1;
    private JTextField interval2;
    private JTextField interval3;
    private JTextField interval4;
    private JTextField interval5;
    private JTextField interval6;
    private JTextField interval7;
    private JTextField interval8;
    private JCheckBox content1;
    private JCheckBox content2;
    private JCheckBox content3;
    private JCheckBox content4;
    private JCheckBox content5;
    private JCheckBox content6;
    private JCheckBox content7;
    private JCheckBox content8;
    private JCheckBox ping1;
    private JCheckBox ping2;
    private JCheckBox ping3;
    private JCheckBox ping4;
    private JCheckBox ping5;
    private JCheckBox ping6;
    private JCheckBox ping7;
    private JCheckBox ping8;
    private JButton startButton;
    private JButton stopButton;

    // GUI-Element-Arrays
    private JPanel[] websiteSettings;
    private JTextField[] url;
    private JTextField[] interval;
    private JCheckBox[] content;
    private JCheckBox[] ping;

    // Other
    private static final int maxCheckerId = 8;
    private static TrayIcon[] trayIcon = new TrayIcon[maxCheckerId];
    private Checker[] checker = new Checker[maxCheckerId];
    private SettingsManager settings;

    /**
     * main()-method: Start the GUI!
     * Prepares everything and then shows the form.
     */
    public static void startGUI() {
        Main.sayHello();

        // For an nicer look according to the used OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        frame = new JFrame("GuiApplication");
        GuiApplication gui = new GuiApplication();
        frame.setContentPane(gui.mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle(Main.APPLICATION);
        frame.setIconImage(iconOk);
        frame.setResizable(false);

        // Run an update-check
        runUpdateCheck(true);
    }

    /**
     * Constructor: Initializes some Objects, gives every button an action and adds an JMenuBar.
     */
    public GuiApplication() {
        // Initialize SettingsManager
        settings = new SettingsManager();

        // Show GUI if Autorun is disabled
        if (!settings.getAutorunFromSettings()) frame.setVisible(true);

        // Load important GUI-elements
        websiteSettings = new JPanel[]{websiteSettings1, websiteSettings2, websiteSettings3, websiteSettings4, websiteSettings5, websiteSettings6, websiteSettings7, websiteSettings8};
        url = new JTextField[]{url1, url2, url3, url4, url5, url6, url7, url8};
        interval = new JTextField[]{interval1, interval2, interval3, interval4, interval5, interval6, interval7, interval8};
        content = new JCheckBox[]{content1, content2, content3, content4, content5, content6, content7, content8};
        ping = new JCheckBox[]{ping1, ping2, ping3, ping4, ping5, ping6, ping7, ping8};

        // Load saved / default values
        for (int i = 0; i < maxCheckerId; i++) {
            url[i].setText(settings.getUrlFromSettings(i));
            interval[i].setText("" + settings.getIntervalFromSettings(i));
            content[i].setSelected(settings.getCheckContentFromSettings(i));
            ping[i].setSelected(settings.getCheckPingFromSettings(i));
        }

        // How many Checkers will be shown
        final int checkerAmount = settings.getCheckerCountFromSettings();
        addWebsiteSettings(checkerAmount);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean allowStart= true;
                for (int i = 0; i < settings.getCheckerCountFromSettings(); i++) {
                    if (!checkInput(i)) allowStart = false;
                }

                if (allowStart) {
                    for (int i = 0; i < settings.getCheckerCountFromSettings(); i++) {
                        start(i);
                    }
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
                                "\n" + Messages.ABOUT_AUTHOR, "About", JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        mnFile.add(mntmAbout);

        mntmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (int i = 0; i < 5; i++) {
                    try {
                        checker[i].stopTesting();
                    } catch (NullPointerException ignored) {
                    }
                }
                System.exit(0);
            }
        });
        mnFile.add(mntmExit);
        // END: File-Menu

        // START: Checker-Menu
        mnChecker = new JMenu("Checker");

        final JRadioButtonMenuItem[] rb = new JRadioButtonMenuItem[maxCheckerId];
        ButtonGroup checkerGroup = new ButtonGroup();
        for (int i = 0; i < maxCheckerId; i++) {
            final int j = i + 1;
            rb[i] = new JRadioButtonMenuItem("" + (i + 1));
            rb[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    settings.setCheckerCountForSettings(j);
                    addWebsiteSettings(j);
                }
            });
            checkerGroup.add(rb[i]);
            mnChecker.add(rb[i]);
        }
        rb[checkerAmount - 1].setSelected(true);

        menuBar.add(mnChecker);
        // END: Checker-Menu

        // START: Log-Menu
        mnLogs = new JMenu("Logs");
        final JCheckBoxMenuItem cbLogEnable = new JCheckBoxMenuItem("Enable");
        final JCheckBoxMenuItem cbLogValid = new JCheckBoxMenuItem("Log valid Checks");
        menuBar.add(mnLogs);

        cbLogEnable.setSelected(settings.getCreateLogFromSettings());
        cbLogEnable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setCreateLogForSettings(cbLogEnable.isSelected());

                if (!cbLogEnable.isSelected()) {
                    cbLogValid.setEnabled(false);
                    cbLogValid.setSelected(false);
                    settings.setCreateValidLogForSettings(false);
                } else {
                    cbLogValid.setEnabled(true);
                }
            }
        });
        mnLogs.add(cbLogEnable);

        cbLogValid.setEnabled(cbLogEnable.isSelected());
        cbLogValid.setSelected(settings.getCreateValidLogFromSettings());
        cbLogValid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setCreateValidLogForSettings(cbLogValid.isSelected());
            }
        });
        mnLogs.add(cbLogValid);
        // END: Log-Menu

        // START: Tools-Menu
        JMenu mnTools = new JMenu("Tools");
        final JCheckBoxMenuItem cbAutorun = new JCheckBoxMenuItem("Start with Windows");
        menuBar.add(mnTools);

        cbAutorun.setSelected(settings.getAutorunFromSettings());
        cbAutorun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                settings.setAutorunForSettings(cbAutorun.isSelected());

                if (cbAutorun.isSelected()) {
                    if (!Helper.addToAutorun()) {
                        JOptionPane.showMessageDialog(null, Messages.AUTORUN_ERROR, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Helper.removeFromAutorun();
                }
            }
        });

        // Do not show these items if the user doesn't use Windows
        if (!System.getProperty("os.name").startsWith("Windows")) {
            cbAutorun.setVisible(false);
        } else if (System.getProperty("os.name").equals("Windows XP")) {
            cbAutorun.setVisible(false);
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

        cbNotificationBubbles.setSelected(settings.getShowBubblesSettings());
        cbNotificationBubbles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                settings.setShowBubblesSettings(cbNotificationBubbles.isSelected());
            }
        });
        mnOther.add(cbNotificationBubbles);
        // END: Other-Menu
        // END: JMenuBar

        // Check for Autorun --> Start Checker
        if (settings.getAutorunFromSettings()) {
            for (int i = 0; i < settings.getCheckerCountFromSettings(); i++) {
                start(i);
            }
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } else {
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
    }

    /**
     * This will create an TrayIcon and show information about the current check(s).
     * @param checkerId The Checker-id the TrayIcon represents
     */
    private static void createTrayIcon(int checkerId) {
        // Not supported? Bye, Bye!
        if (!SystemTray.isSupported()) {
            Logger.error("SystemTray is not supported. Exiting...");
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
            Logger.error("TrayIcon could not be added. Exiting...");
            System.exit(1);
        }
    }

    /**
     * Check whether a particular Checker is allowed to start or not.
     * @param checkerId Checker to check
     * @return <code>true</code> if the Checker is allowed to start or <code>false</code> if it is not.
     */
    private boolean checkInput(int checkerId) {
        if (!Helper.validateUrlInput(url[checkerId].getText().trim()) || !Helper.validateIntervalInput(Helper.parseInt(interval[checkerId].getText().trim()))) {
            // +1 to be more user-friendly
            JOptionPane.showMessageDialog(null, Messages.INVALID_PARAMETERS, "Invalid Input (Website " + (checkerId + 1) + ")", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Start testing: Prepare the GUI, the TrayIcon and start the Checker.
     */
    private void start(int checkerId) {
        String cUrl = url[checkerId].getText().trim();
        int cInterval = Helper.parseInt(interval[checkerId].getText().trim());
        boolean cContent = content[checkerId].isSelected();
        boolean cPing = ping[checkerId].isSelected();

        createTrayIcon(checkerId);
        trayIcon[checkerId].setToolTip("Running - " + Main.APPLICATION_SHORT);

        // Disable/Dispose GUI(-elements)
        frame.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mnChecker.setEnabled(false);
        mnLogs.setEnabled(false);
        for (int i = 0; i < maxCheckerId; i++) {
            this.url[i].setEnabled(false);
            this.interval[i].setEnabled(false);
            this.content[i].setEnabled(false);
            this.ping[i].setEnabled(false);
        }
        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        // Save the values
        settings.setUrlForSettings(checkerId, cUrl);
        settings.setIntervalForSettings(checkerId, cInterval);
        settings.setCheckContentForSettings(checkerId, cContent);
        settings.setCheckPingForSettings(checkerId, cPing);

        // Create the Checker
        checker[checkerId] = new Checker(checkerId, cUrl, cInterval, cContent, cPing, settings.getCreateLogFromSettings(), settings.getCreateValidLogFromSettings(), this);
        checker[checkerId].startTesting();
    }

    /**
     * Stop testing: Prepare the GUI, the TrayIcon and stop the Checker
     */
    private void stop() {
        SystemTray tray = SystemTray.getSystemTray();
        for (int i = 0; i < maxCheckerId; i++) {
            try {
                tray.remove(trayIcon[i]);
                checker[i].stopTesting();
            } catch (Exception ignored) {
            }
        }

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Enable GUI-elements
        mnChecker.setEnabled(true);
        mnLogs.setEnabled(true);
        for (int i = 0; i < maxCheckerId; i++) {
            this.url[i].setEnabled(true);
            this.interval[i].setEnabled(true);
            this.content[i].setEnabled(true);
            this.ping[i].setEnabled(true);
        }
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    /**
     * Hide unused Checkers and only show the needed ones
     * @param checkerAmount Amount of used Checkers
     */
    private void addWebsiteSettings(int checkerAmount) {
        for (int i = 0; i < maxCheckerId; i++) {
            websiteSettings[i].setVisible(true);
        }
        for (int i = checkerAmount; i < maxCheckerId; i++) {
            websiteSettings[i].setVisible(false);
        }
        frame.pack();
    }

    /**
     * An update-check for the "GuiApplication": If there is an update available, it will show an message and a button to open the website in a browser.
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
                } else if (myUpdater.getServerVersion().equalsIgnoreCase("SNAPSHOT")) {
                    // Show this message if the Updater was created by the user
                    if (!startup) {
                        JOptionPane.showMessageDialog(null, Messages.UPDATE_SNAPSHOT, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (myUpdater.isUpdateAvailable()) {
                    int value = JOptionPane.showConfirmDialog(null, Messages.UPDATE_AVAILABLE.replace("%version", myUpdater.getServerVersion()) +
                            "\n" + Messages.UPDATE_AVAILABLE_CHANGES.replace("%changes", myUpdater.getServerChangelog()) +
                            "\n\n" + Messages.UPDATE_NOW, Messages.UPDATE_AVAILABLE_TITLE, JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        new eu.menzerath.imwd.updater.Updater(true);
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
    public void updateTrayIcon(Checker checker, int status, boolean showMessage) {
        if (status == 1) {
            trayIcon[checker.ID].setImage(iconOk);
            trayIcon[checker.ID].setToolTip(Messages.OK + " - " + Main.APPLICATION_SHORT + "\n" + Helper.getUrlWithoutProtocol(checker.URL));
        } else if (status == 2) {
            trayIcon[checker.ID].setImage(iconWarning);
            trayIcon[checker.ID].setToolTip(Messages.ERROR_NOT_REACHABLE_TITLE + " - " + Main.APPLICATION_SHORT + "\n" + Helper.getUrlWithoutProtocol(checker.URL));
            if (showMessage && settings.getShowBubblesSettings())
                trayIcon[checker.ID].displayMessage(Messages.ERROR_NOT_REACHABLE_TITLE + ": " + Helper.getUrlWithoutProtocol(checker.URL), Messages.ERROR_NOT_REACHABLE_PING, TrayIcon.MessageType.WARNING);
        } else if (status == 3) {
            trayIcon[checker.ID].setImage(iconError);
            trayIcon[checker.ID].setToolTip(Messages.ERROR_NOT_REACHABLE_TITLE + " - " + Main.APPLICATION_SHORT + "\n" + Helper.getUrlWithoutProtocol(checker.URL));
            if (showMessage && settings.getShowBubblesSettings())
                trayIcon[checker.ID].displayMessage(Messages.ERROR_NOT_REACHABLE_TITLE + ": " + Helper.getUrlWithoutProtocol(checker.URL), Messages.ERROR_NOT_REACHABLE_NO_PING, TrayIcon.MessageType.ERROR);
        } else if (status == 4) {
            trayIcon[checker.ID].setImage(iconNoConnection);
            trayIcon[checker.ID].setToolTip(Messages.ERROR_NO_CONNECTION_TITLE + " - " + Main.APPLICATION_SHORT + "\n" + Helper.getUrlWithoutProtocol(checker.URL));
            if (showMessage && settings.getShowBubblesSettings())
                trayIcon[checker.ID].displayMessage(Messages.ERROR_NO_CONNECTION_TITLE, Messages.ERROR_NO_CONNECTION, TrayIcon.MessageType.ERROR);
        }
    }
}