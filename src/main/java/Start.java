import user_gi.AuthenticationGI;
import user_gi.WorkspaceGI;
import model.Controller;
import input_output.IOFileHandling;
import model.User;
import model.UsersRights;

import java.util.HashSet;
import java.util.Set;

public class Start {

    public static void main(String[] args) {

        initUserSet();
        startWorkspaceGI();

    }

    public static void startAuthGI() {
        Controller controller = new Controller(IOFileHandling.loadUsersSet());
        try {
            new AuthenticationGI(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startWorkspaceGI() {
        Controller controller = new Controller(IOFileHandling.loadUsersSet());
        try {
            User user = new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN);
            user.setAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/*-+=!?., ");
            new WorkspaceGI(user, controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initUserSet() {
        Set<User> userSet = new HashSet<>();
        userSet.add(new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN));
        userSet.add(new User("Vitaliy", "Kobrin", "vetal", "<f,tyrjDthf#01", UsersRights.ADMIN));
        userSet.add(new User("Ivan", "Artemenko", "vanomas", "vanno2012", UsersRights.USER_WITH_SIMPLE_PASSWORD));
        userSet.add(new User("Maksim", "Davidenko", "asteroid", "12345@qQ", UsersRights.LOCK_USERNAME));
        userSet.add(new User("Petrov", "Denis", "petromas", "petro123@", UsersRights.BLOCKED_USER));
        userSet.add(new User("Empty1", UsersRights.EMPTY));
        userSet.add(new User("Empty2", UsersRights.EMPTY_SIMPLE_PASSWORD));
        IOFileHandling.saveUsersSet(userSet);
    }
}
