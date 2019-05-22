package hangmanserver.lobby.controller;

import com.google.gson.Gson;
import hangmanserver.hangman.controller.HangmanWebSocketController;
import hangmanserver.hangman.controller.HangmanWebSocketServer;
import hangmanshared.models.DTO.LeaveGameDTO;
import hangmanshared.utility.WebsocketMessageProcessor;
import hangmanshared.models.DTO.HangmanMessageDTO;
import hangmanshared.models.enums.HangmanMessageOperation;
import hangmanshared.models.DTO.JoinLeaveGameDTO;
import hangmanshared.models.Hangman;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/lobby")
public class LobbyWebSocketController {
    private HangmanWebSocketServer hangmanWebSocketController;
    private static final List<Session> sessions = new ArrayList<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Gson gson;

    private WebsocketMessageProcessor clientMessageHandler;

    public LobbyWebSocketController() {
        gson = new Gson();
        clientMessageHandler = new WebsocketMessageProcessor();
        hangmanWebSocketController = new HangmanWebSocketController();
    }

    @OnOpen
    public void onConnect(Session session) {
        logger.log(Level.INFO, "[Websocket connected] SessionID : " + session.getId());
        sessions.add(session);
        logger.log(Level.INFO, "[#sessions] : " + sessions.size());
    }

    @OnMessage
    public void onText(String jsonMessage, Session session) {
        HangmanMessageDTO messageDTO = clientMessageHandler.handleMessageFromClient(jsonMessage);
        processOperation(messageDTO, session);
    }

    private void processOperation(HangmanMessageDTO message, Session session) {
        HangmanMessageOperation operation = message.getOperation();
        String content = message.getContent();

        if (operation != null && content != null && !content.equals("")) {
            switch (operation) {
                case CREATE:
                    Hangman game = gson.fromJson(content, Hangman.class);
                    HangmanWebSocketController.addGameToActiveGames(game);

                    sendMessageToAllSubscribers();
                    break;
                case JOIN:

                    JoinLeaveGameDTO joinGame = gson.fromJson(content, JoinLeaveGameDTO.class);
                    Hangman selectedGame = HangmanWebSocketController.getActiveGames().stream().filter(g -> g.getGameid() == joinGame.getGameid()).findFirst().get();
                    HangmanWebSocketController.getActiveGames().stream().filter(g -> g.equals(selectedGame)).findFirst().get().join(joinGame.getPlayer());
                    hangmanWebSocketController.updatePlayerList(joinGame.getGameid());
                    sendMessageToAllSubscribers();
            }
        }
    }

    public void playerHasLeft(Hangman content) {
        if (content.getPlayers().size() <= 0) {
            HangmanWebSocketController.getActiveGames().remove(content);
        }

        sendMessageToAllSubscribers();
    }

    private void sendMessageToAllSubscribers() {
        for (Session sess : sessions) {
            sess.getAsyncRemote().sendText(gson.toJson(HangmanWebSocketController.getActiveGames()));
        }
    }
}

