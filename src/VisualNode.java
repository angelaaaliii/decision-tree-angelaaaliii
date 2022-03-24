package src;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to represent a node in the visualizer
 */
public class VisualNode {
    private String label;
    private Map<String, VisualNode> children;

    public VisualNode(String label) {
        this.label = label;
        this.children = new HashMap<>();
    }

    public int size() {
        int size = 1;
        for (VisualNode child : this.children.values()) {
            size += child.size();
        }
        return size;
    }

    public String getLabel() {
        return this.label;
    }

    public Map<String, VisualNode> getChildren() {
        return this.children;
    }
}
