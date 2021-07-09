package thedrake.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class TheDrakeApp extends Application {

    public static final int DIMENSION = 4;

    public static void main ( String[] args ) {
        launch(args);
    }

    @Override
    public void start ( Stage primaryStage ) {
        setTitleScreen ( primaryStage, getClass().getResource("title.fxml") );
    }

    public static void setTitleScreen ( Stage stage, URL url ) {
        try {
            stage.setScene( new Scene(FXMLLoader.load(url)) );
        }
        catch ( IOException exc ) {
            exc.printStackTrace();
            return;
        }

        // Title, animated background
        stage.setTitle("The Drake");
        stage.setOnShown( event -> (new TitleScreenAnimation(stage.getScene().getRoot())).playFromStart() );
        stage.show();
    }

}
