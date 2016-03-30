package user_gi;

import components.BoxPanel;
import components.LabelComponentPanel;
import model.SingleController;
import components.SingleMessage;
import model.User;
import model.UsersRights;
import frame_utils.FrameUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

public class AuthenticationGI extends JFrame {

    private static final int COLUMNS_COUNT = 35;

    private JPanel loginPanel;
    private JPanel signUpPanel;
    private JPanel fieldsPanel;
    private JPanel centerPanel;
    private JComboBox<Object> usernameBox;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField usernameField;
    private JPasswordField firstPasswordField;
    private JPasswordField secondPasswordField;
    private JButton loginButton;
    private JButton signUpButton;
    private JButton cancelButton;
    private JButton createNewButton;

    private Dimension loginDimension = new Dimension(400, 180);
    private Dimension signUpDimension = new Dimension(400, 275);
    private int rights;

    public AuthenticationGI() throws Exception {
        super("Login");

        FrameUtils.setLookAndFeel();

        prepareCenterPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(SingleMessage.getMessageInstance(SingleMessage.LOGIN), BorderLayout.NORTH);
        getContentPane().add(new JLabel("Trial version. Start up attempts left: " +
                SingleController.getInstance().getAttempts()),BorderLayout.SOUTH);

        frameSetup();
        checkAttempts();
    }

    private void frameSetup() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(loginDimension);
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void checkAttempts() {
        if (!SingleController.getInstance().checkAttempts()) {
            loginButton.setVisible(false);
            createNewButton.setEnabled(false);
        }
    }

