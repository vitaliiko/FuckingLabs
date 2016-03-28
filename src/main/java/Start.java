import coder.CardanGrilleCoder;
import model.SingleController;
import model.User;
import model.UsersRights;
import user_gi.FriendshipWorkspaceGI;
import utils.TimeUtil;
import user_gi.AuthenticationGI;
import user_gi.WorkspaceGI;
import input_output.IOFileHandling;
import utils.FrameUtils;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.Arrays;

public class Start {

    public static void main(String[] args) {

        FrameUtils.setLookAndFeel();
//        initUserSet();
//        startAuthGI();
//        startWorkspaceWithCheckTime();
//        startFriendshipGI();

        turn();
    }

    public static void turn() {
        CardanGrilleCoder coder = (CardanGrilleCoder) CardanGrilleCoder.getInstance();

        try {
            System.out.println(coder.encode(null, "Решётка Кардано — инструмент шифрова"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startWorkspaceWithCheckTime() {
        if (TimeUtil.checkLastModifiedTime()) {
            startWorkspaceGI();
        } else {
            JOptionPane.showConfirmDialog(null, "System file was modified", "ACHTUNG!",
                    JOptionPane.OK_CANCEL_OPTION);
        }
    }

    public static void startFriendshipGI() {
        SingleController.getInstance().setUserSet(IOFileHandling.loadUsers());
        try {
            User user = new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN);
            new FriendshipWorkspaceGI(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startAuthGI() {
        SingleController.getInstance().setUserSet(IOFileHandling.loadUsers());
        try {
            new AuthenticationGI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startWorkspaceGI() {
        SingleController.getInstance().setUserSet(IOFileHandling.loadUsers());
        try {
            User user = new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN);
            user.setAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/*-+=!?., ");
            new WorkspaceGI(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initUserSet() {
        SingleController controller = SingleController.getInstance();
        controller.addUser(new User("Admin", "Admin", "ADMIN", "111111", UsersRights.ADMIN));
        controller.addUser(new User("Vitaliy", "Kobrin", "vetal", "<f,tyrjDthf#01", UsersRights.ADMIN));
        controller.addUser(new User("Ivan", "Artemenko", "vanomas", "vanno2012", UsersRights.USER_WITH_SIMPLE_PASSWORD));
        controller.addUser(new User("Maksim", "Davidenko", "asteroid", "12345@qQ", UsersRights.LOCK_USERNAME));
        controller.addUser(new User("Petrov", "Denis", "petromas", "petro123@", UsersRights.BLOCKED_USER));
        controller.addUser(new User("Empty1", UsersRights.EMPTY));
        controller.addUser(new User("Empty2", UsersRights.EMPTY_SIMPLE_PASSWORD));
        IOFileHandling.saveUsers();
    }
}
