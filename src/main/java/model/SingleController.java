package model;

import input_output.IOFileHandling;
import input_output.SingleMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class SingleController extends Validator {

    private static SingleController instance = new SingleController();
    private Set<User> userSet = new HashSet<>();
    private Map<String, Integer> userNameMap;

    private SingleController() {}

//    public SingleController() {
//        userSet = new HashSet<>();
//    }
//
//    public SingleController(Set<User> userSet) {
//        this.userSet = userSet;
//        userNameMap = new TreeMap<>();
//        for (User user : userSet) {
//            userNameMap.put(user.getLogin(), user.getRights());
//        }
//    }


    public static SingleController getInstance() {
        return instance;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
        userNameMap = new TreeMap<>();
        for (User user : userSet) {
            userNameMap.put(user.getLogin(), user.getRights());
        }
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
        User newUser = new User(name, surname, userName, password, rights);
        if (!userSet.add(newUser)) {
            throw new IOException(SingleMessage.EXIST_USER);
        }
        userNameMap.put(userName, rights);
        IOFileHandling.saveUsers();
    }

    public void updateUsersInfo(User user, String name, String surname, String telephone, String mail)
            throws IOException {
        validateName(name, surname);
        user.setFirstName(name);
        user.setLastName(surname);
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
            throw new IOException(SingleMessage.EXIST_USER);
        }
        userNameMap.put(userName, rights);
    }

    public void removeUser(User user) {
        userSet.remove(user);
        userNameMap.remove(user.getLogin());
    }

    public User authorizedUsers(String userName, char[] password) {
        for (User user : userSet) {
            if (user.isMatches(userName, password)) {
                return user;
            }
        }
        return null;
    }

    public User getAdmin() {
        return userSet.stream()
                .filter(u -> u.getLogin().equals("ADMIN"))
                .collect(Collectors.toList()).get(0);
    }

    public void addUser(User user) {
        userSet.add(user);
    }
}
