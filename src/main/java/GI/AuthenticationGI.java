package GI;

import support.Controller;
import support.Message;
import support.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class AuthenticationGI extends JFrame {

    private static final int COLUMNS_COUNT = 25;

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
    private ImageIcon warningImage = new ImageIcon("resources/warning.png");
    private Controller controller;

    public AuthenticationGI() throws HeadlessException {
        super("Log in");

        controller = new Controller();
        prepareCenterPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        prepareMessageLabel();
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

        usernameBox = new JComboBox<>(controller.getUserNameList().toArray());
        usernameBox.setSelectedIndex(-1);
        usernameBox.setEditable(true);
        ((JTextField) usernameBox.getEditor().getEditorComponent()).getDocument().
                addDocumentListener(new LoginTypeListener());
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

    public void prepareMessageLabel() {
        messageLabel = new JLabel(Message.LOGIN);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(new EmptyBorder(8, 0, 8, 0));
        messageLabel.setFont(new Font("times new roman", Font.PLAIN, 16));
    }

    public void prepareLoginButton() {
        loginButton = new JButton("Login");
        loginButton.setEnabled(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = controller.authorizedUsers(
                        ((JTextField) usernameBox.getEditor().getEditorComponent()).getText(),
                        passwordField.getPassword());
                if (user == null) {
                    messageLabel.setIcon(warningImage);
                    messageLabel.setText(Message.WRONG_USER);
                } else {
                    setVisible(false);
                    new WorkspaceGI(user);
                }
            }
        });
    }

    public void prepareCreateNewButton() {
        createNewButton = new JButton("New Account");
        createNewButton.addActionListener(new CreateNewListener(this));
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

        nameField = new JTextField(COLUMNS_COUNT);
        nameField.getDocument().addDocumentListener(new SignUpTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Your name: ", nameField));

        surnameField = new JTextField(COLUMNS_COUNT);
        surnameField.getDocument().addDocumentListener(new SignUpTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Your surname: ", surnameField));

        usernameField = new JTextField(COLUMNS_COUNT);
        usernameField.getDocument().addDocumentListener(new SignUpTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Username: ", usernameField));

        firstPasswordField = new JPasswordField(COLUMNS_COUNT);
        firstPasswordField.getDocument().addDocumentListener(new SignUpTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Password: ", firstPasswordField));

        secondPasswordField = new JPasswordField(COLUMNS_COUNT);
        secondPasswordField.getDocument().addDocumentListener(new SignUpTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Repeat password: ", secondPasswordField));
    }

    public void prepareSignUpButton() {
        signUpButton = new JButton("Sign Up");
        signUpButton.setEnabled(false);
        signUpButton.addActionListener(new SignUPListener(this));
    }

    public void prepareCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelListener(this));
    }

    public class LabelComponentPanel extends JPanel {

        JLabel label;

        public LabelComponentPanel(String labelText, JComponent component) {
            this.setLayout(new GridLayout(1, 2));
            this.setBorder(new EmptyBorder(8, 1, 1, 8));
            label = new JLabel(labelText);
            label.setHorizontalAlignment(JLabel.RIGHT);
            add(label);
            add(component);
        }
    }

    public void clearFields() {
        usernameBox.setSelectedIndex(-1);
        passwordField.setText("");
        nameField.setText("");
        surnameField.setText("");
        usernameField.setText("");
        firstPasswordField.setText("");
        secondPasswordField.setText("");
    }

    public class CreateNewListener implements ActionListener {

        JFrame frame;

        public CreateNewListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loginPanel.setVisible(false);
            signUpPanel.setVisible(true);
            clearFields();
            messageLabel.setIcon(null);
            messageLabel.setText(Message.CREATE);
            frame.setSize(signUpDimension);
            frame.setTitle("Sign up");
        }
    }

    public class CancelListener implements ActionListener {

        JFrame frame;

        public CancelListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            signUpPanel.setVisible(false);
            loginPanel.setVisible(true);
            clearFields();
            messageLabel.setIcon(null);
            messageLabel.setText(Message.LOGIN);
            frame.setSize(loginDimension);
            frame.setTitle("Log in");
        }
    }

    public class SignUPListener implements ActionListener {

        JFrame frame;

        public SignUPListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (Arrays.equals(firstPasswordField.getPassword(), secondPasswordField.getPassword())) {
                    controller.createUser(nameField.getText(),
                            surnameField.getText(), usernameField.getText(), firstPasswordField.getPassword());
                } else {
                    throw new IOException(Message.DOES_NOT_MATCH);
                }
                loginPanel.setVisible(true);
                signUpPanel.setVisible(false);
                frame.setSize(loginDimension);
                usernameBox.getEditor().setItem(usernameField.getText());
                passwordField.setText(String.valueOf(firstPasswordField.getPassword()));
                loginButton.setEnabled(true);
            } catch (IOException exception) {
                messageLabel.setIcon(warningImage);
                messageLabel.setText(exception.getMessage());
            }
        }
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
}