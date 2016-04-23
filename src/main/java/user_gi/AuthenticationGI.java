package user_gi;

import components.BoxPanel;
import components.LabelComponentPanel;
import components.SingleMessage;
import frame_utils.FrameUtils;
import input_output.IOFileHandling;
import model.SingleController;
import model.User;
import model.UsersRights;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;

public class AuthenticationGI extends JFrame {

    private static final int COLUMNS_COUNT = 35;

    private JPanel loginPanel;
    private JPanel extendedLoginPanel;
    private JPanel signUpPanel;
    private JPanel fieldsPanel;
    private JPanel centerPanel;
    private JPanel timerPanel;
    private JComboBox<Object> usernameBox;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField usernameField;
    private JTextField telephoneField;
    private JTextField loginTelephoneField;
    private JTextField emailFiled;
    private JTextField loginEmailField;
    private JPasswordField firstPasswordField;
    private JPasswordField secondPasswordField;
    private JButton loginButton;
    private JButton signUpButton;
    private JButton cancelButton;
    private JButton createNewButton;
    private JLabel timerLabel;

    private Dimension loginDimension = new Dimension(400, 140);
    private Dimension signUpDimension = new Dimension(400, 290);
    private Dimension extendedLoginDimension = new Dimension(400, 205);
    private int rights;
    private int attemptsLeft;
    private Timer timer;

    public AuthenticationGI() {
        super("Авторизация");
        attemptsLeft = IOFileHandling.loadAttmpts();
        FrameUtils.setLookAndFeel();

        prepareCenterPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        prepareTimerPanel();
        getContentPane().add(timerPanel, BorderLayout.SOUTH);

        if (attemptsLeft <= 0) {
            extendedLoginPanel.setVisible(true);
        }
        if (attemptsLeft < 0) {
            startTimer();
        }
        frameSetup();
    }

    private void frameSetup() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(attemptsLeft <= 0 ? extendedLoginDimension : loginDimension);
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

