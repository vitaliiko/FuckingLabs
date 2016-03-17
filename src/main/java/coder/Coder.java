package coder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class Coder {

    protected String alphabet = "";

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public abstract String encode(String key, String inputText) throws IOException;

    public abstract String decode(String key, String inputText) throws IOException;

    public void createAlphabet(String inputAlphabet) {
        ArrayList<Character> alphabet = new ArrayList<>();
        Random random = new Random();
        int randomNum;

        if (inputAlphabet.isEmpty()) {
            for (int i = 65; i <= 122; i++) {
                alphabet.add((char) i);
            }
            for (int i = 48; i <= 57; i++) {
                alphabet.add((char) i);
            }
            alphabet.add(' ');
            alphabet.add('.');
            alphabet.add(',');
            alphabet.add('-');
            alphabet.add('\'');
        } else {
            for (char c : inputAlphabet.toCharArray()) {
                alphabet.add(c);
            }
        }
        this.alphabet = "";
        while (alphabet.size() > 0) {
            randomNum = random.nextInt(alphabet.size());
            this.alphabet += alphabet.remove(randomNum);
        }
    }

    public float calculateEntropy(String line){
        float x, entropy = 0;
        int count;
        ArrayList<Integer> charCount = new ArrayList<>();
        ArrayList<Character> charList = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            if (!charList.contains(line.charAt(i))) {
                charList.add(line.charAt(i));
                count = 1;
                for (int j = i + 1; j < line.length(); j++) {
                    if (line.charAt(i) == line.charAt(j)) {
                        count++;
                    }
                }
                charCount.add(count);
            }
        }
        for (Integer aCharCount : charCount) {
            x = aCharCount;
            x /= line.length();
            entropy += (float) (-1) * (x * (Math.log(x) / Math.log(2)));
        }
        return entropy;
    }

    @Override
    public abstract String toString();
}
