package sol;

import src.ITreeGenerator;
import src.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that implements the ITreeGenerator interface
 * used to generate a tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {
    private String targetAttribute;
    private ITreeNode tree;

    /**
     * Creates an ITreeNode and its following subtrees
     *
     * @param training - the dataset that represents the data needed to be used
     *                 to create the rest of the tree underneath it
     * @return an ITreeNode representing the decision tree created from
     * the given training dataset.
     */
    public ITreeNode generateHelper(Dataset training) {
        // picking random attribute:
        Random random = new Random();
        int upperBound = training.getAttributeList().size();
        int randomNum = random.nextInt(upperBound);
        String nodeAttribute = training.getAttributeList().get(randomNum);

        // compute default value for this attribute node.
        String defaultValue = training.computeDefaultValue(
                this.targetAttribute);

        // split the dataset based on the attribute created for the node.
        List<Dataset> newDataSet = training.partition(nodeAttribute);

        // initialize list of edges to add to.
        List<Edge> edges = new ArrayList<Edge>();

        for (Dataset subset : newDataSet) {

            if (subset.allSameValue(this.targetAttribute)) {
                // if all of the dataset down this edge has the same outcome,
                // make a leaf.
                Edge edge = new Edge(subset.getSingleValue(nodeAttribute),
                        new Leaf(subset.getSingleValue(this.targetAttribute)));
                edges.add(edge);
            } else if (subset.getAttributeList().size() == 0) {
                // if have no more attributes to split on but still have
                // non-unique outcomes, create a leaf storing the most
                // common decision value in the dataset at that point.
                Edge edge = new Edge(subset.getSingleValue(nodeAttribute),
                        new Leaf(subset.computeDefaultValue(
                                this.targetAttribute)));
                edges.add(edge);
            } else {
                // make edge that connects to an attribute node
                Edge edge = new Edge(subset.getSingleValue(nodeAttribute),
                        generateHelper(subset));
                // recursive call to build subtree
                edges.add(edge);
            }
        }
        return new Node(nodeAttribute, defaultValue, edges);
    }


    /**
     * Generates the tree for a given training dataset.
     *
     * @param trainingData    the dataset to train on
     * @param targetAttribute the attribute to predict
     * @throws RuntimeException - the generateTree method throws a
     *                          RuntimeException error if the given Dataset
     *                          argument contains an empty list of Rows.
     */

    public void generateTree(Dataset trainingData, String targetAttribute) {
        // store original data in the TreeGenerator object's fields
        this.targetAttribute = targetAttribute;
        if (trainingData.getDataObjects().size() == 0) {
            throw new RuntimeException("No rows received");
        }
        // removing target attribute from list of available attributes to
        // split on
        trainingData.getAttributeList().remove(targetAttribute);
        this.tree = this.generateHelper(trainingData);
    }

    /**
     * Recursively traverses the generated ITreeNode (the generated decision
     * tree) to predict the outcome for the given datum.
     *
     * @param datum the datum to lookup a decision for
     * @return String representing the prediction for the given datum, using
     * the generated decision tree.
     */
    public String getDecision(Row datum) {
        return this.tree.getDecision(datum);
    }

}
