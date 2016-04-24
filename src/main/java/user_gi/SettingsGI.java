package user_gi;

import input_output.IOFileHandling;
import components.SingleMessage;
import components.BoxPanel;
import components.LabelComponentPanel;
import model.*;
import frame_utils.FrameUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class SettingsGI extends JDialog {

    private static final int COLUMNS_COUNT = 24;

    private User user;
    private SingleController controller;
    private JFrame frame;

    private JPanel fieldsPanel;
    private JPanel passwordPanel;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField usernameField;
    private JTextField telephoneField;
    private JTextField mailField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField repeatPasswordField;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton removeButton;
    private TypeListener typeListener;

    public SettingsGI(Frame frame, User user) {
        super(frame);
        this.frame = (JFrame) frame;
        this.user = user;
        this.controller = SingleController.getInstance();
        typeListener = new TypeListener();

        FrameUtil.setLookAndFeel();
        prepareComponents();
        setup();
    }

    private void prepareComponents() {
        getContentPane().add(SingleMessage.getMessageInstance(SingleMessage.SETTINGS), BorderLayout.NORTH);
        prepareFieldsPanel();
        getContentPane().add(fieldsPanel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        prepareSaveButton();
        buttonsPanel.add(saveButton);
        prepareCancelButton();
        buttonsPanel.add(cancelButton);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void setup() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(335, 430));
        setIconImage(new ImageIcon("resources/settings.png").getImage());
        setTitle("Settings");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareFieldsPanel() {
        fieldsPanel = new BoxPanel(BoxLayout.Y_AXIS);

        fieldsPanel.add(
                new LabelComponentPanel("Account type: ", new JLabel(UsersRights.accountType(user.getRights())))
        );

        nameField = new JTextField(user.getFirstName(), COLUMNS_COUNT);
        nameField.getDocument().addDocumentListener(typeListener);
        fieldsPanel.add(new LabelComponentPanel("Your name: ", nameField));

        surnameField = new JTextField(user.getLastName(), COLUMNS_COUNT);
        surnameField.getDocument().addDocumentListener(typeListener);
        fieldsPanel.add(new LabelComponentPanel("Your surname: ", surnameField));

        usernameField = new JTextField(user.getLogin(), COLUMNS_COUNT);
        usernameField.getDocument().addDocumentListener(typeListener);
        usernameField.setEnabled(user.getRights() != UsersRights.LOCK_USERNAME &&
                user.getRights() != UsersRights.ADMIN &&
                user.getRights() != UsersRights.LOCK_USERNAME_WITH_SIMPLE_PASSWORD);
        fieldsPanel.add(new LabelComponentPanel("Username: ", usernameField));

        telephoneField = new JTextField(user.getTelephoneNum(), COLUMNS_COUNT);
        telephoneField.getDocument().addDocumentListener(typeListener);
        fieldsPanel.add(new LabelComponentPanel("Your telephone: ", telephoneField));

        mailField = new JTextField(user.getMailAddress(), COLUMNS_COUNT);
        mailField.getDocument().addDocumentListener(typeListener);
        fieldsPanel.add(new LabelComponentPanel("Your e-mail: ", mailField));

        prepareRemoveButton();
        fieldsPanel.add(removeButton);

        preparePasswordPanel();
        fieldsPanel.add(passwordPanel);
    }

    private void prepareRemoveButton(){
        removeButton = new JButton("Remove account");
        removeButton.setEnabled(user.getRights() != UsersRights.ADMIN);
        removeButton.addActionListener(e -> {
            controller.removeUser(user);
            IOFileHandling.saveUsers();
            dispose();
            frame.dispose();
        });
    }

    private void preparePasswordPanel() {
        passwordPanel = new BoxPanel(BoxLayout.Y_AXIS);
        passwordPanel.setBorder(new EmptyBorder(8, 0, 0, 0));
        passwordPanel.add(new JSeparator());

        JLabel label = new JLabel("Change your password");
        label.setHorizontalAlignment(JLabel.CENTER);
        passwordPanel.add(label);

        currentPasswordField = new JPasswordField(COLUMNS_COUNT);
        currentPasswordField.getDocument().addDocumentListener(typeListener);
        passwordPanel.add(new LabelComponentPanel("Current password: ", currentPasswordField));

        newPasswordField = new JPasswordField(COLUMNS_COUNT);
        newPasswordField.getDocument().addDocumentListener(typeListener);
        passwordPanel.add(new LabelComponentPanel("New password: ", newPasswordField));

        repeatPasswordField = new JPasswordField(COLUMNS_COUNT);
        repeatPasswordField.getDocument().addDocumentListener(typeListener);
        passwordPanel.add(new LabelComponentPanel("Repeat new password: ", repeatPasswordField));
    }

    private void prepareSaveButton() {
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e -> {
            try {
                controller.updateUsersInfo(user, nameField.getText(), surnameField.getText(),
                        telephoneField.getText(), mailField.getText());

                usernameChecker();
                if (isNotPasswordsFieldsEmpty()) {
                    passwordsChecker();
                }
                SingleMessage.setDefaultMessage(SingleMessage.SAVED);
                IOFileHandling.saveUsers();
                currentPasswordField.setText("");
                newPasswordField.setText("");
                repeatPasswordField.setText("");
                saveButton.setEnabled(false);
            } catch (IOException e1) {
                SingleMessage.setWarningMessage(e1.getMessage());
            }
        });
    }

    private void usernameChecker() throws IOException {
        String username = usernameField.getText();
        if (!user.getLogin().equals(username)) {
            if (controller.validateUsername(username)) {
                for (String s : controller.getUserNameMap().keySet()) {
                    if (s.equals(username)) {
                        throw new IOException(SingleMessage.EXIST_USER);
                    }
                }
                user.setLogin(username);
            }
        }
    }

    private void passwordsChecker() throws IOException {
        if (!user.isPasswordsMatches(currentPasswordField.getPassword())) {
            throw new IOException(SingleMessage.INCORRECT_PASSWORD);
        }
        if (!Arrays.equals(newPasswordField.getPassword(), repeatPasswordField.getPassword())) {
            throw new IOException(SingleMessage.PASSWORDS_DOES_NOT_MATCH);
        }
        if (user.getRights() == UsersRights.USER_WITH_SIMPLE_PASSWORD ||
                user.getRights() == UsersRights.LOCK_USERNAME_WITH_SIMPLE_PASSWORD) {
            user.setPassword(newPasswordField.getPassword());
        } else {
            if (controller.validatePassword(newPasswordField.getPassword())) {
                user.setPassword(newPasswordField.getPassword());
            }
        }
    }

    private void prepareCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
    }

    private boolean isPasswordsFieldsEmpty() {
        return currentPasswordField.getPassword().length == 0 &&
                    newPasswordField.getPassword().length == 0 &&
                    repeatPasswordField.getPassword().length == 0;
    }

    private boolean isNotPasswordsFieldsEmpty() {
        return currentPasswordField.getPassword().length != 0 &&
                    newPasswordField.getPassword().length != 0 &&
                    repeatPasswordField.getPassword().length != 0;
    }

    private boolean isNotFieldsEmpty() {
        return !nameField.getText().isEmpty() &&
                !surnameField.getText().isEmpty() &&
                !usernameField.getText().isEmpty();
    }

    private class TypeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            saveButton.setEnabled(isNotFieldsEmpty() && (isNotPasswordsFieldsEmpty() || isPasswordsFieldsEmpty()));
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            insertUpdate(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            insertUpdate(e);
        }
    }
}
