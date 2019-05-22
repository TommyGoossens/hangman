package hangmanserver.hangman.controller;

import com.google.gson.Gson;
import hangmanserver.database.HangmanDatabase;
import hangmanserver.database.IHangmanDatabase;
import hangmanshared.models.DTO.PlayerStatsDTO;
import hangmanshared.models.Hangman;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/hangman")
public class HangmanRestController implements HangmanRestServer {
    private static final Collection<Hangman> activegames = HangmanWebSocketController.getActiveGames();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    Gson gson;
    IHangmanDatabase database;

    public HangmanRestController() {
        gson = new Gson();
        database = new HangmanDatabase();
    }

    @Override
    @PUT
    @Path("/updateplayer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePlayerStats(String jsonMessage) {
        PlayerStatsDTO player = gson.fromJson(jsonMessage,PlayerStatsDTO.class);
        int updateSucceeded = database.updatePlayerStats(player);
        if (updateSucceeded > 0) return Response.status(200).entity(gson.toJson(true)).build();

        return Response.status(409).entity(gson.toJson(false)).build();


    }
@Override
    @GET
    @Path("/{gameid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameDetails(@PathParam("gameid") String gameIdAsString) {
        int gameId = Integer.parseInt(gameIdAsString);

        Hangman game = activegames.stream().filter(g -> g.getGameid() == gameId).findFirst().get();
        if (game == null) {
            logger.log(Level.SEVERE, "[Unable to find game] : {0}", gameIdAsString);
            return Response.serverError().build();
        }

        return Response.ok().entity(gson.toJson(game)).build();

    }
}
