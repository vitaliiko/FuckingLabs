package utils;

import model.Controller;
import model.User;
import model.UsersRights;
import user_gi.AddEmptyUserGI;
import user_gi.SettingsGI;
import user_gi.UsersInfoGI;
import user_gi.WorkspaceGI;

import javax.swing.*;

public class WorkspaceUtil {

    private WorkspaceGI workspaceGI;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JFrame usersInfoGI;
    private User user;
    private Controller controller;

    public WorkspaceUtil(WorkspaceGI workspaceGI, User user, Controller controller) {
        this.workspaceGI = workspaceGI;
        this.user = user;
        this.controller = controller;
    }

    public JMenuBar prepareMenuBar() {
        menuBar = new JMenuBar();
        prepareFileMenu();
        menuBar.add(fileMenu);
        menuBar.add(prepareHelpMenu());
        return menuBar;
    }

    private void prepareFileMenu() {
        fileMenu = new JMenu("File");

        JMenuItem logoutItem = new JMenuItem("Log out");
        logoutItem.setIcon(new ImageIcon("resources/logout.png"));
        logoutItem.addActionListener(e -> {
            workspaceGI.dispose();
            if (usersInfoGI != null) {
                usersInfoGI.dispose();
            }
        });
        fileMenu.add(logoutItem);

        JMenuItem settingsItem = new JMenuItem("Settings          ");
        settingsItem.setIcon(new ImageIcon("resources/settings.png"));
        settingsItem.addActionListener(e -> new SettingsGI(workspaceGI, user, controller));
        fileMenu.add(settingsItem);

        JMenuItem usersItem = new JMenuItem("Users info");
        usersItem.setIcon(new ImageIcon("resources/users.png"));
        if (user.getRights() != UsersRights.ADMIN) {
            usersItem.setVisible(false);
        }
        usersItem.addActionListener(e -> usersInfoGI = new UsersInfoGI(controller));
        fileMenu.add(usersItem);

        JMenuItem addUsersItem = new JMenuItem("Add user");
        addUsersItem.setIcon(new ImageIcon("resources/addUsers.png"));
        if (user.getRights() != UsersRights.ADMIN) {
            addUsersItem.setVisible(false);
        }
        addUsersItem.addActionListener(e -> new AddEmptyUserGI(workspaceGI, controller));
        fileMenu.add(addUsersItem);

        fileMenu.addSeparator();

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        fileMenu.add(closeItem);
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

