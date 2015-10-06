package GI;

import support.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UsersInfoGI extends JFrame {

    private Controller controller;
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
    private JLabel messageLabel;
    private JTable usersTable;

    public UsersInfoGI(Controller controller) throws HeadlessException {
        super("Users");
        this.controller = controller;
        usersList = new ArrayList<>(controller.getUserSet());
        userInfoPanelsList = new ArrayList<>();
        userInfoPanelsList.addAll(usersList.stream().map(UserInfoPanel::new).collect(Collectors.toList()));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        messageLabel = Message.prepareMessageLabel(Message.USER_LIST);
        getContentPane().add(messageLabel, BorderLayout.NORTH);

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

    public void prepareCenterPanel() {
        centerPanel = new JPanel();
        prepareTablePanel();
        centerPanel.add(tablePanel);
        prepareUserInfoPanel();
        centerPanel.add(viewUserInfoPanel);
    }

    public void prepareTablePanel() {
        tablePanel = new BoxPanel(BoxLayout.Y_AXIS);

        prepareShowButton();
        tablePanel.add(new BoxPanel(showInfoButton));

        prepareUsersTable();
        tablePanel.add(new JScrollPane(usersTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
    }

    public void prepareUsersTable() {
        TableModel tableModel = new UsersTableModel();
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

    public void prepareShowButton() {
        showInfoButton = new JButton("Show info");
        showInfoButton.setEnabled(false);
        showInfoButton.setHorizontalAlignment(JButton.CENTER);
        showInfoButton.addActionListener(e -> {
            tablePanel.setVisible(false);
            viewUserInfoPanel.setVisible(true);
            usersIndex = usersTable.getSelectedRow();
            userInfoPanelsList.get(usersIndex).setVisible(true);
            checkButtonsEnabled();
            messageLabel.setText(Message.USER_INFO);
        });
    }

    public void prepareSaveCancelPanel() {
        saveCancelPanel = new JPanel();

        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e -> {
            saveButton.setEnabled(false);
            IOFileHandling.saveUsersSet(controller.getUserSet());
        });
        saveCancelPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        saveCancelPanel.add(cancelButton);
    }

    public void prepareUserInfoPanel() {
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

    public void prepareNavigationPanel() {
        prevButton= new JButton("Prev");
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
            messageLabel.setText(Message.USER_LIST);
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

    public void prepareAddRemovePanel() {
        JButton addButton = new JButton("Add user");
        addButton.setIcon(new ImageIcon("resources/addUsers.png"));
        addButton.addActionListener(e -> {
            int usersCount = controller.getUserSet().size();
            new AddEmptyUserGI(this, controller);
            if (usersCount < controller.getUserSet().size()) {
                saveButton.setEnabled(true);
            }
        });

        removeButton = new JButton("Remove user");
        removeButton.setIcon(new ImageIcon("resources/remove.png"));
        removeButton.addActionListener(e -> {
            controller.removeUser(usersList.get(usersIndex));
            usersList.remove(usersIndex);
            viewUserInfoPanel.setVisible(false);
            userInfoPanelsList.get(usersIndex).setVisible(false);
            userInfoPanelsList.remove(usersIndex);
            tablePanel.setVisible(true);
            saveButton.setEnabled(true);
        });

        addRemovePanel = new BoxPanel(addButton, removeButton);
    }

    public void checkButtonsEnabled() {
        nextButton.setEnabled(usersIndex != userInfoPanelsList.size() - 1);
        prevButton.setEnabled(usersIndex != 0);
        removeButton.setEnabled(usersList.get(usersIndex).getRights() != UsersRights.ADMIN);
    }

    public class UsersTableModel implements TableModel {

        private String[] items = {"Name", "Surname", "Username", "Account type"};

        @Override
        public int getRowCount() {
            return controller.getUserSet().size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return items[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: return usersList.get(rowIndex).getName();
                case 1: return usersList.get(rowIndex).getSurname();
                case 2: return usersList.get(rowIndex).getUserName();
                case 3: return UsersRights.accountType(usersList.get(rowIndex).getRights());
                default: return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

    public class UserInfoPanel extends JPanel {

        private JComboBox<Object> rightsBox;
        private User user;

        public UserInfoPanel(User user) {
            super();
            this.user = user;
            initPanel();
        }

        private void initPanel() {
            setLayout(new GridLayout(6, 2));
            setVisible(false);
            setBorder(new EmptyBorder(10, 0, 10, 0));
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    user.setRights(rightsBox.getSelectedIndex());
                }
            });

            add(new JLabel("Account type: "));
            prepareRightsBox();
            add(rightsBox);

            add(new JLabel("Name: "));
            add(new JLabel(user.getName()));

            add(new JLabel("Surname: "));
            add(new JLabel(user.getSurname()));

            add(new JLabel("Username: "));
            add(new JLabel(user.getUserName()));

            add(new JLabel("Telephone: "));
            add(new JLabel(user.getTelephoneNum()));

            add(new JLabel("E-mail address: "));
            add(new JLabel(user.getMailAddress()));
        }

        private void prepareRightsBox() {
            String[] items = UsersRights.getItems();
            rightsBox = new JComboBox<>(items);
            rightsBox.setSelectedIndex(user.getRights());
            rightsBox.setEnabled(user.getRights() != UsersRights.ADMIN);
            rightsBox.addItemListener(e -> {
                int selectedRights = rightsBox.getSelectedIndex();
                if (selectedRights == UsersRights.ADMIN) {
                    rightsBox.setSelectedIndex(user.getRights());
                }
                if (user.getRights() != UsersRights.EMPTY && user.getRights() != UsersRights.EMPTY_SIMPLE_PASSWORD) {
                    if (selectedRights == UsersRights.EMPTY ||
                            rightsBox.getSelectedIndex() == UsersRights.EMPTY_SIMPLE_PASSWORD) {
                        rightsBox.setSelectedIndex(user.getRights());
                    }
                }
                if (user.getRights() == UsersRights.EMPTY || user.getRights() == UsersRights.EMPTY_SIMPLE_PASSWORD) {
                    if (selectedRights != UsersRights.EMPTY &&
                            rightsBox.getSelectedIndex() != UsersRights.EMPTY_SIMPLE_PASSWORD) {
                        rightsBox.setSelectedIndex(user.getRights());
                    }
                }
                saveButton.setEnabled(true);
            });
        }
    }
}
