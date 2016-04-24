import input_output.IOFileHandling;
import model.*;
import user_gi.AuthenticationGI;
import user_gi.MainWindowGI;
import user_gi.WorkspaceGI;

import javax.swing.*;

public class Start {

    public static void main(String[] args) {

        initAttempts();
        initUserSet();
//        startAuthGI();
        new MainWindowGI();
//        new GoodsGI(new User("Petrov", "Denis", "petromas", "petro123@", UsersRights.ADMIN));
    }

    public static void startWorkspaceWithCheckTime() {
        if (TimeUtil.checkLastModifiedTime()) {
            startWorkspaceGI();
        } else {
            JOptionPane.showConfirmDialog(null, "System file was modified. Can not start up", "ACHTUNG!",
                    JOptionPane.OK_CANCEL_OPTION);
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
            User user = new User("Admin", "Admin", "ADMIN", "111111", UserRights.ADMIN);
            user.setAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/*'-+=!?., ");
            new WorkspaceGI(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initUserSet() {
        SingleController controller = SingleController.getInstance();
        User admin = new User("Admin", "Admin", "ADMIN", PasswordDigest.hashText("111111"), "02598745896", "superadmin@gmail.com", UserRights.ADMIN);
        admin.setStartUpCount(SingleController.MAX_START_UP_COUNT);
        controller.addUser(admin);
        controller.addUser(new User("Виталий", "Кобрин", "vitaliykobrin", PasswordDigest.hashText("111111"), "066532578589", "vitaliykobrin@mail.ru", UserRights.BLOCKED_USER));
        controller.addUser(new User("Иван", "Артеменко", "vanomas", PasswordDigest.hashText("111111"), "023258745878", "vanomas@gmail.com", UserRights.SIMPLE_USER));
        controller.addUser(new User("Петров", "Денис", "petromas", PasswordDigest.hashText("111111"), "032526969696", "petromas@gmail.com", UserRights.SIMPLE_USER));
        controller.addUser(new User("Иванов", "Денис", "ivanov", PasswordDigest.hashText("111111"), "02578741478", "ivanov@gmail.com", UserRights.BLOCKED_USER));
        controller.addUser(new User("Иванов", "Иван", "vanno", PasswordDigest.hashText("111111"), "00235987412", "vanno@gmail.com", UserRights.BLOCKED_USER));
        IOFileHandling.saveUsers();
    }

    private static void initAttempts() {
        IOFileHandling.saveAttempts(3);
    }
}
