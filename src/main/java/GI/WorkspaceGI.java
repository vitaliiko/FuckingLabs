package GI;

import InputOutput.IOFileHandling;
import InputOutput.TextFileFilter;
import coder.CeasarCoder;
import coder.VigenereCoder;
import panels.BoxPanel;
import coder.Coder;
import support.Controller;
import support.User;
import support.UsersRights;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

public class WorkspaceGI extends JFrame {

    private User user;
    private Controller controller;
    private Coder coder;
    private String outputFilePath;

    private JTextField keyField;
    private JTextField alphabetField;
    private JTextField inputFilePathField;
    private JTextField outputFilePathField;
    private JLabel alphabetPowerLabel;
    private JLabel inputTextEntropyLabel;
    private JLabel outputTextEntropyLabel;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JComboBox<Coder> selectCoderBox;
    private JButton createAlphabetButton;
    private JButton saveAlphabetButton;
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
        super("BPZKS-Lab1");
        this.user = user;
        this.controller = controller;
        this.coder = new CeasarCoder();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        addComponents();
        setupFrame();
    }

    public void addComponents() {
        prepareMenuBar();
        setJMenuBar(menuBar);

        prepareNorthPanel();
        getContentPane().add(northPanel, BorderLayout.NORTH);

        prepareTextAreasPanel();
        getContentPane().add(textAreasPanel, BorderLayout.CENTER);

        prepareEncryptionPanel();
        getContentPane().add(encryptionPanel, BorderLayout.SOUTH);
    }

    public void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(924, 620));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prepareNorthPanel() {
        northPanel = new BoxPanel(BoxLayout.Y_AXIS);

        prepareKeyField();
        northPanel.add(new BoxPanel(new JLabel("       Key: "), keyField));

        prepareAlphabetField();
        northPanel.add(new BoxPanel(new JLabel("Alphabet: "), alphabetField));

        prepareAlphabetButtons();
        prepareSelectCoderBox();
        northPanel.add(new BoxPanel(selectCoderBox, createAlphabetButton, saveAlphabetButton));

        alphabetPowerLabel = new JLabel(String.valueOf(alphabetField.getText().length()));
        northPanel.add(new BoxPanel(new JLabel("Alphabet power: "), alphabetPowerLabel));

        prepareSelectFilesPanel();
        northPanel.add(selectFilesPanel);
        northPanel.add(new JSeparator());
    }

    public void prepareKeyField() {
        keyField = new JPasswordField(50);
        keyField.setFont(font);
        keyField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkButtonsEnabled();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkButtonsEnabled();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkButtonsEnabled();
            }
        });
    }

    public void prepareAlphabetField() {
        alphabetField = new JTextField(user.getAlphabet() == null ? "" : user.getAlphabet(), 50);
        alphabetField.setFont(font);
        alphabetField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                alphabetPowerLabel.setText(String.valueOf(alphabetField.getText().length()));
                saveAlphabetButton.setEnabled(!alphabetField.getText().isEmpty());
                checkButtonsEnabled();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });
    }

    public void prepareAlphabetButtons() {
        createAlphabetButton = new JButton("Random", new ImageIcon("resources/random.png"));
        createAlphabetButton.setToolTipText("Create random alphabet");
        createAlphabetButton.addActionListener(e -> {
            coder.createAlphabet(alphabetField.getText());
            alphabetField.setText(coder.getAlphabet());
        });

        saveAlphabetButton = new JButton("Save", new ImageIcon("resources/save.png"));
        saveAlphabetButton.setEnabled(false);
        saveAlphabetButton.addActionListener(e -> {
            user.setAlphabet(alphabetField.getText());
            IOFileHandling.saveUsersSet(controller.getUserSet());
            saveAlphabetButton.setEnabled(false);
        });
    }

    private void prepareSelectCoderBox() {
        selectCoderBox = new JComboBox<>(new Coder[] {new CeasarCoder(), new VigenereCoder()});
        selectCoderBox.addActionListener(e -> coder = (Coder) selectCoderBox.getSelectedItem());
    }

    public void prepareSelectFilesPanel() {
        selectFilesPanel = new JPanel();
        selectFilesPanel.setLayout(new GridLayout(1, 2));

        inputFilePathField = new JTextField(27);
        inputFilePathField.setFont(font);
        JButton selectInputFileButton = new JButton(new ImageIcon("resources/folder.png"));
        selectInputFileButton.addActionListener(e -> {
            String filePath = callFileChooser();
            if (filePath != null) {
                inputFilePathField.setText(filePath);
                inputTextArea.setText(IOFileHandling.readFromFile(filePath));
            }
        });
        selectFilesPanel.add(new BoxPanel(new JLabel("Original file: "), inputFilePathField, selectInputFileButton));

        outputFilePathField = new JTextField(24);
        outputFilePathField.setFont(font);
        JButton selectOutputFileButton = new JButton(new ImageIcon("resources/folder.png"));
        selectOutputFileButton.addActionListener(e -> {
            outputFilePath = callFileChooser();
            outputFilePathField.setText(outputFilePath);
        });
        JButton clearButton = new JButton(new ImageIcon("resources/clear.png"));
        clearButton.addActionListener(e -> {
            outputFilePathField.setText("");
            outputFilePath = null;
        });
        selectFilesPanel.add(new BoxPanel(new JLabel("Final file: "), outputFilePathField, selectOutputFileButton, clearButton));
    }

    public String callFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new TextFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public void prepareTextAreasPanel() {
        textAreasPanel = new JPanel();
        textAreasPanel.setLayout(new GridLayout(1, 2));

        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(font);
        inputTextArea.setName("input");
        inputTextArea.getDocument().addDocumentListener(new TextAreasListener(inputTextArea));
        inputTextEntropyLabel = new JLabel("Entropy = 0");
        JPanel inputTextPanel = new BoxPanel(BoxLayout.Y_AXIS, inputTextEntropyLabel,
                new JScrollPane(inputTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        inputTextPanel.setBorder(new EmptyBorder(10, 10, 10, 5));
        textAreasPanel.add(inputTextPanel);

        outputTextArea = new JTextArea();
        outputTextArea.setLineWrap(true);
        outputTextArea.setFont(font);
        outputTextArea.setName("output");
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.getDocument().addDocumentListener(new TextAreasListener(outputTextArea));
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
        encryptButton.addActionListener(e -> {
            try {
            coder.setAlphabet(alphabetField.getText());
            String outputText = coder.encode(keyField.getText(), inputTextArea.getText());
            outputTextArea.setText(outputText);
            writeToFile(outputText);
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        });

        decryptButton = new JButton("Decrypt");
        decryptButton.setEnabled(false);
        decryptButton.addActionListener(e -> {
            try {
                coder.setAlphabet(alphabetField.getText());
                String outputText = coder.decode(keyField.getText(), inputTextArea.getText());
                outputTextArea.setText(outputText);
                writeToFile(outputText);
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        });

        encryptionPanel = new BoxPanel(encryptButton, decryptButton);
        encryptionPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    public void writeToFile(String outputLine) {
        if (outputFilePath != null) {
            IOFileHandling.writeToFile(outputLine, outputFilePath);
        }
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
                "<html>Програму розробив студент групи СПС-1466 Кобрін В.О.<br><br>" +
                        "ЛР №2:<br>" +
                        "Індивідувальне завдання згідно варіанта №9:<br>" +
                        "При перевірці на валідність обраного користувачем пароля<br>" +
                        "наобхідно враховувати наявність великих і малих літер,<br>" +
                        "цифр та розділових знаків.<br><br>" +
                        "ЛР №3:<br>" +
                        "Шифрування та розшифровування текстових повідомлень методом Віженера.</html>", "About",
                JOptionPane.DEFAULT_OPTION));
        helpMenu.add(aboutItem);
    }

    public void checkButtonsEnabled() {
        encryptButton.setEnabled(!keyField.getText().isEmpty() &&
                !alphabetField.getText().isEmpty() &&
                !inputTextArea.getText().isEmpty());
        decryptButton.setEnabled(encryptButton.isEnabled());
    }

    public class TextAreasListener implements DocumentListener {

        JTextArea textArea;

        public TextAreasListener(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            float entropy = coder.calculateEntropy(textArea.getText());
            if (textArea.getName().equals("input")) {
                inputTextEntropyLabel.setText("Entropy: " + String.valueOf(entropy));
            } else {
                outputTextEntropyLabel.setText("Entropy: " + String.valueOf(entropy));
            }
            checkButtonsEnabled();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            insertUpdate(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            insertUpdate(e);
        }
    }
}
