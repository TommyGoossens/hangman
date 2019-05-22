package hangmanserver.database;

import hangmanserver.database.config.DatabaseConfig;
import hangmanserver.database.rowmappers.PlayerRowMapper;
import hangmanshared.models.DTO.PlayerDTO;
import hangmanshared.models.DTO.PlayerStatsDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HangmanDatabase implements IHangmanDatabase {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private JdbcTemplate jdbcTemplate;

    public HangmanDatabase() {
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(new DatabaseConfig().mySQLDataSource());
    }

    @Override
    public int updatePlayerStats(PlayerStatsDTO player) {
        final String column;
        if (player.hasWon()) column = "games_won";
        else {
            column = "games_lost";
        }
        try {
            GeneratedKeyHolder holder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement("UPDATE players set games_played = games_played + 1, " + column + "=" + column + " +1 where playername =?", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, player.getPlayername());
                return statement;
            }, holder);

            return 1;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[unable to update player] : {0}", player.getPlayername());
            return -1;
        }
    }

    @Override
    public PlayerDTO getPlayer(String playerName) {
        logger.log(Level.INFO, "[Trying to retrieve data from player] : {0}", playerName);
        PlayerDTO player;
        try {
            String playerQuery = "SELECT * FROM players WHERE playername =(?)";
            player = (PlayerDTO) jdbcTemplate.queryForObject(playerQuery, new Object[]{playerName}, new PlayerRowMapper());
            return player;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[Unable to retrieve data from player] : {0}", playerName);
            return null;
        }
    }
}