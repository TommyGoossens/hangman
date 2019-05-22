package hangmanclient.hangman.controller;

import hangmanshared.models.Hangman;
import hangmanshared.models.Player;

public interface IHangman {
    /**
     * Method called from the lobby whenever a user creates a game
     * This method sets the game details
     * @param game which contains the details from the lobby
     */
    void createGame(Hangman game);

    /**
     * Sets the secret word
     * sends a message to the server if the word meets requirements
     * @param word the word which the user has inputted
     */
    void setSecretWord(String word);

    /**
     * Makes a guess based on the letter the user has inputted
     * @param input is what the user has inputted
     */
    void guessLetter(String input);

    /**
     * Makes a guess based on the word the user has inputted
     * @param input is what the user has inputted
     */
    void guessWord(String input);

    /**
     * This method is called from the lobby whenever a player selects the join option
     * It subscribes the player to the chat and the game websockets
     * @param player The player which has joined
     * @param gameId the gameid of the game the player has joined
     */
    void joinGame(Player player, int gameId);

    /**
     * This method is called from the lobby whenever a player selects the leave option
     * It unsubscribes the player to the chat and the game websockets
     * @param player The player which has joined
     * @param gameId the gameid of the game the player has left
     */
    void leaveGame(Player player, int gameId);

    /**
     * Restarts the game
     */
    void startNewGame();

    /**
     * Whenever the websocket service receives a game update, this method will update current game.
     * This ensures that everyone connected in the game has the same stats
     * @param game
     */
    void updateGameDetails(Hangman game);

    /**
     * Checks if the secret word meets the requirements
     * @param secretWord word the player has inputted
     * @return true or false depending on the result
     */
    boolean doesWordMeetRequirements(String secretWord);
}
