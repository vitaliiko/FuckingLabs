package user_gi;

import frame_utils.FrameUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BrowseDbContentGI extends JFrame {

    private JTabbedPane tabbedPane;

    public BrowseDbContentGI() throws HeadlessException {
        super("Лабораторна робота №2");
        FrameUtils.setLookAndFeel();

        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(924, 620));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepateTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(5, 5, 0, 5));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFocusable(false);


    }
}
