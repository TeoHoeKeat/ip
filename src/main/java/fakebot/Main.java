package fakebot;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


//Solution below adapted from https://se-education.org/guides/tutorials/javaFx.html

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private static final String APP_NAME = "FakeBot";

    private FakeBot bot = new FakeBot();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/fakebot.png")));
            stage.setTitle(APP_NAME);
            fxmlLoader.<MainWindow>getController().setFakeBot(bot);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
