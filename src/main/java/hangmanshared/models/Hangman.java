package hangmanshared.models;

import hangmanshared.models.enums.Difficulty;
import hangmanshared.models.enums.GameMode;

import java.util.ArrayList;
import java.util.List;

public class Hangman {
    private Difficulty difficulty;
    private GameMode gameMode;
    private Player creator;
    private List<Player> players;
    private int maxPlayers = 2;
    private int gameid;

    private boolean hasStarted = false;
    private boolean secretWordHasBeenSet = false;

    private String secretWord;
    private List<String> guessedCorrectLetters;
    private List<String> guessedWrongLetters;
    private int wordlLength;

    public Hangman(Player creator) {
        players = new ArrayList<>();
        this.creator = creator;
        players.add(creator);
        guessedCorrectLetters = new ArrayList<>();
        guessedWrongLetters = new ArrayList<>();
    }

    public Hangman() {
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Player getCreator() {
        return creator;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public boolean getSecretWordHasBeenSet() {
        return secretWordHasBeenSet;
    }

    public void setSecretWordHasBeenSet(boolean secretWordHasBeenSet) {
        this.secretWordHasBeenSet = secretWordHasBeenSet;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }

    public List<String> getGuessedCorrectLetters() {
        return guessedCorrectLetters;
    }

    public void setGuessedCorrectLetters(List<String> guessedCorrectLetters) {
        this.guessedCorrectLetters = guessedCorrectLetters;
    }

    public List<String> getGuessedWrongLetters() {
        return guessedWrongLetters;
    }

    public void setGuessedWrongLetters(List<String> guessedWrongLetters) {
        this.guessedWrongLetters = guessedWrongLetters;
    }

    @Override
    public String toString() {
        return "Creator: " + creator + "; Players: " + players.size() + "/2; difficulty: " + difficulty.toString();
    }

    public void join(Player joinPlayer) {
        if (this.players.size() < maxPlayers) this.players.add(joinPlayer);
    }

    public void remove(Player player) {
        Player leavingPlayer = players.stream().filter(p -> p.getName().equals(player.getName())).findFirst().get();
        this.players.remove(leavingPlayer);
        if (this.creator == player) creator = players.get(0);
    }

    public int getWordLength() {
        return wordlLength;
    }

    public void setWordLength(int wordLength) {
        this.wordlLength = wordLength;
    }
}
