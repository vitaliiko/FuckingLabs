package utils;

import user_gi.WorkspaceGI;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class KeyFieldListener implements DocumentListener {

    private WorkspaceGI frame;

    public KeyFieldListener(WorkspaceGI frame) {
        this.frame = frame;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        frame.checkButtonsEnabled();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        insertUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        insertUpdate(e);
    }
}
