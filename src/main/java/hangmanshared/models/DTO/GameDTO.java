package hangmanshared.models.DTO;

import hangmanshared.models.Player;

import java.util.List;

public class GameDTO {
    private int gameid;
    private List<Player> players;
    private Player creator;
    public int wordLength;

    public GameDTO(int gameid, List<Player> players, Player creator, int wordLength) {
        this.gameid = gameid;
        this.players = players;
        this.creator = creator;
        this.wordLength = wordLength;
    }

    public GameDTO(int gameid, List<Player> players, Player creator) {
        this.gameid = gameid;
        this.players = players;
        this.creator = creator;
    }

    public GameDTO() {
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getCreator() {
        return creator;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getWordLength() {
        return wordLength;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }
}
