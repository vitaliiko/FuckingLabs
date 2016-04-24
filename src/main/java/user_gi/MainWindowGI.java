package user_gi;

import coder.*;
import components.BoxPanel;
import frame_utils.FrameUtils;
import frame_utils.WorkspaceUtil;
import input_output.IOFileHandling;
import model.SingleController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainWindowGI extends JFrame {

    private JPanel buttonPanel;
    private Dimension buttonDimension = new Dimension(300, 40);

    public MainWindowGI() throws HeadlessException {
        super(WorkspaceUtil.FRAME_NAME);
        FrameUtils.setLookAndFeel();

        prepareButtonPanel();
        getContentPane().add(new JLabel(WorkspaceUtil.FRAME_NAME), BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.CENTER);

        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setMinimumSize(new Dimension(300, 310));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void prepareButtonPanel() {
        buttonPanel = new BoxPanel(BoxLayout.Y_AXIS);
        buttonPanel.add(createButton("Лабораторна робота №1",
                "Шифрування методом \"Код Цезаря\"",
                CeasarCoder.getInstance().createRusoAlphabet()));
        buttonPanel.add(createButton("Лабораторна робота №2", e -> {
            SingleController.getInstance().setUserSet(IOFileHandling.loadUsers());
            new AuthenticationGI();
            this.dispose();
        }));
        buttonPanel.add(createButton("Лабораторна робота №3",
                "Шифрування за допомогою таблиці Віженера",
                VigenereCoder.getInstance().createRusoAlphabet()));
        buttonPanel.add(createButton("Лабораторна робота №4",
                "Шифрування методом \"Решітка кардано\"",
                CardanGrilleCoder.getInstance().createRusoAlphabet()));
        buttonPanel.add(createButton("Лабораторна робота №5",
                "Шифрування з використанням шифру Вернама",
                VerrnamCoder.getInstance().createRusoAlphabet()));
    }

    private JButton createButton(String title, String subject, Coder coder) {
        JButton button = new JButton(title);
        button.addActionListener(e -> {
            new FriendlyWorkspaceGI(title, subject, coder);
            this.dispose();
        });
        button.setMaximumSize(buttonDimension);
        return button;
    }

    private JButton createButton(String title, ActionListener listener) {
        JButton button = new JButton(title);
        button.addActionListener(listener);
        button.setMaximumSize(buttonDimension);
        return button;
    }
}
