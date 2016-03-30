package model;

import input_output.CommandLineInterpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsbDeviceManager {

    public static final String MODEL = "WMIC diskdrive get model";
    public static final String SERIAL = "WMIC diskdrive get serialnumber";

    private static Map<String, String> serialMap = new HashMap<>();

    public static Map<String, String> searchStorageDevicesSerials() {
        List<String> modelList = CommandLineInterpreter.sendCommand(MODEL);
        List<String> serialList = CommandLineInterpreter.sendCommand(SERIAL);
        for (int i = 1; i < modelList.size(); i++) {
            serialMap.put(serialList.get(i), modelList.get(i));
        }
        return serialMap;
    }

    public static boolean isTokenConnected(String tokenSerial) {
        return tokenSerial == null || serialMap.keySet().contains(tokenSerial);
    }

    public static String[] getDevices() {
        searchStorageDevicesSerials();
        String[] devices = new String[serialMap.size()];
        int i = 0;
        for (String value : serialMap.values()) {
            devices[i]= value;
            i++;
        }
        return devices;
    }

    public static String getDeviceSerial(String deviceModel) {
        StringBuilder serial = new StringBuilder("");
        serialMap.forEach((k, v) -> {
            if (v.equals(deviceModel)) {
                serial.append(k);
            }
        });
        return serial.toString();
    }
}
