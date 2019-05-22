package hangmanshared.models.DTO;

public class PlayerStatsDTO {
    private String playername;
    private boolean won;

    public PlayerStatsDTO() {
    }

    public PlayerStatsDTO(String playername, boolean won) {
        this.playername = playername;
        this.won = won;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public boolean hasWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }
}
