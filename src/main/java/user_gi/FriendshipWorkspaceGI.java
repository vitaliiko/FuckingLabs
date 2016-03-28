package user_gi;

import coder.CeasarCoder;
import coder.Coder;
import coder.VerrnamCoder;
import coder.VigenereCoder;
import components.BoxPanel;
import model.User;
import utils.FrameUtils;
import utils.WorkspaceUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class FriendshipWorkspaceGI extends JFrame {

    private Coder coder = CeasarCoder.getInstance();
    private WorkspaceUtil workspaceUtil;

    private JFormattedTextField keyField;
    private JTextField inputTextField;
    private JTextField outputTextField;
    private JComboBox<Coder> selectCoderBox;
    private JButton encryptButton;
    private JButton decryptButton;
    private JPanel inputPanel;
    private JPanel outputPanel;
    private JPanel selectPanel;

    public FriendshipWorkspaceGI(User user) {
        super(WorkspaceUtil.FRAME_NAME);
        this.workspaceUtil = new WorkspaceUtil(this, user);

        FrameUtils.setLookAndFeel();

        addComponents();
        setupFrame();
    }

    public void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 255));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void addComponents() {
        setJMenuBar(workspaceUtil.prepareMenuBar());

        prepareInputPanel();
        prepareSelectPanel();
        prepareButtonsPanel();
        prepareOutputPanel();
        BoxPanel panel = new BoxPanel(BoxLayout.Y_AXIS, inputPanel, selectPanel,
                new BoxPanel(encryptButton, decryptButton), outputPanel);
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void prepareInputPanel() {
        JLabel label = new JLabel("Введите открытое сообщение:");
        label.setBorder(new EmptyBorder(0, 0, 4, 0));
        inputTextField = new JTextField();
        inputPanel = new BoxPanel(BoxLayout.Y_AXIS, label, inputTextField);
        inputPanel.setBorder(new EmptyBorder(0, 0, 8, 0));
    }

    private void prepareSelectPanel() {
        JLabel selectLabel = new JLabel("Выберете способ шифрования:");
        selectLabel.setBorder(new EmptyBorder(0, 0, 4, 0));
        
        JLabel keyLabel = new JLabel("Ключ:");
        keyLabel.setBorder(new EmptyBorder(0, 0, 4, 0));
        
        prepareSelectCoderBox();
        BoxPanel boxPanel = new BoxPanel(BoxLayout.Y_AXIS, selectLabel, selectCoderBox);
        boxPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        keyField = new JFormattedTextField();
        keyField.setText("5");
        
        selectPanel = new BoxPanel(BoxLayout.X_AXIS, boxPanel,
                new BoxPanel(BoxLayout.Y_AXIS, keyLabel, keyField));
        selectPanel.setBorder(new EmptyBorder(0, 0, 8, 0));
    }

    private void prepareSelectCoderBox() {
        selectCoderBox = new JComboBox<>(new Coder[] {
                CeasarCoder.getInstance(),
                VigenereCoder.getInstance(),
                VerrnamCoder.getInstance()
        });
        selectCoderBox.addActionListener(e -> coder = (Coder) selectCoderBox.getSelectedItem());
        selectCoderBox.setPreferredSize(new Dimension(150, 23));
    }

    private void prepareButtonsPanel() {
        encryptButton = new JButton("Зашифровать");
        encryptButton.addActionListener(e -> doCrypt(encryptButton));

        decryptButton = new JButton("Расшифровать");
        decryptButton.addActionListener(e -> doCrypt(decryptButton));
    }

    private void doCrypt(JButton button) {
        try {
            coder.createRusoAlphabet();
            String outputText;
            if (button.getText().equals("Зашифровать")) {
                outputText = coder.encode(keyField.getText(), inputTextField.getText());
            } else {
                outputText = coder.decode(keyField.getText(), inputTextField.getText());
            }
            outputTextField.setText(outputText);
        } catch (IOException e1) {
            FrameUtils.showErrorDialog(this, e1.getMessage());
        }
    }

    private void prepareOutputPanel() {
        JLabel label = new JLabel("Результат:");
        label.setBorder(new EmptyBorder(0, 0, 4, 0));
        outputTextField = new JTextField();
        outputPanel = new BoxPanel(BoxLayout.Y_AXIS, label, outputTextField);
        outputPanel.setBorder(new EmptyBorder(8, 0, 0, 0));
    }
}
