package hangmanshared.utility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import hangmanshared.models.DTO.HangmanMessageDTO;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WebsocketMessageProcessor {
    private Gson gson;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public WebsocketMessageProcessor() {
        gson = new Gson();
    }

    public HangmanMessageDTO handleMessageFromClient(String jsonMessage) {
        HangmanMessageDTO message = null;
        try {
            message = gson.fromJson(jsonMessage, HangmanMessageDTO.class);
        } catch (JsonSyntaxException e) {
            logger.log(Level.SEVERE, "[Unable to parse JSON message] : {0}", e);
        }
        return message;
    }
}
