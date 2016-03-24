package user_gi;

import components.BoxPanel;
import input_output.Message;
import input_output.TimeUtil;
import utils.FrameUtils;

import javax.swing.*;
import java.awt.*;

public class CreationTimeGI extends JDialog {

    private BoxPanel mainPanel;
    private BoxPanel spinnerPanel;
    private JSpinner daySpinner;
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;
    private JButton submitButton;
    private JButton cancelButton;
    private JLabel messageLabel;

    public CreationTimeGI(Frame frame) {
        super(frame);
        FrameUtils.setLookAndFeel();
        prepareMainPanel();
        getContentPane().add(mainPanel, BorderLayout.NORTH);
        prepareButtons();
        getContentPane().add(new BoxPanel(submitButton, cancelButton), BorderLayout.SOUTH);
        setup();
    }

    private void setup() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 180));
        setIconImage(new ImageIcon("resources/clock.png").getImage());
        setTitle("Set creation time");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareSpinners() {
        spinnerPanel = new BoxPanel(BoxLayout.X_AXIS);
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        yearSpinner = new JSpinner(new SpinnerNumberModel(1980, 1940, 2000, 10));
        spinnerPanel.add(daySpinner, monthSpinner, yearSpinner);
    }

    private void prepareMainPanel() {
        messageLabel = Message.prepareMessageLabel("Set creation time");
        prepareSpinners();
        mainPanel = new BoxPanel(BoxLayout.Y_AXIS, messageLabel, spinnerPanel);
    }

    private void prepareButtons() {
        submitButton = new JButton("Save changes");
        submitButton.addActionListener(e -> {
            TimeUtil.changeFileCreationTime(
                    (Integer) daySpinner.getValue(),
                    (Integer) monthSpinner.getValue(),
                    (Integer) yearSpinner.getValue());
            messageLabel.setText("Creation time changed successfully)");
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
    }
}
