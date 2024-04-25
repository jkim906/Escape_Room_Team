package nz.ac.auckland.se206;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/** Class for adding hover effects to nodes. */
public class HoverEffect {

  /**
   * Adds a hover effect to the given node.
   *
   * @param node the node to add the hover effect to
   * @param scaleFactor the scale factor to scale the node by
   */
  public static void addHoverEffect(Node node, double scaleFactor) {
    if (node == null) {
      return;
    }
    // create scale transitions
    ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), node);
    scaleIn.setToX(scaleFactor);
    scaleIn.setToY(scaleFactor);

    ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), node);
    scaleOut.setToX(1.0);
    scaleOut.setToY(1.0);

    // add event handler for when mouse enters node
    node.addEventHandler(
        MouseEvent.MOUSE_ENTERED,
        event -> {
          scaleIn.playFromStart();
        });

    // add event handler for when mouse exits node
    node.addEventHandler(
        MouseEvent.MOUSE_EXITED,
        event -> {
          scaleOut.playFromStart();
        });
  }
}