    private void prepareCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        prepareLoginPanel();
        centerPanel.add(loginPanel);
        prepareSighUpPanel();
        centerPanel.add(signUpPanel);
    }

    private void prepareLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        JPanel fieldsPanel = new BoxPanel(BoxLayout.Y_AXIS);

        prepareUsernameBox();
        fieldsPanel.add(new LabelComponentPanel("Username: ", usernameBox), BorderLayout.EAST);

        passwordField = new JPasswordField(COLUMNS_COUNT);
        passwordField.getDocument().addDocumentListener(new LoginTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Password: ", passwordField));

        loginPanel.add(fieldsPanel, BorderLayout.EAST);

        prepareLoginButton();
        prepareCreateNewButton();
        loginPanel.add(new BoxPanel(loginButton, createNewButton), BorderLayout.SOUTH);
    }

    private void prepareUsernameBox() {
        usernameBox = new JComboBox<>(SingleController.getInstance().getUserNameMap().keySet().toArray());
        usernameBox.setSelectedIndex(-1);
        usernameBox.setEditable(true);
        usernameBox.addItemListener(e -> checkUsersRights());
        usernameBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                checkUsersRights();
            }
        });
        ((JTextField) usernameBox.getEditor().getEditorComponent()).getDocument().
                addDocumentListener(new LoginTypeListener());
    }

    private void prepareLoginButton() {
        loginButton = new JButton("Login");
        loginButton.setEnabled(false);
        loginButton.addActionListener(e -> {
            User user = SingleController.getInstance().authorizedUsers(
                    ((JTextField) usernameBox.getEditor().getEditorComponent()).getText(),
                    passwordField.getPassword());
            if (user == null) {
                SingleMessage.setWarningMessage(SingleMessage.WRONG_USER);
            } else {
                setVisible(false);
                clearFields();
                rights = -1;
                WorkspaceGI workspaceGI = new WorkspaceGI(user);
                workspaceGI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true);
                    }
                });
            }
        });
    }

    private void prepareCreateNewButton() {
        createNewButton = new JButton("New Account");
        createNewButton.addActionListener(e -> {
            loginPanel.setVisible(false);
            signUpPanel.setVisible(true);
            clearFields();
            SingleMessage.setDefaultMessage(SingleMessage.CREATE);
            setSize(signUpDimension);
            setTitle("Sign up");
        });
    }

    private void prepareSighUpPanel() {
        signUpPanel = new JPanel();
        signUpPanel.setLayout(new BorderLayout());
        signUpPanel.setVisible(false);

        prepareFieldsPanel();
        signUpPanel.add(fieldsPanel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        prepareSignUpButton();
        buttonsPanel.add(signUpButton);
        prepareCancelButton();
        buttonsPanel.add(cancelButton);
        signUpPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void prepareFieldsPanel() {
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        SignUpTypeListener signUpTypeListener = new SignUpTypeListener();

        nameField = new JTextField(COLUMNS_COUNT);
        nameField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Your name: ", nameField));

        surnameField = new JTextField(COLUMNS_COUNT);
        surnameField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Your surname: ", surnameField));

        usernameField = new JTextField(COLUMNS_COUNT);
        usernameField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Username: ", usernameField));

        firstPasswordField = new JPasswordField(COLUMNS_COUNT);
        firstPasswordField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Password: ", firstPasswordField));

        secondPasswordField = new JPasswordField(COLUMNS_COUNT);
        secondPasswordField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Repeat password: ", secondPasswordField));
    }

    private void prepareSignUpButton() {
        signUpButton = new JButton("Sign Up");
        signUpButton.setEnabled(false);
        signUpButton.addActionListener(e -> {
            try {
                if (rights == UsersRights.EMPTY || rights == UsersRights.LOCK_USERNAME) {
                    rights = UsersRights.LOCK_USERNAME;
                } else {
                    if (rights == UsersRights.EMPTY_SIMPLE_PASSWORD ||
                            rights == UsersRights.LOCK_USERNAME_WITH_SIMPLE_PASSWORD) {
                        rights = UsersRights.LOCK_USERNAME_WITH_SIMPLE_PASSWORD;
                    } else {
                        rights = UsersRights.SIMPLE_USER;
                    }
                }
                if (Arrays.equals(firstPasswordField.getPassword(), secondPasswordField.getPassword())) {
                    SingleController.getInstance().createUser(nameField.getText(), surnameField.getText(),
                            usernameField.getText(), firstPasswordField.getPassword(), rights);
                } else {
                    throw new IOException(SingleMessage.PASSWORDS_DOES_NOT_MATCH);
                }
                loginPanel.setVisible(true);
                signUpPanel.setVisible(false);
                SingleMessage.setDefaultMessage(SingleMessage.ADD_USER_SUC);
                usernameBox.addItem(usernameField.getText());
                usernameBox.setSelectedItem(usernameField.getText());
                passwordField.setText(String.valueOf(firstPasswordField.getPassword()));
                loginButton.setEnabled(true);
                setSize(loginDimension);
            } catch (IOException exception) {
                SingleMessage.setWarningMessage(exception.getMessage());
            }
        });
    }

    private void prepareCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            signUpPanel.setVisible(false);
            loginPanel.setVisible(true);
            clearFields();
            SingleMessage.setDefaultMessage(SingleMessage.LOGIN);
            setSize(loginDimension);
            setTitle("Log in");
        });
    }

    private void clearFields() {
        nameField.setText("");
        surnameField.setText("");
        if (SingleMessage.getMessageText().equals(SingleMessage.USER_IS_EMPTY)) {
            usernameField.setText((String) usernameBox.getSelectedItem());
            usernameField.setEnabled(false);
        } else {
            usernameField.setText("");
            usernameField.setEnabled(true);
        }
        firstPasswordField.setText("");
        secondPasswordField.setText("");
        usernameBox.setSelectedIndex(-1);
        passwordField.setText("");
        passwordField.setEnabled(true);
    }

    private class SignUpTypeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            signUpButton.setEnabled(!nameField.getText().isEmpty() &&
                    !surnameField.getText().isEmpty() &&
                    !usernameField.getText().isEmpty() &&
                    firstPasswordField.getPassword().length != 0 &&
                    secondPasswordField.getPassword().length != 0);
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

    private class LoginTypeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            loginButton.setEnabled(passwordField.getPassword().length != 0 &&
                    usernameBox.getSelectedItem() != null);
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

    private void checkUsersRights() {
        if (usernameBox.getSelectedIndex() == -1) {
            return;
        }
        String username = (String) usernameBox.getSelectedItem();
        if (SingleController.getInstance().getUserNameMap().get(username) != null) {
            rights = SingleController.getInstance().getUserNameMap().get(username);
        } else {
            usernameBox.removeItem(username);
        }
        switch (rights) {
            case UsersRights.EMPTY_SIMPLE_PASSWORD:
            case UsersRights.EMPTY: {
                passwordField.setEnabled(false);
                SingleMessage.setWarningMessage(SingleMessage.USER_IS_EMPTY);
                break;
            }
            case UsersRights.BLOCKED_USER: {
                passwordField.setEnabled(false);
                SingleMessage.setWarningMessage(SingleMessage.USER_IS_LOCKED);
                break;
            }
            default: {
                passwordField.setEnabled(true);
                SingleMessage.setWarningMessage(SingleMessage.LOGIN);
            }
        }
    }
}