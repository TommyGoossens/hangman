package hangmanserver;

import hangmanserver.authentication.controller.AuthenticationServerController;
import hangmanserver.chat.controller.ChatServerController;
import hangmanserver.hangman.controller.HangmanRestController;
import hangmanserver.hangman.controller.HangmanWebSocketController;
import hangmanserver.lobby.controller.LobbyRestController;
import hangmanserver.lobby.controller.LobbyWebSocketController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HangmanServer {
    private static Logger logger = Logger.getLogger(HangmanServer.class.getName());

    private static final int PORT_REST = 8090;
    private static final int PORT_WEBSOCKET = 8095;

    public static int getPortRest() {
        return PORT_REST;
    }

    public static int getPortWebsocket() {
        return PORT_WEBSOCKET;
    }

    public static void main(String[] args) {
        Thread rest = new Thread(threadRest);
        rest.setName("THREAD-REST");
        Thread wSocket = new Thread(threadWebSocket);
        wSocket.setName("THREAD-WEBSOCKET");
        rest.start();
        wSocket.start();
    }

    private static void startRestServer() {
        ServletContextHandler contextHandler = new ServletContextHandler((ServletContextHandler.SESSIONS));
        contextHandler.setContextPath("/");
        Server jettyServer = new Server(PORT_REST);
        jettyServer.setHandler(contextHandler);
        ServletHolder jerseyServlet = contextHandler.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                '"' + " " + AuthenticationServerController.class.getCanonicalName() + "; " + HangmanRestController.class.getCanonicalName() + "; " + LobbyRestController.class.getCanonicalName() + "; " + '"');
        try {
            jettyServer.start();
            logger.log(Level.INFO, "Rest server started on port 8090!");
            jettyServer.join();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[Unable to start rest server]: {0}", e);
        }
    }

    // Start the web socket server
    private static void startWebSocketServer() {
        Server webSocketServer = new Server();
        ServerConnector connector = new ServerConnector(webSocketServer);
        connector.setPort(PORT_WEBSOCKET);
        webSocketServer.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler webSocketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        webSocketContext.setContextPath("/");
        webSocketServer.setHandler(webSocketContext);

        try {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(webSocketContext);

            // Add WebSocket endpoint to javax.websocket layer
            wscontainer.addEndpoint(ChatServerController.class);
            wscontainer.addEndpoint(LobbyWebSocketController.class);
            wscontainer.addEndpoint(HangmanWebSocketController.class);
            webSocketServer.start();

            webSocketServer.join();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[Unable to join]: {0}", e);
        }
    }

    static Runnable threadRest = () -> startRestServer();

    static Runnable threadWebSocket = () -> startWebSocketServer();
}
