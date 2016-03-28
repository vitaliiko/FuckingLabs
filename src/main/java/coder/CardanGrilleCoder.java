package coder;

import java.io.IOException;
import java.util.function.BiConsumer;

public class CardanGrilleCoder extends Coder {

    public static final int ARR_SIZE = 6;

    private static Coder instance = new CardanGrilleCoder();

    private byte[][] key = new byte[][] {
            {0, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0},
            {1, 0, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 1, 0},
            {1, 1, 0, 0, 0, 0}
    };
    private char[][] charsGrille = new char[ARR_SIZE][ARR_SIZE];

    private CardanGrilleCoder() {}

    public static Coder getInstance() {
        return instance;
    }

    @Override
    public void validateMessage(String message) throws IOException {
        if (message.length() < ARR_SIZE * ARR_SIZE) {
            if (checkForForbiddenSymbols(message)) {
                throw new IOException("Собщение должно содержать " + ARR_SIZE * ARR_SIZE + " символов");
            }
            throw new IOException("Message must contain " + ARR_SIZE * ARR_SIZE + " characters");
        }
    }

    @Override
    public String encode(String key, String inputText) throws IOException {
        validateMessage(inputText);
        int[] charNum = {0};
        for (int k = 0; k < 4; k++) {
            doCycle((i, j) -> {
                if (this.key[i][j] == 1) {
                    charsGrille[i][j] = inputText.charAt(charNum[0]++);
                }
            });

            byte[][] turnedKey = new byte[ARR_SIZE][ARR_SIZE];
            doCycle((i, j) -> turnedKey[j][ARR_SIZE - 1 - i] = this.key[i][j]);
            this.key = turnedKey;
        }

        String[] output = {""};
        doCycle((i, j) -> output[0] += charsGrille[i][j]);
        return output[0];
    }

    @Override
    public String decode(String key, String inputText) throws IOException {
        String[] output = {""};
        int[] charNum = {0};
        doCycle((i, j) -> charsGrille[i][j] = inputText.charAt(charNum[0]++));

        for (int k = 0; k < 4; k++) {
            doCycle((i, j) -> {
                if (this.key[i][j] == 1) {
                    output[0] += charsGrille[i][j];
                }
            });

            byte[][] turnedKey = new byte[ARR_SIZE][ARR_SIZE];
            doCycle((i, j) -> turnedKey[j][ARR_SIZE - 1 - i] = this.key[i][j]);
            this.key = turnedKey;
        }

        return output[0];
    }

    @Override
    public void validateKey(String key) throws IOException {

    }

    private void doCycle(BiConsumer<Integer, Integer> consumer) {
        for (int i = 0; i < ARR_SIZE; i++) {
            for (int j = 0; j < ARR_SIZE; j++) {
                consumer.accept(i, j);
            }
        }
    }

    @Override
    public String toString() {
        return "Cardan Grille";
    }
}
