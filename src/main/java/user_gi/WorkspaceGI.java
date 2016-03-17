package user_gi;

import input_output.IOFileHandling;
import coder.CeasarCoder;
import coder.VigenereCoder;
import components.BoxPanel;
import coder.Coder;
import model.Controller;
import model.User;
import model.UsersRights;
import utils.FrameUtils;
import utils.TextAreasListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

public class WorkspaceGI extends JFrame {

    private User user;
    private Controller controller;
    private Coder coder = CeasarCoder.getInstance();
    private String outputFilePath;

    private JTextField keyField;
    private JTextField alphabetField;
    private JTextField inputFilePathField;
    private JTextField outputFilePathField;
    private JLabel alphabetPowerLabel;
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
    private JFrame usersInfoGI;

    public WorkspaceGI(User user, Controller controller) {
        super("BPZKS-Lab1");
        this.user = user;
        this.controller = controller;

        FrameUtils.setLookAndFeel();

        addComponents();
        setupFrame();
    }

    private void addComponents() {
        prepareMenuBar();
        setJMenuBar(menuBar);

        prepareNorthPanel();
        getContentPane().add(northPanel, BorderLayout.NORTH);

        prepareTextAreasPanel();
        getContentPane().add(textAreasPanel, BorderLayout.CENTER);

        prepareEncryptionPanel();
        getContentPane().add(encryptionPanel, BorderLayout.SOUTH);
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(924, 620));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareNorthPanel() {
        northPanel = new BoxPanel(BoxLayout.Y_AXIS);

        prepareKeyField();
        northPanel.add(new BoxPanel(new JLabel("       Key: "), keyField));

        prepareAlphabetField();
        northPanel.add(new BoxPanel(new JLabel("Alphabet: "), alphabetField));

        prepareAlphabetButtons();
        prepareSelectCoderBox();
        northPanel.add(new BoxPanel(new JLabel("Method: "), selectCoderBox, createAlphabetButton, saveAlphabetButton));

        alphabetPowerLabel = new JLabel(String.valueOf(alphabetField.getText().length()));
        northPanel.add(new BoxPanel(new JLabel("Alphabet power: "), alphabetPowerLabel));

        prepareSelectFilesPanel();
        northPanel.add(selectFilesPanel);
        northPanel.add(new JSeparator());
    }

    private void prepareKeyField() {
        keyField = new JPasswordField(50);
        keyField.setFont(FrameUtils.FONT);
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

    private void prepareAlphabetField() {
        alphabetField = new JTextField(user.getAlphabet() == null ? "" : user.getAlphabet(), 50);
        alphabetField.setFont(FrameUtils.FONT);
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

    private void prepareAlphabetButtons() {
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
        selectCoderBox = new JComboBox<>(new Coder[] {CeasarCoder.getInstance(), VigenereCoder.getInstance()});
        selectCoderBox.addActionListener(e -> coder = (Coder) selectCoderBox.getSelectedItem());
    }

    private void prepareSelectFilesPanel() {
        selectFilesPanel = new JPanel();
        selectFilesPanel.setLayout(new GridLayout(1, 2));

        inputFilePathField = new JTextField(27);
        inputFilePathField.setFont(FrameUtils.FONT);
        JButton selectInputFileButton = new JButton(new ImageIcon("resources/folder.png"));
        selectInputFileButton.addActionListener(e -> {
            String filePath = FrameUtils.callFileChooser();
            if (filePath != null) {
                inputFilePathField.setText(filePath);
                inputTextArea.setText(IOFileHandling.readFromFile(filePath));
            }
        });
        selectFilesPanel.add(new BoxPanel(new JLabel("Original file: "), inputFilePathField, selectInputFileButton));

        outputFilePathField = new JTextField(24);
        outputFilePathField.setFont(FrameUtils.FONT);
        JButton selectOutputFileButton = new JButton(new ImageIcon("resources/folder.png"));
        selectOutputFileButton.addActionListener(e -> {
            outputFilePath = FrameUtils.callFileChooser();
            outputFilePathField.setText(outputFilePath);
        });
        JButton clearButton = new JButton(new ImageIcon("resources/clear.png"));
        clearButton.addActionListener(e -> {
            outputFilePathField.setText("");
            outputFilePath = null;
        });
        selectFilesPanel.add(new BoxPanel(new JLabel("Final file: "), outputFilePathField, selectOutputFileButton, clearButton));
    }

    private void prepareTextAreasPanel() {
        textAreasPanel = new JPanel();
        textAreasPanel.setLayout(new GridLayout(1, 2));

        inputTextArea = new JTextArea();
        JLabel inputTextEntropyLabel = new JLabel("Entropy = 0");
        DocumentListener inputListener = new TextAreasListener(this, inputTextArea, inputTextEntropyLabel, coder);
        FrameUtils.textAreaFactory(inputTextArea, "input", inputListener);

        JPanel inputTextPanel = new BoxPanel(BoxLayout.Y_AXIS, inputTextEntropyLabel,
                new JScrollPane(inputTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        inputTextPanel.setBorder(new EmptyBorder(10, 10, 10, 5));
        textAreasPanel.add(inputTextPanel);

        outputTextArea = new JTextArea();
        JLabel outputTextEntropyLabel = new JLabel("Entropy = 0");
        DocumentListener outputListener = new TextAreasListener(this, inputTextArea, outputTextEntropyLabel, coder);
        FrameUtils.textAreaFactory(outputTextArea, "output", outputListener);

        JPanel outputTextPanel = new BoxPanel(BoxLayout.Y_AXIS, outputTextEntropyLabel,
                new JScrollPane(outputTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        outputTextPanel.setBorder(new EmptyBorder(10, 5, 10, 10));
        textAreasPanel.add(outputTextPanel);
    }

    private void prepareEncryptionPanel() {
        encryptButton = new JButton("Encrypt");
        encryptButton.setEnabled(false);
        encryptButton.addActionListener(e -> {
            try {
                coder.setAlphabet(alphabetField.getText());
                String outputText = coder.encode(keyField.getText(), inputTextArea.getText());
                outputTextArea.setText(outputText);
                writeToFile(outputText);
            } catch (IOException e1) {
                FrameUtils.showErrorDialog(this, e1.getMessage());
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
                FrameUtils.showErrorDialog(this, e1.getMessage());
            }
        });

        encryptionPanel = new BoxPanel(encryptButton, decryptButton);
        encryptionPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    private void writeToFile(String outputLine) {
        if (outputFilePath != null) {
            IOFileHandling.writeToFile(outputLine, outputFilePath);
        }
    }

    private void prepareMenuBar() {
        menuBar = new JMenuBar();
        prepareFileMenu();
        menuBar.add(fileMenu);
        menuBar.add(FrameUtils.prepareHelpMenu());
    }

    private void prepareFileMenu() {
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

    public void checkButtonsEnabled() {
        encryptButton.setEnabled(!keyField.getText().isEmpty() &&
                !alphabetField.getText().isEmpty() &&
                !inputTextArea.getText().isEmpty());
        decryptButton.setEnabled(encryptButton.isEnabled());
    }
}
