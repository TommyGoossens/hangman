package hangmanserver.database;

import hangmanserver.database.config.DatabaseConfig;
import hangmanserver.database.rowmappers.UserRowMapper;
import hangmanserver.database.utility.PasswordBCrypt;
import hangmanshared.models.AuthenticationParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationDatabase implements IAuthenticationDatabase {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private PasswordBCrypt bCrypt;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public AuthenticationDatabase() {
        bCrypt = new PasswordBCrypt();
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(new DatabaseConfig().mySQLDataSource());
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return true;
    }

    @Override
    public AuthenticationParticipant signIn(String username, String password) {
        logger.log(Level.INFO,"[Trying to sign user {0} in with password {1}]",new Object[]{username,password});
        AuthenticationParticipant user;
        try {
            String signInQuery = "SELECT * FROM users WHERE username =(?)";
            user = (AuthenticationParticipant) jdbcTemplate.queryForObject(signInQuery,new Object[]{username}, new UserRowMapper());
            if(bCrypt.verifyHash(password,user.getPassword())) return user;
        }catch (Exception e){
            logger.log(Level.SEVERE, "[Unable to sign in user {0}] : {1}", new Object[]{username, e});
            return null;
        }

        logger.log(Level.INFO, "[Incorrect credentials] : [Username] : {0} ; [Password] : {1}", new Object[]{username, password});
        return null;
    }

    @Override
    @RequestMapping("/")
    public int signUp(String username, String password) {
        logger.log(Level.INFO, "[Registering new user] : Username: {0} ; Password: {1}", new Object[]{username, password});
        String hashedPassword = bCrypt.enCryptPassWord(password);
        try {
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username,password) values(?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                return statement;
            }, holder);
            return holder.getKey().intValue();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[Unable to register user {0}] : {1}", new Object[]{username, e});
            return -1;
        }
    }
}
