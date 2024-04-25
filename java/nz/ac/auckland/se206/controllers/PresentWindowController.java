package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;

/**
 * Controller for PresentWindow.fxml
 *
 * <p>Handles events and user interaction for the PresentWindow scene
 */
public class PresentWindowController {

  @FXML private ImageView imageView;
  @FXML private ImageView key;
  @FXML private AnchorPane anchor;
  @FXML private ImageView returnButton;
  @FXML private ImageView scope;
  @FXML private ImageView circle;
  @FXML private Label presentWindowTimer;
  @FXML private ImageView key2;
  @FXML private ProgressBar progressBarWindow;
  @FXML private Label upKey;
  @FXML private Label leftKey;
  @FXML private Label rightKey;
  @FXML private Label downKey;
  @FXML private ImageView upButton;
  @FXML private ImageView downButton;
  @FXML private ImageView leftButton;
  @FXML private ImageView rightButton;
  @FXML private ImageView gamemaster;
  @FXML private Label labelHint;

  private boolean isMedium;
  private double stepSize = 15.0;
  private double horizontal;
  private double vertical;

  /**
   * Method to initialize the scene
   *
   * <p>Initializes the scene by adding hover effects to buttons and setting the visibility of
   * objects.
   *
   * <p>Initializes the timer and progress bar.
   *
   * <p>Initializes the checking of the win and lose conditions..
   */
  public void initialize() {
    HoverEffect.addHoverEffect(leftButton, 1.2);
    HoverEffect.addHoverEffect(rightButton, 1.2);
    HoverEffect.addHoverEffect(upButton, 1.2);
    HoverEffect.addHoverEffect(downButton, 1.2);
    HoverEffect.addHoverEffect(gamemaster, 1.2);
    HoverEffect.addHoverEffect(returnButton, 1.2);

    // set objects visibility
    if (GameState.isObject1Visible) {
      key.setVisible(true);
    } else {
      key2.setVisible(true);
    }

    // progress bar getting the time left
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  // update progress bar
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  double progress = ((double) timerValue) / timerMax;
                  progressBarWindow.setProgress(progress);
                }));
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    progressBarUpdater.play();

    scope.setMouseTransparent(true);
    circle.setMouseTransparent(true);
    upKey.setMouseTransparent(true);
    leftKey.setMouseTransparent(true);
    rightKey.setMouseTransparent(true);
    downKey.setMouseTransparent(true);
    // set the timer
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          // This method will be executed in a background thread.
          // Updates the timer.
          @Override
          public void run() {
            // update timer
            isMedium = GameState.isLevel == 2;
            // update timer and hints
            Platform.runLater(() -> presentWindowTimer.setText(GameState.toPrintTime));
            Platform.runLater(() -> anchor.requestFocus());
            Platform.runLater(
                () -> {
                  // update medium diffculty
                  if (isMedium) {
                    labelHint.setVisible(true);
                    labelHint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                  presentWindowTimer.setText(GameState.toPrintTime);
                });
          }
        };

    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  // move the window around depending on the player(up)

  /** Method to handle when the up button is clicked. */
  public void upClicked() {
    if (vertical <= 465) {
      System.out.println("up");
      imageView.setY(vertical += 15);
      key.setY(vertical += 15);
      key2.setY(vertical += 15);
      System.out.println(imageView.getY());
    } else {
      System.out.println("up");
      // move window
      imageView.setY(vertical += 0);
      key.setY(vertical += 0);
      key2.setY(vertical += 0);
      System.out.println(imageView.getY());
    }
  }

  // move the window around depending on the player(down)

  /** Method to handle when the down button is clicked. */
  public void downClicked() {
    if (vertical >= -510) {
      // move window
      System.out.println("down");
      imageView.setY(vertical -= 15);
      key.setY(vertical -= 15);
      key2.setY(vertical -= 15);
      System.out.println(imageView.getY());
    } else {
      System.out.println("down");
      // move window
      imageView.setY(vertical -= 0);
      key.setY(vertical -= 0);
      key2.setY(vertical -= 0);
      System.out.println(imageView.getY());
    }
  }

  // move the window around depending on the player(left)

  /** Method to handle when the left button is clicked. */
  public void leftClicked() {
    if (horizontal <= 915) {
      System.out.println("left");
      // move window
      imageView.setX(horizontal += 15);
      key.setX(horizontal += 15);
      key2.setX(horizontal += 15);
      System.out.println(imageView.getX());
    } else {
      System.out.println("left");
      // move window
      imageView.setX(horizontal += 0);
      key.setX(horizontal += 0);
      key2.setX(horizontal += 0);
      System.out.println(imageView.getX());
    }
  }

  // move the window around depending on the player(right)

  /** Method to handle when the right button is clicked. */
  public void rightClicked() {
    if (horizontal >= -465) {
      System.out.println("right");
      // move window
      imageView.setX(horizontal -= 15);
      key.setX(horizontal -= 15);
      key2.setX(horizontal -= 15);
      System.out.println(imageView.getX());
      // move window
    } else {
      imageView.setX(horizontal -= 0);
      key.setX(horizontal -= 0);
      key2.setX(horizontal -= 0);
      System.out.println(imageView.getX());
      // move window
    }
  }

  // event handler for when key is pressed

  /** Method to handle when the key is clicked. */
  @FXML
  public void onKeyPressedTwo() {
    // check if its two player or one player to update the right gamestate variable.
    if (GameState.isPlayerScore == 2 && GameState.whichPlayer == 1) {
      GameState.pOneEasterEgg = true;
    } else if (GameState.isPlayerScore == 2 && GameState.whichPlayer == 2) {
      GameState.pTwoEasterEgg = true;
    } else {
      GameState.isWindowObjectFound = true;
    }
    // set the key to be invisible and update the items picked up.
    key.setVisible(false);
    GameState.itemsPickedUp = GameState.itemsPickedUp + "Cash(Bonus)\n";
  }

  // return button pressed using sub method

  /** Method to handle when the return button is pressed. */
  @FXML
  public void returnButtonPressed() throws IOException {
    App.loadScene("present", SceneName.PRESENT);
  }

  // event handler for when key is pressed depending on key

  /**
   * Method to handle when the key is pressed.
   *
   * @param event the key event that is triggered based on the key pressed.
   */
  @FXML
  private void handleKeyPressed(KeyEvent event) {
    KeyCode code = event.getCode();
    // Move the window up if the W key is pressed.
    if (code == KeyCode.W) {
      moveUp();
      System.out.println("up");
      // Move the window left if the A key is pressed.
    } else if (code == KeyCode.A) {
      moveLeft();
      System.out.println("left");
      // Move the window down if the S key is pressed.
    } else if (code == KeyCode.S) {
      moveDown();
      System.out.println("down");
      // Move the window right if the D key is pressed.
    } else if (code == KeyCode.D) {
      moveRight();
      System.out.println("right");
    }
  }

  // move the window around depending on the player(up)

  /** Method to handle moving the scene objects when right action activated. */
  private void moveRight() {
    if (imageView.getX() > -465) {
      // move window
      imageView.setX(imageView.getX() - stepSize);
      key.setX(key.getX() - stepSize);
      key2.setX(key.getX() - stepSize);
      System.out.println(imageView.getX());
    } else {
      // move window
      imageView.setX(imageView.getX() - 0);
      key.setX(key.getX() - 0);
      key2.setX(key.getX() - 0);
      System.out.println(imageView.getX());
    }
  }

  // Move the window around depending on the player(right).

  /** Method to handle moving the scene objects when up action activated. */
  private void moveUp() {
    if (imageView.getY() <= 465) {
      imageView.setY(imageView.getY() + stepSize);
      // move window
      key.setY(key.getY() + stepSize);
      key2.setY(key.getY() + stepSize);
      System.out.println(imageView.getY());
    } else {
      imageView.setY(imageView.getY() + 0);
      key.setY(key.getY() + 0);
      key2.setY(key.getY() + 0);
      // move window
      System.out.println(imageView.getY());
    }
  }

  // Move the window around depending on the player(down)

  /** Method to handle moving the scene objects when down action activated. */
  private void moveDown() {
    if (imageView.getY() >= -480) {
      imageView.setY(imageView.getY() - stepSize);
      // move window
      key.setY(key.getY() - stepSize);
      key2.setY(key.getY() - stepSize);
      System.out.println(imageView.getY());
    } else {
      imageView.setY(imageView.getY() - 0);
      // move window
      key.setY(key.getY() - 0);
      key2.setY(key.getY() - 0);
      System.out.println(imageView.getY());
    }
  }

  /** Method to handle moving the scene objects when left action activated. */
  private void moveLeft() {
    if (imageView.getX() <= 915) {
      imageView.setX(imageView.getX() + stepSize);
      // move window
      key.setX(key.getX() + stepSize);
      key2.setX(key.getX() + stepSize);
      System.out.println(imageView.getX());
    } else {
      imageView.setX(imageView.getX() + 0);
      key.setX(key.getX() + 0);
      key2.setX(key.getX() + 0);
      // move window
      System.out.println(imageView.getX());
    }
  }

  // event handler for when key is pressed depending on key

  /** Method to handle when the key is pressed. */
  @FXML
  private void clickKey2() {
    // check if its two player or one player to update the right gamestate variable.
    if (GameState.isPlayerScore == 2 && GameState.whichPlayer == 1) {
      GameState.pOneEasterEgg = true;
    } else if (GameState.isPlayerScore == 2 && GameState.whichPlayer == 2) {
      GameState.pTwoEasterEgg = true;
    } else {
      GameState.isWindowObjectFound = true;
    }
    // set the key to be invisible and update the items picked up.
    key2.setVisible(false);
    GameState.itemsPickedUp = GameState.itemsPickedUp + "Cash(Bonus)\n";
  }

  // event handler for GameMaster is pressed.

  /** Method to handle when the GameMaster is pressed. */
  @FXML
  private void clickGameMaster() throws IOException {
    GameState.sceneBeforeChat = SceneName.PRESENTWINDOW;
    GameMaster.newChat();
    // load chat scene
    App.loadScene("chat", SceneName.CHAT);
  }
}
