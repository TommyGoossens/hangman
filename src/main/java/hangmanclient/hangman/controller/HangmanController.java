package hangmanclient.hangman.controller;

import hangmanclient.HangmanClientApplication;
import hangmanclient.hangman.service.HangmanService;
import hangmanclient.hangman.service.IHangmanService;
import hangmanclient.hangman.utility.RandomWordGenerator;
import hangmanclient.utility.SceneLoader;
import hangmanshared.models.DTO.*;
import hangmanshared.models.Hangman;
import hangmanshared.models.Player;
import hangmanshared.models.enums.GameMode;
import hangmanshared.utility.GuessChecker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class HangmanController implements Initializable, IHangman {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private SceneLoader sceneLoader = new SceneLoader();
    /**
     * All images of the hangman
     */
    //region Hangman images
    @FXML
    private ImageView imgFloor;
    @FXML
    private ImageView imgPole;
    @FXML
    private ImageView imgBeam;
    @FXML
    private ImageView imgSupportBeam;
    @FXML
    private ImageView imgRope;
    @FXML
    private ImageView imgHead;
    @FXML
    private ImageView imgLeftEye;
    @FXML
    private ImageView imgRightEye;
    @FXML
    private ImageView imgMouth;
    @FXML
    private ImageView imgNeck;
    @FXML
    private ImageView imgBody;
    @FXML
    private ImageView imgLeftArm;
    @FXML
    private ImageView imgRightArm;
    @FXML
    private ImageView imgLeftLeg;
    @FXML
    private ImageView imgRightLeg;
    //endregion

    //List containing all hangmanPartsEasy
    private List<ImageView> hangmanPartsEasy;
    private List<List<ImageView>> hangmanPartsMedium;
    private List<List<ImageView>> hangmanPartsHard;

    /**
     * All labels
     */
    //region Labels
    //Label displaying dashes representing the hidden word
    @FXML
    private Label lblHiddenWord;
    //Label displaying all the guessed letters
    @FXML
    private Label lblWrongLetters;

    //Label displaying the amount of mistakes the user has made
    @FXML
    private Label lblMistakes;

    //Label display the name of the current creator
    @FXML
    private Label lblCreatorName;
    //endregion

    /**
     * All buttons
     */
    //region Buttons

    //Button to confirm the input of one letter
    @FXML
    private Button btnConfirmChar;
    @FXML
    private Button btnGuessWord;
    @FXML
    private Button btnLeaveGame;

    //endregion

    //Text field where the user can enter a word or letter
    @FXML
    private TextField tfInputChar;
    //Button to confirm the input of the word

    //In single player mode this will return a new word
    private RandomWordGenerator aiWordGenerator;

    //Tool which checks if letters match the word
    private GuessChecker checker;

    private Hangman gamedetails;
    private IHangmanService service;

    public HangmanController() {
        checker = new GuessChecker();
        service = new HangmanService(this);
    }

    /**
     * FXML initialize methods
     */
    //region FXML initialize methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListenersToControls();
        hangmanPartsEasy = Arrays.asList(imgFloor, imgPole, imgBeam, imgSupportBeam, imgRope, imgHead, imgLeftEye, imgRightEye, imgMouth, imgNeck, imgBody, imgLeftArm, imgRightArm, imgLeftLeg, imgRightLeg);
        hangmanPartsMedium = Arrays.asList(Arrays.asList(imgFloor, imgPole), Arrays.asList(imgBeam, imgSupportBeam), Collections.singletonList(imgRope), Collections.singletonList(imgHead), Arrays.asList(imgLeftEye, imgRightEye, imgMouth), Arrays.asList(imgNeck, imgBody), Collections.singletonList(imgLeftArm), Collections.singletonList(imgRightArm), Collections.singletonList(imgLeftLeg), Collections.singletonList(imgRightLeg));
        hangmanPartsHard = Arrays.asList(Arrays.asList(imgFloor, imgPole, imgBeam, imgSupportBeam), Arrays.asList(imgRope, imgHead, imgLeftEye, imgRightEye), Collections.singletonList(imgBody), Arrays.asList(imgLeftArm, imgRightArm), Arrays.asList(imgLeftLeg, imgRightLeg));
    }

    //Sets up all the event listeners
    private void addListenersToControls() {
        addBtnConfirmLetterListener();
        addBtnGuessWordListener();
        addLetterInputListener();
        addBtnLeaveGameListener();
    }

    //Sets the on action event for the confirm letter button

    private void addBtnConfirmLetterListener() {
        btnConfirmChar.setOnAction(event -> {
            guessLetter(tfInputChar.getText());
            clearTextField(tfInputChar);
        });
    }
    //Sets the on action event for the guess word button

    private void addBtnGuessWordListener() {
        btnGuessWord.setOnAction(event -> {
            guessWord(tfInputChar.getText());
            clearTextField(tfInputChar);
        });
    }
    //Adds a listener to the input field in order to change visibility of the buttons

    private void addLetterInputListener() {
        tfInputChar.textProperty().addListener((observable, oldValue, newValue) -> {
            btnConfirmChar.setDisable(tfInputChar.getText().isEmpty());
            btnGuessWord.setDisable(tfInputChar.getText().isEmpty());
        });
    }

    private void addBtnLeaveGameListener() {
        btnLeaveGame.setOnAction(event -> {
            leaveGame(HangmanClientApplication.getCurrentPlayer(), gamedetails.getGameid());
            sceneLoader.showScene((Stage) btnLeaveGame.getScene().getWindow(), "Lobby");
        });

    }

    //endregion

    /**
     * UI draw methods
     */
    //region UI draw methods


    //Draws the dashes showing the amount of letters in the secret word
    private void drawHiddenWord() {
        Platform.runLater(() -> {
            lblHiddenWord.setText("");
            for (int i = 0; i < gamedetails.getGuessedCorrectLetters().size(); i++) {
                lblHiddenWord.setText(lblHiddenWord.getText() + gamedetails.getGuessedCorrectLetters().get(i));
            }
        });
    }

    //Draws all incorrect letters
    private void drawMistakes() {
        Platform.runLater(() -> {
            lblWrongLetters.setText("");
            for (int i = 0; i < gamedetails.getGuessedWrongLetters().size(); i++) {
                lblWrongLetters.setText(lblWrongLetters.getText() + gamedetails.getGuessedWrongLetters().get(i) + ", ");
            }
            lblMistakes.setText(gamedetails.getGuessedWrongLetters().size() + "/" + gamedetails.getDifficulty().getMistakes());
        });
    }

    //Draws a part of the hangman, depending on the difficulty
    private void drawHangmanPart() {
        if (gamedetails.getGuessedWrongLetters().size() > 0) {
            Platform.runLater(() -> {
                switch (gamedetails.getDifficulty()) {
                    case EASY:
                        hangmanPartsEasy.get(gamedetails.getGuessedWrongLetters().size() - 1).setVisible(true);
                        break;
                    case MEDIUM:
                        for (ImageView iv : hangmanPartsMedium.get(gamedetails.getGuessedWrongLetters().size() - 1)) {
                            iv.setVisible(true);
                        }
                        break;
                    case HARD:
                        for (ImageView iv : hangmanPartsHard.get(gamedetails.getGuessedWrongLetters().size() - 1)) {
                            iv.setVisible(true);
                        }
                        break;
                }
            });
        }
    }

    //Hides the hangman for the new game
    private void hideAllHangmanParts() {
        for (ImageView iv : hangmanPartsEasy) {
            iv.setVisible(false);
        }
    }

    //Clears the textfield after confirming a guess

    private void clearTextField(TextField textField) {
        Platform.runLater(textField::clear);
    }

    //Shows a dialog where the user can enter the word which need to be guessed
    private void showInputSecretWord() {
        Platform.runLater(() -> {
            TextInputDialog secretWordDialog = new TextInputDialog();
            secretWordDialog.setTitle("Please enter the secret word");
            secretWordDialog.setHeaderText("Enter your secret word:");
            secretWordDialog.setContentText("Word:");

            Optional<String> result = secretWordDialog.showAndWait();

            result.ifPresent(secretWord -> setSecretWord(secretWord.toUpperCase()));
        });

    }

    //Disables the input for the creator
    private void disableInput() {
        if (gamedetails.getGameMode().equals(GameMode.MULTI_PLAYER)) {
            boolean isAuthUserCreator = gamedetails.getCreator().getName().equals(HangmanClientApplication.getAuthenticatedUser().getUsername());
            tfInputChar.setDisable(isAuthUserCreator);
            btnConfirmChar.setDisable(isAuthUserCreator);
            btnGuessWord.setDisable(isAuthUserCreator);
        }
    }

    //This sets up all the correct controls and text
    private void initializeGameControls() {
        Platform.runLater(() -> {
            hideAllHangmanParts();
            lblCreatorName.setText(gamedetails.getCreator().getName());
            drawHiddenWord();
            drawMistakes();
            drawHangmanPart();
            disableInput();
        });
    }
    //endregion

    //region Methods regarding guessing and setting the word
    /**
     * Sets the secret word when in multiplayer mode
     * When in singleplayer mode, the word generator will generate a new random word
     *
     * @param word is inputted from the dialog, in singleplayer mode the word will be empty
     */
    @Override
    public void setSecretWord(String word) {
        if (!doesWordMeetRequirements(word)) {
            showInputSecretWord();
            return;
        }
        gamedetails.setWordLength(word.length());
        service.setSecretWord(new SecretWordDTO(word, gamedetails.getGameid()));
        initializeHiddenLetters();
        initializeGameControls();

    }


    private void generateNewAIWord() {
        aiWordGenerator.generateNewWord();
        gamedetails.setWordLength(aiWordGenerator.getWordLength());
        initializeHiddenLetters();
        initializeGameControls();
    }


    /**
     * Gets the first letter from the text field checks whether the secret word contains it or not
     */
    @Override
    public void guessLetter(String input) {
        String guess = Character.toString(input.charAt(0));
        guess = guess.toUpperCase();


        switch (gamedetails.getGameMode()) {
            case MULTI_PLAYER:
                service.guessLetter(new GuessDTO(HangmanClientApplication.getAuthenticatedUser().getUsername(), guess, gamedetails.getGameid()));
                break;
            case SINGLE_PLAYER:
                handleLetter(guess);
        }
    }

    /**
     * Gets the entered word from the textfield and checks if it is the correct word
     */
    @Override
    public void guessWord(String input) {
        input = input.toUpperCase();
        switch (gamedetails.getGameMode()) {
            case MULTI_PLAYER:
                service.guessWord(new GuessDTO(HangmanClientApplication.getAuthenticatedUser().getUsername(), input, gamedetails.getGameid()));
                break;
            case SINGLE_PLAYER:
                handleWord(aiWordGenerator.guessWord(input));
                break;
        }
    }

    private void handleLetter(String guess) {
        List<String> tempLetters = checker.getCorrectLetters(gamedetails.getGuessedCorrectLetters(), aiWordGenerator.getSecretWord(), guess);
        if (tempLetters.contains(guess)) handleCorrectLetter(tempLetters);
        else handleMistake(guess);
    }


    //Handles correct letters in single player mode
    private void handleCorrectLetter(List<String> letters) {
        gamedetails.setGuessedCorrectLetters(letters);
        drawHiddenWord();
        if (hasPlayerWon()) {
            updatePlayerStats(HangmanClientApplication.getCurrentPlayer().getName(), true);
            startNewGame();
        }
    }


    private void handleWord(boolean correct) {
        if (correct) {
            updatePlayerStats(HangmanClientApplication.getCurrentPlayer().getName(), true);
            startNewGame();
        } else {
            handleMistake("");
        }
    }

    private void handleMistake(String input) {
        gamedetails.getGuessedWrongLetters().add(input);
        drawHangmanPart();
        drawMistakes();
        if (hasPlayerLost()) {
            updatePlayerStats(HangmanClientApplication.getCurrentPlayer().getName(), false);
            startNewGame();
        }
    }

    //endregion

    /**
     * Game methods
     */
    //region Game methods

    //Method to initialize the game. In multiplayer mode it will subscribe on the server
    //In single player mode it will generate a random word
    @Override
    public void createGame(Hangman game) {
        gamedetails = game;
        switch (gamedetails.getGameMode()) {
            case MULTI_PLAYER:
                service.connectToServer(game.getGameid());
                showInputSecretWord();
                break;
            case SINGLE_PLAYER:
                aiWordGenerator = new RandomWordGenerator(gamedetails.getDifficulty());
                generateNewAIWord();
                break;
        }

    }

    //The lobby calls this method in order to make the player join the game
    @Override
    public void joinGame(Player player, int gameId) {
        service.connectToServer(gameId);
        gamedetails = service.getGameDetails(gameId);
        gamedetails.join(player);

        initializeGameControls();

    }

    //This method is called when the player leaves the game
    @Override
    public void leaveGame(Player player, int gameId) {
        if (gamedetails.getGameMode().equals(GameMode.MULTI_PLAYER)) {
            service.leave(player, gameId);
        }
    }

    //The service calls this method to update the game
    @Override
    public void updateGameDetails(Hangman game) {
        gamedetails = game;
        initializeGameControls();
    }

    @Override
    public void startNewGame() {
        gamedetails.setGuessedCorrectLetters(new ArrayList<>());
        gamedetails.setGuessedWrongLetters(new ArrayList<>());

        Platform.runLater(() -> {
            lblHiddenWord.setText("");
            lblWrongLetters.setText("");
        });
        gamedetails.setHasStarted(false);
        if (gamedetails.getGameMode() == GameMode.MULTI_PLAYER) {
            if (gamedetails.getCreator().getName().equals(HangmanClientApplication.getAuthenticatedUser().getUsername())) {
                showInputSecretWord();

            }
        } else {
            generateNewAIWord();
        }
    }

    //endregion

    private void initializeHiddenLetters() {
        for (int i = 0; i < gamedetails.getWordLength(); i++) {
            gamedetails.getGuessedCorrectLetters().add("_ ");
        }
    }


    //checks if the inputted word meets the requirements
    @Override
    public boolean doesWordMeetRequirements(String secretWord) {
        return (wordIsLongEnough(secretWord) && !hasWordTooManyDifferentCharacters(secretWord));

    }

    //Checks if the inputted word, doesnt have too many distinct characters
    private boolean hasWordTooManyDifferentCharacters(String secretWord) {
        return secretWord.chars().distinct().count() >= 15;
    }

    //Checks if the word is long enough
    private boolean wordIsLongEnough(String secretWord) {
        return secretWord.length() > 3;
    }

    //Checks if the player has won
    private boolean hasPlayerWon() {
        return aiWordGenerator.wordIsCompletelyGuessed(gamedetails.getGuessedCorrectLetters());
    }

    //Checks if the player has lost
    private boolean hasPlayerLost() {
        return gamedetails.getGuessedWrongLetters().size() == gamedetails.getDifficulty().getMistakes();
    }

    //Updates the players stats
    private void updatePlayerStats(String playerName, boolean won) {
        service.updatePlayerStats(playerName, won);
    }

}
