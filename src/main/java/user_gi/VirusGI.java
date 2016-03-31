package user_gi;

import model.Virus;
import frame_utils.FrameUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.List;

public class VirusGI extends JFrame {

    private JTextArea filesNamesArea;
    private JLabel infoLabel;

    public VirusGI() throws HeadlessException {
        super("Virus");

        FrameUtils.setLookAndFeel();

        prepareComponents();
        getContentPane().add(infoLabel, BorderLayout.NORTH);
        getContentPane().add(FrameUtils.createWithHorizontalScroll(filesNamesArea), BorderLayout.CENTER);

        setupFrame();
        searchFiles();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(400, 600));
        setIconImage(new ImageIcon("resources/virus.png").getImage());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareComponents() {
        filesNamesArea = new JTextArea();
        filesNamesArea.setFont(FrameUtils.ARIAL_12);
        filesNamesArea.setEditable(false);
        infoLabel = new JLabel(Virus.DIRECTORY_WITH_EXE_FILES.toString());
        infoLabel.setBorder(new EmptyBorder(8, 8, 8, 8));
    }

    private void searchFiles() {
        List<String> filesList = Virus.searchFiles();
        filesList.forEach(f -> filesNamesArea.append(f + "\r\n"));
        infoLabel.setText(infoLabel.getText() + "\\: found " + filesList.size() + " files");
    }
}
