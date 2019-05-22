package hangmanclient.authentication.service;

import hangmanshared.models.AuthenticationParticipant;

public interface Authenticatable {
    /**
     * Registers the AuthenticationParticipant on the user
     * @param participant object which contains the password and username
     * @return true or false whether the register method has succeeded
     */
    boolean signUp(AuthenticationParticipant participant);

    /**
     * sends a message to the rest server to sign in the user
     * @param username username the user has inputted
     * @param password password the user has inputted
     * @return an AuthenticatationParticipant if the credentials are correct, otherwise null
     */
    AuthenticationParticipant signIn(String username, String password);

    /**
     * Checks if the username is unique in the database
     * @param username which needs to be tested
     * @return boolean whether the username is available or mot
     */
    boolean checkUsernameAvailability(String username);
}
