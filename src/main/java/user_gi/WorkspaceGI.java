package user_gi;

import coder.*;
import input_output.IOFileHandling;
import components.BoxPanel;
import model.User;
import frame_utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

public class WorkspaceGI extends JFrame {

    private User user;
    private Coder coder;
    private String outputFilePath;
    private WorkspaceUtil workspaceUtil;

    private JTextField keyField;
    private JTextField alphabetField;
    private JTextField inputFilePathField;
    private JTextField outputFilePathField;
    private JLabel alphabetPowerLabel;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JComboBox<Coder> selectCoderBox;
    private JButton generateAlphabetButton;
    private JButton saveAlphabetButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton generateKeyButton;
    private JPanel selectFilesPanel;
    private JPanel textAreasPanel;
    private JPanel northPanel;
    private JPanel encryptionPanel;

    public WorkspaceGI(User user) {
        super(WorkspaceUtil.FRAME_NAME);
        this.user = user;
        this.workspaceUtil = new WorkspaceUtil(this, user);
        this.coder = CeasarCoder.getInstance();

        FrameUtil.setLookAndFeel();

        addComponents();
        setupFrame();
    }

    public void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(924, 620));
        setIconImage(new ImageIcon("resources/finger.png").getImage());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addComponents() {
        setJMenuBar(workspaceUtil.prepareMenuBar());

        prepareNorthPanel();
        getContentPane().add(northPanel, BorderLayout.NORTH);

        prepareTextAreasPanel();
        getContentPane().add(textAreasPanel, BorderLayout.CENTER);

        prepareEncryptionPanel();
        getContentPane().add(encryptionPanel, BorderLayout.SOUTH);
    }

    private void prepareNorthPanel() {
        northPanel = new BoxPanel(BoxLayout.Y_AXIS);

        prepareKeyField();
        northPanel.add(new BoxPanel(new JLabel("       Key: "), keyField));

        prepareAlphabetField();
        northPanel.add(new BoxPanel(new JLabel("Alphabet: "), alphabetField));

        prepareAlphabetButtons();
        prepareSelectCoderBox();
        northPanel.add(new BoxPanel(new JLabel("Method: "),
                selectCoderBox, generateAlphabetButton, saveAlphabetButton, generateKeyButton));

        alphabetPowerLabel = new JLabel(String.valueOf(alphabetField.getText().length()));
        northPanel.add(new BoxPanel(new JLabel("Alphabet power: "), alphabetPowerLabel));

        prepareSelectFilesPanel();
        northPanel.add(selectFilesPanel);
        northPanel.add(new JSeparator());
    }

    private void prepareKeyField() {
        keyField = new JPasswordField(50);
        keyField.setFont(FrameUtil.ARIAL_12);
        keyField.getDocument().addDocumentListener(new KeyFieldListener(this));
    }

    private void prepareAlphabetField() {
        alphabetField = new JTextField(user.getAlphabet() == null ? "" : user.getAlphabet(), 50);
        alphabetField.setFont(FrameUtil.ARIAL_12);
        alphabetField.getDocument().addDocumentListener(new AlphabetFieldListener(this));
    }

    private void prepareAlphabetButtons() {
        generateAlphabetButton = new JButton("Random", new ImageIcon("resources/random.png"));
        generateAlphabetButton.setToolTipText("Generate random alphabet");
        generateAlphabetButton.addActionListener(e -> {
            coder.createAlphabet(alphabetField.getText());
            alphabetField.setText(coder.getAlphabet());
        });

        generateKeyButton = new JButton("Generate key");
        generateKeyButton.setVisible(false);
        generateKeyButton.addActionListener(e -> {
            String key = KeyGenerator.generateKey(alphabetField.getText(), inputTextArea.getText().length());
            keyField.setText(key);
        });

        saveAlphabetButton = new JButton("Save", new ImageIcon("resources/save.png"));
        saveAlphabetButton.setEnabled(false);
        saveAlphabetButton.addActionListener(e -> {
            user.setAlphabet(alphabetField.getText());
            IOFileHandling.saveUsers();
            saveAlphabetButton.setEnabled(false);
        });
    }

    private void prepareSelectCoderBox() {
        selectCoderBox = new JComboBox<>(new Coder[] {
                CeasarCoder.getInstance(),
                VigenereCoder.getInstance(),
                VerrnamCoder.getInstance(),
                CardanGrilleCoder.getInstance()
        });
        selectCoderBox.addActionListener(e -> {
            coder = (Coder) selectCoderBox.getSelectedItem();
            keyField.setEnabled(!(coder instanceof CardanGrilleCoder));
            alphabetField.setEnabled(keyField.isEnabled());
            generateKeyButton.setVisible(coder instanceof VerrnamCoder);
        });
        selectCoderBox.setPreferredSize(new Dimension(150, 23));
    }

    private void prepareSelectFilesPanel() {
        selectFilesPanel = new JPanel();
        selectFilesPanel.setLayout(new GridLayout(1, 2));

        inputFilePathField = new JTextField(27);
        inputFilePathField.setFont(FrameUtil.ARIAL_12);
        JButton selectInputFileButton = new JButton(new ImageIcon("resources/folder.png"));
        selectInputFileButton.addActionListener(e -> {
            String filePath = FrameUtil.callFileChooser();
            if (filePath != null) {
                inputFilePathField.setText(filePath);
                inputTextArea.setText(IOFileHandling.readFromFile(filePath));
            }
        });
        selectFilesPanel.add(new BoxPanel(new JLabel("Input file: "), inputFilePathField, selectInputFileButton));

        outputFilePathField = new JTextField(24);
        outputFilePathField.setFont(FrameUtil.ARIAL_12);
        JButton selectOutputFileButton = new JButton(new ImageIcon("resources/folder.png"));
        selectOutputFileButton.addActionListener(e -> {
            outputFilePath = FrameUtil.callFileChooser();
            outputFilePathField.setText(outputFilePath);
        });
        JButton clearButton = new JButton(new ImageIcon("resources/clear.png"));
        clearButton.addActionListener(e -> {
            outputFilePathField.setText("");
            outputFilePath = null;
        });
        selectFilesPanel.add(new BoxPanel(new JLabel("Output file: "), outputFilePathField, selectOutputFileButton, clearButton));
    }

    private void prepareTextAreasPanel() {
        textAreasPanel = new JPanel();
        textAreasPanel.setLayout(new GridLayout(1, 2));

        inputTextArea = new JTextArea();
        JLabel inputTextEntropyLabel = new JLabel("Entropy = 0");
        DocumentListener inputListener = new TextAreasListener(this, inputTextArea, inputTextEntropyLabel, coder);
        FrameUtil.textAreaFactory(inputTextArea, "input", inputListener);

        JPanel inputTextPanel = new BoxPanel(BoxLayout.Y_AXIS,
                inputTextEntropyLabel,
                FrameUtil.createScroll(inputTextArea));
        inputTextPanel.setBorder(new EmptyBorder(10, 10, 10, 5));
        textAreasPanel.add(inputTextPanel);

        outputTextArea = new JTextArea();
        JLabel outputTextEntropyLabel = new JLabel("Entropy = 0");
        DocumentListener outputListener = new TextAreasListener(this, inputTextArea, outputTextEntropyLabel, coder);
        FrameUtil.textAreaFactory(outputTextArea, "output", outputListener);

        JPanel outputTextPanel = new BoxPanel(BoxLayout.Y_AXIS,
                outputTextEntropyLabel,
                FrameUtil.createScroll(outputTextArea));
        outputTextPanel.setBorder(new EmptyBorder(10, 5, 10, 10));
        textAreasPanel.add(outputTextPanel);
    }

    private void prepareEncryptionPanel() {
        encryptButton = new JButton("Encrypt");
        encryptButton.setEnabled(false);
        encryptButton.addActionListener(e -> doCrypt(encryptButton));

        decryptButton = new JButton("Decrypt");
        decryptButton.setEnabled(false);
        decryptButton.addActionListener(e -> doCrypt(decryptButton));

        encryptionPanel = new BoxPanel(encryptButton, decryptButton);
        encryptionPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    private void doCrypt(JButton button) {
        try {
            coder.setAlphabet(alphabetField.getText());
            String outputText;
            if (button.getText().equals("Encrypt")) {
                outputText = coder.encode(keyField.getText(), inputTextArea.getText());
            } else {
                outputText = coder.decode(keyField.getText(), inputTextArea.getText());
            }
            outputTextArea.setText(outputText);
            IOFileHandling.writeToFile(outputText, outputFilePath);
        } catch (IOException e1) {
            FrameUtil.showErrorDialog(this, e1.getMessage());
        }
    }

    public void checkButtonsEnabled() {
        encryptButton.setEnabled(!keyField.getText().isEmpty() ||
                !keyField.isEnabled() &&
                !alphabetField.getText().isEmpty() &&
                !inputTextArea.getText().isEmpty());
        decryptButton.setEnabled(encryptButton.isEnabled());
    }

    public void checkAlphabetPowerAndSaveButton() {
        alphabetPowerLabel.setText(String.valueOf(alphabetField.getText().length()));
        saveAlphabetButton.setEnabled(!alphabetField.getText().isEmpty());
    }
}