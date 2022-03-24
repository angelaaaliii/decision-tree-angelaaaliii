package visualizer.exceptions;

public class DecisionTreeException extends RuntimeException {

    public DecisionTreeException(String message) {
        super();
        System.err.println(message);
        super.printStackTrace();
    }
}