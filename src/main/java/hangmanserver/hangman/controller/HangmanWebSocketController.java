package hangmanserver.hangman.controller;

import com.google.gson.Gson;
import hangmanserver.lobby.controller.LobbyWebSocketController;
import hangmanshared.models.DTO.*;
import hangmanshared.models.Hangman;
import hangmanshared.models.Player;
import hangmanshared.models.enums.HangmanMessageOperation;
import hangmanshared.utility.GuessChecker;
import hangmanshared.utility.WebsocketMessageProcessor;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/hangman/{gameid}")
public class HangmanWebSocketController implements HangmanWebSocketServer {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Gson gson;


    private static final Map<Session, Integer> sessions = new HashMap<>();
    private static final List<Hangman> games = new ArrayList<>();

    public static List<Hangman> getActiveGames() {
        return games;
    }

    public static void addGameToActiveGames(Hangman game) {
        games.add(game);
    }

    private GuessChecker checker;

    private WebsocketMessageProcessor clientMessageHandler;

    public HangmanWebSocketController() {
        gson = new Gson();
        checker = new GuessChecker();
        clientMessageHandler = new WebsocketMessageProcessor();
    }

    @OnOpen
    public void onConnect(@PathParam("gameid") String gameIdAsString, Session session) {
        int gameid = Integer.parseInt(gameIdAsString);
        logger.log(Level.INFO, "[Websocket connected] SessionID : {0}", session.getId());
        sessions.put(session, gameid);
        logger.log(Level.INFO, "[#sessions] : {0}", sessions.size());
    }

    @OnMessage
    public void onText(String jsonMessage, Session session) {
        logger.log(Level.INFO, "[Websocket Session] : {0} [Message] : {1})", new Object[]{session.getId(), jsonMessage});
        HangmanMessageDTO message = clientMessageHandler.handleMessageFromClient(jsonMessage);
        processOperation(message, session);
    }


    private void processOperation(HangmanMessageDTO message, Session session) {
        HangmanMessageOperation operation = message.getOperation();
        String content = message.getContent();
        if (operation != null && content != null && !content.equals("")) {
            switch (operation) {
                case SET_SECRET_WORD:
                    setSecretWord(content);
                    break;
                case GUESS_WORD:
                    guessSecretWord(content);
                    break;
                case GUESS_LETTER:
                    guessLetter(content);
                    break;
                case LEAVE:
                    sessions.remove(session);
                    leaveGame(content);
                    break;
            }
        }
    }

    @Override
    public void leaveGame(String content) {
        LobbyWebSocketController lobbyController = new LobbyWebSocketController();
        LeaveGameDTO leaveGame = gson.fromJson(content, LeaveGameDTO.class);
        Hangman selectedGame = findGameById(leaveGame.getGameid());
        boolean isLeavingPlayerCreator = selectedGame.getCreator().equals(leaveGame.getPlayer());

        selectedGame.remove(leaveGame.getPlayer());
        lobbyController.playerHasLeft(selectedGame);

        HangmanMessageDTO message = new HangmanMessageDTO();
        if (isLeavingPlayerCreator) message.setOperation(HangmanMessageOperation.START);
        message.setContent(gson.toJson(message));
        notifySubscribedUsers(message, leaveGame.getGameid());
    }

    @Override
    public void updatePlayerList(int gameId) {
        Hangman selectedGame = findGameById(gameId);
        HangmanMessageDTO messageDTO = new HangmanMessageDTO(HangmanMessageOperation.UPDATE);
        messageDTO.setContent(gson.toJson(selectedGame));

        notifySubscribedUsers(messageDTO, gameId);
    }

    @Override
    public void guessSecretWord(String content) {
        GuessDTO guessWord = gson.fromJson(content, GuessDTO.class);
        Hangman selectedGame = games.stream().filter(g -> g.getGameid() == guessWord.getGameId()).findFirst().get();
        boolean isWordCorrect = selectedGame.getSecretWord().equals(guessWord.getGuess());

        if (!isWordCorrect) selectedGame.getGuessedWrongLetters().add("");

        HangmanMessageDTO message = new HangmanMessageDTO(HangmanMessageOperation.UPDATE);
        message.setProperty("");
        if (hasPlayerLost(selectedGame))
            message.setProperty(gson.toJson(new PlayerStatsDTO(guessWord.getPlayername(), false)));
        else if (isWordCorrect) message.setProperty(gson.toJson(new PlayerStatsDTO(guessWord.getPlayername(), true)));

        if(!message.getProperty().isEmpty()){
            message.setOperation(HangmanMessageOperation.START);
            setNewCreator(selectedGame);
        }
        message.setContent(gson.toJson(selectedGame));

        notifySubscribedUsers(message, guessWord.getGameId());

    }

