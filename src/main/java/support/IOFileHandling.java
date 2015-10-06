package support;

import javax.swing.*;
import java.io.*;
import java.util.Set;

import static java.lang.System.out;

public class IOFileHandling {

    private final static String USERS_SER = "IOFiles/users.ser";
    public final static String SOURCE_FILE_NAME = "sourceText.txt";
    public final static String ENCODED_FILE_NAME = "encodedText";

    public static void saveUsersSet(Set<User> usersSet) {
        try{
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(USERS_SER));
            os.writeObject(usersSet);
            os.close();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Error when saving data base file", "ACHTUNG!",
                    JOptionPane.DEFAULT_OPTION);
        }
    }

    public static Set<User> loadUsersSet() {
        Set<User> usersSet = null;
        ObjectInputStream is;
        try {
            is = new ObjectInputStream(new FileInputStream(USERS_SER));
            usersSet = (Set<User>) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showConfirmDialog(null, "Error when reading data base file", "ACHTUNG!",
                    JOptionPane.DEFAULT_OPTION);
        }
        return usersSet;
    }

    public static String readFromFile(String fileName) {
        String interLine, finalLine = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((interLine = reader.readLine()) != null) {
                finalLine = finalLine + " " + interLine.toLowerCase();
            }
            reader.close();
        } catch (IOException e) {
            out.println(e);
        }
        return finalLine;
    }

    public static void writeToFile(String outputLine, String fileName){
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(outputLine);
            writer.close();
        } catch (IOException e) {
            out.println(e);
        }
    }
}
