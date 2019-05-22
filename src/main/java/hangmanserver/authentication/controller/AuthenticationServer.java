package hangmanserver.authentication.controller;

import hangmanshared.models.AuthenticationParticipant;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public interface AuthenticationServer {

    /**
     * Checks on the server if the supplied username does not already exist
     *
     * @param username The supplied username which the user tries to register
     * @return true or false, depending on the result
     */
    Response checkUsernameAvailability(String username);

    /**
     * Method to authenticate the user
     *
     * @param credentials Participant with a username and plain text password
     * @return json response with a user if one is found with the right credentials
     */
    Response signIn(AuthenticationParticipant credentials);

    /**
     * Registers a new user in the database
     *
     * @param credentials Participant with a username and plain text password
     * @return true or false, depending on the result.
     */
    Response signUp(AuthenticationParticipant credentials);
}
