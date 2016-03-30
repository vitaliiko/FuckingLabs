package user_gi;

import input_output.IOFileHandling;
import components.SingleMessage;
import components.BoxPanel;
import model.*;
import components.UserInfoPanel;
import components.UsersTableModel;
import frame_utils.FrameUtils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UsersInfoGI extends JFrame {

    private SingleController controller;
    private ArrayList<User> usersList;
    private ArrayList<JPanel> userInfoPanelsList;
    private int usersIndex;

    private JPanel tablePanel;
    private JPanel viewUserInfoPanel;
    private JPanel centerPanel;
    private JPanel saveCancelPanel;
    private JPanel navigationPanel;
    private JPanel addRemovePanel;
    private JButton prevButton;
    private JButton nextButton;
    private JButton saveButton;
    private JButton showInfoButton;
    private JButton removeButton;
    private JTable usersTable;

    public UsersInfoGI() throws HeadlessException {
        super("Users");
        this.controller = SingleController.getInstance();
        usersList = new ArrayList<>(controller.getUserSet());
        userInfoPanelsList = new ArrayList<>();
        userInfoPanelsList.addAll(usersList.stream().map(UserInfoPanel::new).collect(Collectors.toList()));

        FrameUtils.setLookAndFeel();

        getContentPane().add(SingleMessage.getMessageInstance(SingleMessage.USER_LIST), BorderLayout.NORTH);

        prepareCenterPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        prepareSaveCancelPanel();
        getContentPane().add(saveCancelPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(500, 350));
        setIconImage(new ImageIcon("resources/users.png").getImage());
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareCenterPanel() {
        centerPanel = new JPanel();
        prepareTablePanel();
        centerPanel.add(tablePanel);
        prepareUserInfoPanel();
        centerPanel.add(viewUserInfoPanel);
    }

    private void prepareTablePanel() {
        tablePanel = new BoxPanel(BoxLayout.Y_AXIS);

        prepareShowButton();
        tablePanel.add(new BoxPanel(showInfoButton));

        prepareUsersTable();
        tablePanel.add(new JScrollPane(usersTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
    }

    private void prepareUsersTable() {
        TableModel tableModel = new UsersTableModel(controller, usersList);
        usersTable = new JTable(tableModel);
        usersTable.getTableHeader().setReorderingAllowed(false);
        usersTable.setShowHorizontalLines(false);
        usersTable.setShowVerticalLines(false);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.getSelectionModel().addListSelectionListener(e -> showInfoButton.setEnabled(true));
        usersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showInfoButton.doClick();
                }
            }
        });
    }

    private void prepareShowButton() {
        showInfoButton = new JButton("Show info");
        showInfoButton.setEnabled(false);
        showInfoButton.setHorizontalAlignment(JButton.CENTER);
        showInfoButton.addActionListener(e -> {
            tablePanel.setVisible(false);
            viewUserInfoPanel.setVisible(true);
            usersIndex = usersTable.getSelectedRow();
            userInfoPanelsList.get(usersIndex).setVisible(true);
            checkButtonsEnabled();
            SingleMessage.setDefaultMessage(SingleMessage.USER_INFO);
        });
    }

    private void prepareSaveCancelPanel() {
        saveCancelPanel = new JPanel();

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            saveButton.setEnabled(false);
            IOFileHandling.saveUsers();
        });
        saveCancelPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        saveCancelPanel.add(cancelButton);
    }

    private void prepareUserInfoPanel() {
        viewUserInfoPanel = new BoxPanel(BoxLayout.Y_AXIS);
        viewUserInfoPanel.setVisible(false);

        prepareNavigationPanel();
        viewUserInfoPanel.add(navigationPanel);

        viewUserInfoPanel.add(new JSeparator());
        userInfoPanelsList.forEach(viewUserInfoPanel::add);
        viewUserInfoPanel.add(new JSeparator());

        prepareAddRemovePanel();
        viewUserInfoPanel.add(addRemovePanel);
    }

    private void prepareNavigationPanel() {
        prevButton = new JButton("Prev");
        prevButton.setIcon(new ImageIcon("resources/prev.png"));
        prevButton.setHorizontalAlignment(JButton.LEFT);
        prevButton.addActionListener(e -> {
            userInfoPanelsList.get(usersIndex).setVisible(false);
            userInfoPanelsList.get(--usersIndex).setVisible(true);
            checkButtonsEnabled();
        });

        JButton backButton = new JButton("Back");
        backButton.setHorizontalAlignment(JButton.CENTER);
        backButton.addActionListener(e -> {
            viewUserInfoPanel.setVisible(false);
            userInfoPanelsList.get(usersIndex).setVisible(false);
            tablePanel.setVisible(true);
            SingleMessage.setDefaultMessage(SingleMessage.USER_LIST);
            usersIndex = 0;

        });

        nextButton = new JButton("Next");
        nextButton.setIcon(new ImageIcon("resources/next.png"));
        nextButton.setHorizontalTextPosition(SwingConstants.LEFT);
        nextButton.setHorizontalAlignment(JButton.RIGHT);
        nextButton.addActionListener(e -> {
            userInfoPanelsList.get(usersIndex).setVisible(false);
            userInfoPanelsList.get(++usersIndex).setVisible(true);
            checkButtonsEnabled();
        });

        navigationPanel = new BoxPanel(prevButton, backButton, nextButton);
    }

    private void prepareAddRemovePanel() {
        JButton addButton = new JButton("Add user");
        addButton.setIcon(new ImageIcon("resources/addUsers.png"));
        addButton.addActionListener(e -> new AddEmptyUserGI(this));

        removeButton = new JButton("Remove user");
        removeButton.setIcon(new ImageIcon("resources/remove.png"));
        removeButton.addActionListener(e -> {
            controller.removeUser(usersList.get(usersIndex));
            usersList.remove(usersIndex);
            viewUserInfoPanel.setVisible(false);
            userInfoPanelsList.get(usersIndex).setVisible(false);
            userInfoPanelsList.remove(usersIndex);
            tablePanel.setVisible(true);
        });

        addRemovePanel = new BoxPanel(addButton, removeButton);
    }

    private void checkButtonsEnabled() {
        nextButton.setEnabled(usersIndex != userInfoPanelsList.size() - 1);
        prevButton.setEnabled(usersIndex != 0);
        removeButton.setEnabled(usersList.get(usersIndex).getRights() != UsersRights.ADMIN);
    }
}
