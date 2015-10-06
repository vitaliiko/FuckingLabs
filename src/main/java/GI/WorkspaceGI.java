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
    private JTextField inputFilePathField;
    private JTextField outputFilePathField;
    private JLabel alphabetPowerLabel;
    private JLabel inputTextEntropyLabel;
    private JLabel outputTextEntropyLabel;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton createAlphabetButton;
    private JButton saveAlphabetButton;
    private JButton selectInputFileButton;
    private JButton selectOutputFileButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel selectFilesPanel;
    private JPanel textAreasPanel;
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
        northPanel = new BoxPanel(BoxLayout.Y_AXIS);

        keyField = new JTextField(40);
        keyField.setFont(font);
        northPanel.add(new BoxPanel(new JLabel("       Key: "), keyField));

        alphabetField = new JTextField(40);
        alphabetField.setFont(font);
        northPanel.add(new BoxPanel(new JLabel("Alphabet: "), alphabetField));

        prepareAlphabetButtons();
        northPanel.add(new BoxPanel(createAlphabetButton, saveAlphabetButton));

        northPanel.add(new BoxPanel(new JLabel("Alphabet power: "), alphabetPowerLabel = new JLabel("0")));

        prepareSelectFilesPanel();
        northPanel.add(selectFilesPanel);
        northPanel.add(new JSeparator());
    }

    public void prepareAlphabetButtons() {
        createAlphabetButton = new JButton("Random", new ImageIcon("resources/random.png"));
        createAlphabetButton.setToolTipText("Create random alphabet");

        saveAlphabetButton = new JButton("Save", new ImageIcon("resources/save.png"));
        saveAlphabetButton.setEnabled(false);
    }

    public void prepareSelectFilesPanel() {
        selectFilesPanel = new JPanel();
        selectFilesPanel.setLayout(new GridLayout(1, 2));

        inputFilePathField = new JTextField(27);
        inputFilePathField.setFont(font);
        selectInputFileButton = new JButton(new ImageIcon("resources/folder.png"));
        selectFilesPanel.add(new BoxPanel(new JLabel("Original file: "), inputFilePathField, selectInputFileButton));

        outputFilePathField = new JTextField(27);
        outputFilePathField.setFont(font);
        selectOutputFileButton = new JButton(new ImageIcon("resources/folder.png"));
        selectFilesPanel.add(new BoxPanel(new JLabel("Final file: "), outputFilePathField, selectOutputFileButton));
    }

    public void prepareTextAreasPanel() {
        textAreasPanel = new JPanel();
        textAreasPanel.setLayout(new GridLayout(1, 2));

        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setFont(font);
        inputTextEntropyLabel = new JLabel("Entropy = 0");
        JPanel inputTextPanel = new BoxPanel(BoxLayout.Y_AXIS, inputTextEntropyLabel,
                new JScrollPane(inputTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        inputTextPanel.setBorder(new EmptyBorder(10, 10, 10, 5));
        textAreasPanel.add(inputTextPanel);

        outputTextArea = new JTextArea();
        outputTextArea.setLineWrap(true);
        outputTextArea.setFont(font);
        outputTextEntropyLabel = new JLabel("Entropy = 0");
        JPanel outputTextPanel = new BoxPanel(BoxLayout.Y_AXIS, outputTextEntropyLabel,
                new JScrollPane(outputTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        outputTextPanel.setBorder(new EmptyBorder(10, 5, 10, 10));
        textAreasPanel.add(outputTextPanel);
    }

    public void prepareEncryptionPanel() {
        encryptButton = new JButton("Encrypt");
        encryptButton.setEnabled(false);

        decryptButton = new JButton("Decrypt");
        decryptButton.setEnabled(false);

        encryptionPanel = new BoxPanel(encryptButton, decryptButton);
        encryptionPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
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

        JMenuItem settingsItem = new JMenuItem("Settings          ");
        settingsItem.setIcon(new ImageIcon("resources/settings.png"));
        settingsItem.addActionListener(e -> new SettingsGI(this, user, controller));
        fileMenu.add(settingsItem);

        JMenuItem usersItem = new JMenuItem("Users info");
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

        JMenuItem aboutItem = new JMenuItem("About           ");
        aboutItem.addActionListener(e -> JOptionPane.showConfirmDialog(null,
                "<html>Програму розробив студент групи СПС-1466 Кобрін В.О.<br>" +
                        "Індивідувальне завдання згідно варіанта №9:<br>" +
                        "При перевірці на валідність обраного користувачем пароля<br>" +
                        "наобхідно враховувати наявність великих і малих літер,<br>" +
                        "цифр та розділових знаків.</html>", "About",
                JOptionPane.DEFAULT_OPTION));
        helpMenu.add(aboutItem);
    }
}
