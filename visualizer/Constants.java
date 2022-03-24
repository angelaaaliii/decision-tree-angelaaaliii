package visualizer;

/**
 * This is the constants class for the support code. Most of it is taken from the Convex Hull support
 * code and modified for this project.
 *
 * @author Jeffrey Kennan
 */
class Constants {
    /**
     * The width of the canvas background
     */
    public static final int CANVAS_WIDTH = 750;

    /**
     * The height of the canvas background
     */
    public static final int CANVAS_HEIGHT = 470;

    /**
     * The width of the sidebar
     */
    public static final int SIDEBAR_WIDTH = 100;

    /**
     * The height of the sidebar
     */
    public static final int SIDEBAR_HEIGHT = 20;

    /**
     * Radius of tree nodes on visualizer
     */
    public static final int NODE_SIZE = 70;

    /**
     * X position of the root node for visualizer.
     */
    public static final int ORIGIN_X = CANVAS_WIDTH / 2;

    /**
     * X position of the root node for visualizer.
     */
    public static final int ORIGIN_Y = NODE_SIZE;


    /**
     * Beginning string for text area
     */
    public static final String TEXT_AREA_DEFAULT =
        "Welcome to Decision Tree! To train a tree on a data set, "
            + "click \"Train\", and you'll be prompted to choose a data file. To test on a "
            +
            "different set of data, click \"Test\" and choose a testing data file. \n\nThis window "
            + "will provide more information as you perform these actions.\n\n"
            + "You must select a testing file as well as a training file before running it.";
}
