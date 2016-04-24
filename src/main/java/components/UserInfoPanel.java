package components;

import model.User;
import model.UserRights;

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

        add(new JLabel("Тип учетной записи: "));
        prepareRightsBox();
        add(rightsBox);

        add(new JLabel("Имя: "));
        add(new JLabel(user.getFirstName()));

        add(new JLabel("Фамилия: "));
        add(new JLabel(user.getLastName()));

        add(new JLabel("Логин: "));
        add(new JLabel(user.getLogin()));

        add(new JLabel("Телефон: "));
        add(new JLabel(user.getTelephoneNum()));

        add(new JLabel("E-mail: "));
        add(new JLabel(user.getMailAddress()));
    }

    private void prepareRightsBox() {
        String[] items = UserRights.getItems();
        rightsBox = new JComboBox<>(items);
        rightsBox.setSelectedIndex(user.getRights());
        rightsBox.setEnabled(user.getRights() != UserRights.ADMIN);
        rightsBox.addItemListener(e -> {
            int selectedRights = rightsBox.getSelectedIndex();
            if (selectedRights == UserRights.ADMIN) {
                rightsBox.setSelectedIndex(user.getRights());
            }
            if (user.getRights() != UserRights.EMPTY && user.getRights() != UserRights.EMPTY_SIMPLE_PASSWORD) {
                if (selectedRights == UserRights.EMPTY ||
                        rightsBox.getSelectedIndex() == UserRights.EMPTY_SIMPLE_PASSWORD) {
                    rightsBox.setSelectedIndex(user.getRights());
                }
            }
            if (user.getRights() == UserRights.EMPTY || user.getRights() == UserRights.EMPTY_SIMPLE_PASSWORD) {
                if (selectedRights != UserRights.EMPTY &&
                        rightsBox.getSelectedIndex() != UserRights.EMPTY_SIMPLE_PASSWORD) {
                    rightsBox.setSelectedIndex(user.getRights());
                }
            }
        });
    }
}

