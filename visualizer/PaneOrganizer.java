package visualizer;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import sol.Dataset;
import sol.TreeGenerator;
import src.DecisionTreeTester;

import java.lang.reflect.InvocationTargetException;

public class PaneOrganizer {

    private final BorderPane borderPane;
    private ZoomableScrollPane zoomPane;
    private final GridPane treePane;


    public PaneOrganizer() {
        this.borderPane = new BorderPane();
        DataLoader reader = new DataLoader(this.borderPane);
        this.treePane = this.createVizPane();
        try {
            DecisionTreeRunner runner = new DecisionTreeRunner(
                new DecisionTreeTester<>(TreeGenerator.class, Dataset.class), reader);
            this.createControlPane(runner);
        } catch (InstantiationException | InvocationTargetException
            | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void createControlPane(DecisionTreeRunner runner) {
        ControlPanel controlPanel =
            new ControlPanel(runner, this.zoomPane, new TreeVisualizer(this.treePane));
        this.borderPane.setBottom(controlPanel.getRoot());
    }

    private GridPane createVizPane() {
        GridPane treePane = new GridPane();
        treePane.setMinWidth(Constants.CANVAS_WIDTH);
        treePane.setStyle("-fx-background-color: #D3D3D3;");
        this.zoomPane = new ZoomableScrollPane(treePane);
        this.zoomPane.setPrefSize(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
        this.zoomPane.setPannable(true);
        this.borderPane.setCenter(this.zoomPane);
        return treePane;
    }

    public Pane getRoot() {
        return this.borderPane;
    }
}