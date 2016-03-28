package model;

import input_output.IOFileHandling;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.prefs.Preferences;

public class RegisterManager {

    public static final String PREF_KEY = "Signature";

    public static void writeKeyToReg() {
        Controller controller = Controller.getInstance();
        controller.setUserSet(IOFileHandling.loadUsers());
        String prefKey = controller.getAdmin().getLastName() + "/Signature";

        // Write Preferences information to HKCU (HKEY_CURRENT_USER),
        // HKCUSoftwareJavaSoftPrefscom.javacoderanch
        //
        Preferences userPref = Preferences.userRoot();
        userPref.put(prefKey, "key");

        //
        // Below we read back the value we've written in the code above.
        //
        System.out.println("Preferences = "
                + userPref.get(prefKey, prefKey + " was not found."));

        //
        // Write Preferences information to HKLM (HKEY_LOCAL_MACHINE),
        // HKLMSoftwareJavaSoftPrefscom.javacoderanch
        //
//        Preferences systemPref = Preferences.systemRoot();
//        systemPref.put(PREF_KEY, "www.javacoderanch.org");

        //
        // Read back the value we've written in the code above.
        //
//        System.out.println("Preferences = "
//                + systemPref.get(PREF_KEY, PREF_KEY + " was not found."));
    }
}
