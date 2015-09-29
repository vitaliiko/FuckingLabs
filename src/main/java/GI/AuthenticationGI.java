package GI;

import GI.WorkspaceGI;
import support.IOFileHandling;
import support.User;
import support.UsersRights;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AuthenticationGI extends JFrame {

    private static final int COLUMNS_COUNT = 25;
    private static final String WRONG_USER = "Wrong login or password";
    private static final String EXIST_USER = "A user with that login or name already exists";
    private static final String ADD_USER = "Your account created successfully!";
    private static final String DOES_NOT_MATCH = "Passwords does not match";
    private static final String FORBIDDEN_CHAR = "Password contains forbidden characters";
    private static final String LOGIN = "Log in your account";
    private static final String CREATE = "Creating new account";

    private JPanel loginPanel;
    private JPanel signUpPanel;
    private JPanel fieldsPanel;
    private JPanel centerPanel;
    private JComboBox<Object> loginBox;
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

    private Set<User> userSet = new HashSet<>();
    private ArrayList<String> userNameList = new ArrayList<>();
    private Dimension loginDimension = new Dimension(400, 170);
    private Dimension signUpDimension = new Dimension(400, 250);

    public AuthenticationGI() throws HeadlessException {
        super("Login in");

        initUsersSet();
        prepareCenterPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        prepareMessageLabel();
        getContentPane().add(messageLabel, BorderLayout.NORTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(loginDimension);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUsersSet() {
        userSet.add(new User("Vova", "Ivanov", "vovan", "qwerty", UsersRights.SIMPLE_USER));
        userSet.add(new User("Vitaliy", "Kobrin", "vetal", "CthdktnL;fdf", UsersRights.ADMIN));
        userSet.add(new User("Mihail", "Kuznetsov", "mishania", "CjltydbRjv", UsersRights.SIMPLE_USER));
        userSet.add(new User("Maksim", "Davidenko", "makson3/4", "SPS-1466", UsersRights.SIMPLE_USER));
        for (User user : userSet) {
            userNameList.add(user.getUserName());
        }
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

        loginBox = new JComboBox<>(userNameList.toArray());
        loginBox.setSelectedIndex(-1);
        loginBox.setEditable(true);
        fieldsPanel.add(new LabelComponentPanel("Username: ", loginBox), BorderLayout.EAST);

        passwordField = new JPasswordField(COLUMNS_COUNT);
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
        messageLabel = new JLabel(LOGIN);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(new EmptyBorder(8, 0, 8, 0));
        messageLabel.setFont(new Font("times new roman", Font.PLAIN, 14));
    }

    public void prepareLoginButton() {
        loginButton = new JButton("Login");
        loginButton.setEnabled(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (User user : userSet) {
                    if (user.isMatches(usernameField.getText(), passwordField.getPassword())) {
                        setVisible(false);
                        new WorkspaceGI(user);
                        return;
                    }
                }
                messageLabel.setText(WRONG_USER);
                messageLabel.setVisible(true);
                loginPanel.setVisible(true);
                signUpPanel.setVisible(false);
                userNameList.add(usernameField.getText());
                passwordField.setText(Arrays.toString(passwordField.getPassword()));
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
        fieldsPanel.add(new LabelComponentPanel("Your name: ", nameField));

        surnameField = new JTextField(COLUMNS_COUNT);
        fieldsPanel.add(new LabelComponentPanel("Your surname: ", surnameField));

        usernameField = new JTextField(COLUMNS_COUNT);
        fieldsPanel.add(new LabelComponentPanel("Username: ", usernameField));

        firstPasswordField = new JPasswordField(COLUMNS_COUNT);
        fieldsPanel.add(new LabelComponentPanel("Password: ", firstPasswordField));

        secondPasswordField = new JPasswordField(COLUMNS_COUNT);
        fieldsPanel.add(new LabelComponentPanel("Repeat password: ", secondPasswordField));
    }

    public void prepareSignUpButton() {
        signUpButton = new JButton("Sign Up");
        signUpButton.setEnabled(false);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Arrays.equals(firstPasswordField.getPassword(), secondPasswordField.getPassword())) {
                    messageLabel.setText(DOES_NOT_MATCH);
                }
                if (userSet.add(new User(nameField.getText(),
                                        surnameField.getText(),
                                        usernameField.getText(),
                                        firstPasswordField.getPassword(),
                                        UsersRights.SIMPLE_USER))){
                    messageLabel.setText(ADD_USER);
                    IOFileHandling.saveUsersSet(userSet);
                } else {
                    messageLabel.setText(EXIST_USER);
                }
            }
        });
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

    public class CreateNewListener implements ActionListener {

        JFrame frame;

        public CreateNewListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loginPanel.setVisible(false);
            signUpPanel.setVisible(true);
            messageLabel.setText(CREATE);
            frame.setSize(signUpDimension);
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
            loginBox.setSelectedIndex(-1);
            passwordField.setText("");
            messageLabel.setText(LOGIN);
            frame.setSize(loginDimension);
        }
    }
}


