package visualizer.graph;

import visualizer.exceptions.AttributeNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Graphviz graph base object. It can add one or more GraphAttribute to
 * description.
 * Created by frank on 2014/11/19.
 */
public abstract class BaseGraphObject {
    private String id;
    private List<GraphAttribute> attrList;

    /**
     * Constructor.
     *
     * @param id This graph object's id.
     */
    public BaseGraphObject(String id) {
        this.id = id;
        this.attrList = new ArrayList<>();
    }

    /**
     * Add an GraphAttribute to GraphAttribute list.
     *
     * @param attr GraphAttribute
     */
    public void addAttribute(GraphAttribute attr) {
        this.attrList.add(attr);
    }

    /**
     * Remove GraphAttribute by GraphAttribute name. If this graph object has two or more GraphAttribute
     * with same name, it will remove them.
     *
     * @param graphAttributeName
     */
    public void removeAttribute(String graphAttributeName) {
        List<GraphAttribute> removeList = new ArrayList<>();
        for (GraphAttribute attr : this.attrList) {
            if (attr.getAttrName().equals(graphAttributeName)) {
                removeList.add(attr);
            }
        }
        if (removeList.size() == 0) {
            throw new AttributeNotFoundException(
                "ID: " + this.id + ";GraphAttribute:" + graphAttributeName);
        }
        for (GraphAttribute attr : removeList) {
            this.attrList.remove(attr);
        }
    }

    /**
     * Graph object id getter.
     *
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * GraphAttribute to dot string.
     *
     * @return dot format string.
     */
    protected String genAttributeDotString() {
        StringBuilder attrDotString = new StringBuilder();
        for (GraphAttribute attr : this.attrList) {
            attrDotString.append(attr.getAttrName() + "=" + attr.getAttrValue() + ";\n");
        }
        return attrDotString.toString();
    }

    /**
     * Convert this graph object to graphviz dot format.
     *
     * @return
     */
    abstract public String genDotString();

}

