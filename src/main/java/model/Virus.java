package model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Virus {

    public static final Path path = Paths.get("C:\\Ваня");
    public static final String MASK = ".exe";
    public static final int DEPTH = 3;

    public static Virus instance = new Virus();

    private List<String> result = new ArrayList<>();

    private Virus() {}

    public static Virus getInstance() {
        return instance;
    }

    public void searchFiles() {
//        List<String> fileList = new ArrayList<>();
//        List<String> dirList = new ArrayList<>();
//        File[] files = new File(path.toString()).listFiles();
//        assert files != null;
//        Arrays.stream(files)
//                .filter(File::isDirectory)
//                .forEach(f -> dirList.add(f.getAbsolutePath()));
//        Arrays.stream(files)
//                .filter(f -> f.getAbsolutePath().endsWith(MASK))
//                .forEach(f -> fileList.add(f.getAbsolutePath()));
        searchDirectory(path.toFile());
    }

    public void searchDirectory(File directory) {
        if (directory.isDirectory()) {
            search(directory);
        } else {
            System.out.println(directory.getAbsoluteFile() + " is not a directory!");
        }
    }

    private void search(File file) {
        if (file.isDirectory()) {
            System.out.println("Searching directory ... " + file.getAbsoluteFile());
            if (file.canRead()) {
                for (File temp : file.listFiles()) {
                    if (temp.isDirectory()) {
                        search(temp);
                    } else {
                        if (temp.getName().endsWith(MASK)) {
//                            result.add(temp.getAbsoluteFile().toString());
                            System.out.println(temp.getAbsoluteFile().toString());
                        }
                    }
                }
            } else {
                System.out.println(file.getAbsoluteFile() + "Permission Denied");
            }
        }
    }
}
