import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Klasa FXProject sluzy do uruchomienia
 * programu jako aplikacji
 * @author Weronika Wajda
 * @version 1.0 20/10/2019
 *
 */

public class FXProject extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/fxproject.fxml"));
        Scene scene = new Scene(root,900,600);
        scene.getStylesheets().add("/fxml/fxproject.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Stan Powietrza");
        primaryStage.show();

    }
}
