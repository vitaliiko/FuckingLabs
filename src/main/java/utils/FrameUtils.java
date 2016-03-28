package utils;

import input_output.TextFileFilter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;

public final class FrameUtils {

    public static final Font ARIAL_12 = new Font("Arial", Font.PLAIN, 12);
    public static final String RESOURCES_PATH = "resources/";

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
        textArea.setFont(ARIAL_12);
        textArea.setName(name);
        textArea.getDocument().addDocumentListener(listener);
    }

    public static void showErrorDialog(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static JScrollPane createScroll(JComponent component) {
        return new JScrollPane(component,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public static JPanel createLabelGridPanel(int alignment, String... strings) {
        JPanel panel = createPanel(strings.length, 6);
        for (String s : strings) {
            JLabel label = new JLabel(s, alignment);
            panel.add(label);
        }
        return panel;
    }

    public static JPanel createComponentsGridPanel(Component... components) {
        JPanel panel = createPanel(components.length, 6);
        for (Component component : components) {
            panel.add(component);
        }
        return panel;
    }

    private static JPanel createPanel(int rowCount, int distance) {
        JPanel panel = new JPanel(new GridLayout(rowCount, 1, 0, distance));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 5, 8, 0));
        return panel;
    }

}
