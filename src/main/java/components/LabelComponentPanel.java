package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LabelComponentPanel extends JPanel {

    JLabel label;

    public LabelComponentPanel(String labelText, JComponent component) {
        this.setLayout(new GridLayout(1, 2));
        this.setBorder(new EmptyBorder(8, 1, 1, 8));
        label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.RIGHT);
        add(label);
        add(component);
    }
}