    private void prepareTimerPanel() {
        timerLabel = new JLabel();
        timerPanel = new BoxPanel(new JLabel("До следующей попытки сталось: "), timerLabel);
        timerPanel.setVisible(false);
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
        extendedLoginPanel = new BoxPanel(BoxLayout.Y_AXIS);
        extendedLoginPanel.setVisible(attemptsLeft == 0);
        JPanel fieldsPanel = new BoxPanel(BoxLayout.Y_AXIS);

        prepareUsernameBox();
        fieldsPanel.add(new LabelComponentPanel("Логин: ", usernameBox), BorderLayout.EAST);

        passwordField = new JPasswordField(COLUMNS_COUNT);
        passwordField.getDocument().addDocumentListener(new LoginTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Пароль: ", passwordField));

        loginTelephoneField = new JTextField(COLUMNS_COUNT);
        loginTelephoneField.getDocument().addDocumentListener(new LoginTypeListener());
        extendedLoginPanel.add(new LabelComponentPanel("Телефон: ", loginTelephoneField));

        loginEmailField = new JTextField(COLUMNS_COUNT);
        loginEmailField.getDocument().addDocumentListener(new LoginTypeListener());
        extendedLoginPanel.add(new LabelComponentPanel("E-mail: ", loginEmailField));

        loginPanel.add(new BoxPanel(BoxLayout.Y_AXIS, fieldsPanel, extendedLoginPanel), BorderLayout.EAST);

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
        loginButton = new JButton("Войти");
        loginButton.setEnabled(false);
        loginButton.addActionListener(e -> {
            User user;
            String login = ((JTextField) usernameBox.getEditor().getEditorComponent()).getText();
            char[] password = passwordField.getPassword();
            if (extendedLoginPanel.isVisible()) {
                user = SingleController.getInstance().extendedUserAuthorization(login, password,
                        loginTelephoneField.getText(), loginEmailField.getText());
            } else {
                user = SingleController.getInstance().simpleUserAuthorization(login, password);
            }
            if (user != null) {
                IOFileHandling.saveAttempts(3);
                startWorkspace(user);
            } else {
                showErrorMessage();
                showExtendedLoginPanel();
                if (attemptsLeft < 0) {
                    startTimer();
                }
            }
        });
    }

    private void startWorkspace(User user) {
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

    private void showErrorMessage() {
        attemptsLeft--;
        IOFileHandling.saveAttempts(attemptsLeft);
        if (attemptsLeft == 0) {
            FrameUtils.showErrorDialog(this, "Неправильный логин или пароль.\n" +
                    "Для авторизации необходимо ввести\n" +
                    "номер телефона и адресс электронной почты");
        } else if (attemptsLeft > 0) {
            FrameUtils.showErrorDialog(this, "Неправильный логин или пароль.\nПопыток осталось: " + attemptsLeft);
        } else {
            FrameUtils.showErrorDialog(this, "Введены неверные данные.\nСледующая попытка через 1 мин.");
        }
    }

    private void showExtendedLoginPanel() {
        if (attemptsLeft == 0) {
            setSize(extendedLoginDimension);
            extendedLoginPanel.setVisible(true);
            loginButton.setEnabled(false);
        }
    }

    private void startTimer() {
        passwordField.setText("");
        passwordField.setEnabled(false);
        timerLabel.setText("01:00");
        timerPanel.setVisible(true);
        final int[] seconds = {60};
        timer = new Timer(1000, e -> {
            seconds[0]--;
            timerLabel.setText("00:" + (seconds[0] < 10 ? "0" + seconds[0] : seconds[0]));
            if (seconds[0] == 0) {
                stopTimer();
            }
        });
        timer.start();
    }

    private void stopTimer() {
        timer.stop();
        timerPanel.setVisible(false);
        passwordField.setEnabled(true);
    }

    private void prepareCreateNewButton() {
        createNewButton = new JButton("Регистрация");
        createNewButton.addActionListener(e -> {
            loginPanel.setVisible(false);
            signUpPanel.setVisible(true);
            clearFields();
            SingleMessage.setDefaultMessage(SingleMessage.CREATE);
            setSize(signUpDimension);
            setTitle("Регистрация");
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
        fieldsPanel.add(new LabelComponentPanel("Имя: ", nameField));

        surnameField = new JTextField(COLUMNS_COUNT);
        surnameField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Фамилия: ", surnameField));

        usernameField = new JTextField(COLUMNS_COUNT);
        usernameField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Логин: ", usernameField));

        telephoneField = new JTextField(COLUMNS_COUNT);
        telephoneField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Телефон: ", telephoneField));

        emailFiled = new JTextField(COLUMNS_COUNT);
        emailFiled.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("E-mail: ", emailFiled));

        firstPasswordField = new JPasswordField(COLUMNS_COUNT);
        firstPasswordField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Пароль: ", firstPasswordField));

        secondPasswordField = new JPasswordField(COLUMNS_COUNT);
        secondPasswordField.getDocument().addDocumentListener(signUpTypeListener);
        fieldsPanel.add(new LabelComponentPanel("Пароль еще раз: ", secondPasswordField));
    }

    private void prepareSignUpButton() {
        signUpButton = new JButton("Регистрация");
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
                            usernameField.getText(), telephoneField.getText(), emailFiled.getText(),
                            firstPasswordField.getPassword(), rights);
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
                FrameUtils.showErrorDialog(this, exception.getMessage());
            }
        });
    }

    private void prepareCancelButton() {
        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> {
            signUpPanel.setVisible(false);
            loginPanel.setVisible(true);
            clearFields();
            SingleMessage.setDefaultMessage(SingleMessage.LOGIN);
            if (attemptsLeft <= 0) {
                setSize(extendedLoginDimension);
                passwordField.setEnabled(attemptsLeft >= 0);
            } else {
                setSize(loginDimension);
            }
            setTitle("Авторизация");
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
                    !telephoneField.getText().isEmpty() &&
                    !emailFiled.getText().isEmpty() &&
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
            if (extendedLoginPanel.isVisible()) {
                loginButton.setEnabled(passwordField.getPassword().length != 0
                        && usernameBox.getSelectedItem() != null
                        && !loginTelephoneField.getText().isEmpty()
                        && !loginEmailField.getText().isEmpty());
            } else {
                loginButton.setEnabled(passwordField.getPassword().length != 0
                        && usernameBox.getSelectedItem() != null);
            }
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