package utils;

import components.SingleMessage;
import input_output.IOFileHandling;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static final long MAX_FILE_TIME = 1293753600000L;
    public static final int MAX_YEAR = 2010;

    public static void setLastModifiedTime(Integer day, Integer month, Integer year) {
        try {
            File file = new File(IOFileHandling.USERS_SER);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String newLastModified = day + "/" + month + "/" + year;
            Date newDate = sdf.parse(newLastModified);
            if (!file.setLastModified(newDate.getTime())) {
                throw new IOException("");
            }
        } catch (ParseException | IOException e){
            SingleMessage.setWarningMessage(SingleMessage.TIME_NOT_CHANGED);
        }
    }

    public static boolean checkLastModifiedTime() {
        File file = new File(IOFileHandling.USERS_SER);
        return file.lastModified() <= MAX_FILE_TIME;
    }
}
