package sol;

import src.Row;

import java.util.List;

/**
 * The Node class represents an attribute node in the decision class. It
 * stores the attribute type that we are looking at, the default attribute
 * value to use if trying to predict the outcome of an object that has
 * an unseen attribute value, and a list of edges that the node connects to
 * in the decision tree.
 */
public class Node implements ITreeNode {
    private String attribute;
    private String defaultValue;
    private List<Edge> edges;

    /**
     * Constructs a Node object
     *
     * @param attribute    - the Node's attribute
     * @param defaultValue - the Node's defaultValue
     * @param edges        - a list of the Node's edges extending down the tree
     */
    public Node(String attribute, String defaultValue, List<Edge> edges) {
        this.attribute = attribute;
        this.defaultValue = defaultValue;
        this.edges = edges;
    }


    /**
     * Recursively traverses decision tree to return tree's decision for a row.
     *
     * @param forDatum the datum to lookup a decision for
     * @return the decision tree's decision
     */
    public String getDecision(Row forDatum) {
        String value = forDatum.getAttributeValue(this.attribute);
        for (Edge edge : this.edges) {
            if (edge.sameValue(value)) {
                return edge.getNext().getDecision(forDatum);
            }
        }
        return this.defaultValue;
    }
}
