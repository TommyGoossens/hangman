package hangmanshared.models.DTO;

import java.util.List;

/**
 * Data transfer object used when the player guesses a word or letter. Depending on the usage, the string "guess" can contain a letter or a word
 */
public class GuessDTO {
    private String guess;
    private int gameId;
    private String playername;
    List<Integer> indexes;
    public GuessDTO() {

    }

    public GuessDTO(String playername,String guess, int gameId) {
        this.playername = playername;
        this.guess = guess;
        this.gameId = gameId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }
}
