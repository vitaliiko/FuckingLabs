package GI;

import InputOutput.IOFileHandling;
import InputOutput.Message;
import panels.BoxPanel;
import panels.LabelComponentPanel;
import support.*;

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
    private Controller controller;
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
    private JLabel messageLabel;
    private TypeListener typeListener;

    public SettingsGI(Frame frame, User user, Controller controller) {
        super(frame);
        this.frame = (JFrame) frame;
        this.user = user;
        this.controller = controller;
        typeListener = new TypeListener();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        messageLabel = Message.prepareMessageLabel(Message.SETTINGS);
        getContentPane().add(messageLabel, BorderLayout.NORTH);
        prepareFieldsPanel();
        getContentPane().add(fieldsPanel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        prepareSaveButton();
        buttonsPanel.add(saveButton);
        prepareCancelButton();
        buttonsPanel.add(cancelButton);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(335, 430));
        setIconImage(new ImageIcon("resources/settings.png").getImage());
        setTitle("Settings");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prepareFieldsPanel() {
        fieldsPanel = new BoxPanel(BoxLayout.Y_AXIS);

        fieldsPanel.add(
                new LabelComponentPanel("Account type: ", new JLabel(UsersRights.accountType(user.getRights())))
        );

        nameField = new JTextField(user.getName(), COLUMNS_COUNT);
        nameField.getDocument().addDocumentListener(typeListener);
        fieldsPanel.add(new LabelComponentPanel("Your name: ", nameField));

        surnameField = new JTextField(user.getSurname(), COLUMNS_COUNT);
        surnameField.getDocument().addDocumentListener(typeListener);
        fieldsPanel.add(new LabelComponentPanel("Your surname: ", surnameField));

        usernameField = new JTextField(user.getUserName(), COLUMNS_COUNT);
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

    public void prepareRemoveButton(){
        removeButton = new JButton("Remove account");
        removeButton.setEnabled(user.getRights() != UsersRights.ADMIN);
        removeButton.addActionListener(e -> {
            controller.removeUser(user);
            IOFileHandling.saveUsersSet(controller.getUserSet());
            dispose();
            frame.dispose();
        });
    }

    public void preparePasswordPanel() {
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

    public void prepareSaveButton() {
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
                messageLabel.setIcon(null);
                messageLabel.setText(Message.SAVED);
                IOFileHandling.saveUsersSet(controller.getUserSet());
                currentPasswordField.setText("");
                newPasswordField.setText("");
                repeatPasswordField.setText("");
                saveButton.setEnabled(false);
            } catch (IOException e1) {
                messageLabel.setIcon(Message.WARNING_IMAGE);
                messageLabel.setText(e1.getMessage());
            }
        });
    }

    public void usernameChecker() throws IOException {
        String username = usernameField.getText();
        if (!user.getUserName().equals(username)) {
            if (controller.validateUsername(username)) {
                for (String s : controller.getUserNameMap().keySet()) {
                    if (s.equals(username)) {
                        throw new IOException(Message.EXIST_USER);
                    }
                }
                user.setUserName(username);
            }
        }
    }

    public void passwordsChecker() throws IOException {
        if (!user.isPasswordsMatches(currentPasswordField.getPassword())) {
            throw new IOException(Message.INCORRECT_PASSWORD);
        }
        if (!Arrays.equals(newPasswordField.getPassword(), repeatPasswordField.getPassword())) {
            throw new IOException(Message.PASSWORDS_DOES_NOT_MATCH);
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

    public void prepareCancelButton() {
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

    public class TypeListener implements DocumentListener {

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
