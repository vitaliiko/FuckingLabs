package coder;

public class KeyGenerator {

    public static String generateKey(String charSet, int charCount) {
        String password = "";
        char[] charList = charSet.toCharArray();
        int range = charSet.length();
        for (int i = 0; i < charCount; i++) {
            int randomNum = (int) (Math.random() * range);
            password += charList[randomNum];
        }
        return password;
    }
}
