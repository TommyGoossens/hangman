package hangmanclient.utility;

import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ClientEndpoint
public class WebSocketCommunicator extends Observable {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private String uri = "ws://localhost:8095/";

    private Session session;

    private Gson gson;

    boolean isRunning = false;

    public WebSocketCommunicator(String endpoint) {
        this.uri += endpoint;
        gson = new Gson();
    }

    public void start() {
        logger.log(Level.INFO, "[WebSocket Client start connection]");
        if (!isRunning) {
            isRunning = true;
            startClient();
        }
    }

    public void sendMessage(Object message) {
        String jsonMessage = gson.toJson(message);
        session.getAsyncRemote().sendText(jsonMessage);
    }

    private void startClient() {
        logger.log(Level.INFO, "[WebSocket Client start]");

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
        } catch (IOException | URISyntaxException | DeploymentException ex) {
            logger.log(Level.SEVERE, "[Unable to start client] : {0}", ex);
        }
    }

    public void stop() {
        logger.log(Level.INFO, "[WebSocket Client stop]");
        if (isRunning) {
            isRunning = false;
            stopClient();
        }
    }

    private void stopClient() {
        logger.log(Level.INFO, "[WebSocket Client stop]");
        try {
            session.close();

        } catch (IOException ex) {
            logger.log(Level.SEVERE, "[Unable to close client] : {0}", ex);
        }
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        logger.log(Level.INFO, "[WebSocket Client open session] : {0}", session.getRequestURI());
        this.session = session;

    }

    @OnMessage
    public void onWebSocketText(String message, Session session) {
        logger.log(Level.INFO, "[WebSocket Client message received] : {0}",message);
        this.setChanged();
        this.notifyObservers(message);
    }

}
