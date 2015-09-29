package support;

import GI.WorkspaceGI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private static final String NAME_REG = "^[a-zA-Zà-ÿÀ-ß][a-zA-Zà-ÿÀ-ß-]{2,}$";
    private static final String USERNAME_REG = "^[a-zA-Z][a-zA-Z0-9_-]{6,15}$";
    private static final String PASSWORD_REG = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static final String PLAIN_PASSWORD_REG = "^[\\w\\W]{6,15}$";

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
        userSet.add(new User("Vova", "Ivanov", "vovan", "qwerty", UsersRights.SIMPLE_USER));
        userSet.add(new User("Vitaliy", "Kobrin", "vetal", "CthdktnL;fdf", UsersRights.ADMIN));
        userSet.add(new User("Mihail", "Kuznetsov", "mishania", "CjltydbRjv", UsersRights.SIMPLE_USER));
        userSet.add(new User("Maksim", "Davidenko", "makson3/4", "SPS-1466", UsersRights.SIMPLE_USER));
        userNameList = new ArrayList<>();
        for (User user : userSet) {
            userNameList.add(user.getUserName());
        }
    }

    public boolean addUser(User user) {
        return userSet.add(user);
    }

    public void createUser(String name, String surname, String userName, char[] password) throws IOException {
        if (!validate(name, NAME_REG) && !validate(surname, NAME_REG)) {
            throw new IOException(Message.WRONG_NAME);
        }
        if (!validate(userName, USERNAME_REG)) {
            throw new IOException(Message.WRONG_USERNAME);
        }
        System.out.println(String.valueOf(password));
        if (!validate(String.valueOf(password), PASSWORD_REG)) {
            throw new IOException(Message.WRONG_PASSWORD);
        }
        User user = new User(name, surname, userName, password, UsersRights.SIMPLE_USER);
        if (!userSet.add(user)) {
            throw new IOException(Message.EXIST_USER);
        }
    }

    public User authorizedUsers(String userName, char[] password) {
        for (User user : userSet) {
            if (user.isMatches(userName, password)) {
                return user;
            }
        }
        return null;
    }

    public boolean validate(String text, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
