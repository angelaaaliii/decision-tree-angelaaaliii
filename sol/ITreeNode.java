package sol;

import src.Row;

/**
 * Interface for nodes and leaves of tree.
 * <p>
 */
public interface ITreeNode {

    /**
     * Recursively traverses decision tree to return tree's decision for a row.
     *
     * @param forDatum the datum to lookup a decision for
     * @return the decision tree's decision
     */
    public String getDecision(Row forDatum);
}
