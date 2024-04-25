package nz.ac.auckland.se206.controllers;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/** Controller for the loading scene that displays a loading image during loading of the API. */
public class LoadingController {

  @FXML private ImageView image;

  /** Initializes the controller and sets up the loading animation. */
  @FXML
  public void initialize() {
    // rotate image
    RotateTransition rotate = new RotateTransition();
    rotate.setNode(image);
    rotate.setDuration(Duration.millis(1500));
    rotate.setByAngle(360);
    // set cycle count
    rotate.setCycleCount(TranslateTransition.INDEFINITE);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setAxis(Rotate.Z_AXIS);
    rotate.play();
  }
}
