package user_gi;

import components.BoxPanel;
import frame_utils.FrameUtils;
import model.SingleController;
import model.UsbDeviceManager;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SetTokenGI extends JFrame {

    private User user;
    private JList<String> devicesList;
    private JButton confirmButton;
    private JButton cancelButton;
    private JLabel infoLabel;

    public SetTokenGI(User user) throws HeadlessException {
        super("Set token");
        this.user = user;

        FrameUtils.setLookAndFeel();

        prepareComponents();
        getContentPane().add(infoLabel, BorderLayout.NORTH);
        getContentPane().add(FrameUtils.createScroll(devicesList), BorderLayout.CENTER);
        getContentPane().add(new BoxPanel(confirmButton, cancelButton), BorderLayout.SOUTH);

        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 400));
        setIconImage(new ImageIcon("resources/usb.png").getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void prepareComponents() {
        devicesList = new JList<>(UsbDeviceManager.getDevices());
        devicesList.setFont(FrameUtils.ARIAL_12);
        devicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        devicesList.addListSelectionListener(e -> confirmButton.setEnabled(true));
        infoLabel = new JLabel("Select your USB device: ");
        infoLabel.setBorder(new EmptyBorder(8, 8, 8, 8));
        prepareConfirmButton();
        prepareCancelButton();
    }

    private void prepareConfirmButton() {
        confirmButton = new JButton("OK");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> {
            SingleController.getInstance()
                    .setUsbSerial(user, UsbDeviceManager.getDeviceSerial(devicesList.getSelectedValue()));
            this.dispose();
        });
    }

    private void prepareCancelButton() {
        cancelButton= new JButton("Cancel");
        cancelButton.addActionListener(e -> this.dispose());
    }
}
