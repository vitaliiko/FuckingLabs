package support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordDigest {

    public static String hashPassword(char[] password) {
        return hashPassword(String.valueOf(password));
    }

    public static String hashPassword(String password) {
        MessageDigest md5 ;
        StringBuilder hexString = new StringBuilder();
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.reset();
            md5.update(password.getBytes());

            byte messageDigest[] = md5.digest();
            for (byte aMessageDigest : messageDigest) {
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            }
        }
        catch (NoSuchAlgorithmException e) {
            return e.toString();
        }
        System.out.println(password + " " + hexString.toString());
        return hexString.toString();
    }
}
