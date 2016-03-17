package components;

import model.User;
import model.UsersRights;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UserInfoPanel extends JPanel {

    private JComboBox<Object> rightsBox;
    private User user;

    public UserInfoPanel(User user) {
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
        });
    }
}

