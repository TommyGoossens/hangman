package hangmanshared.models.DTO;

import hangmanshared.models.Player;

public class LeaveGameDTO {
    private Player player;
    private int gameid;

    public LeaveGameDTO(Player player, int gameid) {
        this.player = player;
        this.gameid = gameid;
    }

    public LeaveGameDTO() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

}
