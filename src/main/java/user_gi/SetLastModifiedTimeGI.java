package user_gi;

import components.BoxPanel;
import input_output.SingleMessage;
import input_output.TimeUtil;
import utils.FrameUtils;

import javax.swing.*;
import java.awt.*;

public class SetLastModifiedTimeGI extends JDialog {

    private BoxPanel mainPanel;
    private BoxPanel spinnerPanel;
    private JSpinner daySpinner;
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;
    private JButton submitButton;
    private JButton cancelButton;

    public SetLastModifiedTimeGI(Frame frame) {
        super(frame);
        FrameUtils.setLookAndFeel();

        getContentPane().add(SingleMessage.getMessageInstance(SingleMessage.SET_LAST_MODIFIED_TIME));
        prepareMainPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        prepareButtons();
        getContentPane().add(new BoxPanel(submitButton, cancelButton), BorderLayout.SOUTH);
        setup();
    }

    private void setup() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 180));
        setIconImage(new ImageIcon("resources/clock.png").getImage());
        setTitle("Set last modified time");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareSpinners() {
        spinnerPanel = new BoxPanel(BoxLayout.X_AXIS);
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        yearSpinner = new JSpinner(new SpinnerNumberModel(1995, 1990, 2010, 5));
        spinnerPanel.add(daySpinner, monthSpinner, yearSpinner);
    }

    private void prepareMainPanel() {
        prepareSpinners();
        mainPanel = new BoxPanel(BoxLayout.Y_AXIS, spinnerPanel);
    }

    private void prepareButtons() {
        submitButton = new JButton("Save changes");
        submitButton.addActionListener(e -> {
            TimeUtil.setLastModified(
                    (Integer) daySpinner.getValue(),
                    (Integer) monthSpinner.getValue(),
                    (Integer) yearSpinner.getValue());
            SingleMessage.setDefaultMessage(SingleMessage.TIME_CHANGED);
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
    }
}
