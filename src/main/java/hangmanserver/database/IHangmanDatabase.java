package hangmanserver.database;

import hangmanshared.models.DTO.PlayerDTO;
import hangmanshared.models.DTO.PlayerStatsDTO;

public interface IHangmanDatabase {
    /**
     * Updates the player stats in the database when a user has won or lost the game
     *
     * @param player PlayerStatsDTO containing the player name and a boolean: won
     * @return an integer based on the result; 1 if succeeded, -1 if not succeeded
     */
    int updatePlayerStats(PlayerStatsDTO player);

    /**
     * Retrieves the player data from the database (username, games played / lost / won)
     * @param playerName player name of the player who needs to be retrieved
     * @return PlayerDTO object
     */
    PlayerDTO getPlayer(String playerName);
}
