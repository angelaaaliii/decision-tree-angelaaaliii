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

    public void setTargetAttribute(String targetAttribute) {
        this.targetAttribute = targetAttribute;
    }
}

