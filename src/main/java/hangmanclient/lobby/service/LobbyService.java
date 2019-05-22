package hangmanclient.lobby.service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import hangmanclient.utility.RestExecuter;
import hangmanshared.models.DTO.PlayerDTO;
import hangmanshared.models.Hangman;
import hangmanshared.models.Player;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyService implements LobbyServer {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private String baseurl = "lobby/";
    private RestExecuter executer;
    private Gson gson;

    public LobbyService() {
        executer = new RestExecuter();
        gson = new Gson();
    }

    public List<Hangman> getAllGames() {
        String request = executer.executeGet(baseurl + "getallgames");
        Type gameList = new TypeToken<List<Hangman>>() {
        }.getType();
        return gson.fromJson(request, gameList);
    }

    public Player getPlayerStats(String playername) {
        String request = executer.executeGet(baseurl + "getplayer/" + playername);
        PlayerDTO message = gson.fromJson(request, PlayerDTO.class);

        Player player = new Player(message.getUsername(),
                message.getGamesPlayed(),
                message.getWins(),
                message.getLosses()
        );
        return player;
    }
}
