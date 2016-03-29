package model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Virus {

    public static final Path DIRECTORY_PATH = Paths.get("C:", "Program Files");
    public static final int MAX_DEPTH = 3;

    private static List<String> filesNamesList = new ArrayList<>();
    private static Predicate<File> predicate = f -> f.getName().endsWith(".exe");
    private static int depth = 0;

    public static List<String> searchFiles() {
        search(DIRECTORY_PATH.toFile());
        return filesNamesList;
    }

    private static void search(File file) {
        if (file.isDirectory() && file.canRead()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                Arrays.stream(files).forEach(Virus::enterFile);
            }
        }
    }

    private static void enterFile(File f) {
        if (f.isDirectory()) {
            if (depth < MAX_DEPTH) {
                depth++;
                search(f);
                depth--;
            }
        } else if (predicate.test(f)) {
            if (f.getAbsolutePath().split("/").length > 5) {
                System.out.println(f.getAbsolutePath());
            }
            filesNamesList.add(f.getAbsolutePath());
        }
    }
}
