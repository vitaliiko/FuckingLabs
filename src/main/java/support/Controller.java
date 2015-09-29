package support;

import GI.WorkspaceGI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Controller {

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

    public User authorizedUsers(String userName, char[] password) {
        for (User user : userSet) {
            if (user.isMatches(userName, password)) {
                return user;
            }
        }
        return null;
    }
}
