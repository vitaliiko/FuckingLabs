package coder;

import java.util.ArrayList;

abstract public class Mixer {

    DataArrays data = new DataArrays();
    ArrayList<Integer> primeNumbers = new ArrayList<>();

    public int primeNumber(int finNum) {
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

    public int nextPrimeNumber(){
        int nextPrime = primeNumbers.get(primeNumbers.size() - 1);

        int i = 1;
        while (i > 0) {
            nextPrime++;
            i = 0;
            for (int j = 0; j < primeNumbers.size(); j++) {
                if (nextPrime % primeNumbers.get(j) == 0){
                    i++;
                }
            }
        }

        return nextPrime;
    }

    public void sequenceGenerator(int mesLength){
        primeNumber(mesLength);

        int primeNumber = nextPrimeNumber();
        int i = 3;

        do {
            data.getSequence().clear();
            data.setSequence(1);

            for (int j = 0; j < primeNumber - 2; j++) {
                data.setSequence((data.getSequence(j) * i) % primeNumber);
            }

            i++;
        } while (data.getSequence().lastIndexOf(1) != 0);
    }

    public String encoder(String textLine){
        char[] encodedLine = new char[textLine.length()];

        sequenceGenerator(textLine.length());

        for (int i = 0; i < textLine.length(); i++){
            if (data.getSequence(i) <= textLine.length()){
                encodedLine[data.getSequence(i) - 1] = textLine.charAt(i);
            } else {
                encodedLine[data.getSequence(data.getSequence(i) - 1) - 1] = textLine.charAt(i);
            }
        }

        return String.valueOf(encodedLine);
    }

    public String[] encoder(String[] textLine){
        String[] encodedLine = new String[textLine.length];

        sequenceGenerator(textLine.length);

        for (int i = 0; i < textLine.length; i++){
            if (data.getSequence(i) <= textLine.length){
                encodedLine[data.getSequence(i) - 1] = textLine[i];
            } else {
                encodedLine[data.getSequence(data.getSequence(i) - 1) - 1] = textLine[i];
            }
        }

        return encodedLine;
    }

    public String decoder(String encodedText){
        String decodedText = "";

        sequenceGenerator(encodedText.length());

        for (int i = 0; i < encodedText.length(); i++){
            if (data.getSequence(i) <= encodedText.length()){
                decodedText += encodedText.charAt(data.getSequence(i) - 1);
            } else {
                decodedText += encodedText.charAt(data.getSequence(data.getSequence(i) - 1) - 1);
            }
        }

        return decodedText;
    }

    public String[] decoder(String[] encodedText){
        String[] decodedText = new String[encodedText.length];

        sequenceGenerator(encodedText.length);

        for (int i = 0; i < encodedText.length; i++){
            if (data.getSequence(i) <= encodedText.length){
                decodedText[i] = encodedText[data.getSequence(i) - 1];
            } else {
                decodedText[i] = encodedText[data.getSequence(data.getSequence(i) - 1) - 1];
            }
        }

         return decodedText;
    }

    public abstract void performedPermutation();
}
