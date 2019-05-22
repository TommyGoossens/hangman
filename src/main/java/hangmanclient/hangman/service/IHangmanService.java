package hangmanclient.hangman.service;

import hangmanshared.models.DTO.GameDTO;
import hangmanshared.models.DTO.GuessDTO;
import hangmanshared.models.DTO.SecretWordDTO;
import hangmanshared.models.Hangman;
import hangmanshared.models.Player;

import java.util.Observable;

public interface IHangmanService {
    void update(Observable o, Object arg);

    /**
     * Connects the user to the server
     * @param gameid of the game which created / joined
     */
    void connectToServer(int gameid);

    /**
     * Sets the secretword on the server
     * The SecretWordDTO contains the game id and secret word
     * @param secretWord
     */
    void setSecretWord(SecretWordDTO secretWord);

    /**
     * sends a websocket message to the server to check if the guessed letter is correct
     * @param guessedLetter GuessedDTO containing the user who guessed, the letter and the game id
     */
    void guessLetter(GuessDTO guessedLetter);

    /**
     * sends a websocket message to the server to check if the guessed letter is correct
     * @param guessedWord GuessedDTO containing the user who guessed, the letter and the game id
     */
    void guessWord(GuessDTO guessedWord);

    /**
     * Gets all information from the server regarding the current game
     * @param gameId id of the game which needs to be retrieved
     * @return Hangman game which was retrieved
     */
    Hangman getGameDetails(int gameId);

    /**
     * Notifys the server that a new game has to be started
     * @param gameDTO
     */
    void notifyStart(GameDTO gameDTO);

    /**
     * Updates the player stats in the database
     * Increments the games played and based on the won boolean the games_won or games_lost column
     * @param playerName the name of the player who won
     * @param won boolean whether the player has won or lost
     */
    void updatePlayerStats(String playerName, boolean won);

    /**
     * Unsubscribes the player from the game
     * @param player player object which need to be remove
     * @param gameId game id of the game which needs to be altered
     */
    void leave(Player player, int gameId);
}
