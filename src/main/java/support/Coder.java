package support;

import java.util.ArrayList;
import java.util.Random;

public class Coder {

    private String alphabet = "";

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public String vigenereEncoder(String key, String inputText) {
        String outputText = "";
        int alphabetPower = alphabet.length();

        int i = 0, outputCharIndex;
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

    public String vigenereDecoder(String key, String inputText) {
        String outputText = "";
        int alphabetPower = alphabet.length();

        int i = 0, outputCharIndex;
        for (char c : inputText.toCharArray()) {
            int keyCharIndex = (i < key.length() ? i : 0);
            int index = alphabet.indexOf(c) - alphabet.indexOf(key.charAt(keyCharIndex));
            outputCharIndex = (index < 0 ? index + alphabetPower : index) % alphabetPower;
            outputText += alphabet.charAt(outputCharIndex);
            i++;
        }
        return outputText;
    }

    public void createAlphabet(String inputAlphabet) {
        ArrayList<Character> alphabet = new ArrayList<>();
        Random random = new Random();
        int randomNum;

        if (inputAlphabet.isEmpty()) {
            for (int i = 97; i <= 122; i++) {
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
}
