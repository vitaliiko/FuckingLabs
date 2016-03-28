package utils;

import model.Controller;
import model.User;
import model.UsersRights;
import user_gi.*;

import javax.swing.*;
import java.awt.event.ActionListener;

public class WorkspaceUtil {

    private WorkspaceGI workspaceGI;
    private JMenu fileMenu;
    private JFrame usersInfoGI;
    private User user;
    private Controller controller;

    public WorkspaceUtil(WorkspaceGI workspaceGI, User user) {
        this.workspaceGI = workspaceGI;
        this.user = user;
        this.controller = Controller.getInstance();
    }

    public JMenuBar prepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        prepareFileMenu();
        menuBar.add(fileMenu);
        menuBar.add(prepareHelpMenu());
        return menuBar;
    }

    private void prepareFileMenu() {
        fileMenu = new JMenu("File");
        createMenuItem("Log out", "logout.png", e -> {
            workspaceGI.dispose();
            if (usersInfoGI != null) {
                usersInfoGI.dispose();
            }
        });
        createMenuItem("Settings          ", "settings.png", e -> new SettingsGI(workspaceGI, user));
        if (user.getRights() == UsersRights.ADMIN) {
            createMenuItem("Change time", "clock.png", e -> new SetLastModifiedTimeGI(workspaceGI));
            createMenuItem("Users info", "users.png", e -> usersInfoGI = new UsersInfoGI());
            createMenuItem("Add user", "addUsers.png", e -> new AddEmptyUserGI(workspaceGI));
        }
        fileMenu.addSeparator();
        createMenuItem("Close", null, e -> System.exit(0));
    }

    private void createMenuItem(String name, String iconPath, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setIcon(new ImageIcon(FrameUtils.RESOURCES_PATH + iconPath));
        menuItem.addActionListener(listener);
        fileMenu.add(menuItem);
    }

    private JMenu prepareHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About           ");
        aboutItem.addActionListener(e -> JOptionPane.showConfirmDialog(null,
                "<html>" +
                        "Програму розробли студенти групи СПС-1466 <br>" +
                        "Артеменко І.Ю.<br>" +
                        "Кобрін В.О.<br>" +
                        "Петров Д.В.<br>" +
                        "</html>",
                "About",
                JOptionPane.DEFAULT_OPTION));
        helpMenu.add(aboutItem);
        return helpMenu;
    }
}

