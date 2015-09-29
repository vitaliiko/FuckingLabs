package GI;

import support.User;
import support.UsersRights;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkspaceGI extends JFrame {

    private User user;

    private JPanel workspacePanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;

    public WorkspaceGI(User user) {
        super("NBKS-Lab2");
        this.user = user;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        prepareWorkspacePanel();
        getContentPane().add(workspacePanel, BorderLayout.CENTER);
        prepareMenuBar();
        setJMenuBar(menuBar);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setIconImage(new ImageIcon("resources/icon.png").getImage());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prepareWorkspacePanel() {
        workspacePanel = new JPanel();
        workspacePanel.setLayout(new BoxLayout(workspacePanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Hello " + user.getName() + " " + user.getSurname() + "!\n" + "Welcome in your workspace!");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("times new roman", Font.PLAIN, 16));
        workspacePanel.add(label);

        JTextArea textArea = new JTextArea();
        workspacePanel.add(new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
    }

    public void prepareMenuBar() {
        menuBar = new JMenuBar();
        prepareFileMenu();
        menuBar.add(fileMenu);
    }

    public void prepareFileMenu() {
        fileMenu = new JMenu("File");

        JMenuItem logoutItem = new JMenuItem("Log out");
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fileMenu.add(logoutItem);

        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fileMenu.add(settingsItem);

        JMenuItem usersItem = new JMenuItem("Users");
        if (user.getRights() != UsersRights.ADMIN) {
            usersItem.setVisible(false);
        }
        usersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fileMenu.add(usersItem);

        fileMenu.addSeparator();

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fileMenu.add(closeItem);
    }
}
