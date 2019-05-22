package hangmanserver.chat.controller;


import com.google.gson.Gson;
import hangmanshared.models.DTO.MessageDTO;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/chat/{gameid}")
public class ChatServerController {
    private static final Map<Session, Integer> sessions = new HashMap<>();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Gson gson;

    public ChatServerController() {
        gson = new Gson();
    }

    @OnOpen
    public void onConnect(@PathParam("gameid") String gameIdAsString, Session session) {
        logger.log(Level.INFO, "[Chat Websocket connected] SessionID : {0} [Gameid]: {1}", new Object[]{session.getId(), gameIdAsString});
        sessions.put(session, Integer.parseInt(gameIdAsString));
    }

    @OnMessage
    public void onText(String jsonMessage, Session session) throws IOException {
        MessageDTO message = gson.fromJson(jsonMessage, MessageDTO.class);
        for (Map.Entry<Session, Integer> connectedUsers : sessions.entrySet()) {
            if (connectedUsers.getValue() == message.getGameId())
                connectedUsers.getKey().getAsyncRemote().sendText(jsonMessage);
        }
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        logger.log(Level.SEVERE, "[WebSocket Session ID] : {0} [Error] : {1}", new Object[]{session.getId(), cause});
    }

}
