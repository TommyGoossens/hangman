package hangmanclient.authentication.controller;

import hangmanclient.HangmanClientApplication;
import hangmanclient.authentication.service.Authenticatable;
import hangmanclient.authentication.service.AuthenticationService;
import hangmanclient.utility.SceneLoader;
import hangmanshared.models.AuthenticationParticipant;
import hangmanshared.models.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SignInController implements ISignIn, Initializable {
    private Authenticatable authServer = new AuthenticationService();
    private SceneLoader loader = new SceneLoader();
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private Button btnRegister;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void signIn() {
        if(!hasUsernameBeenFilledIn()) return;
        if(!hasPasswordBeenFilledIn()) return;
        AuthenticationParticipant user = authServer.signIn(tfUsername.getText(), tfPassword.getText());
        if(user != null){
            HangmanClientApplication.setAuthenticatedUser(user);
            showLobby();
        } else {
            sendAlert(Alert.AlertType.WARNING,"Incorrect credentials","Incorrect username / password");
        }
    }

    private void showLobby() {
       loader.showScene((Stage) tfUsername.getScene().getWindow(),"Lobby");
    }

    public void showSignUp() {
        loader.showScene((Stage) tfUsername.getScene().getWindow(),"SignUp");

    }

    private boolean hasUsernameBeenFilledIn() {
        if (tfUsername.getText().isEmpty()) {
            sendAlert(Alert.AlertType.WARNING, "No username entered", "Please enter your username");
            return false;
        }
        return true;
    }

    private boolean hasPasswordBeenFilledIn() {
        if (tfPassword.getText().isEmpty()) {
            sendAlert(Alert.AlertType.WARNING, "No username entered", "Please enter your password");
            return false;
        }
        return true;
    }

    private void sendAlert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
