package visualizer;

import sol.Dataset;
import sol.TreeGenerator;
import src.DecisionTreeTester;
import src.VisualNode;

public class DecisionTreeRunner {

    private DecisionTreeTester<TreeGenerator, Dataset> treeTester;
    private DataLoader dataLoader;

    private Dataset trainingDataset;
    private Dataset testingDataset;
    private String targetAttribute;

    DecisionTreeRunner(DecisionTreeTester<TreeGenerator, Dataset> tester, DataLoader reader) {
        this.treeTester = tester;
        this.dataLoader = reader;
    }

    public DataLoader.Result loadTrainingData() {
        DataLoader.Result result = this.dataLoader.loadDataset();
        if (result.isPresent()) {
            this.trainingDataset = result.loadedDataset;
        }
        return result;
    }

    public DataLoader.Result loadTestingData() {
        DataLoader.Result result = this.dataLoader.loadDataset();
        if (result.isPresent()) {
            this.testingDataset = result.loadedDataset;
        }
        return result;
    }

    public RunResult run() {
        if (this.trainingDataset == null) {
            return RunResult.error("No training data loaded.");
        }
        if (this.testingDataset == null) {
            return RunResult.error("No testing data loaded");
        }
        if (this.targetAttribute == null || this.targetAttribute.equals("")) {
            return RunResult.error("No target attribute selected");
        }
        this.treeTester.getDecisionTreeAccuracy(this.trainingDataset, this.trainingDataset,
            this.targetAttribute);
        VisualNode regeneratedTree =
            this.treeTester.regenerateTreeFromTrainingData(this.trainingDataset);
        double accuracy =
            this.treeTester.getDecisionTreeAccuracy(this.testingDataset, this.targetAttribute);

        return RunResult.success(regeneratedTree, accuracy);
    }

//    void train(TextArea ta) {
//        trainingDataset = dataLoader.loadDataset();
//        if (trainingDataset == null) {
//            ta.appendText("Error: No file selected or unreadable file selected.\n\n");
//            return;
//        }
//        treeTester.getDecisionTreeAccuracy(trainingDataset, trainingDataset, "hired");
//        currentTree = treeTester.regenerateTreeFromTrainingData(trainingDataset);
//
//
//        if (currentTree == null) {
//            ta.appendText("Error: train tree returned null.\n\n");
//            return;
//        }
//
//        visualizer.drawTree(currentTree);
//    }


//    void test(TextArea ta) {
//        if (currentTree == null) {
//            ta.appendText("Error: Cannot test because tree has not been trained\n\n");
//            return;
//        }
//
//        if (trainingDataset == null) {
//            ta.appendText("Error: Cannot test because tree has not been trained\n\n");
//            return;
//        }
//
//        Dataset testingData = dataLoader.loadDataset();
//        if (testingData == null) {
//            ta.appendText("Error: No file selected or unreadable file selected");
//            return;
//        }
//
//
//        double accuracy = treeTester.getDecisionTreeAccuracy(testingData, "hired");
//        ta.appendText("Test results: " + accuracy * 100 + " % of examples were classified correctly!\n\n");
//    }

    public void setTargetAttribute(String targetAttribute) {
        this.targetAttribute = targetAttribute;
    }
}

