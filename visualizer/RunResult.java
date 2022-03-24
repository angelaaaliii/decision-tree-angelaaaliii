package visualizer;

import src.VisualNode;

public class RunResult {

    private boolean isSuccess;
    private String message;
    private VisualNode tree;
    private double accuracy;

    public RunResult(boolean isSuccess, String message, VisualNode tree, double accuracy) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.tree = tree;
        this.accuracy = accuracy;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public String getMessage() {
        return this.message;
    }

    public VisualNode getTree() {
        return this.tree;
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public static RunResult error(String message) {
        return new RunResult(false, message, null, 0);
    }

    public static RunResult success(VisualNode tree, double accuracy) {
        return new RunResult(true, null, tree, accuracy);
    }
}
