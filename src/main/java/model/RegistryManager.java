package model;

import input_output.WinRegistry;

import java.lang.reflect.InvocationTargetException;

public class RegistryManager {

    public static final String KEY = "Software\\lab4\\";
    public static final String VALUE_NAME = "Signature";

    public static void createAppKey(String userName) {
        try {
            WinRegistry.createKey(
                    WinRegistry.HKEY_CURRENT_USER,
                    KEY + userName);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void writeValueToReg(String userName, String value) {
        try {
            WinRegistry.writeStringValue(
                    WinRegistry.HKEY_CURRENT_USER,
                    KEY + userName,
                    VALUE_NAME,
                    value
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static String readValueFromReg(String userName) {
        try {
            return WinRegistry.readString(
                    WinRegistry.HKEY_CURRENT_USER,
                    KEY + userName,
                    VALUE_NAME
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