    @Override
    public void guessLetter(String content) {
        GuessDTO guessLetter = gson.fromJson(content, GuessDTO.class);
        Hangman selectedGame =findGameById(guessLetter.getGameId());

        if (selectedGame.getSecretWord().contains(guessLetter.getGuess())) {
            selectedGame.setGuessedCorrectLetters(checker.getCorrectLetters(selectedGame.getGuessedCorrectLetters(), selectedGame.getSecretWord(), guessLetter.getGuess()));
        } else {
            selectedGame.getGuessedWrongLetters().add(guessLetter.getGuess());
        }

        HangmanMessageDTO message = new HangmanMessageDTO(HangmanMessageOperation.UPDATE);
        message.setProperty("");
        if (hasPlayerLost(selectedGame))
            message.setProperty(gson.toJson(new PlayerStatsDTO(guessLetter.getPlayername(), false)));
        else if (hasPlayerWon(selectedGame))
            message.setProperty(gson.toJson(new PlayerStatsDTO(guessLetter.getPlayername(), true)));

        if(!message.getProperty().isEmpty()) {
            message.setOperation(HangmanMessageOperation.START);
            setNewCreator(selectedGame);
        }

        message.setContent(gson.toJson(selectedGame));

        notifySubscribedUsers(message, selectedGame.getGameid());
    }

    @Override
    public void setSecretWord(String content) {
        SecretWordDTO secretWord = gson.fromJson(content, SecretWordDTO.class);
        Hangman selectedGame = findGameById(secretWord.getGameId());

        if (selectedGame.getSecretWordHasBeenSet() || selectedGame.isHasStarted()) return;

        selectedGame.setSecretWord(secretWord.getWord());
        selectedGame.setGuessedWrongLetters(new ArrayList<>());
        selectedGame.setGuessedCorrectLetters(new ArrayList<>());
        for (int i = 0; i < secretWord.getWord().length(); i++) {
            selectedGame.getGuessedCorrectLetters().add("_ ");
        }
        selectedGame.setSecretWordHasBeenSet(true);
        selectedGame.setHasStarted(true);

        HangmanMessageDTO message = new HangmanMessageDTO(HangmanMessageOperation.UPDATE);
        message.setProperty("");
        message.setContent(gson.toJson(selectedGame));

        logger.log(Level.INFO,"[Secret word has been set] [Word] : {0} ; [Game] : {1}",new Object[]{secretWord.getWord(),secretWord.getGameId()} );
        notifySubscribedUsers(message,secretWord.getGameId());
    }


    private boolean hasPlayerLost(Hangman game) {
        return game.getGuessedWrongLetters().size() >= game.getDifficulty().getMistakes();
    }

    private boolean hasPlayerWon(Hangman game) {
        return !game.getGuessedCorrectLetters().contains("_ ");
    }

    private void notifySubscribedUsers(HangmanMessageDTO messageDTO, int gameid) {
        String jsonMessage = gson.toJson(messageDTO);
        for (Map.Entry<Session, Integer> connectedUsers : sessions.entrySet()) {
            if (connectedUsers.getValue() == gameid) {
                connectedUsers.getKey().getAsyncRemote().sendText(jsonMessage);
            }
        }
    }

    private void setNewCreator(Hangman gamedetails) {
        Player tempPlayer = gamedetails.getCreator();
        gamedetails.remove(gamedetails.getCreator());
        gamedetails.getPlayers().add(tempPlayer);
        gamedetails.setHasStarted(false);
        gamedetails.setSecretWordHasBeenSet(false);
    }

    private Hangman findGameById(int gameId){
//        Optional<Hangman> selectedGame = games.stream().filter(g -> g.getGameid() == gameId).findFirst();
        return games.stream().filter(g -> g.getGameid() == gameId).findFirst().get();
    }

}
