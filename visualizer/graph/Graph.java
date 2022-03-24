package visualizer.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * The Graphviz Graph class. It also can use to build subgraph.
 * Created by frank on 2014/11/20.
 */
public class Graph extends BaseGraphObject {

    private List<Node> nodeList;
    private List<Edge> edgeList;
//    private List<Graph> subgraphList;

    /**
     * Constructor.
     *
     * @param id This Graph's id.
     */
    public Graph(String id) {
        super(id);
        this.nodeList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
//        this.subgraphList = new ArrayList<>();
    }

    /**
     * Add a node to this graph.
     *
     * @param node
     */
    public void addNode(Node node) {
        this.nodeList.add(node);
    }

    /**
     * Add an edge to this graph.
     *
     * @param edge
     */
    public void addEdge(Edge edge) {
        this.edgeList.add(edge);
    }

    @Override
    public String genDotString() {
        StringBuilder dotString = new StringBuilder();
        dotString.append("{\n");
        dotString.append("nodesep=1.0;");
        dotString.append("graph [bgcolor=lightgrey];");
        dotString.append(this.genAttributeDotString());
//        dotString.append(this.genSubgraphString());
        dotString.append(this.genNodesString());
        dotString.append(this.genEdgeDotString());
        dotString.append("}\n");
        return dotString.toString();
    }

//    private String genSubgraphString() {
//        StringBuilder subgraphString = new StringBuilder();
//        for (Graph graph : this.subgraphList) {
//            subgraphString.append("subgraph ");
//            subgraphString.append(graph.getId());
//            subgraphString.append(graph.genDotString());
//            subgraphString.append("\n");
//        }
//        return subgraphString.toString();
//    }

    private String genNodesString() {
        StringBuilder nodeString = new StringBuilder();
        for (Node node : this.nodeList) {
            nodeString.append(node.getId());
            nodeString.append(node.genDotString());
            nodeString.append("\n");
        }
        return nodeString.toString();
    }

    private String genEdgeDotString() {
        StringBuilder edgeString = new StringBuilder();
        for (Edge edge : this.edgeList) {
            edgeString.append(edge.getFromNode().getId() + "->" + edge.getToNode().getId());
            edgeString.append(edge.genDotString());
            edgeString.append("\n");
        }
        return edgeString.toString();
    }
}

