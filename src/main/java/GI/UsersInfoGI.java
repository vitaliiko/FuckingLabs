package GI;

import support.Controller;
import support.Message;
import support.User;
import support.UsersRights;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
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
    private JPanel cancelPanel;
    private JPanel buttonsPanel;
    private JButton prevButton;
    private JButton nextButton;
    private JButton backButton;
    private JButton addButton;
    private JButton showInfoButton;
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
        prepareCancelButtonPanel();
        getContentPane().add(cancelPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(500, 400));
        setIconImage(new ImageIcon("resources/table.png").getImage());
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
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        prepareShowButton();
        tablePanel.add(showInfoButton);
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
        usersTable.setSize(new Dimension(450, 300));
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
        showInfoButton.addActionListener(e -> {
            usersIndex = usersTable.getSelectedRow();
            tablePanel.setVisible(false);
            viewUserInfoPanel.setVisible(true);
            userInfoPanelsList.get(usersIndex).setVisible(true);
            checkButtonsEnabled();
            messageLabel.setText(Message.USER_INFO);
        });
    }

    public void prepareCancelButtonPanel() {
        cancelPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setHorizontalAlignment(JButton.LEFT);
        cancelButton.setVerticalAlignment(JButton.BOTTOM);
        cancelButton.addActionListener(e -> dispose());
        cancelPanel.add(cancelButton);
    }

    public void prepareUserInfoPanel() {
        viewUserInfoPanel = new JPanel();
        viewUserInfoPanel.setVisible(false);
        viewUserInfoPanel.setLayout(new BoxLayout(viewUserInfoPanel, BoxLayout.Y_AXIS));

        prepareButtonsPanel();
        viewUserInfoPanel.add(buttonsPanel);

        userInfoPanelsList.forEach(viewUserInfoPanel::add);
    }

    public void prepareButtonsPanel() {
        buttonsPanel = new JPanel();

        prevButton= new JButton("Prev");
        prevButton.setIcon(new ImageIcon("resources/prev.png"));
        prevButton.setHorizontalAlignment(JButton.LEFT);
        prevButton.addActionListener(e -> {
            userInfoPanelsList.get(usersIndex).setVisible(false);
            userInfoPanelsList.get(--usersIndex).setVisible(true);
            checkButtonsEnabled();
        });
        buttonsPanel.add(prevButton);

        backButton = new JButton("Back");
        backButton.setHorizontalAlignment(JButton.CENTER);
        backButton.addActionListener(e -> {
            viewUserInfoPanel.setVisible(false);
            userInfoPanelsList.get(usersIndex).setVisible(false);
            tablePanel.setVisible(true);
            messageLabel.setText(Message.USER_LIST);
            usersIndex = 0;

        });
        buttonsPanel.add(backButton);

        nextButton = new JButton("Next");
        nextButton.setIcon(new ImageIcon("resources/next.png"));
        nextButton.setHorizontalTextPosition(SwingConstants.LEFT);
        nextButton.setHorizontalAlignment(JButton.RIGHT);
        nextButton.addActionListener(e -> {
            userInfoPanelsList.get(usersIndex).setVisible(false);
            userInfoPanelsList.get(++usersIndex).setVisible(true);
            checkButtonsEnabled();
        });
        buttonsPanel.add(nextButton);
    }

    public void checkButtonsEnabled() {
        nextButton.setEnabled(usersIndex != userInfoPanelsList.size() - 1);
        prevButton.setEnabled(usersIndex != 0);
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

        public void initPanel() {
            setLayout(new GridLayout(6, 2));
            setVisible(false);

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

        public void prepareRightsBox() {
            String[] items = UsersRights.getItems();
            rightsBox = new JComboBox<>(items);
            rightsBox.setSelectedIndex(user.getRights());
            rightsBox.setEnabled(user.getRights() != UsersRights.ADMIN && user.getRights() != UsersRights.EMPTY);
            rightsBox.addItemListener(e -> {
                if (rightsBox.getSelectedIndex() == UsersRights.ADMIN) {
                    rightsBox.setSelectedIndex(user.getRights());
                }
                if (user.getRights() != UsersRights.EMPTY && user.getRights() != UsersRights.EMPTY_SIMPLE_PASSWORD) {
                    if (rightsBox.getSelectedIndex() == UsersRights.EMPTY ||
                            rightsBox.getSelectedIndex() == UsersRights.EMPTY_SIMPLE_PASSWORD) {
                        rightsBox.setSelectedIndex(user.getRights());
                    }
                }
                if (user.getRights() == UsersRights.EMPTY || user.getRights() == UsersRights.EMPTY_SIMPLE_PASSWORD) {
                    if (rightsBox.getSelectedIndex() != UsersRights.EMPTY &&
                            rightsBox.getSelectedIndex() != UsersRights.EMPTY_SIMPLE_PASSWORD) {
                        rightsBox.setSelectedIndex(user.getRights());
                    }
                }
                user.setRights(rightsBox.getSelectedIndex());
            });
        }
    }
}
