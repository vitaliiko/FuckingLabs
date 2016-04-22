package user_gi;

import coder.*;
import components.BoxPanel;
import frame_utils.FrameUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FriendlyWorkspaceGI extends JFrame {

    private Coder coder;
    private String subject;

    private JTextField keyTextField;
    private JTextField inputTextField;
    private JTextField outputTextField;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel inputPanel;
    private JPanel outputPanel;
    private JPanel keyPanel;

    public FriendlyWorkspaceGI(String frameTitle, String subject, Coder coder) {
        super(frameTitle);
        this.coder = coder;
        this.subject = subject;

        FrameUtils.setLookAndFeel();

        addComponents();
        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 310));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void addComponents() {
        prepareComponents();
        prepareButtons();
        BoxPanel panel = new BoxPanel(BoxLayout.Y_AXIS, new JLabel(subject), inputPanel, keyPanel,
                new BoxPanel(encryptButton, decryptButton), outputPanel);
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        getContentPane().add(panel, BorderLayout.CENTER);
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

    private void prepareButtons() {
        encryptButton = new JButton("Зашифровать");
        encryptButton.addActionListener(e -> doCrypt(encryptButton));
        encryptButton.setEnabled(false);

        decryptButton = new JButton("Расшифровать");
        decryptButton.addActionListener(e -> doCrypt(decryptButton));
        decryptButton.setEnabled(false);
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
            FrameUtils.showErrorDialog(this, e1.getMessage());
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
