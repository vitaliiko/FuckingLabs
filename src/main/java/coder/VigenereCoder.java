package coder;

import input_output.Message;

import java.io.IOException;

public class VigenereCoder extends Coder {

    private static Coder instance = new VigenereCoder();

    private VigenereCoder() {}

    public static Coder getInstance() {
        return instance;
    }

    @Override
    public String encode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        validateKey(key);
        String outputText = "";
        int alphabetPower = alphabet.length();
        int i = 0;
        int outputCharIndex;

        for (char c : inputText.toCharArray()) {
            if ((int) c == 10) {
                c = (char) 32;
            }
            int keyCharIndex = (i < key.length() ? i : 0);
            outputCharIndex = (alphabet.indexOf(c) + alphabet.indexOf(key.charAt(keyCharIndex))) % alphabetPower;
            outputText += alphabet.charAt(outputCharIndex);
            i++;
        }
        return outputText;
    }

    @Override
    public String decode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        validateKey(key);
        String outputText = "";
        int alphabetPower = alphabet.length();
        int i = 0;
        int outputCharIndex;

        for (char c : inputText.toCharArray()) {
            int keyCharIndex = (i < key.length() ? i : 0);
            int index = alphabet.indexOf(c) - alphabet.indexOf(key.charAt(keyCharIndex));
            outputCharIndex = (index < 0 ? index + alphabetPower : index) % alphabetPower;
            outputText += alphabet.charAt(outputCharIndex);
            i++;
        }
        return outputText;
    }

    @Override
    public void validateKey(String key) throws IOException {
        if (!checkForForbiddenSymbols(key)) {
            throw new IOException(Message.FORBIDDEN_SYMBOLS_IN_KEY);
        }
    }

    @Override
    public String toString() {
        return "Vigenere";
    }
}
