package hangmanclient.authentication.controller;

import java.io.IOException;

public interface ISignUp {
    /**
     * Registers the user based on the text filled in in the text fields
     */
    void signUp();

    /**
     * Shows the login page
     */
    void showSignIn() throws IOException;

    /**
     * Checks if the username is unique in the database
     * @param username which needs to be tested
     * @return boolean whether the username is available or mot
     */
    boolean usernameIsUnique(String username);


    /**
     * Checks if the 2 passwords match
     * @param password first password
     * @param password2 second password
     * @return boolean whether the passwords match
     */
    boolean passwordMeetsRequirements(String password, String password2);
}
