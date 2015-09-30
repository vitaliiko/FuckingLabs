package GI;

import support.Controller;
import support.User;

import javax.swing.*;
import java.awt.*;

public class SettingsGI extends JDialog {

    private User user;
    private Controller controller;

    public SettingsGI(Frame owner, User user, Controller controller) {
        super(owner);
        this.user = user;
        this.controller = controller;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
