package user_gi;

import components.BoxPanel;
import components.LabelComponentPanel;
import components.SingleMessage;
import model.SingleController;
import input_output.IOFileHandling;
import model.UsersRights;
import utils.FrameUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import java.io.IOException;

public class AddEmptyUserGI extends JDialog {

    private SingleController controller;

    private JPanel fieldPanel;
    private JPanel buttonPanel;
    private JTextField usernameField;
    private JButton addButton;
    private JButton cancelButton;
    private JCheckBox checkBox;

    public AddEmptyUserGI(Frame owner) {
        super(owner);
        this.controller = SingleController.getInstance();

        FrameUtils.setLookAndFeel();

        getContentPane().add(SingleMessage.getMessageInstance(SingleMessage.ADD_NEW_USER), BorderLayout.NORTH);
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

    private void preparePanels() {
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

    private void prepareAddButton() {
        addButton = new JButton("Add");
        addButton.setEnabled(false);
        addButton.addActionListener(e -> {
            try {
                if (checkBox.isSelected()) {
                    controller.addEmptyUser(usernameField.getText(), UsersRights.EMPTY_SIMPLE_PASSWORD);
                } else {
                    controller.addEmptyUser(usernameField.getText(), UsersRights.EMPTY);
                }
                SingleMessage.setDefaultMessage(SingleMessage.ADD_USER_SUC);
                addButton.setEnabled(false);
                IOFileHandling.saveUsers();
            } catch (IOException e1) {
                SingleMessage.setWarningMessage(e1.getMessage());
            }
        });
    }

    private void prepareCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
    }
}
