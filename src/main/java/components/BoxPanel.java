package components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class BoxPanel extends JPanel {

    public BoxPanel(int axis) {
        checkAxis(axis);
        setLayout(new BoxLayout(this, axis));
        setOpaque(false);
    }

    public BoxPanel(BorderLayout borderLayout) {
        setLayout(borderLayout);
    }

    public BoxPanel(JComponent... components) {
        for (JComponent component : components) {
            add(component);
        }
        setOpaque(false);
    }

    public BoxPanel(JComponent component, Border border) {
        add(component);
        setBorder(border);
        setOpaque(false);
    }

    public BoxPanel(int axis, JComponent... components) {
        checkAxis(axis);
        setLayout(new BoxLayout(this, axis));
        for (JComponent component : components) {
            add(component);
        }
        setOpaque(false);
    }

    public BoxPanel(JComponent component, String constraint) {
        checkConstraint(constraint);
        setLayout(new BorderLayout());
        add(component, constraint);
        setOpaque(false);
    }

    public void add(JComponent... components) {
        if (!(getLayout() instanceof BorderLayout)) {
            for (JComponent component : components) {
                add(component);
            }
        }
    }

    public void add(JComponent component, String constrains) {
        checkConstraint(constrains);
        super.add(component, constrains);
    }

    private void checkAxis(int axis) {
        try {
            if (axis != BoxLayout.Y_AXIS
                    && axis != BoxLayout.X_AXIS
                    && axis != BoxLayout.LINE_AXIS
                    && axis != BoxLayout.PAGE_AXIS) {
                throw new IOException("Axis mast be equal 1, 2, 3 or 4");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkConstraint(String constraint) {
        try {
            if (!constraint.equals(BorderLayout.SOUTH)
                    && !constraint.equals(BorderLayout.NORTH)
                    && !constraint.equals(BorderLayout.WEST)
                    && !constraint.equals(BorderLayout.EAST)
                    && !constraint.equals(BorderLayout.CENTER)) {
                throw new IOException("Constraint mast be equal Center, North, East, South or West");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
