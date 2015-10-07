import GI.AuthenticationGI;
import support.Controller;
import InputOutput.IOFileHandling;

public class Start {

    public static void main(String[] args) {

        Controller controller = new Controller(IOFileHandling.loadUsersSet());
        try {
            new AuthenticationGI(controller);
//            User user = new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN);
//            user.setAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/*-+=!¹?., ");
//            new WorkspaceGI(user, controller);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Set<User> userSet = new HashSet<>();
//        userSet.add(new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN));
//        userSet.add(new User("Vitaliy", "Kobrin", "vetal", "Ybrjlbv<f,tyrj#01", UsersRights.ADMIN));
//        userSet.add(new User("Ivan", "Artemenko", "vanomas", "111111", UsersRights.USER_WITH_SIMPLE_PASSWORD));
//        userSet.add(new User("Maksim", "Davidenko", "asteroid", "12345@qQ", UsersRights.LOCK_USERNAME));
//        userSet.add(new User("Petrov", "Denis", "petromas", "petro123@", UsersRights.BLOCKED_USER));
//        userSet.add(new User("Empty1", UsersRights.EMPTY));
//        userSet.add(new User("Empty2", UsersRights.EMPTY_SIMPLE_PASSWORD));
//        IOFileHandling.saveUsersSet(userSet);
    }
}
