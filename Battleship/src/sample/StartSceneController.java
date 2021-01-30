package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class StartSceneController {

    @FXML
    private Button ExitButton;

    @FXML
    private Button StartGameButton;

    @FXML
    private void ExitButtonClicked() {
        Platform.exit();
    }

    @FXML
    private void StartButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("gameScene.fxml");
        loader.setLocation(xmlUrl);
        Parent newRoot = loader.load();

        StartGameButton.getScene().setRoot(newRoot);
    }


}
