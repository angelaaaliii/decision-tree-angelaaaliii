package visualizer;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import src.VisualNode;
import visualizer.graph.Edge;
import visualizer.graph.Graph;
import visualizer.graph.GraphAttribute;
import visualizer.graph.GraphViz;
import visualizer.graph.Node;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*
 * Visualizes a decision treee!
 *
 * @author skaragou
 */
public class TreeVisualizer {

    private GridPane treePane;
    private GraphViz graphviz;
    private Graph graph;
    private Integer counter;

    private Map<String, String> classificationToColor;
    private int classificationCount;
    private final String[] availableColors =
        {"deepskyblue", "darkorange", "forestgreen", "crimson", "gold"};

    public TreeVisualizer(GridPane treePane) {
        this.treePane = treePane;
        this.graphviz = new GraphViz();
        this.counter = 0;
    }

    void drawTree(VisualNode root) {

        this.treePane.getChildren().clear();

        this.graph = new Graph("g1");
        this.graph.addAttribute(new GraphAttribute("color", "blue"));

        this.classificationToColor = new HashMap<>();
        this.classificationCount = 0;
        this.createGraph(root);

        byte[] imageInByte = this.graphviz.getGraphByteArray(this.graph, "png", "100");

        try {
            InputStream in = new ByteArrayInputStream(imageInByte);
            BufferedImage bufferedImage = ImageIO.read(in);
            ImageView iv = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
            this.treePane.getChildren().add(iv);
            this.treePane.setAlignment(Pos.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Node createGraph(VisualNode node) {
        this.counter++;
        Node n1 = new Node(this.counter.toString());
        String label = node.getLabel();
        n1.addAttribute(new GraphAttribute("label", label));

        if (node.getChildren().isEmpty()) {
            n1.addAttribute(new GraphAttribute("shape", "diamond"));
            n1.addAttribute(new GraphAttribute("fontcolor", "white"));
            n1.addAttribute(new GraphAttribute("fontname", "Arial"));
            n1.addAttribute(new GraphAttribute("style", "filled"));

            String color = this.getColorForClassification(node.getLabel());
            n1.addAttribute(new GraphAttribute("fillcolor", color));
            n1.addAttribute(new GraphAttribute("color", color));

        } else {
            n1.addAttribute(new GraphAttribute("shape", "rectangle"));
            n1.addAttribute(new GraphAttribute("style", "filled"));
            n1.addAttribute(new GraphAttribute("fillcolor", "gray39"));
            n1.addAttribute(new GraphAttribute("color", "gray39"));
            n1.addAttribute(new GraphAttribute("fontcolor", "white"));
            n1.addAttribute(new GraphAttribute("fontname", "Arial"));
        }
        this.graph.addNode(n1);

        for (Map.Entry<String, VisualNode> edgeToChild : node.getChildren().entrySet()) {
            Node n2 = this.createGraph(edgeToChild.getValue());
            Edge edge = new Edge("", n1, n2);

            edge.addAttribute(new GraphAttribute("label", " " + edgeToChild.getKey()));
            edge.addAttribute(new GraphAttribute("color", "gray39"));
            edge.addAttribute(new GraphAttribute("fontcolor", "gray39"));
            edge.addAttribute(new GraphAttribute("fontname", "Arial"));
            this.graph.addEdge(edge);
        }
        return n1;
    }

    private String getColorForClassification(String classification) {
        if (!this.classificationToColor.containsKey(classification)) {
            int index = this.classificationCount % this.availableColors.length;
            this.classificationToColor.put(classification, this.availableColors[index]);
            this.classificationCount++;
        }
        return this.classificationToColor.get(classification);
    }
}
