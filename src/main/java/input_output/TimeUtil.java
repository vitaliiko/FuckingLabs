package input_output;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static final long MAX_FILE_TIME = 978220800000L;

    public static void setLastModified(Integer day, Integer month, Integer year) {
        try {
            File file = new File(IOFileHandling.USERS_SER);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String newLastModified = day + "/" + month + "/" + year;
            Date newDate = sdf.parse(newLastModified);
            if (!file.setLastModified(newDate.getTime())) {
                throw new IOException("");
            }
        } catch(ParseException | IOException e){
            JOptionPane.showConfirmDialog(null, "Creation time doesn't changed", "ACHTUNG!",
                    JOptionPane.OK_CANCEL_OPTION);
        }
    }

    public static boolean checkLastModified() {
        File file = new File(IOFileHandling.USERS_SER);
        System.out.println(file.lastModified());
        return file.lastModified() <= MAX_FILE_TIME;
    }
}
