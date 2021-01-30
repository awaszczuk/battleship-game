package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.net.URL;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("startScene.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.getIcons().add(new Image(getClass().getResource("battleship.png").toString()));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Battleship Game");
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
