package visualizer;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.transform.Scale;

public class ZoomableScrollPane extends ScrollPane {
    private Scale scaleTransform;
    private double scaleValue = 1.0;
    private static final double delta = 0.1;

    ZoomableScrollPane(Node content) {
        Group contentGroup = new Group();
        Group zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(content);
        this.setContent(contentGroup);
        this.scaleTransform = new Scale(this.scaleValue, this.scaleValue, 0, 0);
        zoomGroup.getTransforms().add(this.scaleTransform);
    }

    void zoomTo(double scaleValue) {
        this.scaleValue = scaleValue;
        this.scaleTransform.setX(scaleValue);
        this.scaleTransform.setY(scaleValue);
    }

    void zoomOut() {
        this.scaleValue -= this.delta;
        if (Double.compare(this.scaleValue, 0.1) < 0) {
            this.scaleValue = 0.1;
        }
        this.zoomTo(this.scaleValue);
    }

    void zoomIn() {
        this.scaleValue += this.delta;
        if (Double.compare(this.scaleValue, 10) > 0) {
            this.scaleValue = 10;
        }
        this.zoomTo(this.scaleValue);
    }

    /**
     * @param minimizeOnly If the content fits already into the viewport, then we don't
     *                     zoom if this parameter is true.
     */
    void zoomToFit(boolean minimizeOnly) {

        double scaleX = this.getViewportBounds().getWidth() /
            this.getContent().getBoundsInLocal().getWidth();
        double scaleY =
            this.getViewportBounds().getHeight() / this.getContent().getBoundsInLocal().getHeight();

        // consider current scale (in content calculation)
        scaleX *= this.scaleValue;
        scaleY *= this.scaleValue;

        // distorted zoom: we don't want it => we search the minimum scale
        // factor and apply it
        double scale = Math.min(scaleX, scaleY);

        // check precondition
        if (minimizeOnly) {
            // check if zoom factor would be an enlargement and if so, just set
            // it to 1
            if (Double.compare(scale, 1) > 0) {
                scale = 1;
            }
        }
        // apply zoom
        this.zoomTo(scale);
    }
}
