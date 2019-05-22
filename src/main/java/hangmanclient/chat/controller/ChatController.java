package hangmanclient.chat.controller;

import com.google.gson.Gson;
import hangmanclient.HangmanClientApplication;
import hangmanshared.models.DTO.MessageDTO;
import hangmanclient.utility.WebSocketCommunicator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class ChatController implements Initializable, Observer {

    private WebSocketCommunicator communicator = null;
    private Gson gson;
    private int gameId;

    @FXML
    private ListView<String> lvChatBox;
    @FXML
    private Button btnSendMessage;
    @FXML
    private TextField tfChatInput;

    public ChatController() {
        gson = new Gson();

    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
        connectToServer();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();

    }

    @Override
    public void update(Observable o, Object arg) {
        MessageDTO receivedMessage = gson.fromJson(arg.toString(), MessageDTO.class);
        Platform.runLater(() -> lvChatBox.getItems().add(receivedMessage.toString()));
    }

    private void addListeners() {
        addSendMessageListener();
        addTextFieldListener();
    }

    private void addSendMessageListener() {
        btnSendMessage.setOnAction(event -> {
            MessageDTO message = new MessageDTO(tfChatInput.getText());
            message.setSender(HangmanClientApplication.getAuthenticatedUser().getUsername());
            message.setGameId(gameId);
            communicator.sendMessage(message);
            Platform.runLater(() ->{
                tfChatInput.clear();
            });
        });
    }

    private void addTextFieldListener() {
        tfChatInput.textProperty().addListener(((observable, oldValue, newValue) -> btnSendMessage.setDisable(newValue.isEmpty())));
    }

    private void connectToServer() {
        communicator = new WebSocketCommunicator("chat/" + gameId);
        communicator.addObserver(this);
        communicator.start();
    }
}
