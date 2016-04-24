package model;

import input_output.IOFileHandling;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemPropertiesReader {
    
    private static String[] properties = new String[] {
            "user.home",
            "java.class.version",
            "os.version",
            "user.dir",
            "os.name",
            "os.arch",
            "user.name",
    };

    public static String getSystemProperties() {
        List<String> propertiesList = new ArrayList<>();
        Arrays.stream(properties).forEach(p -> propertiesList.add(p + ": " + System.getProperty(p)));
        propertiesList.add("partition: " + Paths.get(IOFileHandling.USERS_SER).toAbsolutePath().toString());
        propertiesList.add("available processors: " + Runtime.getRuntime().availableProcessors());
        propertiesList.add("memory: " + Runtime.getRuntime().maxMemory());

        Arrays.stream(File.listRoots()).forEach(root -> {
            long totalSpace = root.getTotalSpace();
            if (totalSpace > 0) {
                propertiesList.add(root.getAbsolutePath() + " total space: " + totalSpace);
            }
        });
        UsbDeviceManager.searchStorageDevicesSerials().keySet().forEach(propertiesList::add);

        return String.join(", ", propertiesList);
    }

    public static String getPropertiesHex() {
        return PasswordDigest.hashText(getSystemProperties());
    }

    public static boolean isPropertiesMatches(String userName) {
        String properties = RegistryManager.readValueFromReg(userName);
        assert properties != null;
        return properties.equals(getPropertiesHex());
    }
}
