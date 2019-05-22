package hangmanclient.authentication.controller;

import hangmanclient.authentication.service.Authenticatable;
import hangmanshared.models.AuthenticationParticipant;
import hangmanclient.authentication.service.AuthenticationService;
import hangmanclient.utility.SceneLoader;
import hangmanshared.utility.PasswordChecker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpController implements ISignUp, Initializable {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfConfirmPassword;
    private Authenticatable service = new AuthenticationService();

    private SceneLoader loader = new SceneLoader();
    private AuthenticationParticipant participant;
    private PasswordChecker passwordChecker;

    public SignUpController() {
        passwordChecker = new PasswordChecker();
        this.participant = new AuthenticationParticipant();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addListenerToUsernameTextField();
    }

    @Override
    public void signUp() {
        if (!passwordMeetsRequirements(tfPassword.getText(), tfConfirmPassword.getText())) {
            sendAlert(Alert.AlertType.WARNING, "Password does not meet the requirements", "Passwords need to match and are at least 6 characters long");
            return;
        }
        if (!usernameIsUnique(tfUsername.getText())) {
            sendAlert(Alert.AlertType.WARNING, "Username is not unique", "Please enter a different username");
            return;
        }
        participant.setUsername(tfUsername.getText());
        participant.setPassword(tfPassword.getText());
        service.signUp(participant);
        showSignIn();
    }

    @Override
    public void showSignIn() {
        loader.showScene((Stage) tfUsername.getScene().getWindow(), "SignIn");

    }

    /*
    Add listeners
     */
    private void addListenerToUsernameTextField() {
        tfUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!usernameIsUnique(tfUsername.getText()))
                logger.log(Level.WARNING, "[Username is not unique]: " + newValue);
        });
    }

    // region Username checks
    @Override
    public boolean usernameIsUnique(String username) {
        if (username.isEmpty()) return false;
        return service.checkUsernameAvailability(username);
    }

    //endregion


    //region Password checks
    @Override
    public boolean passwordMeetsRequirements(String password, String password2) {
        return passwordChecker.passwordMeetsRequirements(password,password2);
    }
    //endregion


    private void sendAlert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
