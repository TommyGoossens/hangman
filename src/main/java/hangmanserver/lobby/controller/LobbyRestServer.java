package hangmanserver.lobby.controller;

import javax.ws.rs.core.Response;

public interface LobbyRestServer {

    /**
     * Retrieves all current active games from the server
     *
     * @return json respone with the list
     */
    Response getAllGames();

    /**
     * retrieves a PlayerDTO object from the database
     *
     * @param playerName the name of the player which needs to be retrieved
     * @return json response of the PlayerDTO
     */
    Response getPlayer(String playerName);
}
