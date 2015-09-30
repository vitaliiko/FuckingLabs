package support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {

    private static final String NAME_REG = "^[A-Z][a-zA-Z-]{2,}$";
    private static final String USERNAME_REG = "^[a-zA-Z][a-zA-Z0-9_-]{6,15}$";
    private static final String PASSWORD_REG = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,24})";
    private static final String PLAIN_PASSWORD_REG = "^[\\w\\W]{6,24}$";
    private static final String TEL_NUM_REG = "^[\\d]{10}$";
    private static final String MAIL_REG = "([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*[a-z]{2,4}";

    private Set<User> userSet;
    private ArrayList<String> userNameList;

    public Controller() {
        initUsersSet();
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public ArrayList<String> getUserNameList() {
        return userNameList;
    }

    private void initUsersSet() {
        userSet = new HashSet<>();
        userSet.add(new User("admin", "admin", "admin", "111111", UsersRights.ADMIN));
        userSet.add(new User("Vova", "Ivanov", "vovan", "qwerty", UsersRights.SIMPLE_USER));
        userSet.add(new User("Vitaliy", "Kobrin", "vetal", "CthdktnL;fdf", UsersRights.ADMIN));
        userSet.add(new User("Mihail", "Kuznetsov", "mishania", "CjltydbRjv", UsersRights.SIMPLE_USER));
        userSet.add(new User("Maksim", "Davidenko", "makson3/4", "SPS-1466", UsersRights.SIMPLE_USER));
        userNameList = new ArrayList<>();
        userNameList.addAll(userSet.stream().map(User::getUserName).collect(Collectors.toList()));
    }

    public void createUser(String name, String surname, String userName, char[] password) throws IOException {
        validateName(name, surname);
        validateUsername(userName);
        validatePassword(password);

        User user = new User(name, surname, userName, password, UsersRights.SIMPLE_USER);
        if (!userSet.add(user)) {
            throw new IOException(Message.EXIST_USER);
        }
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

        User user = new User(userName, rights);
        if (!userSet.add(user)) {
            throw new IOException(Message.EXIST_USER);
        }
    }

    public boolean validateName(String name, String surname) throws IOException{
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
        if (!validate(String.valueOf(password), PASSWORD_REG)) {
            throw new IOException(Message.WRONG_PASSWORD);
        }
        return true;
    }

    public boolean validateTelephone(String telephone) throws IOException{
        if (!validate(telephone, TEL_NUM_REG)) {
            throw new IOException(Message.WRONG_TEL);
        }
        return true;
    }

    public boolean validateMail(String mail) throws IOException{
        if (!validate(mail, MAIL_REG)) {
            throw new IOException(Message.WRONG_MAIL);
        }
        return true;
    }

    public User authorizedUsers(String userName, char[] password) {
        for (User user : userSet) {
            if (user.isMatches(userName, password)) {
                return user;
            }
        }
        return null;
    }

    private boolean validate(String text, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
