package visualizer.exceptions;

/**
 * The Attribute not found Exception.
 * Created by frank on 2014/11/27.
 */

public class AttributeNotFoundException extends GraphVizApiException {
    private final String IDTAG = "GRAPH ID:";
    private final String ATTRTAG = "Attribute :";

    public AttributeNotFoundException(String message) {
        super(message);
    }
}
