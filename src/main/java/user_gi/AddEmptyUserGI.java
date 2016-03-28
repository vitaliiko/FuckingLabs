package user_gi;

import components.BoxPanel;
import components.LabelComponentPanel;
import model.Controller;
import input_output.IOFileHandling;
import input_output.Message;
import model.UsersRights;
import utils.FrameUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import java.io.IOException;

public class AddEmptyUserGI extends JDialog {

    private Controller controller;

    private JPanel fieldPanel;
    private JPanel buttonPanel;
    private JTextField usernameField;
    private JButton addButton;
    private JButton cancelButton;
    private JCheckBox checkBox;
    private JLabel messageLabel;

    public AddEmptyUserGI(Frame owner) {
        super(owner);
        this.controller = Controller.getInstance();

        FrameUtils.setLookAndFeel();

        messageLabel = Message.prepareMessageLabel(Message.ADD_NEW_USER);
        getContentPane().add(messageLabel, BorderLayout.NORTH);
        preparePanels();
        getContentPane().add(fieldPanel, BorderLayout.EAST);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(320, 150));
        setIconImage(new ImageIcon("resources/addUsers.png").getImage());
        setTitle("Add user");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void preparePanels() {
        usernameField = new JTextField(30);
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                addButton.setEnabled(!usernameField.getText().isEmpty());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });

        checkBox = new JCheckBox("Authorize to use a simple password");
        checkBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
        fieldPanel = new BoxPanel(BoxLayout.Y_AXIS, (new LabelComponentPanel("Username: ", usernameField)), checkBox);

        prepareAddButton();
        prepareCancelButton();
        buttonPanel = new BoxPanel(addButton, cancelButton);
    }

    public void prepareAddButton() {
        addButton = new JButton("Add");
        addButton.setEnabled(false);
        addButton.addActionListener(e -> {
            try {
                if (checkBox.isSelected()) {
                    controller.addEmptyUser(usernameField.getText(), UsersRights.EMPTY_SIMPLE_PASSWORD);
                } else {
                    controller.addEmptyUser(usernameField.getText(), UsersRights.EMPTY);
                }
                messageLabel.setIcon(null);
                messageLabel.setText(Message.ADD_USER_SUC);
                addButton.setEnabled(false);
                IOFileHandling.saveUsers();
            } catch (IOException e1) {
                messageLabel.setIcon(Message.WARNING_IMAGE);
                messageLabel.setText(e1.getMessage());
            }
        });
    }

    public void prepareCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
    }
}
