package InputOutput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Message {

    public static final ImageIcon WARNING_IMAGE = new ImageIcon("resources/warning.png");

    public static final String WRONG_USER = "Wrong username or password";
    public static final String EXIST_USER = "This username already exists";
    public static final String ADD_USER_SUC = "Your account created successfully!";
    public static final String PASSWORDS_DOES_NOT_MATCH = "Passwords does not match";
    public static final String WRONG_USERNAME = "Wrong username";
    public static final String NON_ADMIN_USER = "You can\'t create account with such username";
    public static final String SHORT_PASSWORD = "Password is too short";
    public static final String LONG_PASSWORD = "Password is too long";
    public static final String WRONG_PASSWORD = "<html>Password must contains uppercase and <br>lowercase letters, " +
            "numbers and special characters</html>";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String WRONG_NAME = "Name or surname contains forbidden characters";
    public static final String LOGIN = "Log in your account";
    public static final String CREATE = "Creating new account";
    public static final String USER_IS_LOCKED = "This username is blocked";
    public static final String USER_IS_EMPTY = "This username has no password. Press \"New account\"";

    public static final String SETTINGS = "Set your account";
    public static final String WRONG_TEL = "Wrong telephone number";
    public static final String WRONG_MAIL = "Wrong mail address";
    public static final String SAVED = "Changes saved";

    public static final String ADD_NEW_USER = "Add new empty user";

    public static final String USER_LIST = "A list of users";
    public static final String USER_INFO = "User information";

    public static JLabel prepareMessageLabel(String message) {
        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(new EmptyBorder(8, 0, 8, 0));
        messageLabel.setFont(new Font("times new roman", Font.PLAIN, 16));
        return messageLabel;
    }
}
