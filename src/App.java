package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import visualizer.PaneOrganizer;

/**
 * App class used to run the provided visualizer.
 * Initiates the program -- to run and visualize your decision tree
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        PaneOrganizer po = new PaneOrganizer();
        Scene scene = new Scene(po.getRoot());
        stage.setScene(scene);
        stage.setTitle("Decision Tree");
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
