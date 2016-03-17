package coder;

import java.io.IOException;

public class CeasarCoder extends Coder {

    private static Coder instance = new CeasarCoder();

    private CeasarCoder() {}

    public static Coder getInstance() {
        return instance;
    }

    @Override
    public boolean validateKey(String key) throws IOException {
        if (key.length() > 1) {
            throw new IOException("Key length must be equal 1!!!!!(");
        }
        if (validateMessage(key)) {
            throw new IOException("Key contain danger symbol");
        }
        return true;
    }

    @Override
    protected boolean validateMessage(String message) throws IOException {
        return super.validateMessage(message);
    }

    @Override
    public String encode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        validateKey(key);
        String outputText = "";
        int alphabetPower = alphabet.length();
        int keyCharIndex = alphabet.indexOf(key);
        int outputCharIndex;

        for (char c : inputText.toCharArray()) {
            if ((int) c == 10) {
                c = (char) 32;
            }
            outputCharIndex = (alphabet.indexOf(c) + keyCharIndex) % alphabetPower;
            outputText += alphabet.charAt(outputCharIndex);
        }
        return outputText;
    }

    @Override
    public String decode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        validateKey(key);
        String outputText = "";
        int alphabetPower = alphabet.length();
        int keyCharIndex = alphabet.indexOf(key);
        int outputCharIndex;

        for (char c : inputText.toCharArray()) {
            int index = alphabet.indexOf(c) - keyCharIndex;
            outputCharIndex = (index < 0 ? index + alphabetPower : index) % alphabetPower;
            outputText += alphabet.charAt(outputCharIndex);
        }
        return outputText;
    }

    @Override
    public String toString() {
        return "Ceasar";
    }
}
