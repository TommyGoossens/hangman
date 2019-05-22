package hangmanserver.authentication.controller;

import com.google.gson.Gson;
import hangmanserver.database.IAuthenticationDatabase;
import hangmanshared.models.AuthenticationParticipant;
import hangmanserver.database.AuthenticationDatabase;
import hangmanshared.utility.PasswordChecker;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/authentication")
public class AuthenticationServerController implements AuthenticationServer {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private PasswordChecker passwordChecker;
    private Gson gson;
    private IAuthenticationDatabase database;

    public AuthenticationServerController() {
        passwordChecker = new PasswordChecker();
        database = new AuthenticationDatabase();
        gson = new Gson();
    }

    @Override
    @GET
    @Path("/checkavailability/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkUsernameAvailability(@PathParam("username") String username) {
        boolean output = database.isUsernameAvailable(username);
        return Response.ok().entity(gson.toJson(output)).build();
    }

    @Override
    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signIn(AuthenticationParticipant credentials) {
        AuthenticationParticipant user = database.signIn(credentials.getUsername(), credentials.getPassword());

        if (user == null) return Response.status(400).entity(gson.toJson(null)).build();

        return Response.status(200).entity(gson.toJson(user)).build();
    }

    @Override
    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(AuthenticationParticipant credentials) {
        if(passwordChecker.passwordMeetsRequirements(credentials.getPassword(),credentials.getPassword())){
            return Response.status(400).entity(gson.toJson(false)).build();
        }
        int result = database.signUp(credentials.getUsername(), credentials.getPassword());

        if (result > 0) return Response.status(201).entity(gson.toJson(true)).build();

        return Response.status(500).entity(gson.toJson(false)).build();
    }
}
