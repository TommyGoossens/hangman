package hangmanserver.database.rowmappers;

import hangmanshared.models.DTO.PlayerDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        String playername = resultSet.getString("playername");
        int gamesPlayed = resultSet.getInt("games_played");
        int gamesWon = resultSet.getInt("games_won");
        int gamesLost = resultSet.getInt("games_lost");
        PlayerDTO player = new PlayerDTO();

        player.setUsername(playername);
        player.setGamesPlayed(gamesPlayed);
        player.setWins(gamesWon);
        player.setLosses(gamesLost);

        return player;
    }
}

