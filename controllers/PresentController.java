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
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;

/** Controller for the present scene. */
public class PresentController {

  @FXML private Label objectivesPresent;
  @FXML private Rectangle sidebar;
  @FXML private Rectangle inventoryTab;
  @FXML private ImageView returnButtonPresent;
  @FXML private ImageView checklist;
  @FXML private Label presentTimer;
  @FXML private ImageView gamemaster;
  @FXML private ImageView future;
  @FXML private ImageView past;
  @FXML private ProgressBar progressBarPresent;
  @FXML private Rectangle deskLamp;
  @FXML private Rectangle windowView;
  @FXML private Label inventoryLabel;
  @FXML private Label label1;
  @FXML private Label windowLabel;
  @FXML private ImageView object3;
  @FXML private ImageView inventory;
  @FXML private Line line;
  @FXML private Label labelHint;
  @FXML private Button mute;

  private boolean isInventoryVisible = false;
  private boolean isMedium;

  private boolean isChecklistVisible = false;

  /**
   * Method which is called when the present scene is created.
   *
   * <p>Initializes the timer and the progress bar.
   *
   * <p>Initializes the inventory and the sidebar.
   *
   * <p>Initializes the hover effects.
   *
   * <p>Initializes the win and lose conditions.
   */
  @FXML
  public void initialize() {
    HoverEffect.addHoverEffect(inventory, 1.2);
    HoverEffect.addHoverEffect(gamemaster, 1.2);
    HoverEffect.addHoverEffect(checklist, 1.2);
    objectivesPresent.setVisible(false);
    HoverEffect.addHoverEffect(future, 1.2);
    HoverEffect.addHoverEffect(past, 1.2);
    HoverEffect.addHoverEffect(mute, 1.2);
    // progress bar
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  double progress = ((double) timerValue) / timerMax;
                  progressBarPresent.setProgress(progress);
                }));
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    progressBarUpdater.play();

    // set visiblity of items
    sidebar.setVisible(false);
    inventoryTab.setVisible(false);
    returnButtonPresent.setVisible(false);
    inventoryLabel.setVisible(false);
    label1.setVisible(false);
    line.setVisible(false);
    objectivesPresent.setMouseTransparent(true);

    // timer
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          // Runs the timer task which checks the win and lose conditions.
          // This also updates the timer.
          @Override
          public void run() {
            isMedium = GameState.isLevel == 2;
            // update the timer
            Platform.runLater(
                () -> {
                  if (isMedium) {
                    labelHint.setVisible(true);
                    labelHint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                  mute.setText(GameState.mute);
                  presentTimer.setText(GameState.toPrintTime);
                  objectivesPresent.setText(GameState.globalObjectives);
                });
          }
        };

    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  /**
   * Method which is called when the window is pressed.
   *
   * <p>Loads the present window scene.
   *
   * @throws IOException if the present window scene is not found.
   */
  @FXML
  public void onWindowPressed() throws IOException {
    if (GameState.isTelescopeFound) {
      App.loadScene("presentWindow", SceneName.PRESENTWINDOW);
      windowLabel.setText("");
    } else {
      windowLabel.setText("Everything is too \nfar away to see clearly...");
    }
  }

  /**
   * Method which is called when the checklist icon is clicked.
   *
   * <p>Opens the checklist.
   */
  @FXML
  public void onChecklistClicked() {
    // Toggles the visibility of the checklist
    if (isChecklistVisible) {
      objectivesPresent.setVisible(false);
      isChecklistVisible = false;
    } else {
      objectivesPresent.setVisible(true);
      isChecklistVisible = true;
    }
  }

  /**
   * Method which is called when the lamp is pressed.
   *
   * @throws IOException if there is an IO error.
   */
  @FXML
  public void lampPressed() throws IOException {
    App.loadScene("presentLamp", SceneName.PRESENTLAMP);
  }

  /**
   * Method which is called when the players cursor clicks the future arrow.
   *
   * @throws IOException if there is an IO error.
   */
  @FXML
  private void clickFuture() throws IOException {
    App.loadScene("future", SceneName.FUTURE);
  }

  /**
   * Method which is called when the players cursor clicks the past arrow.
   *
   * @throws IOException if there is an IO error.
   */
  @FXML
  private void clickPast() throws IOException {
    App.loadScene("past", SceneName.PAST);
  }

  /**
   * Method which is called when the player clicks on the game master.
   *
   * @throws IOException if there is an IO error.
   */
  @FXML
  private void clickGameMaster() throws IOException {
    GameState.sceneBeforeChat = SceneName.PRESENT;
    GameMaster.newChat();
    App.loadScene("gamemaster", SceneName.CHAT);
  }

  // return button pressed

  /**
   * Handles the event when the inventory button is pressed, toggling the visibility of the
   * inventory.
   */
  @FXML
  private void inventoryPressed() {
    GameState.inventorySetter(
        isInventoryVisible,
        returnButtonPresent,
        inventoryTab,
        sidebar,
        inventoryLabel,
        label1,
        line);
    // Sets the boolean to match the visibility of the inventory.
    isInventoryVisible = !isInventoryVisible;
  }

  /** Method which is called when the window is hovered. */
  @FXML
  private void windowHover() {
    System.out.println("Window hovered");
    windowView.setOpacity(0.2);
  }

  /** Method which is called when the cursor leaves the window. */
  @FXML
  private void windowExit() {
    System.out.println("Window exited");
    windowView.setOpacity(0);
  }

  /** Method which is called when the lamp is hovered. */
  @FXML
  private void lampHover() {
    System.out.println("Lamp hovered");
    deskLamp.setOpacity(0.2);
  }

  /** Method which is called when the cursor leaves the lamp. */
  @FXML
  private void lampExit() {
    System.out.println("Lamp exited");
    deskLamp.setOpacity(0);
  }

  /** Method which is called when the mute button is pressed. */
  @FXML
  private void mute() {
    if (!GameState.isMute) {
      GameState.mute = "Unmute";
    } else {
      GameState.mute = "Mute";
    }

    GameState.isMute = !GameState.isMute;
  }
}
