package hangmanclient.utility;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameWrapperLoader implements Initializable {

    @FXML
    private BorderPane gameWrapperPane;
    private AnchorPane paneGame;
    private AnchorPane paneChat;

    public GameWrapperLoader() // used by fxmlLoader
    {

    }

    public GameWrapperLoader(AnchorPane game, AnchorPane chat) {
        this.paneGame = game;
        this.paneChat = chat;
    }

//    @FXML
//    void initialize() // used by fxmlLoader
//    {
//        gameWrapperPane.setLeft(paneGame);
//        gameWrapperPane.setRight(paneChat);
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameWrapperPane.setLeft(paneGame);
        gameWrapperPane.setRight(paneChat);
    }
}
