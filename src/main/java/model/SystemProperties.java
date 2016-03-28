package model;

import input_output.IOFileHandling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class SystemProperties {

    public static void getSystemProperties() {
        initProperty("java.vendor.url");
        initProperty("java.class.path");
        initProperty("user.home");
        initProperty("java.class.version");
        initProperty("os.version");
        initProperty("java.vendor");
        initProperty("user.dir");
        initProperty("user.timezone");
        initProperty("path.separator");
        initProperty("os.name");
        initProperty("os.arch");
        initProperty("line.separator");
        initProperty("file.separator");
        initProperty("user.name");
        initProperty("java.version");
        initProperty("java.home");
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

    /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " +
                Runtime.getRuntime().freeMemory());

    /* This will return Long.MAX_VALUE if there is no preset limit */
        long maxMemory = Runtime.getRuntime().maxMemory();
    /* Maximum amount of memory the JVM will attempt to use */
        System.out.println("Maximum memory (bytes): " +
                (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

    /* Total memory currently available to the JVM */
        System.out.println("Total memory available to JVM (bytes): " +
                Runtime.getRuntime().totalMemory());

    /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();

    /* For each filesystem root, print some info */
        for (File root : roots) {
            System.out.println("File system root: " + root.getAbsolutePath());
            System.out.println("Total space (bytes): " + root.getTotalSpace());
            System.out.println("Free space (bytes): " + root.getFreeSpace());
            System.out.println("Usable space (bytes): " + root.getUsableSpace());
        }
    }
}
