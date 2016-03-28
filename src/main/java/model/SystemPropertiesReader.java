package model;

import input_output.IOFileHandling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

public class SystemPropertiesReader {
    
    private static String[] properties = new String[] {
            "java.vendor.url",
            "java.class.path",
            "user.home",
            "java.class.version",
            "os.version",
            "java.vendor",
            "user.dir",
            "user.timezone",
            "path.separator",
            "os.name",
            "os.arch",
            "line.separator",
            "file.separator",
            "user.name",
            "java.version",
            "java.home",
    };

    public static void getSystemProperties() {
        Arrays.stream(properties).forEach(SystemPropertiesReader::initProperty);
        getPartition();
        systemInfo();
    }

    private static void initProperty(String property) {
        System.out.println(property + " : " + System.getProperty(property));
    }

    public static void readFileAttributes(String fileName) {
        try {
            Path path = Paths.get(fileName);
            Map<String, Object> f = Files.readAttributes(path, "*");
            f.forEach((k, v) -> System.out.println(k + " : " + v));
            System.out.println("File path : " + path.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getPartition() {
        String path = Paths.get(IOFileHandling.USERS_SER).toAbsolutePath().toString();
        System.out.println("Partition : " + path.substring(0, 3));
    }

    private static void systemInfo() {
        System.out.println("Available processors (cores): " +
                Runtime.getRuntime().availableProcessors());

        System.out.println("Free memory (bytes): " +
                Runtime.getRuntime().freeMemory());

        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("Maximum memory (bytes): " +
                (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

        System.out.println("Total memory available to JVM (bytes): " +
                Runtime.getRuntime().totalMemory());

        File[] roots = File.listRoots();

        for (File root : roots) {
            System.out.println("File system root: " + root.getAbsolutePath());
            System.out.println("Total space (bytes): " + root.getTotalSpace());
            System.out.println("Free space (bytes): " + root.getFreeSpace());
            System.out.println("Usable space (bytes): " + root.getUsableSpace());
        }
    }
}
