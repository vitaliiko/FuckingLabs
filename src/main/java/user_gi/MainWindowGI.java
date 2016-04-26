package user_gi;

import coder.*;
import components.BoxPanel;
import frame_utils.FrameUtil;
import frame_utils.WorkspaceUtil;
import input_output.IOFileHandling;
import model.SingleController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainWindowGI extends JFrame {

    private JPanel buttonPanel;
    private JLabel titleLabel;
    private Dimension buttonDimension = new Dimension(320, 40);

    public MainWindowGI() throws HeadlessException {
        super(WorkspaceUtil.FRAME_NAME);
        FrameUtil.setLookAndFeel();

        prepareTitleLAbel();
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        prepareButtonPanel();
        getContentPane().add(buttonPanel, BorderLayout.CENTER);

        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setMinimumSize(new Dimension(320, 400));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void prepareButtonPanel() {
        buttonPanel = new BoxPanel(BoxLayout.Y_AXIS);
        buttonPanel.setBorder(new EmptyBorder(5, 8, 8, 8));
        buttonPanel.add(createButton("Лабораторная работа №1",
                "Шифрование методом \"Код Цезаря\"",
                CeasarCoder.getInstance().createRusoAlphabet()));
        buttonPanel.add(createButton("Лабораторная работа №2", e -> {
            SingleController.getInstance().setUserSet(IOFileHandling.loadUsers());
            new AuthenticationGI();
            this.dispose();
        }));
        buttonPanel.add(createButton("Лабораторная работа №3",
                "Шифрование с помощью таблицы Виженера",
                VigenereCoder.getInstance().createRusoAlphabet()));
        buttonPanel.add(createButton("Лабораторная работа №4",
                "Шифрование методом \"Решетка Кардано\"",
                CardanGrilleCoder.getInstance().createRusoAlphabet()));
        buttonPanel.add(createButton("Лабораторная работа №5",
                "Шифрование с использованием шифра Вернама",
                VerrnamCoder.getInstance().createRusoAlphabet()));
        buttonPanel.add(createButton("РГР",
                "Шифрование методом гаммирования",
                XorCoder.getInstance().createRusoAlphabet()));
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

    private void prepareTitleLAbel() {
        titleLabel = new JLabel();
        titleLabel.setText("<html>Проектування систем комплексного захисту інформації<br><br>" +
                "Програму розробили студенти групи СПС-1466<br>" +
                "Артеменко І.Ю.<br>" +
                "Кобрін В.О.<br>" +
                "Петров Д.В.<br><br>" +
                "Перевірив викладач:<br>" +
                "Куницька С.Ю.</html>");
        titleLabel.setBorder(new EmptyBorder(5, 8, 5, 8));
    }
}
