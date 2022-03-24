package visualizer;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;


/**
 * This class contains all of the buttons needed for a (first draft) GUI to appear.
 * <p>
 * It's largely lifted from ConvexHull support.
 *
 * @author Jeffrey Kennan
 */
class ControlPanel {

    private VBox container;

    private HBox buttonPane;
    private HBox trainingPane;
    private HBox testingPane;
    private HBox textAreaPane;

    private TextArea textArea;
    private ZoomableScrollPane zoomPane;

    private DecisionTreeRunner runner;
    private TreeVisualizer visualizer;

    public ControlPanel(DecisionTreeRunner runner, ZoomableScrollPane zoomPane,
                        TreeVisualizer visualizer) {
        this.runner = runner;
        this.zoomPane = zoomPane;
        this.visualizer = visualizer;
        this.setupContainer();
    }

    private void setupContainer() {
        this.container = new VBox();
        this.setupButtonPane();
        this.setupTrainingPane();
        this.setupTestingPane();
        this.setupTextAreaPane();
        this.container.getChildren().addAll(this.buttonPane, this.trainingPane, this.testingPane,
            this.textAreaPane);
    }


    private void setupButtonPane() {
        this.buttonPane = this.createStandardHBox();

        Button runButton = this.createStandardButton("Run");
        runButton.setOnAction((ActionEvent e) -> {
            RunResult result = this.runner.run();
            if (result.isSuccess()) {
                this.visualizer.drawTree(result.getTree());
                this.textArea.appendText(
                    "Classification accuracy on testing data: " + result.getAccuracy() + "\n\n");
            } else {
                this.textArea.appendText(result.getMessage() + "\n\n");
            }
        });

        Button quitButton = this.createStandardButton("Quit");
        quitButton.setOnAction((ActionEvent e) -> {
            quitButton.setEffect(new DropShadow());
            Platform.exit();
        });

        Button zoomInButton = this.createStandardButton("+");
        zoomInButton.setOnAction((ActionEvent e) -> {
            this.zoomPane.zoomIn();
        });


        Button zoomOutButton = this.createStandardButton("-");
        zoomOutButton.setOnAction((ActionEvent e) -> {
            this.zoomPane.zoomOut();
        });

        this.buttonPane.getChildren().addAll(runButton, quitButton, zoomInButton, zoomOutButton);
    }

    private void setupTrainingPane() {
        this.trainingPane = this.createStandardHBox();

        Label label = new Label("Training");

        Text fileNameText = new Text("");

        ChoiceBox<String> targetAttributeSelector = new ChoiceBox<>();
        targetAttributeSelector.setDisable(true);
        targetAttributeSelector.setOnAction((ActionEvent e) -> {
            this.runner.setTargetAttribute(targetAttributeSelector.getValue());
        });

        Button loadTrainingDataButton = new Button("Load data");
        loadTrainingDataButton.setOnAction((ActionEvent e) -> {
            DataLoader.Result result = this.runner.loadTrainingData();
            if (result.isPresent()) {
                fileNameText.setText(this.getFileNameFromPath(result.fileName));

                List<String> attributeList = result.loadedDataset.getAttributeList();
                targetAttributeSelector.setItems(FXCollections.observableList(attributeList));
                targetAttributeSelector.setDisable(false);
            }
        });

        this.trainingPane.getChildren().addAll(label, loadTrainingDataButton,
            fileNameText, targetAttributeSelector);
    }

    private void setupTestingPane() {
        this.testingPane = this.createStandardHBox();

        Label label = new Label("Testing");
        Text fileNameText = new Text("");
        Button loadTestingDataButton = new Button("Load data");
        loadTestingDataButton.setOnAction((ActionEvent e) -> {
            DataLoader.Result result = this.runner.loadTestingData();
            if (result.isPresent()) {
                fileNameText.setText(this.getFileNameFromPath(result.fileName));
            }
        });

        this.testingPane.getChildren().addAll(label, loadTestingDataButton, fileNameText);
    }

//    /**
//     * Creates the buttons that end up on the sidebar.
//     */
//    private void setupButtons() {
//        Button testButton = new Button("Test");
//        testButton.setDisable(true);
//        testButton.setOnAction((ActionEvent e) -> {
//            textArea.appendText("Testing algorithm and displaying results.\n\n");
//            runner.test(textArea);
//        });
//        testButton.setStyle("-fx-background-color: #a8a8a8;-fx-text-fill: black;");
//
//        Button trainButton = new Button("Train");
//        trainButton.setOnAction((ActionEvent e) -> {
//            textArea.appendText("Training algorithm and displaying tree\n\n");
//            runner.train(textArea);
//            trainButton.setText("Loaded");
//            testButton.setDisable(false);
//        });
//        trainButton.setEffect(null);
//        trainButton.setStyle("-fx-background-color: #a8a8a8;-fx-text-fill: black;");
//
//        Rectangle seperator = new Rectangle(10,0);
//
//        hBox.getChildren().addAll(trainButton, testButton, quitButton ,seperator,zoomInButton,zoomOutButton);
//    }


    private void setupTextAreaPane() {
        this.setupTextArea();
        this.textAreaPane = new HBox();
        this.textAreaPane.setAlignment(Pos.CENTER);
        this.textAreaPane.getChildren().add(this.textArea);
    }

    private void setupTextArea() {
        this.textArea = new TextArea();
        this.textArea.setEditable(false);
        this.textArea.setWrapText(true);
        this.textArea.setPrefWidth(Constants.CANVAS_WIDTH);
        this.textArea.setText(Constants.TEXT_AREA_DEFAULT);
    }

    private HBox createStandardHBox() {
        HBox box = new HBox(10);
        box.setPadding(new Insets(5, 5, 5, 5));
        box.setStyle("-fx-background-color: #c6c6c6;");
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(Constants.SIDEBAR_WIDTH);
        return box;
    }

    private Button createStandardButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #a8a8a8;-fx-text-fill: black;");
        return b;
    }

    private String getFileNameFromPath(String path) {
        String[] split = path.split("/");
        if (split.length >= 2) {
            return split[split.length - 2] + "/" + split[split.length - 1];
        } else {
            return split[0];
        }
    }

    /**
     * Returns the VBox for use in PaneOrganizer.
     * @return the VBox the sidebar is stored in
     */
    public VBox getRoot() {
        return this.container;
    }
}

