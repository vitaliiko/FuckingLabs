package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LabelComponentPanel extends JPanel {

    JLabel label;

    public LabelComponentPanel(String labelText, JComponent component) {
        setLayout(new GridLayout(1, 2));
        setBorder(new EmptyBorder(8, 1, 1, 8));
        setOpaque(false);
        label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.RIGHT);
        if (component instanceof JLabel) {
            label.setFont(component.getFont());
        }
        add(label);
        add(component);
    }

    public LabelComponentPanel(String labelText, String text) {
        setLayout(new GridLayout(1, 2));
        setBorder(new EmptyBorder(8, 1, 1, 8));
        setOpaque(false);
        label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.RIGHT);
        add(label);
        add(new JLabel(text));
    }

    public LabelComponentPanel(JLabel label, JComponent component) {
        setLayout(new GridLayout(1, 2));
        setBorder(new EmptyBorder(8, 1, 1, 8));
        setOpaque(false);
        label.setHorizontalAlignment(JLabel.RIGHT);
        add(label);
        add(component);
    }
}
