package user_gi;

import coder.*;
import components.BoxPanel;
import model.User;
import frame_utils.FrameUtils;
import frame_utils.WorkspaceUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class FriendlyWorkspaceGI extends JFrame {

    private Coder coder;
    private WorkspaceUtil workspaceUtil;

    private JTextField keyTextField;
    private JTextField inputTextField;
    private JTextField outputTextField;
    private JComboBox<Coder> selectCoderBox;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel inputPanel;
    private JPanel outputPanel;
    private JPanel selectPanel;
    private JPanel keyPanel;

    public FriendlyWorkspaceGI(User user) {
        super(WorkspaceUtil.FRAME_NAME);
        this.workspaceUtil = new WorkspaceUtil(this, user);
        this.coder = CeasarCoder.getInstance();

        FrameUtils.setLookAndFeel();

        addComponents();
        setupFrame();
    }

    public void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 310));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void addComponents() {
        setJMenuBar(workspaceUtil.prepareMenuBar());

        prepareComponents();
        prepareButtons();
        BoxPanel panel = new BoxPanel(BoxLayout.Y_AXIS, inputPanel, selectPanel, keyPanel,
                new BoxPanel(encryptButton, decryptButton), outputPanel);
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void prepareComponents() {
        inputTextField = new JTextField();
        inputPanel = createPanelWithComponents("Введите сообщение:", inputTextField);

        prepareSelectCoderBox();
        selectPanel = createPanelWithComponents("Выберете способ шифрования:", selectCoderBox);

        keyTextField = new JTextField("5");
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

    private void prepareSelectCoderBox() {
        selectCoderBox = new JComboBox<>(new Coder[] {
                CeasarCoder.getInstance().createRusoAlphabet(),
                VigenereCoder.getInstance().createRusoAlphabet(),
                VerrnamCoder.getInstance().createRusoAlphabet(),
                CardanGrilleCoder.getInstance().createRusoAlphabet()
        });
        selectCoderBox.addActionListener(e -> {
            coder = (Coder) selectCoderBox.getSelectedItem();
            keyPanel.setVisible(!(coder instanceof CardanGrilleCoder));
        });
        selectCoderBox.setPreferredSize(new Dimension(50, 26));
    }

    private void prepareButtons() {
        encryptButton = new JButton("Зашифровать");
        encryptButton.addActionListener(e -> doCrypt(encryptButton));

        decryptButton = new JButton("Расшифровать");
        decryptButton.addActionListener(e -> doCrypt(decryptButton));
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
}
