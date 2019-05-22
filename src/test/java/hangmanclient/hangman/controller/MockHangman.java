package hangmanclient.hangman.controller;

import hangmanclient.hangman.service.IHangmanService;
import hangmanclient.hangman.utility.IRandomWordGenerator;
import hangmanshared.models.Hangman;
import hangmanshared.models.Player;
import hangmanshared.utility.GuessChecker;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class MockHangman implements IHangman {
    //In single player mode this will return a new word
    private IRandomWordGenerator aiWordGenerator;

    //Tool which checks if letters match the word
    private GuessChecker checker;

    private Hangman gamedetails;
    private IHangmanService service;
    private int wordLength;

    public MockHangman() {
        checker = new GuessChecker();
    }

    @Override
    public void createGame(Hangman game) {
        gamedetails = game;
    }

    @Override
    public void setSecretWord(String word) {
        if (!doesWordMeetRequirements(word)) return;
        gamedetails.setSecretWord(word);
        wordLength = word.length();
        gamedetails.setSecretWordHasBeenSet(true);

        initializeGameControls();
    }

    private void initializeGameControls() {
        gamedetails.setGuessedCorrectLetters(new ArrayList<>());
        gamedetails.setGuessedWrongLetters(new ArrayList<>());
        for (int i = 0; i < wordLength; i++) {
            gamedetails.getGuessedCorrectLetters().add("_ ");
        }
    }

    @Override
    public void guessLetter(String input) {
        String guess = Character.toString(input.charAt(0));
        guess = guess.toUpperCase();

        handleLetter(guess);
    }

    private void handleLetter(String guess) {
        List<String> tempLetters = checker.getCorrectLetters(gamedetails.getGuessedCorrectLetters(), gamedetails.getSecretWord(), guess);
        if (tempLetters.contains(guess)) handleCorrectLetter(tempLetters);
        else handleIncorrectLetter(guess);
    }

    private void handleCorrectLetter(List<String> input) {

        gamedetails.setGuessedCorrectLetters(input);
    }


    private void handleIncorrectLetter(String input) {
        gamedetails.getGuessedWrongLetters().add(input);
    }


    private void handleWord(String playername, boolean correct) {
        if (!correct)  gamedetails.getGuessedWrongLetters().add("");
    }

    @Override
    public void guessWord(String input) {
        input = input.toUpperCase();
        handleWord("", gamedetails.getSecretWord().equals(input));
    }

    @Override
    public void joinGame(Player player, int gameId) {
        gamedetails.join(player);
    }

    @Override
    public void leaveGame(Player player, int gameId) {
        gamedetails.remove(player);
    }

    @Override
    public void startNewGame() {

    }


    @Override
    public void updateGameDetails(Hangman game) {

    }


    @Override
    public boolean doesWordMeetRequirements(String secretWord) {
        return (wordIsLongEnough(secretWord) && !hasWordTooManyDifferentCharacters(secretWord));

    }

    private boolean hasWordTooManyDifferentCharacters(String secretWord) {
        return secretWord.chars().distinct().count() >= 15;
    }

    private boolean wordIsLongEnough(String secretWord) {
        return secretWord.length() >= 3;
    }

    public Hangman getGamedetails() {
        return gamedetails;
    }
}
