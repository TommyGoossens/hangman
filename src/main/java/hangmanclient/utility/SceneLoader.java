package hangmanclient.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneLoader {
private Logger logger = Logger.getLogger(this.getClass().getName());
    public void showScene(Stage window, String sceneName) {
        Stage primaryStage = window;

        primaryStage.close();

        Scene scene = null;
        try {
            scene = new Scene(
                    FXMLLoader.load(
                            getClass().getResource("/FXML/" + sceneName + ".fxml")
                    )
            );
        } catch (IOException e) {
            logger.log(Level.SEVERE,"[Unable to show] : {0} [ERROR] : {1}",new Object[]{sceneName,e});
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle(sceneName);
        primaryStage.show();
    }
}
