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
    private char[][] grille = new char[ARR_SIZE][ARR_SIZE];

    private CardanGrilleCoder() {}

    public static Coder getInstance() {
        return instance;
    }

    @Override
    public String encode(String key, String inputText) throws IOException {
        int charNum = 0;
        for (int k = 0; k < 4; k++) {
            for (int i = 0; i < ARR_SIZE; i++) {
                for (int j = 0; j < ARR_SIZE; j++) {
                    if (this.key[i][j] == 1) {
                        grille[i][j] = inputText.charAt(charNum++);
                    }
                }
            }
            turnArray();
        }
        String output = "";
        for (int i = 0; i < ARR_SIZE; i++) {
            for (int j = 0; j < ARR_SIZE; j++) {
                output += grille[i][j];
            }
        }
        return output;
    }

    @Override
    public String decode(String key, String inputText) throws IOException {
        return null;
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

    private void turnArray() {
        byte[][] turnedKey = new byte[ARR_SIZE][ARR_SIZE];
        for (int i = 0; i < ARR_SIZE; i++) {
            for (int j = 0; j < ARR_SIZE; j++) {
                turnedKey[j][ARR_SIZE - 1 - i] = key[i][j];
            }
        }
        key = turnedKey;
    }

    @Override
    public String toString() {
        return "Cardan Grille";
    }
}
