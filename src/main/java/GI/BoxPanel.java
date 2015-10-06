package GI;

import javax.swing.*;
import java.io.IOException;

public class BoxPanel extends JPanel {

    public BoxPanel(int axis) {
        super();

        checkAxis(axis);
        setLayout(new BoxLayout(this, axis));
    }

    public BoxPanel(JComponent... components) {
        super();
        add(components);
    }

    public BoxPanel(int axis, JComponent... components) {
        super();

        checkAxis(axis);
        setLayout(new BoxLayout(this, axis));

        add(components);
    }

    public void add(JComponent... components) {
        for (JComponent component : components) {
            add(component);
        }
    }

    private void checkAxis(int axis) {
        try {
            if (axis != BoxLayout.Y_AXIS && axis != BoxLayout.X_AXIS &&
                    axis != BoxLayout.LINE_AXIS && axis != BoxLayout.PAGE_AXIS) {
                throw new IOException("Axis mast be equal 1, 2, 3 or 4");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
