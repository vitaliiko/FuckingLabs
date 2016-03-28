package model;

import components.SingleMessage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String NAME_REG = "^[A-Z][a-zA-Z-]{2,}$";
    private static final String USERNAME_REG = "^[-a-zA-Z0-9_]{5,15}$";
    private static final String PASSWORD_REG = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&*-_]).{8,24})";
    private static final String TEL_NUM_REG = "^[\\d]{10}$";
    private static final String MAIL_REG = "^[a-zA-Z0-9\\._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,4}$";

    public boolean validateName(String name, String surname) throws IOException {
        if (!validate(name, NAME_REG) || !validate(surname, NAME_REG)) {
            throw new IOException(SingleMessage.WRONG_NAME);
        }
        return true;
    }

    public boolean validateUsername(String userName) throws IOException {
        if (userName.toLowerCase().contains("admin")) {
            throw new IOException(SingleMessage.NON_ADMIN_USER);
        }
        if (!validate(userName, USERNAME_REG)) {
            throw new IOException(SingleMessage.WRONG_USERNAME);
        }
        return true;
    }

    public boolean validatePassword(char[] password) throws IOException {
        if (password.length < 8) {
            throw new IOException(SingleMessage.SHORT_PASSWORD);
        }
        if (password.length > 24) {
            throw new IOException(SingleMessage.LONG_PASSWORD);
        }
        if (!validate(String.valueOf(password), PASSWORD_REG)) {
            throw new IOException(SingleMessage.WRONG_PASSWORD);
        }
        return true;
    }

    public boolean validateTelephone(String telephone) throws IOException {
        if (!validate(telephone, TEL_NUM_REG)) {
            throw new IOException(SingleMessage.WRONG_TEL);
        }
        return true;
    }

    public boolean validateMail(String mail) throws IOException {
        if (!validate(mail, MAIL_REG)) {
            throw new IOException(SingleMessage.WRONG_MAIL);
        }
        return true;
    }

    private boolean validate(String text, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
