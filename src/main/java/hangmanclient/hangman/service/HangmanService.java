package hangmanclient.hangman.service;

import com.google.gson.Gson;
import hangmanclient.HangmanClientApplication;
import hangmanclient.hangman.controller.IHangman;
import hangmanclient.utility.RestExecuter;
import hangmanclient.utility.WebSocketCommunicator;
import hangmanshared.models.DTO.*;
import hangmanshared.models.Hangman;
import hangmanshared.models.Player;
import hangmanshared.models.enums.HangmanMessageOperation;
import hangmanshared.utility.WebsocketMessageProcessor;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HangmanService implements Observer, IHangmanService {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private IHangman controller;
    private WebsocketMessageProcessor serverMessageHandler;
    private String baseurl = "hangman/";
    private RestExecuter executer;
    private WebSocketCommunicator communicator;
    private Gson gson;

    public HangmanService(IHangman hangmanController) {
        this.controller = hangmanController;
        executer = new RestExecuter();
        serverMessageHandler = new WebsocketMessageProcessor();
        gson = new Gson();
    }

    @Override
    public void update(Observable o, Object arg) {
        HangmanMessageDTO message = serverMessageHandler.handleMessageFromClient((String) arg);
        handleMessageFromServer(message);
    }

    private void handleMessageFromServer(HangmanMessageDTO message) {
        HangmanMessageOperation operation = message.getOperation();
        Hangman content = gson.fromJson(message.getContent(), Hangman.class);
        PlayerStatsDTO property;
        if(!message.getProperty().isEmpty()) {
            property = gson.fromJson(message.getProperty(),PlayerStatsDTO.class);
            if(!property.getPlayername().equals(HangmanClientApplication.getCurrentPlayer().getName())) return;
            updatePlayerStats(property.getPlayername(),property.hasWon());
        }
        controller.updateGameDetails(content);
        switch (operation){
            case START:
                controller.startNewGame();
                break;
        }
    }

    @Override
    public void connectToServer(int gameid) {
        String url = baseurl + gameid;
        communicator = new WebSocketCommunicator(url);
        communicator.addObserver(this);
        communicator.start();
    }

    @Override
    public void setSecretWord(SecretWordDTO secretWord) {
        HangmanMessageDTO message = new HangmanMessageDTO(HangmanMessageOperation.SET_SECRET_WORD);
        message.setContent(gson.toJson(secretWord));
        communicator.sendMessage(message);
    }

    @Override
    public void guessLetter(GuessDTO guessedLetter) {
        HangmanMessageDTO message = new HangmanMessageDTO(HangmanMessageOperation.GUESS_LETTER);
        message.setContent(gson.toJson(guessedLetter));

        communicator.sendMessage(message);

    }

    @Override
    public void guessWord(GuessDTO guessedWord) {
        HangmanMessageDTO message = new HangmanMessageDTO(HangmanMessageOperation.GUESS_WORD);
        message.setContent(gson.toJson(guessedWord));

        communicator.sendMessage(message);
    }

    @Override
    public Hangman getGameDetails(int gameId) {
        String response = executer.executeGet(baseurl + gameId);
        Hangman game = gson.fromJson(response, Hangman.class);
        return game;
    }

    @Override
    public void notifyStart(GameDTO gameDTO) {
        HangmanMessageDTO message = new HangmanMessageDTO(HangmanMessageOperation.START);
        message.setContent(gson.toJson(gameDTO));

        communicator.sendMessage(message);
    }

    @Override
    public void updatePlayerStats(String playerName, boolean won) {
        PlayerStatsDTO playerStats = new PlayerStatsDTO(playerName, won);
        String result = executer.executePut(baseurl + "updateplayer", playerStats);
        logger.log(Level.INFO, "[Update stats] : {0}", result);
    }

    @Override
    public void leave(Player player, int gameId) {
        HangmanMessageDTO message = new HangmanMessageDTO();
        message.setOperation(HangmanMessageOperation.LEAVE);
        message.setContent(gson.toJson(new JoinLeaveGameDTO(player, gameId)));
        communicator.sendMessage(message);
    }
}
