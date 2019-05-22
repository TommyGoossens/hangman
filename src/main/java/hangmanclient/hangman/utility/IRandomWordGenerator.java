package hangmanclient.hangman.utility;

import java.util.List;

public interface IRandomWordGenerator {
    boolean wordIsCompletelyGuessed(List<String> guessedCorrectLetters);

    void generateNewWord();
}
