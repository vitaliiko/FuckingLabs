package GI;

import support.Controller;
import support.User;
import support.UsersRights;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WorkspaceGI extends JFrame {

    private User user;
    private Controller controller;

    private JTextField keyField;
    private JTextField alphabetField;
    private JTextField originalFilePathField;
    private JTextField finalFilePathField;
    private JLabel alphabetPowerLabel;
    private JTextArea originalTextArea;
    private JTextArea finalTextArea;
    private JButton createAlphabetButton;
    private JButton saveAlphabetButton;
    private JButton selectOriginalFileButton;
    private JButton selectFinalFileButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel selectFilesPanel;
    private JPanel textAreasPanel;
    private JPanel alphabetButtonsPanel;
    private JPanel northPanel;
    private JPanel encryptionPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JFrame usersInfoGI;

    private Font font = new Font("Arial", Font.PLAIN, 12);

    public WorkspaceGI(User user, Controller controller) {
        super("NBKS-Lab2");
        this.user = user;
        this.controller = controller;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        prepareMenuBar();
        setJMenuBar(menuBar);

        prepareNorthPanel();
        getContentPane().add(northPanel, BorderLayout.NORTH);

        prepareTextAreasPanel();
        getContentPane().add(textAreasPanel, BorderLayout.CENTER);

        prepareEncryptionPanel();
        getContentPane().add(encryptionPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(924, 620));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prepareNorthPanel() {
        northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        JPanel keyPanel = new JPanel();
        keyPanel.add(new JLabel("       Key: "));
        keyField = new JTextField(40);
        keyField.setFont(font);
        keyPanel.add(keyField);
        northPanel.add(keyPanel);

        JPanel alphabetPanel = new JPanel();
        alphabetPanel.add(new JLabel("Alphabet: "));
        alphabetField = new JTextField(40);
        alphabetField.setFont(font);
        alphabetPanel.add(alphabetField);
        northPanel.add(alphabetPanel);

        prepareAlphabetButtonsPanel();
        northPanel.add(alphabetButtonsPanel);

        JPanel powerPanel = new JPanel();
        powerPanel.add(new JLabel("Alphabet power: "));
        powerPanel.add(alphabetPowerLabel = new JLabel("0"));
        northPanel.add(powerPanel);

        prepareSelectFilesPanel();
        northPanel.add(selectFilesPanel);
        northPanel.add(new JSeparator());
    }

    public void prepareAlphabetButtonsPanel() {
        alphabetButtonsPanel = new JPanel();

        createAlphabetButton = new JButton("Random", new ImageIcon("resources/random.png"));
        createAlphabetButton.setToolTipText("Create random alphabet");
        alphabetButtonsPanel.add(createAlphabetButton);

        saveAlphabetButton = new JButton("Save", new ImageIcon("resources/save.png"));
        saveAlphabetButton.setEnabled(false);
        alphabetButtonsPanel.add(saveAlphabetButton);
    }

    public void prepareSelectFilesPanel() {
        selectFilesPanel = new JPanel();
        selectFilesPanel.setLayout(new GridLayout(1, 2));

        JPanel originalPanel = new JPanel();
        originalPanel.add(new JLabel("Original file: "));
        originalFilePathField = new JTextField(27);
        originalFilePathField.setFont(font);
        originalPanel.add(originalFilePathField);
        selectOriginalFileButton = new JButton(new ImageIcon("resources/folder.png"));
        originalPanel.add(selectOriginalFileButton);

        JPanel finalPanel = new JPanel();
        finalPanel.add(new JLabel("Final file: "));
        finalFilePathField = new JTextField(27);
        finalFilePathField.setFont(font);
        finalPanel.add(finalFilePathField);
        selectFinalFileButton = new JButton(new ImageIcon("resources/folder.png"));
        finalPanel.add(selectFinalFileButton);

        selectFilesPanel.add(originalPanel);
        selectFilesPanel.add(finalPanel);
    }

    public void prepareTextAreasPanel() {
        textAreasPanel = new JPanel();
        textAreasPanel.setLayout(new GridLayout(1, 2));

        originalTextArea = new JTextArea();
        originalTextArea.setLineWrap(true);
        originalTextArea.setFont(font);
        JScrollPane originalScroll = new JScrollPane(originalTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        originalScroll.setBorder(new EmptyBorder(10, 10, 10, 5));
        textAreasPanel.add(originalScroll);

        finalTextArea = new JTextArea();
        finalTextArea.setLineWrap(true);
        finalTextArea.setFont(font);
        JScrollPane finalScroll = new JScrollPane(finalTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        finalScroll.setBorder(new EmptyBorder(10, 5, 10, 10));
        textAreasPanel.add(finalScroll);
    }

    public void prepareEncryptionPanel() {
        encryptionPanel = new JPanel();
        encryptionPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        encryptButton = new JButton("Encrypt");
        encryptButton.setEnabled(false);
        encryptionPanel.add(encryptButton);

        decryptButton = new JButton("Decrypt");
        decryptButton.setEnabled(false);
        encryptionPanel.add(decryptButton);
    }

    public void prepareMenuBar() {
        menuBar = new JMenuBar();
        prepareFileMenu();
        menuBar.add(fileMenu);
        prepareHelpMenu();
        menuBar.add(helpMenu);
    }

    public void prepareFileMenu() {
        fileMenu = new JMenu("File");

        JMenuItem logoutItem = new JMenuItem("Log out");
        logoutItem.setIcon(new ImageIcon("resources/logout.png"));
        logoutItem.addActionListener(e -> {
            dispose();
            if (usersInfoGI != null) {
                usersInfoGI.dispose();
            }
        });
        fileMenu.add(logoutItem);

        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.setIcon(new ImageIcon("resources/settings.png"));
        settingsItem.addActionListener(e -> new SettingsGI(this, user, controller));
        fileMenu.add(settingsItem);

        JMenuItem usersItem = new JMenuItem("Users");
        usersItem.setIcon(new ImageIcon("resources/users.png"));
        if (user.getRights() != UsersRights.ADMIN) {
            usersItem.setVisible(false);
        }
        usersItem.addActionListener(e -> usersInfoGI = new UsersInfoGI(controller));
        fileMenu.add(usersItem);

        JMenuItem addUsersItem = new JMenuItem("Add user");
        addUsersItem.setIcon(new ImageIcon("resources/addUsers.png"));
        if (user.getRights() != UsersRights.ADMIN) {
            addUsersItem.setVisible(false);
        }
        addUsersItem.addActionListener(e -> new AddEmptyUserGI(this, controller));
        fileMenu.add(addUsersItem);

        fileMenu.addSeparator();

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        fileMenu.add(closeItem);
    }

    public void prepareHelpMenu() {
        helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showConfirmDialog(null,
                "<html>Програму розробив студент групи СПС-1466 Кобрін В.О.<br>" +
                        "Індивідувальне завдання згідно варіанта №9:<br>" +
                        "При перевірці на валідність обраного користувачем пароля<br>" +
                        "наобхідно враховувати наявність великих і малих літер,<br>" +
                        "цифр та розділових знаків</html>", "About",
                JOptionPane.DEFAULT_OPTION));
        helpMenu.add(aboutItem);
    }
}
