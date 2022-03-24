package visualizer.graph;

/**
 * An Attribute Class. It will store graphviz attribute.
 * Created by frank on 2014/11/20.
 */
public class GraphAttribute {
    private String attrName;
    private String attrValue;

    /**
     * @param name  Attribute name. Like 'label' 'color'...etc.
     * @param value Attrubute value. Like 'blue'...
     */
    public GraphAttribute(String name, String value) {
        this.attrName = name;
        this.attrValue = value;
    }

    /**
     * Attribute name getter.
     *
     * @return attribute name.
     */
    public String getAttrName() {
        return this.attrName;
    }

    /**
     * Attribute value getter.
     *
     * @return attribute value.
     */
    public String getAttrValue() {
        if (this.attrName.equals("label")) {
            return "\"" + this.attrValue + "\"";
        }
        return this.attrValue;
    }

}
