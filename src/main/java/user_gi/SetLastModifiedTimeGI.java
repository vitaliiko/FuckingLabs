package user_gi;

import components.BoxPanel;
import components.SingleMessage;
import utils.TimeUtil;
import utils.FrameUtils;

import javax.swing.*;
import java.awt.*;

public class SetLastModifiedTimeGI extends JDialog {

    private BoxPanel mainPanel;
    private JPanel spinnersPanel;
    private JSpinner daySpinner;
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;
    private JButton submitButton;
    private JButton cancelButton;

    public SetLastModifiedTimeGI(Frame frame) {
        super(frame);
        FrameUtils.setLookAndFeel();

        getContentPane().add(SingleMessage.getMessageInstance(SingleMessage.SET_LAST_MODIFIED_TIME), BorderLayout.NORTH);
        prepareMainPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        prepareButtons();
        getContentPane().add(new BoxPanel(submitButton, cancelButton), BorderLayout.SOUTH);

        getContentPane().add(FrameUtils.getFreeSpace(), BorderLayout.WEST);
        getContentPane().add(FrameUtils.getFreeSpace(), BorderLayout.EAST);
        setup();
    }

    private void setup() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(350, 180));
        setIconImage(new ImageIcon("resources/clock.png").getImage());
        setTitle("Set last modified time");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareSpinnersPanel() {
        spinnersPanel = new JPanel(new BorderLayout());
        spinnersPanel.add(FrameUtils.createLabelGridPanel(JLabel.RIGHT, "Day:", "Month:", "Year:"), BorderLayout.WEST);
        spinnersPanel.add(createSpinners(), BorderLayout.CENTER);
    }

    private JPanel createSpinners() {
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        setSpinnerSize(daySpinner);
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        setSpinnerSize(monthSpinner);
        yearSpinner = new JSpinner(new SpinnerNumberModel(1995, 1990, TimeUtil.MAX_YEAR, 5));
        setSpinnerSize(yearSpinner);
        return FrameUtils.createComponentsGridPanel(daySpinner, monthSpinner, yearSpinner);
    }

    private void setSpinnerSize(JSpinner spinner) {
        JComponent field = spinner.getEditor();
        Dimension prefSize = field.getPreferredSize();
        prefSize = new Dimension(100, prefSize.height);
        field.setPreferredSize(prefSize);
    }

    private void prepareMainPanel() {
        prepareSpinnersPanel();
        mainPanel = new BoxPanel(BoxLayout.Y_AXIS, spinnersPanel);
        mainPanel.setMaximumSize(new Dimension(150, 100));
        mainPanel.setPreferredSize(new Dimension(150, 100));
    }

    private void prepareButtons() {
        submitButton = new JButton("Save changes");
        submitButton.addActionListener(e -> {
            TimeUtil.setLastModifiedTime(
                    (Integer) daySpinner.getValue(),
                    (Integer) monthSpinner.getValue(),
                    (Integer) yearSpinner.getValue());
            SingleMessage.setDefaultMessage(SingleMessage.TIME_CHANGED);
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
    }
}
