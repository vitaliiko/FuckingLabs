package GI;

import support.Controller;
import support.Message;
import support.User;
import support.UsersRights;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsGI extends JDialog {

    private static final int COLUMNS_COUNT = 24;

    private User user;
    private Controller controller;

    private JPanel fieldsPanel;
    private JPanel passwordPanel;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField usernameField;
    private JTextField telephoneField;
    private JTextField mailField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField repeatPasswordField;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel messageLabel;

    public SettingsGI(Frame owner, User user, Controller controller) {
        super(owner);
        this.user = user;
        this.controller = controller;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        prepareMessageLabel();
        getContentPane().add(messageLabel, BorderLayout.NORTH);
        prepareFieldsPanel();
        getContentPane().add(fieldsPanel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        prepareSaveButton();
        buttonsPanel.add(saveButton);
        prepareCancelButton();
        buttonsPanel.add(cancelButton);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(335, 400));
        setIconImage(new ImageIcon("resources/settings.png").getImage());
        setTitle("Settings");
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void prepareMessageLabel() {
        messageLabel = new JLabel(Message.SETTINGS);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(new EmptyBorder(8, 0, 8, 0));
        messageLabel.setFont(new Font("times new roman", Font.PLAIN, 16));
    }

    public void prepareFieldsPanel() {
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));

        fieldsPanel.add(
                new LabelComponentPanel("Account type: ",
                new JLabel(UsersRights.accountType(user.getRights()))
        ));

        nameField = new JTextField(user.getName(), COLUMNS_COUNT);
        if (user.getRights() == UsersRights.LOCK_USERNAME) {
            nameField.setEnabled(false);
        }
        //nameField.getDocument().addDocumentListener(new SignUpTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Your name: ", nameField));

        surnameField = new JTextField(user.getSurname(), COLUMNS_COUNT);
        //surnameField.getDocument().addDocumentListener(new SignUpTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Your surname: ", surnameField));

        usernameField = new JTextField(user.getUserName(), COLUMNS_COUNT);
        //usernameField.getDocument().addDocumentListener(new SignUpTypeListener());
        fieldsPanel.add(new LabelComponentPanel("Username: ", usernameField));

        telephoneField = new JTextField(user.getTelephoneNum(), COLUMNS_COUNT);
        fieldsPanel.add(new LabelComponentPanel("Your telephone: ", telephoneField));

        mailField = new JTextField(user.getMailAddress(), COLUMNS_COUNT);
        fieldsPanel.add(new LabelComponentPanel("Your e-mail: ", mailField));

        preparePasswordPanel();
        fieldsPanel.add(passwordPanel);
    }

    public void preparePasswordPanel() {
        passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setBorder(new EmptyBorder(8, 0, 0, 0));
        passwordPanel.add(new JSeparator());

        JLabel label = new JLabel("Change your password");
        label.setHorizontalAlignment(JLabel.CENTER);
        passwordPanel.add(label);

        currentPasswordField = new JPasswordField(COLUMNS_COUNT);
        passwordPanel.add(new LabelComponentPanel("Current password: ", currentPasswordField));

        newPasswordField = new JPasswordField(COLUMNS_COUNT);
        //newPasswordField.getDocument().addDocumentListener(new SignUpTypeListener());
        passwordPanel.add(new LabelComponentPanel("New password: ", newPasswordField));

        repeatPasswordField = new JPasswordField(COLUMNS_COUNT);
        //repeatPasswordField.getDocument().addDocumentListener(new SignUpTypeListener());
        passwordPanel.add(new LabelComponentPanel("Repeat new password: ", repeatPasswordField));
    }

    public void prepareSaveButton() {
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e -> {

        });
    }

    public void prepareCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {

        });
    }
}
