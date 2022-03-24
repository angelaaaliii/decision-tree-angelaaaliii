package src;

import sol.Dataset;
import sol.TreeGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionTreeTester<G extends ITreeGenerator<D>, D extends IDataset> {
    private static final String DATA_BASE = "data/";
    private static final String LIKE_TO_EAT = "likeToEat";

    // mushrooms dataset
    private static final String IS_POISONOUS = "isPoisonous";
    private static final String MUSHROOMS_BASE = DATA_BASE + "mushrooms/";
    private static final String MUSHROOMS_TRAINING = MUSHROOMS_BASE + "training.csv";
    private static final String MUSHROOMS_TESTING = MUSHROOMS_BASE + "testing.csv";

    // villains dataset
    private static final String IS_VILLAIN = "isVillain";
    private static final String VILLAINS_BASE = DATA_BASE + "villains/";
    private static final String VILLAINS_TRAINING = VILLAINS_BASE + "training.csv";
    private static final String VILLAINS_TESTING = VILLAINS_BASE + "testing.csv";

    // candidates dataset
    private static final String CANDIDATES_BASE = DATA_BASE + "candidates/";
    private static final String CANDIDATES_TRAINING_GENDER_EQUAL =
        CANDIDATES_BASE + "training-gender-equal.csv";
    private static final String CANDIDATES_TRAINING_GENDER_UNEQUAL =
        CANDIDATES_BASE + "training-gender-unequal.csv";
    private static final String CANDIDATES_TRAINING_GENDER_CORRELATED =
        CANDIDATES_BASE + "training-gender-correlated.csv";
    private static final String CANDIDATES_TESTING = CANDIDATES_BASE + "testing.csv";

    // songs dataset
    private static final String IS_POPULAR = "isPopular";
    private static final String SONG_BASE = DATA_BASE + "songs/";
    private static final String SONG_TRAINING = SONG_BASE + "training.csv";
    private static final String SONG_TESTING = SONG_BASE + "testing.csv";

    private Class<G> generatorClass;
    private Class<D> datasetClass;
    private ITreeGenerator<D> generator;
    private Map<Row, String> predictionMap;

    /**
     * Constructor for a recommender tester.
     *
     * @param datasetClass   the DataTable class
     * @param generatorClass the TreeGenerator class
     */
    public DecisionTreeTester(Class<G> generatorClass, Class<D> datasetClass)
        throws NoSuchMethodException, IllegalAccessException,
        InvocationTargetException, InstantiationException {
        this.generatorClass = generatorClass;
        this.datasetClass = datasetClass;
        Constructor<G> generatorConstructor = this.generatorClass.getConstructor();
        generatorConstructor.setAccessible(true);
        this.predictionMap = new HashMap<>();
        this.generator = generatorConstructor.newInstance();
    }

    public double getAverageDecisionTreeAccuracy(String trainingDataPath, String testingDataPath,
                                                 String targetAttribute, int numIterations)
        throws InvocationTargetException, NoSuchMethodException,
        InstantiationException, IllegalAccessException {

        D trainingData = makeDataset(trainingDataPath, this.datasetClass);
        D testingData = makeDataset(testingDataPath, this.datasetClass);
        return this.getAverageDecisionTreeAccuracy(trainingData, testingData, targetAttribute,
            numIterations);
    }

    public double getAverageDecisionTreeAccuracy(D trainingData, D testingData,
                                                 String targetAttribute, int numIterations) {

        double[] accuracies = new double[numIterations];
        for (int i = 0; i < numIterations; i++) {
            accuracies[i] = this.getDecisionTreeAccuracy(trainingData, testingData, targetAttribute);
        }
        return this.getMean(accuracies);
    }

    public double getDecisionTreeAccuracy(D trainingData, D testingData, String targetAttribute) {
        this.generator.generateTree(trainingData, targetAttribute);
        return this.getDecisionTreeAccuracy(testingData, targetAttribute);
    }

    public double getDecisionTreeAccuracy(D testingData, String targetAttribute) {
        double numCorrectClassifications = 0;
        for (Row datum : testingData.getDataObjects()) {
            String prediction = this.generator.getDecision(datum);
            if (prediction.equals(datum.getAttributeValue(targetAttribute))) {
                numCorrectClassifications += 1;
            }
            // store prediction so that we can potentially use it later to
            // regenerate tree structure from root-to-leaf paths
            this.predictionMap.put(datum, this.generator.getDecision(datum));
        }
        return numCorrectClassifications / testingData.size();
    }

    /**
     * Generates our own tree structure from the training data that
     * was used to generate the tree that (the copy) was passed back into the
     * decision tree, such that each datum explored and recorded
     * (see {@link Row#getAccessOrder}) a single path from root to leaf of
     * the tree. Because this dataset is the training dataset on which the tree
     * was generated, this list of root-to-leaf paths is enough to regenerate
     * the entire tree.
     *
     * @param trainingData the training data used to generate
     *                     the decision tree that we want to recreate
     *                     that (the copy) was passed back into
     *                     the decision tree, such that each datum
     *                     explored and recorded a single path from
     *                     root to leaf of the tree
     * @return a tree structure representing the tree generated from the
     * inputted training data
     */
    public VisualNode regenerateTreeFromTrainingData(D trainingData) {

        // record decisions for each datum in set
        for (Row datum : trainingData.getDataObjects()) {
            datum.clearAccessOrder(); // clear access order to get fresh recording
            this.predictionMap.put(datum, this.generator.getDecision(datum));
        }

        VisualNode root = null;
        for (Row datum : trainingData.getDataObjects()) {
            List<String> attributeAccessOrder = datum.getAccessOrder();
            if (root == null) {
                root = this.generateFromPath(attributeAccessOrder, datum);
            } else {
                VisualNode previous = null;
                VisualNode current = root;
                while (current != null && !attributeAccessOrder.isEmpty()) {
                    assert (current.getLabel().equals(attributeAccessOrder.get(0)));
                    attributeAccessOrder.remove(current.getLabel());
                    previous = current;
                    current =
                        current.getChildren().get(datum.getAttributeValue(current.getLabel()));
                }
                if (previous != null) {
                    previous.getChildren().put(datum.getAttributeValue(previous.getLabel()),
                        this.generateFromPath(attributeAccessOrder, datum));
                }
            }
        }
        return root;
    }

    private VisualNode generateFromPath(List<String> attributePath, Row datum) {
        if (attributePath.isEmpty()) {
            return new VisualNode(this.predictionMap.get(datum));
        } else {
            String attribute = attributePath.get(0);
            attributePath.remove(attribute);

            VisualNode newNode = new VisualNode(attribute);
            newNode.getChildren().put(datum.getAttributeValue(attribute),
                this.generateFromPath(attributePath, datum));
            return newNode;
        }
    }

    private double getMean(double[] arr) {
        double sum = 0;
        for (double d : arr) {
            sum += d;
        }
        return sum / arr.length;
    }

    public static <D extends IDataset> D makeDataset(String dataPath, Class<D> datasetClass)
        throws NoSuchMethodException,
        IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Row> dataList = DecisionTreeCSVParser.parse(dataPath);

        Constructor<D> constructor = datasetClass.getConstructor(List.class, List.class);
        constructor.setAccessible(true);
        return constructor.newInstance(getAttributesFromData(dataList), dataList);
    }

    private static List<String> getAttributesFromData(List<Row> data) {
        Set<String> attributeSet = new HashSet<>();
        for (Row datum : data) {
            attributeSet.addAll(datum.getAttributes());
        }
        return new ArrayList<>(attributeSet);
    }

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "javafx");

        DecisionTreeTester<TreeGenerator, Dataset> tester;
        try {
            tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);
            Dataset trainingData = makeDataset(MUSHROOMS_TRAINING, Dataset.class);
            double accuracy =
                tester.getDecisionTreeAccuracy(trainingData, trainingData, IS_POISONOUS);
            System.out.println("Accuracy on training data: " + accuracy);
            VisualNode tree = tester.regenerateTreeFromTrainingData(trainingData);
           // TreeVisualizer.visualizeTree(tree);

        } catch (InstantiationException | InvocationTargetException
            | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
