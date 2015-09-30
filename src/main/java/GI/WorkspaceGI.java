package GI;

import support.Controller;
import support.User;
import support.UsersRights;

import javax.swing.*;
import java.awt.*;

public class WorkspaceGI extends JFrame {

    private User user;
    private Controller controller;

    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu;

    public WorkspaceGI(User user, Controller controller) {
        super("NBKS-Lab2");
        this.user = user;
        this.controller = controller;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        textArea = new JTextArea();
        textArea.setFont(new Font("arial", Font.PLAIN, 14));
        textArea.setText("Hello, " + user.getName() + " " + user.getSurname() + "!\n" + "Welcome in your workspace!");
        getContentPane().add(new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        prepareMenuBar();
        setJMenuBar(menuBar);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prepareMenuBar() {
        menuBar = new JMenuBar();
        prepareFileMenu();
        menuBar.add(fileMenu);
    }

    public void prepareFileMenu() {
        fileMenu = new JMenu("File");

        JMenuItem logoutItem = new JMenuItem("Log out");
        logoutItem.setIcon(new ImageIcon("resources/logout.png"));
        logoutItem.addActionListener(e -> dispose());
        fileMenu.add(logoutItem);

        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.setIcon(new ImageIcon("resources/settings.png"));
        settingsItem.addActionListener(e -> new SettingsGI(this, user, controller));
        fileMenu.add(settingsItem);

        JMenuItem usersItem = new JMenuItem("Users");
        usersItem.setIcon(new ImageIcon("resources/users.png"));
        if (user.getRights() != UsersRights.ADMIN) {
            usersItem.setVisible(false);
        }
        usersItem.addActionListener(e -> {

        });
        fileMenu.add(usersItem);

        JMenuItem addUsersItem = new JMenuItem("Add user");
        addUsersItem.setIcon(new ImageIcon("resources/addUsers.png"));
        if (user.getRights() != UsersRights.ADMIN) {
            addUsersItem.setVisible(false);
        }
        addUsersItem.addActionListener(e -> {

        });
        fileMenu.add(addUsersItem);

        fileMenu.addSeparator();

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        fileMenu.add(closeItem);
    }
}
