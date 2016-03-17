package utils;

import input_output.TextFileFilter;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;

public final class FrameUtils {

    public static final Font FONT = new Font("Arial", Font.PLAIN, 12);

    private FrameUtils() {}

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String callFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new TextFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public static void textAreaFactory(JTextArea textArea, String name, DocumentListener listener) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(FONT);
        textArea.setName(name);
        textArea.getDocument().addDocumentListener(listener);
    }

    public static JMenu prepareHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About           ");
        aboutItem.addActionListener(e -> JOptionPane.showConfirmDialog(null,
                "<html>" +
                        "Програму розробли студенти групи СПС-1466 <br>" +
                        "Артеменко І.Ю.<br>" +
                        "Кобрін В.О.<br>" +
                        "Петров Д.В.<br>" +
                        "</html>",
                "About",
                JOptionPane.DEFAULT_OPTION));
        helpMenu.add(aboutItem);
        return helpMenu;
    }

    public static void showErrorDialog(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
