package coder;

import input_output.Message;

import java.io.IOException;

public class VerrnamCoder extends Coder {

    private static Coder instance = new VerrnamCoder();

    private VerrnamCoder() {}

    public static Coder getInstance() {
        return instance;
    }

    @Override
    public String encode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        validateKey(key, inputText);
        String outputText = "";
        int alphabetPower = getAlphabet().length();
        int outputCharIndex;

        for (int i = 0; i < inputText.length(); i++) {
            char keyChar = key.charAt(i);
            char textChar = inputText.charAt(i);
            if ((int) keyChar == 10) {
                keyChar = (char) 32;
            }
            outputCharIndex = (getAlphabet().indexOf(textChar) + getAlphabet().indexOf(keyChar)) % alphabetPower;
            outputText += getAlphabet().charAt(outputCharIndex);
        }
        return outputText;
    }

    @Override
    public String decode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        validateKey(key, inputText);
        String outputText = "";
        int alphabetPower = getAlphabet().length();
        int outputCharIndex;

        for (int i = 0; i < inputText.length(); i++) {
            char keyChar = key.charAt(i);
            char textChar = inputText.charAt(i);

            int index = getAlphabet().indexOf(textChar) - getAlphabet().indexOf(keyChar);
            outputCharIndex = (index < 0 ? index + alphabetPower : index) % alphabetPower;
            outputText += getAlphabet().charAt(outputCharIndex);
        }
        return outputText;
    }

    @Override
    public void validateKey(String key) throws IOException {
        if (!checkForForbiddenSymbols(key)) {
            throw new IOException(Message.FORBIDDEN_SYMBOLS_IN_KEY);
        }
    }

    public void validateKey(String key, String message) throws IOException {
        validateKey(key);
        if (key.length() != message.length()) {
            throw new IOException("Key length should be equal message length");
        }
    }

    @Override
    public String toString() {
        return "Verrnam";
    }
}
