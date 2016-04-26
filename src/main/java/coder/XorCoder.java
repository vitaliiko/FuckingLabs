package coder;

import components.SingleMessage;
import java.io.IOException;

public class XorCoder extends Coder {

    private final int BIN_CAPACITY = 6;
    private String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЫЬЪЭЮЯ";

    private static Coder instance = new XorCoder();

    private XorCoder() {}

    public static Coder getInstance() {
        return instance;
    }

    @Override
    public String encode(String gamma, String inputText) throws IOException {
        inputText = inputText.toUpperCase();
        gamma = gamma.toUpperCase();
        validateKey(gamma, inputText);
        validateMessage(inputText);
        String outputText = "";

        for (int i = 0; i < inputText.length(); i++) {
            byte[] binTextChar = toBinaryCode(alphabet.indexOf(inputText.charAt(i)));
            byte[] binGammaChar = toBinaryCode(alphabet.indexOf(gamma.charAt(i)));
            byte[] result = performXor(binTextChar, binGammaChar);
            outputText += alphabet.charAt(toDecimalCode(result));
        }
        return outputText;
    }

    @Override
    public String decode(String key, String inputText) throws IOException {
        return encode(key, inputText);
    }

    @Override
    public void validateKey(String key) throws IOException {
        if (!checkForForbiddenSymbols(key)) {
            if (getAlphabet().equals(RUSO_ALPHABET)) {
                throw new IOException(SingleMessage.RU_FORBIDDEN_SYMBOLS_IN_KEY);
            }
            throw new IOException(SingleMessage.FORBIDDEN_SYMBOLS_IN_KEY);
        }
    }

    public void validateKey(String key, String message) throws IOException {
        validateKey(key);
        if (key.length() != message.length()) {
            if (getAlphabet().equals(RUSO_ALPHABET)) {
                throw new IOException("Длина гаммы должна соответствовать\r\nдлине открытого сообщения");
            }
            throw new IOException("Gamma length should be equal message length");
        }
    }

    private byte[] toBinaryCode(int decNum){
        byte[] binCode = new byte[BIN_CAPACITY];

        for (int i = BIN_CAPACITY - 1; i >= 0; i--) {
            if (decNum % 2 == 1){
                binCode[i] = 1;
                decNum--;
            } else {
                binCode[i] = 0;
            }
            decNum /= 2;
        }

        return binCode;
    }

    private int toDecimalCode(byte[] binCode){
        int decCode = 0;

        for (int i = 0; i < BIN_CAPACITY; i++){
            if (binCode[BIN_CAPACITY - 1 - i] == 1) {
                decCode += Math.pow(2, i);
            }
        }

        return decCode;
    }

    private byte[] performXor(byte[] text, byte[] gamma) {
        byte[] xorResult = new byte[BIN_CAPACITY];
        for (int i = 0; i < BIN_CAPACITY; i++) {
            xorResult[i] = (byte) ((text[i] + gamma[i]) % 2);
        }
        return xorResult;
    }

    @Override
    protected boolean checkForForbiddenSymbols(String text) {
        for (String c : text.split("")) {
            int charIndex = c.charAt(0);
            if (charIndex > 27 && !alphabet.contains(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return getAlphabet().equals(Coder.RUSO_ALPHABET) ? "Гаммирование" : "XOR cipher";
    }
}
