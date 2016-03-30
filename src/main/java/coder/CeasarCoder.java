package coder;

import components.SingleMessage;

import java.io.IOException;

public class CeasarCoder extends Coder {

    private static Coder instance = new CeasarCoder();

    private CeasarCoder() {}

    public static Coder getInstance() {
        return instance;
    }

    @Override
    public void validateKey(String key) throws IOException {
        if (key.length() > 1) {
            if (getAlphabet().equals(RUSO_ALPHABET)) {
                throw new IOException("Ключ должен содержать не более одного символа");
            }
            throw new IOException("Key length should be less than 1 symbol");
        }
        if (!checkForForbiddenSymbols(key)) {
            if (getAlphabet().equals(RUSO_ALPHABET)) {
                throw new IOException(SingleMessage.RU_FORBIDDEN_SYMBOLS_IN_KEY);
            }
            throw new IOException(SingleMessage.FORBIDDEN_SYMBOLS_IN_KEY);
        }
    }

    @Override
    public String encode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        validateKey(key);
        String outputText = "";
        int alphabetPower = getAlphabet().length();
        int keyCharIndex = getAlphabet().indexOf(key);
        int outputCharIndex;

        for (char c : inputText.toCharArray()) {
            outputCharIndex = (getAlphabet().indexOf(c) + keyCharIndex) % alphabetPower;
            outputText += getAlphabet().charAt(outputCharIndex);
        }
        return outputText;
    }

    @Override
    public String decode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        validateKey(key);
        String outputText = "";
        int alphabetPower = getAlphabet().length();
        int keyCharIndex = getAlphabet().indexOf(key);
        int outputCharIndex;

        for (char c : inputText.toCharArray()) {
            int index = getAlphabet().indexOf(c) - keyCharIndex;
            outputCharIndex = (index < 0 ? index + alphabetPower : index) % alphabetPower;
            outputText += getAlphabet().charAt(outputCharIndex);
        }
        return outputText;
    }

    @Override
    public String toString() {
        return "Ceasar";
    }
}
