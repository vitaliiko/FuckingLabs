package user_gi;

import coder.Coder;
import components.BoxPanel;
import frame_utils.FrameUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class FriendlyWorkspaceGI extends JFrame {

    private Coder coder;
    private String subject;

    private JTextField keyTextField;
    private JTextField inputTextField;
    private JTextField outputTextField;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton mainMenuButton;
    private JPanel inputPanel;
    private JPanel outputPanel;
    private JPanel keyPanel;

    public FriendlyWorkspaceGI(String frameTitle, String subject, Coder coder) {
        super(frameTitle);
        this.coder = coder;
        this.subject = subject;

        FrameUtil.setLookAndFeel();

        addComponents();
        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new MainWindowGI();
            }
        });
    }

    private void addComponents() {
        prepareComponents();
        prepareCryptButtons();
        JLabel subjectLabel = new JLabel(subject);
        subjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subjectLabel.setFont(FrameUtil.ARIAL_12);
        subjectLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
        getContentPane().add(subjectLabel, BorderLayout.NORTH);

        BoxPanel panel = new BoxPanel(BoxLayout.Y_AXIS, inputPanel, keyPanel,
                new BoxPanel(encryptButton, decryptButton), outputPanel);
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        getContentPane().add(panel, BorderLayout.CENTER);

        prepareMainMenuButton();
        getContentPane().add(new BoxPanel(mainMenuButton), BorderLayout.SOUTH);
    }

    private void prepareComponents() {
        inputTextField = new JTextField();
        inputTextField.getDocument().addDocumentListener(new InputTextListener());
        inputPanel = createPanelWithComponents("Введите сообщение:", inputTextField);

        keyTextField = new JTextField("5");
        keyTextField.getDocument().addDocumentListener(new InputTextListener());
        keyPanel = createPanelWithComponents("Введите ключ:", keyTextField);

        outputTextField = new JTextField();
        outputPanel = createPanelWithComponents("Результат:", outputTextField);
    }

    private JPanel createPanelWithComponents(String text, JComponent component) {
        JLabel label = new JLabel(text);
        label.setBorder(new EmptyBorder(4, 0, 4, 0));
        JPanel panel = new BoxPanel(BoxLayout.Y_AXIS, label, component);
        panel.setBorder(new EmptyBorder(0, 0, 6, 0));
        return panel;
    }

    private void prepareCryptButtons() {
        encryptButton = new JButton("Зашифровать");
        encryptButton.addActionListener(e -> doCrypt(encryptButton));
        encryptButton.setEnabled(false);

        decryptButton = new JButton("Расшифровать");
        decryptButton.addActionListener(e -> doCrypt(decryptButton));
        decryptButton.setEnabled(false);
    }

    private void prepareMainMenuButton() {
        mainMenuButton = new JButton("Меню", new ImageIcon("resources/menu.png"));
        mainMenuButton.addActionListener(e -> {
            new MainWindowGI();
            this.dispose();
        });
    }

    private void doCrypt(JButton button) {
        try {
            String outputText;
            if (button.getText().equals("Зашифровать")) {
                outputText = coder.encode(keyTextField.getText(), inputTextField.getText());
            } else {
                outputText = coder.decode(keyTextField.getText(), inputTextField.getText());
            }
            outputTextField.setText(outputText);
        } catch (IOException e1) {
            FrameUtil.showErrorDialog(this, e1.getMessage());
        }
    }

    private class InputTextListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            encryptButton.setEnabled(!inputTextField.getText().isEmpty() && !keyTextField.getText().isEmpty());
            decryptButton.setEnabled(encryptButton.isEnabled());
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
