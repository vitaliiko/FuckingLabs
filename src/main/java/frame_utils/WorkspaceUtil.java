package frame_utils;

import model.User;
import model.UserRights;
import model.Virus;
import user_gi.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class WorkspaceUtil {

    public static final String FRAME_NAME = "Проектування систем комплексного захисту інформації";

    private JFrame workspaceGI;
    private JFrame usersInfoGI;
    private User user;

    public WorkspaceUtil(JFrame workspaceGI, User user) {
        this.workspaceGI = workspaceGI;
        this.user = user;
    }

    public JMenuBar prepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(prepareFileMenu());
        //menuBar.add(prepareViewMenu());
        menuBar.add(prepareVirusMenu());
        menuBar.add(prepareHelpMenu());
        return menuBar;
    }

    private JMenu prepareFileMenu() {
        JMenu fileMenu = new JMenu("File");
        if (workspaceGI instanceof WorkspaceGI) {
            createMenuItem(fileMenu, "Log out", "logout.png", e -> {
                workspaceGI.dispose();
                if (usersInfoGI != null) {
                    usersInfoGI.dispose();
                }
            });
            createMenuItem(fileMenu, "Settings          ", "settings.png", e -> new SettingsGI(workspaceGI, user));
            createMenuItem(fileMenu, "Attach USB-token", "usb_small.png", e -> new SetTokenGI(user));
            if (user.getRights() == UserRights.ADMIN) {
                createMenuItem(fileMenu, "Change time", "clock.png", e -> new SetLastModifiedTimeGI(workspaceGI));
                createMenuItem(fileMenu, "Users info", "users.png", e -> usersInfoGI = new UsersInfoGI());
                createMenuItem(fileMenu, "Add user", "addUsers.png", e -> new AddEmptyUserGI(workspaceGI));
            }
            fileMenu.addSeparator();
        }
        createMenuItem(fileMenu, "Close", null, e -> System.exit(0));

        return fileMenu;
    }

    private JMenuItem createMenuItem(JMenu menu, String name, String iconPath, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(name);
        if (iconPath != null) {
            menuItem.setIcon(new ImageIcon(FrameUtil.RESOURCES_PATH + iconPath));
        }
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        return menuItem;
    }

    private JMenu prepareViewMenu() {
        JMenu viewMenu = new JMenu("View");
        JMenuItem friendlyItem = createMenuItem(viewMenu, "Friendly interface", null, e -> {
            workspaceGI.dispose();
            //new FriendlyWorkspaceGI(user);
        });
        friendlyItem.setVisible(workspaceGI instanceof WorkspaceGI);

        JMenuItem notFriendlyItem = createMenuItem(viewMenu, "Not friendly interface", null, e -> {
            workspaceGI.dispose();
            new WorkspaceGI(user);
        });
        notFriendlyItem.setVisible(workspaceGI instanceof FriendlyWorkspaceGI);

        return viewMenu;
    }

    private JMenu prepareVirusMenu() {
        JMenu virusMenu = new JMenu("Virus");
        createMenuItem(virusMenu, "Search .exe files", null, e -> new VirusGI());
        createMenuItem(virusMenu, "Change file attributes", null, e -> {
            Virus.changeFilesAttributes();
            FrameUtil.showConfirmDialog(workspaceGI, "Virus did their dirty work");
        });
        createMenuItem(virusMenu, "Encode files", null, e -> {
            Virus.encodeFiles();
            FrameUtil.showConfirmDialog(workspaceGI, "Virus did their dirty work");
        });
        JMenuItem decodeItem = createMenuItem(virusMenu, "Decode files", null, e -> {
            Virus.decodeFiles();
            FrameUtil.showConfirmDialog(workspaceGI, "Virus did their dirty work");
        });
        decodeItem.setEnabled(false);
        createMenuItem(virusMenu, "Encode files content", null, e -> encodeFileContent());
        createMenuItem(virusMenu, "Decode file content", null, e -> decodeFileContent());
        virusMenu.setVisible(workspaceGI instanceof WorkspaceGI);
        return virusMenu;
    }

    private void encodeFileContent() {
        try {
            Virus.encodeFileContent();
            FrameUtil.showConfirmDialog(workspaceGI, "Virus did their dirty work");
        } catch (IOException e) {
            FrameUtil.showErrorDialog(workspaceGI, e.getMessage());
        }
    }

    private void decodeFileContent() {
        try {
            Virus.decodeFileContent();
            FrameUtil.showConfirmDialog(workspaceGI, "Virus decoded files");
        } catch (IOException e) {
            FrameUtil.showErrorDialog(workspaceGI, e.getMessage());
        }
    }

    private JMenu prepareHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About           ");
        aboutItem.addActionListener(e -> JOptionPane.showConfirmDialog(null,
                        "Програму розробли студенти групи СПС-1466\r\n" +
                        "Артеменко І.Ю.\r\n" +
                        "Кобрін В.О.\r\n" +
                        "Петров Д.В.\r\n",
                "About",
                JOptionPane.DEFAULT_OPTION));
        helpMenu.add(aboutItem);
        return helpMenu;
    }
}

