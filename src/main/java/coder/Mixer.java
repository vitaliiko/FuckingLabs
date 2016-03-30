package coder;

import java.util.ArrayList;

public class Mixer {

    private static ArrayList<Integer> sequence = new ArrayList<>();
    private static ArrayList<Integer> primeNumbers = new ArrayList<>();

    private static int getPrimeNumber(int finNum) {
        for (int i = 2; i <= finNum; i++) {
            primeNumbers.add(i);
        }

        int i = 0;
        while (i < primeNumbers.size()) {
            int j = i + 1;
            while (j < primeNumbers.size()) {
                if (primeNumbers.get(j) % primeNumbers.get(i) == 0) {
                    primeNumbers.remove(j);
                } else {
                    j++;
                }
            }
            i++;
        }

        return primeNumbers.get(primeNumbers.size() - 1);
    }

    private static int getNextPrimeNumber(){
        int nextPrime = primeNumbers.get(primeNumbers.size() - 1);

        int i = 1;
        while (i > 0) {
            nextPrime++;
            i = 0;
            for (Integer primeNumber : primeNumbers) {
                if (nextPrime % primeNumber == 0) {
                    i++;
                }
            }
        }

        return nextPrime;
    }

    private static void generateSequence(int mesLength){
        getPrimeNumber(mesLength);

        int primeNumber = getNextPrimeNumber();
        int i = 3;

        do {
            sequence.clear();
            sequence.add(1);

            for (int j = 0; j < primeNumber - 2; j++) {
                sequence.add((sequence.get(j) * i) % primeNumber);
            }

            i++;
        } while (sequence.lastIndexOf(1) != 0);
    }

    public static String encode(String textLine){
        char[] encodedLine = new char[textLine.length()];

        generateSequence(textLine.length());

        for (int i = 0; i < textLine.length(); i++){
            if (sequence.get(i) <= textLine.length()){
                encodedLine[sequence.get(i) - 1] = textLine.charAt(i);
            } else {
                encodedLine[sequence.get(sequence.get(i) - 1) - 1] = textLine.charAt(i);
            }
        }

        return String.valueOf(encodedLine);
    }

    public static String decode(String encodedText){
        String decodedText = "";

        generateSequence(encodedText.length());

        for (int i = 0; i < encodedText.length(); i++){
            if (sequence.get(i) <= encodedText.length()){
                decodedText += encodedText.charAt(sequence.get(i) - 1);
            } else {
                decodedText += encodedText.charAt(sequence.get(sequence.get(i) - 1) - 1);
            }
        }

        return decodedText;
    }
}
