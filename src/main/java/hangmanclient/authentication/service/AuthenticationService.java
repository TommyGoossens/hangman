package hangmanclient.authentication.service;

import com.google.gson.Gson;
import hangmanshared.models.AuthenticationParticipant;
import hangmanclient.utility.RestExecuter;

public class AuthenticationService implements Authenticatable {
    private String baseurl = "authentication/";
    private RestExecuter executer;
    private Gson gson;

    public AuthenticationService() {
        executer = new RestExecuter();
        this.gson = new Gson();
    }

    @Override
    public AuthenticationParticipant signIn(String username, String password) {
        AuthenticationParticipant participant = new AuthenticationParticipant(username, password);
        String request = executer.executePost(baseurl + "signin", participant);
        return gson.fromJson(request, AuthenticationParticipant.class);
    }

    @Override
    public boolean checkUsernameAvailability(String username) {
        String request = executer.executeGet(baseurl + "checkavailability/", username);
        return gson.fromJson(request, boolean.class);
    }

    @Override
    public boolean signUp(AuthenticationParticipant participant) {
        String request = executer.executePost(baseurl + "signup",participant);
        return gson.fromJson(request, boolean.class);
    }
}
