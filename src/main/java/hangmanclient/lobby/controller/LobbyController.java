package hangmanclient.lobby.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import hangmanclient.HangmanClientApplication;
import hangmanclient.chat.controller.ChatController;
import hangmanclient.hangman.controller.HangmanController;
import hangmanclient.lobby.service.LobbyService;
import hangmanclient.utility.GameWrapperLoader;
import hangmanclient.utility.SceneLoader;
import hangmanclient.utility.WebSocketCommunicator;
import hangmanshared.models.DTO.HangmanMessageDTO;
import hangmanshared.models.DTO.JoinLeaveGameDTO;
import hangmanshared.models.Hangman;
import hangmanshared.models.enums.Difficulty;
import hangmanshared.models.enums.GameMode;
import hangmanshared.models.enums.HangmanMessageOperation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyController implements Initializable, Observer {
    private Logger logger = Logger.getLogger(this.getClass().getName());


    private SceneLoader sceneLoader;

    @FXML
    private Label lblGamesWon;
    @FXML
    private Label lblGamesLost;
    @FXML
    private Label lblGamesPlayed;
    @FXML
    private Label lblPlayerName;

    @FXML
    private ToggleGroup tgDifficulties;
    @FXML
    private Button btnLogout;
    @FXML
    private RadioButton rbGamemodeMultiplayer;
    @FXML
    private Button btnJoingame;
    @FXML
    private Button btnCreateGame;
    @FXML
    private ListView<Hangman> lvGames;

    private WebSocketCommunicator communicator;
    private LobbyService service;
    private Gson gson;

    public LobbyController() {
        this.gson = new Gson();
        this.sceneLoader = new SceneLoader();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
        connectToServer();
        getAllGames();
        HangmanClientApplication.setPlayer(service.getPlayerStats(HangmanClientApplication.getAuthenticatedUser().getUsername()));
        drawPlayerInfo();
    }

    private void drawPlayerInfo() {
        Platform.runLater(() -> {
            lblGamesLost.setText(Integer.toString(HangmanClientApplication.getCurrentPlayer().getGameslost()));
            lblGamesWon.setText(Integer.toString(HangmanClientApplication.getCurrentPlayer().getGameswon()));
            lblGamesPlayed.setText(Integer.toString(HangmanClientApplication.getCurrentPlayer().getGamesPlayed()));
            lblPlayerName.setText(HangmanClientApplication.getCurrentPlayer().getName());
        });
    }

    private void getAllGames() {
        List<Hangman> games = service.getAllGames();
        Platform.runLater(() -> lvGames.getItems().addAll(games));
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> lvGames.getItems().clear());
        if (arg.toString().equals("")) return;
        Type listType = new TypeToken<List<Hangman>>() {
        }.getType();
        List<Hangman> receivedGames = gson.fromJson(arg.toString(), listType);
        Platform.runLater(() -> lvGames.getItems().addAll(receivedGames));
    }

    private void addListeners() {
        addCreateGameListener();
        addJoinGameListener();
        addGameListListener();
        addLogoutListener();
    }

    private void addCreateGameListener() {
        btnCreateGame.setOnAction(event -> createGame());
    }

    private void addJoinGameListener() {
        btnJoingame.setOnAction(event -> joinGame());
    }

    private void addGameListListener() {
        lvGames.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.getPlayers().size() < newValue.getMaxPlayers()) btnJoingame.setDisable(false);
        }));
    }

    private void addLogoutListener() {
        btnLogout.setOnAction(event -> logOut());
    }

    private void createGame() {
        Hangman game = setGameDetails();
        try {
            notifyServer(game);
            loadGameScreen(game, true);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "[Unable to load game screen] : ", e);
        }

    }

    private void joinGame() {
        Hangman game = lvGames.getSelectionModel().getSelectedItem();
        HangmanMessageDTO message = new HangmanMessageDTO();
        message.setOperation(HangmanMessageOperation.JOIN);
        message.setContent(gson.toJson(new JoinLeaveGameDTO(HangmanClientApplication.getCurrentPlayer(), game.getGameid())));
        communicator.sendMessage(message);

        try {
            loadGameScreen(game, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logOut() {
        HangmanClientApplication.logOut();
        sceneLoader.showScene((Stage) btnJoingame.getScene().getWindow(), "SignIn");

    }

    private void connectToServer() {
        service = new LobbyService();
        communicator = new WebSocketCommunicator("lobby");
        communicator.addObserver(this);
        communicator.start();
    }

    private void loadGameScreen(Hangman game, boolean create) throws IOException {
        Stage primaryStage = (Stage) btnCreateGame.getScene().getWindow();

        FXMLLoader wrapperLoader = new FXMLLoader(getClass().getResource("/FXML/GameWrapper.fxml"));
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/FXML/Game.fxml"));
        AnchorPane gamePane = gameLoader.load();
        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/FXML/Chat.fxml"));
        AnchorPane chatPane = chatLoader.load();

        HangmanController gameController = gameLoader.getController();
        ChatController chatController = chatLoader.getController();

        if (create) gameController.createGame(game);
        if (!create) gameController.joinGame(HangmanClientApplication.getCurrentPlayer(), game.getGameid());
        if(isGameInMultiPlayerMode(game))chatController.setGameId(game.getGameid());
        wrapperLoader.setController(new GameWrapperLoader(gamePane, chatPane));

        primaryStage.hide();
        Scene scene = new Scene(wrapperLoader.load());

//        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
//            leaveGameOnClose(game);
//            sceneLoader.showScene(primaryStage, "Lobby");
//        });

        primaryStage.setOnCloseRequest(event -> {
            gameController.leaveGame(HangmanClientApplication.getCurrentPlayer(),game.getGameid());
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void notifyServer(Hangman game) {
        if(isGameInMultiPlayerMode(game)) {
            HangmanMessageDTO message = new HangmanMessageDTO();
            message.setOperation(HangmanMessageOperation.CREATE);
            message.setContent(gson.toJson(game));
            communicator.sendMessage(message);
        }
    }

    private Hangman setGameDetails() {
        Hangman game = new Hangman(HangmanClientApplication.getCurrentPlayer());

        if(isGameInMultiPlayerMode(game))game.setGameid(service.getAllGames().size());

        game.setDifficulty(getSelectedDifficulty());
        if (rbGamemodeMultiplayer.isSelected()) game.setGameMode(GameMode.MULTI_PLAYER);
        else game.setGameMode(GameMode.SINGLE_PLAYER);

        return game;
    }

    private boolean isGameInMultiPlayerMode(Hangman game){
        return game.getGameMode() == GameMode.MULTI_PLAYER;

    }

    private Difficulty getSelectedDifficulty() {
        RadioButton selectedRadioButton = (RadioButton) tgDifficulties.getSelectedToggle();
        String toggleGroupValue = selectedRadioButton.getText();
        switch (toggleGroupValue) {
            case "Easy":
                return Difficulty.EASY;
            case "Medium":
                return Difficulty.MEDIUM;
            case "Hard":
                return Difficulty.HARD;
            default:
                return Difficulty.EASY;
        }
    }
}
