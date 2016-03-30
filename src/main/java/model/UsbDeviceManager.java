package model;

import input_output.CommandLineInterpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsbDeviceManager {

    public static final String MODEL = "WMIC diskdrive get model";
    public static final String SERIAL = "WMIC diskdrive get serialnumber";

    private static Map<String, String> serialMap = new HashMap<>();

    public static void getStorageDivcesSerials() {
        List<String> modelList = CommandLineInterpreter.sendCommand(MODEL);
        List<String> serialList = CommandLineInterpreter.sendCommand(SERIAL);
        for (int i = 1; i < modelList.size(); i++) {
            serialMap.put(serialList.get(i), modelList.get(i));
        }
    }
}
