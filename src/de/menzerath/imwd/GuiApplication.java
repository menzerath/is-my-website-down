package de.menzerath.imwd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;

public class GuiApplication extends JFrame {
    private static final Image iconOk = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/res/ic_ok.png"));
    private static final Image iconWarning = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/res/ic_warning.png"));
    private static final Image iconError = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/res/ic_error.png"));
    private static final Image iconNoConnection = Toolkit.getDefaultToolkit().getImage(GuiApplication.class.getResource("/res/ic_noConnection.png"));

    private static JFrame frame;
    private JPanel mainPanel;
    private JPanel settingsPanel;
    private JTextField urlTextField;
    private JTextField intervalTextField;
    private JButton startButton;
    private JButton stopButton;
    private JPanel buttonPanel;

    private static TrayIcon trayIcon;
    private Checker checker;

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
        frame.setContentPane(new GuiApplication().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("Is My Website Down?");
        frame.setIconImage(iconOk);
        frame.setResizable(false);
        createTrayIcon();
        frame.setVisible(true);
    }

    /**
     * Gives every button an action and adds an JMenuBar
     */
    public GuiApplication() {
        urlTextField.setText(Main.getUrlFromSettings());
        intervalTextField.setText("" + Main.getIntervalFromSettings());

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                start();
            }
        });

        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                stop();
            }
        });

        // ##########################
        // ### Create an JMenuBar ###
        // ##########################
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    checker.stopTesting();
                } catch (NullPointerException ignored) {
                }
                System.exit(0);
            }
        });

        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null,
                        "Is My Website Down [IMWD] - Version " + Main.getVersion() +
                                "\nIcons by Ampeross - http://ampeross.deviantart.com" +
                                "\nSourcecode: http://github.com/MarvinMenzerath/IsMyWebsiteDown - CC-BY-SA 3.0 License" +
                                "\n© 2012-2013: Marvin Menzerath - http://marvin-menzerath.de", "About Me :)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        mnFile.add(mntmAbout);
        mnFile.add(mntmExit);

        JMenu mnActions = new JMenu("Actions");
        menuBar.add(mnActions);

        JMenuItem mntmAutorun = new JMenuItem("Add to Autorun");
        mntmAutorun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                addToAutorun();
            }
        });

        // Change the text if the user doesn't use Windows and disable it
        if (!System.getProperty("os.name").startsWith("Windows")) {
            mntmAutorun.setEnabled(false);
            mntmAutorun.setText("Add to Autorun (Windows-only)");
        }
        mnActions.add(mntmAutorun);

        JMenuItem mntmUpdates = new JMenuItem("Check for Updates");
        mntmUpdates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runUpdateCheck(false);
            }
        });
        mnActions.add(mntmUpdates);

        JMenu mnLogfile = new JMenu("Log-File");
        menuBar.add(mnLogfile);

        final JCheckBoxMenuItem cbLogEnable = new JCheckBoxMenuItem("Enable");
        cbLogEnable.setSelected(Main.getCreateLogFromSettings());
        cbLogEnable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.setCreateLogForSettings(cbLogEnable.isSelected());
            }
        });
        mnLogfile.add(cbLogEnable);

        final JCheckBoxMenuItem cbLogValid = new JCheckBoxMenuItem("Log valid checks");
        cbLogValid.setSelected(Main.getCreateValidLogFromSettings());
        cbLogValid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.setCreateValidLogForSettigs(cbLogValid.isSelected());
            }
        });
        mnLogfile.add(cbLogValid);

        // Run an update-check
        runUpdateCheck(true);
    }

    /**
     * This will create an TrayIcon and show information about the current check(s)
     */
    private static void createTrayIcon() {
        // Not supported? Bye, Bye!
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            System.exit(1);
            return;
        }
        SystemTray tray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(iconOk, "Is My Website Down?");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Stopped - IMWD");
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            // Not possible? Bye, Bye!
            System.out.println("TrayIcon could not be added.");
            System.exit(1);
        }
    }

    /**
     * Start testing!
     * Prepares the GUI, the TrayIcon and starts the Checker
     */
    private void start() {
        frame.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        trayIcon.setToolTip("Running - IMWD");
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        urlTextField.setEditable(false);
        intervalTextField.setEditable(false);

        int interval = Main.getIntervalFromSettings();
        try {
            interval = Integer.parseInt(intervalTextField.getText());
        } catch (NumberFormatException ignored) {
        }

        // Save the values
        Main.setUrlForSettings(urlTextField.getText());
        Main.setIntervalForSettings(interval);

        trayIcon.setImage(iconOk);

        // Create the Checker
        checker = new Checker(urlTextField.getText().trim(), interval, Main.getCreateLogFromSettings(), Main.getCreateValidLogFromSettings(), true);
        checker.startTesting();
    }

    /**
     * Stop testing!
     * Prepares the GUI, the TrayIcon and stops the Checker
     */
    private void stop() {
        trayIcon.setImage(iconOk);
        checker.stopTesting();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        trayIcon.setToolTip("Stopped - IMWD");
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        urlTextField.setEditable(true);
        intervalTextField.setEditable(true);
    }

    /**
     * An update-check for the "GuiApplication": If there is an update available, it will show an message and the option to open
     * the website in a browser.
     *
     * @param startup Running this on startup? Then don't show "error"'s or "ok"'s.
     */
    private void runUpdateCheck(boolean startup) {
        if (Main.getServerVersion().equalsIgnoreCase("Error")) {
            if (!startup) {
                JOptionPane.showMessageDialog(null, "An error occured while checking the server for a software-update.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (Main.isUpdateAvailable()) {
            int value = JOptionPane.showConfirmDialog(null, "There is an update to version " + Main.getServerVersion() + " available.\nChangelog: " + Main.getServerChangelog() + "\n\nDo you want to download it now?", "Update available", JOptionPane.YES_NO_OPTION);
            if (value == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().browse(new URI("http://marvin-menzerath.de/software/imwd/"));
                } catch (Exception ignored) {
                }
                System.exit(0);
            }
        } else {
            if (!startup) {
                JOptionPane.showMessageDialog(null, "You are running the latest version of this product.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Copy "Is My Website Down" into the Autorun-folder (Works with Windows XP, Vista, 7 and 8 (8.1).
     */
    private void addToAutorun() {
        try {
            CodeSource cSource = GuiApplication.class.getProtectionDomain().getCodeSource();
            File sourceFile = new File(cSource.getLocation().toURI().getPath());
            Path source = Paths.get(sourceFile.getParentFile().getPath() + File.separator + sourceFile.getName());
            Path dest;
            if (System.getProperty("os.name").equals("Windows XP")) {
                dest = Paths.get("C:\\Documents and Settings\\" + System.getProperty("user.name") + "\\Start Menu\\Programs\\Startup\\IMWD.jar");
            } else {
                dest = Paths.get("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\IMWD.jar");
            }
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not copy IMWD to your Autorun. Please check...\n\n  * That you are allowed to copy files to the Autorun-Folder.\n  * That you are running Windows XP or higher.", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Changes the Notification according to the check-result
     *
     * @param status Result of the current check
     */
    public static void setNotification(int status) {
        if (status == 1) {
            trayIcon.setImage(iconOk);
            trayIcon.setToolTip("Everything OK - IMWD");
        } else if (status == 2) {
            trayIcon.setImage(iconWarning);
            trayIcon.displayMessage("Website Not Reachable!", "Unable to reach the website while a ping was successful.", TrayIcon.MessageType.WARNING);
            trayIcon.setToolTip("Website Not Reachable - IMWD");
        } else if (status == 3) {
            trayIcon.setImage(iconError);
            trayIcon.displayMessage("Website Not Reachable!", "Unable to reach and ping the website.", TrayIcon.MessageType.ERROR);
            trayIcon.setToolTip("Website Not Reachable - IMWD");
        } else if (status == 4) {
            trayIcon.setImage(iconNoConnection);
            trayIcon.displayMessage("No Connection!", "Please check your connection to the internet.", TrayIcon.MessageType.ERROR);
            trayIcon.setToolTip("No Connection - IMWD");
        }
    }
}