package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;

/** Controller class for the future safe view. */
public class FutureSafeController {

  @FXML private Button buttonReturn;
  @FXML private Button delete;
  @FXML private Button eight;
  @FXML private Button five;
  @FXML private Button fixclock;
  @FXML private Button four;
  @FXML private Button nine;
  @FXML private Button one;
  @FXML private Button six;
  @FXML private Button seven;
  @FXML private Button three;
  @FXML private Button two;
  @FXML private Button zero;
  @FXML private ImageView back;
  @FXML private ImageView gamemaster;
  @FXML private ImageView imagefixclock;
  @FXML private ImageView key;
  @FXML private ImageView lock;
  @FXML private ImageView opensafe;
  @FXML private Label futureSafeTimer;
  @FXML private Label labelHint;
  @FXML private ProgressBar progressBarSafe;
  @FXML private TextField text;

  private boolean isMedium;
  private StringBuilder sb = new StringBuilder();

  /**
   * Initializes the future safe view, begins timer, checks for difficulty to display hint count.
   */
  @FXML
  private void initialize() {
    // adds hover effects for nodes
    HoverEffect.addHoverEffect(one, 1.2);
    HoverEffect.addHoverEffect(two, 1.2);
    HoverEffect.addHoverEffect(three, 1.2);
    HoverEffect.addHoverEffect(four, 1.2);
    // adds hover effects for nodes
    HoverEffect.addHoverEffect(five, 1.2);
    HoverEffect.addHoverEffect(six, 1.2);
    // adds hover effects for nodes
    HoverEffect.addHoverEffect(seven, 1.2);
    HoverEffect.addHoverEffect(eight, 1.2);
    // adds hover effects for nodes
    HoverEffect.addHoverEffect(nine, 1.2);
    HoverEffect.addHoverEffect(zero, 1.2);
    HoverEffect.addHoverEffect(buttonReturn, 1.2);
    HoverEffect.addHoverEffect(lock, 1.2);
    // adds hover effects for nodes
    HoverEffect.addHoverEffect(gamemaster, 1.2);
    HoverEffect.addHoverEffect(fixclock, 1.2);
    // adds hover effects for nodes
    HoverEffect.addHoverEffect(buttonReturn, 1.2);
    HoverEffect.addHoverEffect(key, 1.2);
    HoverEffect.addHoverEffect(delete, 1.2);
    HoverEffect.addHoverEffect(back, 1.2);

    // progress bar updates every second
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          @Override
          public void run() {
            isMedium = GameState.isLevel == 2;
            Platform.runLater(
                () -> {
                  // update objectives
                  futureSafeTimer.setText(GameState.toPrintTime);
                  if (isMedium) {
                    labelHint.setVisible(true);
                    labelHint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                });
          }
        };

    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);

    // update the progress bar every second
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  // progress bar updating to get current progress
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  double progress = ((double) timerValue) / timerMax;
                  progressBarSafe.setProgress(progress);
                }));
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    progressBarUpdater.play();
  }

  /** Handles the event when the lock is hovered. */
  @FXML
  private void hoverLock() {
    lock.setOpacity(0.2);
  }

  /** Handles the event when the lock is exited. */
  @FXML
  private void exitLock() {
    lock.setOpacity(0);
  }

  /** Handles the event when the zero button is clicked. */
  @FXML
  private void onZeroClicked() {
    sb.append(0);
    text.setText(sb.toString());
  }

  /** Handles the event when the one button is clicked. */
  @FXML
  private void onOneClicked() {
    sb.append(1);
    text.setText(sb.toString());
  }

  /** Handles the event when the two button is clicked. */
  @FXML
  private void onTwoClicked() {
    sb.append(2);
    text.setText(sb.toString());
  }

  /** Handles the event when the three button is clicked. */
  @FXML
  private void onThreeClicked() {
    sb.append(3);
    text.setText(sb.toString());
  }

  /** Handles the event when the four button is clicked. */
  @FXML
  private void onFourClicked() {
    sb.append(4);
    text.setText(sb.toString());
  }

  /** Handles the event when the five button is clicked. */
  @FXML
  private void onFiveClicked() {
    sb.append(5);
    text.setText(sb.toString());
  }

  /** Handles the event when the six button is clicked. */
  @FXML
  private void onSixClicked() {
    sb.append(6);
    text.setText(sb.toString());
  }

  /** Handles the event when the seven button is clicked. */
  @FXML
  private void onSevenClicked() {
    sb.append(7);
    text.setText(sb.toString());
  }

  /** Handles the event when the eight button is clicked. */
  @FXML
  private void onEightClicked() {
    sb.append(8);
    text.setText(sb.toString());
  }

  /** Handles the event when the nine button is clicked. */
  @FXML
  private void onNineClicked() {
    sb.append(9);
    text.setText(sb.toString());
  }

  /** Handles the event when the backspace button is clicked. */
  @FXML
  private void onDeleteClicked() {
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
      text.setText(sb.toString());
    }
  }

  /** Handles the event when the lock button is clicked. */
  @FXML
  private void clickLock() {
    if (sb.toString().equals(GameState.safeCode)) {
      displayOpenSafe();
    } else {
      text.setText("Incorrect.");
    }
    sb.setLength(0);
  }

  /** Displays the open safe image and the clock hand for the player to retrieve. */
  private void displayOpenSafe() {
    opensafe.setVisible(true);
    key.setVisible(true);
  }

  /**
   * Handles the event when the return button is clicked to go back to future room.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void clickReturn() throws IOException {
    // reset
    sb.setLength(0);
    text.clear();
    App.loadScene("future", SceneName.FUTURE);
  }

  /** Handles the event when the key is clicked. */
  @FXML
  private void clickKey() {
    // check if all objectives are completed
    GameState.isClockHandFound = true;
    GameState.itemsPickedUp = GameState.itemsPickedUp + "Clock Hand\n";
    imagefixclock.setVisible(true);
    fixclock.setVisible(true);
  }

  /** Handles the event when the fix clock button is clicked. */
  @FXML
  private void clickFixClock() {
    GameState.isGameWon = true;
    // check if the number of players is 1
    if (GameState.whichPlayer == 1) {
      GameState.playerEndTime = GameState.timeLeftEnd - GameState.timeLeft;
      GameState.pOneTime = GameState.playerEndTime;
      GameState.timeLeft = 0;
    } else {
      // check if the number of players is number two
      GameState.playerEndTime = GameState.timeLeftEnd - GameState.timeLeft;
      GameState.pTwoTime = GameState.playerEndTime;
      GameState.timeLeft = 0;
    }
  }

  /**
   * Handles the event when the game master is clicked.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void clickGameMaster() throws IOException {
    // check if all objectives are completed
    GameState.sceneBeforeChat = SceneName.FUTURESAFE;
    GameMaster.newChat();
    App.loadScene("chat", SceneName.CHAT);
  }
}
