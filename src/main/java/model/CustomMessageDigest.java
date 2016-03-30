package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomMessageDigest {

    public static String hashText(char[] text) {
        return hashText(String.valueOf(text));
    }

    public static String hashText(String text) {
        MessageDigest md5 ;
        StringBuilder hexString = new StringBuilder();
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.reset();
            md5.update(text.getBytes());

            byte messageDigest[] = md5.digest();
            for (byte aMessageDigest : messageDigest) {
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            }
        }
        catch (NoSuchAlgorithmException e) {
            return e.toString();
        }
        return hexString.toString();
    }
}
