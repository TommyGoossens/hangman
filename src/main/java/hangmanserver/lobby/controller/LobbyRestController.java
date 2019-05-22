package hangmanserver.lobby.controller;

import com.google.gson.Gson;
import hangmanserver.database.HangmanDatabase;
import hangmanserver.hangman.controller.HangmanWebSocketController;
import hangmanshared.models.DTO.PlayerDTO;
import hangmanshared.models.Hangman;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/lobby")
public class LobbyRestController implements LobbyRestServer {
    private static final Collection<Hangman> activegames = HangmanWebSocketController.getActiveGames();
    private HangmanDatabase hangmanDatabase;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Gson gson;

    public LobbyRestController() {
        gson = new Gson();
        hangmanDatabase = new HangmanDatabase();
    }

    @Override
    @GET
    @Path("/getallgames")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGames() {
        logger.log(Level.INFO, "[Number of active games] : {0}", activegames.size());
        String output = gson.toJson(activegames);
        return Response.ok().entity(output).build();
    }

    @Override
    @GET
    @Path("/getplayer/{playername}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("playername") String playerName) {
        PlayerDTO player = hangmanDatabase.getPlayer(playerName);
        if (player == null) return Response.status(418).build();

        return Response.status(200).entity(gson.toJson(player)).build();
    }

}
