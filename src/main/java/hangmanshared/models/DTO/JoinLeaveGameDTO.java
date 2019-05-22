package hangmanshared.models.DTO;

import hangmanshared.models.Player;

public class JoinLeaveGameDTO {
    private Player player;
    private int gameid;

    public JoinLeaveGameDTO() {
    }

    public JoinLeaveGameDTO(Player player, int gameid) {
        this.player = player;
        this.gameid = gameid;
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
