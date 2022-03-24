package src;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Map;

/**
 * Class used to represent the visualizer
 */
public class TreeVisualizer {
    private Graph graph;
    private int numNodes;

    public static void visualizeTree(VisualNode tree) {
        TreeVisualizer visualizer = new TreeVisualizer(tree);
        visualizer.display();
    }

    public TreeVisualizer(VisualNode tree) {
        this.graph = new SingleGraph("Decision Tree");
        this.populateGraphFromTree(tree);
    }

    public void display() {
        this.graph.setAttribute("ui.stylesheet", GRAPH_STYLE_SHEET);
        this.graph.display();
    }

    private void populateGraphFromTree(VisualNode root) {
        this.numNodes = 0;
        this.populateGraphFromTreeHelper(null, root, null);
    }

    private void populateGraphFromTreeHelper(String edgeValue, VisualNode node, String parentId) {
        this.numNodes++;

        String nodeId = node.getLabel() + this.numNodes;
        Node n = this.graph.addNode(nodeId);
        n.setAttribute("ui.label", node.getLabel());
        n.setAttribute("layout.weight", 100);
        if (edgeValue != null && !edgeValue.isBlank()) {
            Edge e = this.graph.addEdge(parentId + nodeId, parentId, nodeId, true);
            e.setAttribute("ui.label", edgeValue);
            e.setAttribute("layout.weight", 100);
        }
        for (Map.Entry<String, VisualNode> edge : node.getChildren().entrySet()) {
            this.populateGraphFromTreeHelper(edge.getKey(), edge.getValue(), nodeId);
        }
    }

    private static final String GRAPH_STYLE_SHEET =
        "graph {" +
            "fill-color: white;" +
            "}" +
            "node {" +
            "shape: box;" +
            "size-mode: fit;" +
            "fill-color: white;" +
            "stroke-mode: plain;" +
            "stroke-color: black;" +
            "text-background-mode: plain;" +
            "}" +
            "edge {" +
            "fill-color: black;" +
            "}";
}
