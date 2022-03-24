package sol;

import src.Row;

/**
 * The Leaf class represents the leaf node in the decision tree. It contains
 * the prediction, or the decision, that the algorithm should reach when
 * traversing down the tree to predict the outcome for a given datum.
 */
public class Leaf implements ITreeNode {
    private String decision;

    /**
     * Constructs a Leaf object
     *
     * @param decision - the value of the target attribute at the bottom of the
     *                 decision tree
     */
    public Leaf(String decision) {
        this.decision = decision;
    }

    /**
     * Recursively traverses decision tree to return tree's decision for a row.
     *
     * @param forDatum the datum to lookup a decision for
     * @return the decision tree's decision
     */
    public String getDecision(Row forDatum) {
        return this.decision;
    }

}
