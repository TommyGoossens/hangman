package hangmanshared.utility;

import java.util.ArrayList;
import java.util.List;

public class GuessChecker {

    public GuessChecker() {
    }

    public List<String> getCorrectLetters(List<String> currentLetters, String word, String input) {
        List<String> newLetters = currentLetters;

        input = input.toUpperCase();
        List<Integer> correctIndexes = new ArrayList<>();
        int index = word.indexOf(input);
        while (index >= 0) {
            correctIndexes.add(index);
            index = word.indexOf(input, index + 1);
        }

        for (int i = 0; i < correctIndexes.size(); i++) {
            newLetters.set(correctIndexes.get(i), input);
        }

        return newLetters;
    }
}
