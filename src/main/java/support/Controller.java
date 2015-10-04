package support;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private static final String NAME_REG = "^[A-Z][a-zA-Z-]{2,}$";
    private static final String USERNAME_REG = "^[-a-zA-Z0-9_]{5,15}$";
    private static final String PASSWORD_REG = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&*-_]).{8,24})";
    private static final String TEL_NUM_REG = "^[\\d]{10}$";
    private static final String MAIL_REG = "^[a-zA-Z0-9\\._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,4}$";

    private Set<User> userSet;
    private Map<String, Integer> userNameMap;

    public Controller(Set<User> userSet) {
        this.userSet = userSet;
        userNameMap = new TreeMap<>();
        for (User user : userSet) {
            userNameMap.put(user.getUserName(), user.getRights());
        }
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public Map<String, Integer> getUserNameMap() {
        return userNameMap;
    }

    public void createUser(String name, String surname, String userName, char[] password, int rights)
            throws IOException {
        validateName(name, surname);
        if (rights != UsersRights.LOCK_USERNAME && rights != UsersRights.LOCK_USERNAME_WITH_SIMPLE_PASSWORD) {
            validateUsername(userName);
        }
        if (rights != UsersRights.LOCK_USERNAME_WITH_SIMPLE_PASSWORD) {
            validatePassword(password);
        }
        if (rights == UsersRights.LOCK_USERNAME || rights == UsersRights.LOCK_USERNAME_WITH_SIMPLE_PASSWORD) {
            removeUser(new User(userName, rights));
        }
        if (!userSet.add(new User(name, surname, userName, password, rights))) {
            throw new IOException(Message.EXIST_USER);
        }
        userNameMap.put(userName, rights);
        IOFileHandling.saveUsersSet(userSet);
    }

    public void updateUsersInfo(User user, String name, String surname, String telephone, String mail)
            throws IOException {
        validateName(name, surname);
        user.setName(name);
        user.setSurname(surname);
        if (!telephone.isEmpty()) {
            validateTelephone(telephone);
            user.setTelephoneNum(telephone);
        }
        if (!mail.isEmpty()) {
            validateMail(mail);
            user.setMailAddress(mail);
        }
    }

    public void addEmptyUser(String userName, int rights) throws IOException {
        validateUsername(userName);

        if (!userSet.add(new User(userName, rights))) {
            throw new IOException(Message.EXIST_USER);
        }
        userNameMap.put(userName, rights);
    }

    public void removeUser(User user) {
        userSet.remove(user);
        userNameMap.remove(user.getUserName());
    }

    public boolean validateName(String name, String surname) throws IOException {
        if (!validate(name, NAME_REG) || !validate(surname, NAME_REG)) {
            throw new IOException(Message.WRONG_NAME);
        }
        return true;
    }

    public boolean validateUsername(String userName) throws IOException {
        if (userName.toLowerCase().contains("admin")) {
            throw new IOException(Message.NON_ADMIN_USER);
        }
        if (!validate(userName, USERNAME_REG)) {
            throw new IOException(Message.WRONG_USERNAME);
        }
        return true;
    }

    public boolean validatePassword(char[] password) throws IOException {
        if (password.length < 8) {
            throw new IOException(Message.SHORT_PASSWORD);
        }
        if (password.length > 24) {
            throw new IOException(Message.LONG_PASSWORD);
        }
        if (!validate(String.valueOf(password), PASSWORD_REG)) {
            throw new IOException(Message.WRONG_PASSWORD);
        }
        return true;
    }

    public boolean validateTelephone(String telephone) throws IOException {
        if (!validate(telephone, TEL_NUM_REG)) {
            throw new IOException(Message.WRONG_TEL);
        }
        return true;
    }

    public boolean validateMail(String mail) throws IOException {
        if (!validate(mail, MAIL_REG)) {
            throw new IOException(Message.WRONG_MAIL);
        }
        return true;
    }

    private boolean validate(String text, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public User authorizedUsers(String userName, char[] password) {
        for (User user : userSet) {
            if (user.isMatches(userName, password)) {
                return user;
            }
        }
        return null;
    }
}
