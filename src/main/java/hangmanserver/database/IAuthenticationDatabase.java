package hangmanserver.database;

import hangmanshared.models.AuthenticationParticipant;


public interface IAuthenticationDatabase {
    /**
     * Checks the username in the database, whether it already exists or not
     * @param username which needs to be checked
     * @return true if available, false if not
     */
    boolean isUsernameAvailable(String username);

    /**
     * Signs in the user
     * @param username username of the user
     * @param password password of the user
     * @return a authentication participant
     */
    AuthenticationParticipant signIn(String username, String password);

    /**
     * Registers a new user in the databse
     * @param username username of the user
     * @param password password of the user
     * @return an integer based on the result; 1 if succeeded, -1 if not succeeded
     */
    int signUp(String username, String password);
}
