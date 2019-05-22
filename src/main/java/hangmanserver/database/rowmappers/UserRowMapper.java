package hangmanserver.database.rowmappers;

import hangmanshared.models.AuthenticationParticipant;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet,int i) throws SQLException{
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        AuthenticationParticipant user = new AuthenticationParticipant();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
