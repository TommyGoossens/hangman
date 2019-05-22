package hangmanserver.hangman.controller;

import javax.ws.rs.core.Response;

public interface HangmanRestServer {

    /**
     * Updates the player in the database when it has won or lost a game
     *
     * @param jsonMessage containing the name of the player and a boolean: won
     * @return json response whether it succeeded or not
     */
    Response updatePlayerStats(String jsonMessage);

    /**
     * Returns a HangmanGame object containing all the information regarding the game,
     * players, creator etc etc
     *
     * @param gameIdAsString game id of the requested game
     * @return json response containing the Hangman game
     */
    Response getGameDetails(String gameIdAsString);
}
