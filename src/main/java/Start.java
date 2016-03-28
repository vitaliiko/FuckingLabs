import input_output.TimeUtil;
import user_gi.AuthenticationGI;
import user_gi.WorkspaceGI;
import model.Controller;
import input_output.IOFileHandling;
import model.User;
import model.UsersRights;
import utils.FrameUtils;

import javax.swing.*;

public class Start {

    public static void main(String[] args) {
        FrameUtils.setLookAndFeel();
        if (TimeUtil.checkLastModified()) {
//            initUserSet();
            startWorkspaceGI();
        } else {
            JOptionPane.showConfirmDialog(null, "System file was modified", "ACHTUNG!",
                    JOptionPane.OK_CANCEL_OPTION);
        }

//        initUserSet();
//        startWorkspaceGI();

    }

    public static void startAuthGI() {
        Controller controller = new Controller(IOFileHandling.loadUsers());
        try {
            new AuthenticationGI(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startWorkspaceGI() {
        Controller controller = new Controller(IOFileHandling.loadUsers());
        try {
            User user = new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN);
            user.setAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/*-+=!?., ");
            new WorkspaceGI(user, controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initUserSet() {
//        Controller controller = new Controller(IOFileHandling.loadUsers());
        Controller controller = new Controller();
        controller.addUser(new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN));
        controller.addUser(new User("Vitaliy", "Kobrin", "vetal", "<f,tyrjDthf#01", UsersRights.ADMIN));
        controller.addUser(new User("Ivan", "Artemenko", "vanomas", "vanno2012", UsersRights.USER_WITH_SIMPLE_PASSWORD));
        controller.addUser(new User("Maksim", "Davidenko", "asteroid", "12345@qQ", UsersRights.LOCK_USERNAME));
        controller.addUser(new User("Petrov", "Denis", "petromas", "petro123@", UsersRights.BLOCKED_USER));
        controller.addUser(new User("Empty1", UsersRights.EMPTY));
        controller.addUser(new User("Empty2", UsersRights.EMPTY_SIMPLE_PASSWORD));
        IOFileHandling.saveUsers(controller);
    }
}
