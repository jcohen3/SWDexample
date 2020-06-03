/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This JavaFX app lets the user play scales.
 *
 * @author Janet Davis
 * @author SOLUTION - PROJECT 1
 * @since January 26, 2017
 */
public class TuneComposer extends Application {

    public Stage primaryStage;
    private BorderPane root;

    /**
     * Construct the scene and start the application.
     *
     * @param primaryStage the stage for the main window
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TuneComposer.fxml"));

        root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Tune Player");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });

        primaryStage.show();

        TuneComposerController controller = loader.getController();
        controller.compositionPaneController.addLines();

    }

    /**
     * Launch the application.
     *
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }

}
