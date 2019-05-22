package hangmanshared.models.DTO;

public class SecretWordDTO {
    private String word;
    private int gameId;

    public SecretWordDTO() {
    }

    public SecretWordDTO(String word, int gameId) {
        this.word = word;
        this.gameId = gameId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
