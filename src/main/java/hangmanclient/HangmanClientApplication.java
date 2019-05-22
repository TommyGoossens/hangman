package hangmanclient;

import hangmanshared.models.AuthenticationParticipant;
import hangmanshared.models.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class HangmanClientApplication extends Application {
    private static AuthenticationParticipant authenticatedUser;
    private static Player currentPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    public static void setPlayer(Player player) {
        currentPlayer = player;
    }

    public static AuthenticationParticipant getAuthenticatedUser() {
        return authenticatedUser;
    }

    public static void logOut() {
        authenticatedUser = null;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setAuthenticatedUser(AuthenticationParticipant user) {
        authenticatedUser = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(
                FXMLLoader.load(
                        getClass().getResource("/FXML/SignIn.fxml")
                )
        );

        primaryStage.setScene(scene);
        primaryStage.setTitle("Sign in");
        primaryStage.setResizable(false);

        primaryStage.show();
        primaryStage.sizeToScene();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
