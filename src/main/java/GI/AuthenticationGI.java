package GI;

import support.Controller;
import support.Message;
import support.User;
import support.UsersRights;

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
    private JLabel messageLabel;

    private Dimension loginDimension = new Dimension(400, 170);
    private Dimension signUpDimension = new Dimension(400, 270);
    private Controller controller;

    public AuthenticationGI() throws Exception {
        super("Login");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        controller = new Controller();
        prepareCenterPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        messageLabel = Message.prepareMessageLabel(Message.LOGIN);
        getContentPane().add(messageLabel, BorderLayout.NORTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(loginDimension);
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prepareCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        prepareLoginPanel();
        centerPanel.add(loginPanel);
        prepareSighUpPanel();
        centerPanel.add(signUpPanel);
    }

    public void prepareLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));

        prepareUsernameBox();
        fieldsPanel.add(new LabelComponentPanel("Username: ", usernameBox), BorderLayout.EAST);

        passwordField = new JPasswordField(COLUMNS_COUNT);
        passwordField.getDocument().addDocumentListener(new LoginTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Password: ", passwordField));

        loginPanel.add(fieldsPanel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        prepareLoginButton();
        buttonsPanel.add(loginButton);
        prepareCreateNewButton();
        buttonsPanel.add(createNewButton);
        loginPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    public void prepareUsernameBox() {
        usernameBox = new JComboBox<>(controller.getUserNameMap().keySet().toArray());
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

    public void prepareLoginButton() {
        loginButton = new JButton("Login");
        loginButton.setEnabled(false);
        loginButton.addActionListener(e -> {
            User user = controller.authorizedUsers(
                    ((JTextField) usernameBox.getEditor().getEditorComponent()).getText(),
                    passwordField.getPassword());
            if (user == null) {
                messageLabel.setIcon(Message.WARNING_IMAGE);
                messageLabel.setText(Message.WRONG_USER);
            } else {
                setVisible(false);
                clearFields();
                WorkspaceGI workspaceGI = new WorkspaceGI(user, controller);
                workspaceGI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true);
                        usernameBox = new JComboBox<>(controller.getUserNameMap().keySet().toArray());
                    }
                });
            }
        });
    }

    public void prepareCreateNewButton() {
        createNewButton = new JButton("New Account");
        createNewButton.addActionListener(e -> {
            loginPanel.setVisible(false);
            signUpPanel.setVisible(true);
            clearFields();
            messageLabel.setIcon(null);
            messageLabel.setText(Message.CREATE);
            setSize(signUpDimension);
            setTitle("Sign up");
        });
    }

    public void prepareSighUpPanel() {
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

    public void prepareFieldsPanel() {
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

    public void prepareSignUpButton() {
        signUpButton = new JButton("Sign Up");
        signUpButton.setEnabled(false);
        signUpButton.addActionListener(e -> {
            try {
                if (Arrays.equals(firstPasswordField.getPassword(), secondPasswordField.getPassword())) {
                    controller.createUser(nameField.getText(),
                            surnameField.getText(), usernameField.getText(), firstPasswordField.getPassword(),
                            usernameField.isEnabled() ? UsersRights.SIMPLE_USER : UsersRights.LOCK_USERNAME);
                } else {
                    throw new IOException(Message.PASSWORDS_DOES_NOT_MATCH);
                }
                loginPanel.setVisible(true);
                signUpPanel.setVisible(false);
                messageLabel.setIcon(null);
                messageLabel.setText(Message.ADD_USER_SUCC);
                usernameBox.addItem(usernameField.getText());
                usernameBox.setSelectedItem(usernameField.getText());
                passwordField.setText(String.valueOf(firstPasswordField.getPassword()));
                loginButton.setEnabled(true);
                setSize(loginDimension);
            } catch (IOException exception) {
                messageLabel.setIcon(Message.WARNING_IMAGE);
                messageLabel.setText(exception.getMessage());
            }
        });
    }

    public void prepareCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            signUpPanel.setVisible(false);
            loginPanel.setVisible(true);
            clearFields();
            messageLabel.setIcon(null);
            messageLabel.setText(Message.LOGIN);
            setSize(loginDimension);
            setTitle("Log in");
        });
    }

    public void clearFields() {
        usernameBox.setSelectedIndex(-1);
        passwordField.setText("");
        passwordField.setEnabled(true);
        nameField.setText("");
        surnameField.setText("");
        usernameField.setText("");
        firstPasswordField.setText("");
        secondPasswordField.setText("");
    }

    public class SignUpTypeListener implements DocumentListener {

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

    public class LoginTypeListener implements DocumentListener {

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

    public void checkUsersRights() {
        if (usernameBox.getSelectedIndex() == -1) {
            return;
        }
        String username = (String) usernameBox.getSelectedItem();
        int rights = controller.getUserNameMap().get(username);
        switch (rights) {
            case UsersRights.LOCKED_USER: {
                passwordField.setEnabled(false);
                break;
            }
            case UsersRights.EMPTY_LOCK_USERNAME: {
                usernameField.setEnabled(false);
            }
            case UsersRights.EMPTY: {
                createNewButton.doClick();
                usernameField.setText(username);
                messageLabel.setText(Message.ADD_INFO);
                break;
            }
            default: {
                passwordField.setEnabled(true);
            }
        }
    }
}