package user_gi;

import coder.CardanGrilleCoder;
import coder.CeasarCoder;
import coder.Coder;
import coder.VerrnamCoder;
import coder.VigenereCoder;
import components.BoxPanel;
import frame_utils.FrameUtils;
import frame_utils.WorkspaceUtil;
import java.awt.*;
import javax.swing.*;

public class MainWindowGI extends JFrame {

    private JPanel buttonPanel;

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
        setMinimumSize(new Dimension(300, 310));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void prepareButtonPanel() {
        buttonPanel = new BoxPanel(BoxLayout.Y_AXIS);
        buttonPanel.add(createButton("Лабораторна робота №1",
                "Шифрування методом \"Код Цезаря\"",
                CeasarCoder.getInstance()));
        buttonPanel.add(createButton("Лабораторна робота №3",
                "Шифрування за допомогою таблиці Віженера",
                VigenereCoder.getInstance()));
        buttonPanel.add(createButton("Лабораторна робота №4",
                "Шифрування методом \"Решітка кардано\"",
                CardanGrilleCoder.getInstance()));
        buttonPanel.add(createButton("Лабораторна робота №5",
                "Шифрування з використанням шифру Вернама",
                VerrnamCoder.getInstance()));
    }

    private JButton createButton(String title, String subject, Coder coder) {
        JButton button = new JButton(title);
        button.addActionListener(e -> {
            new FriendlyWorkspaceGI(title, subject, coder);
            this.dispose();
        });
        button.setMaximumSize(new Dimension(300, 40));
        return button;
    }
}
