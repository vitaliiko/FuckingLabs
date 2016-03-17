package utils;

import coder.Coder;
import user_gi.WorkspaceGI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextAreasListener implements DocumentListener {

    private JTextArea textArea;
    private JLabel label;
    private Coder coder;
    private WorkspaceGI owner;

    public TextAreasListener(WorkspaceGI owner, JTextArea textArea, JLabel label, Coder coder) {
        this.textArea = textArea;
        this.label = label;
        this.coder = coder;
        this.owner = owner;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        float entropy = coder.calculateEntropy(textArea.getText());
        label.setText("Entropy: " + String.valueOf(entropy));
        owner.checkButtonsEnabled();
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
