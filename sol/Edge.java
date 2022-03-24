package sol;

/**
 * The Edge class is the representation of a decision tree's branch that
 * connects an ITreeNode to another ITreeNode while representing
 * possible values from the attribute node above it.
 * The Edge class has two fields, value and next. Value is the edge's value and
 * next is an ITreeNode that contains either the next Leaf or Node.
 */
public class Edge {
    private String value;
    private ITreeNode next;

    /**
     * Constructs an Edge object
     *
     * @param value - the edge's value from the ITreeNode above it
     * @param next  - the ITreeNode the edge extends to
     */
    public Edge(String value, ITreeNode next) {
        this.value = value;
        this.next = next;
    }

    /**
     * indicates whether the input attribute value equals the value stored
     * in this edge.
     *
     * @param value - String representing the value we want to check if
     *              it is contained in the edge.
     * @return true if the input value is the same as the edge value, false
     * otherwise.
     */
    public boolean sameValue(String value) {
        return this.value.equals(value);
    }

    /**
     * returns the ITreeNode that the edge is pointing to (either leaf or node).
     *
     * @return the field value of this edge object's next field, an ITreeNode.
     */
    public ITreeNode getNext() {
        return this.next;
    }
}
