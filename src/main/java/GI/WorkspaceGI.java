package GI;

import support.User;

import javax.swing.*;
import java.awt.*;

public class WorkspaceGI extends JFrame {

    private User user;

    public WorkspaceGI(User user) throws HeadlessException {
        this.user = user;
    }
}
