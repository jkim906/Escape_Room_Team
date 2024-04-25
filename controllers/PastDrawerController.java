package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.Gpt;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Controller for the past drawer scene. */
public class PastDrawerController {

  @FXML private Button exitButton;
  @FXML private ImageView exitImage;
  @FXML private ImageView gamemaster;
  @FXML private ImageView telescopeImage;
  @FXML private Rectangle telescope;
  @FXML private Label pastDrawerTimer;
  @FXML private TextField gamemasterText;
  @FXML private ProgressBar progressBarDraw;
  @FXML private Button mute;
  @FXML private Label labelHint;

  private Gpt gpt;
  private String telescopeMessage = "";
  private boolean isMedium;

  /**
   * Method which is called when the past drawer scene is created.
   *
   * <p>Initializes the scene by adding hover effects to the buttons and images. Also sets the
   * progress bar to update every second. Checks lose condition.
   */
  @FXML
  private void initialize() {
    HoverEffect.addHoverEffect(telescopeImage, 1.2);
    HoverEffect.addHoverEffect(gamemaster, 1.2);
    HoverEffect.addHoverEffect(exitImage, 1.2);
    HoverEffect.addHoverEffect(mute, 1.2);

    generateTelescopeMessage();

    // progress bar
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  double progress = ((double) timerValue) / timerMax;
                  progressBarDraw.setProgress(progress);
                }));
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    progressBarUpdater.play();

    // This updates the timer every 500ms.
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          // This method will be executed in a background thread.
          // It checks if the time is up and if so, loads the ending scene.
          // It also updates the timer.
          @Override
          // This method will be executed in a background thread.
          // It checks if the time is up and if so, loads the ending scene.
          public void run() {
            isMedium = GameState.isLevel == 2;

            Platform.runLater(
                () -> {
                  // update objective.
                  // Also updates other labels.
                  pastDrawerTimer.setText(GameState.toPrintTime);
                  mute.setText(GameState.mute);
                  if (isMedium) {
                    labelHint.setVisible(true);
                    labelHint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                });
          }
        };
    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  // https://www.flaticon.com/free-icons/spyglass
  // Spyglass icons created by Freepik - Flaticon

  /**
   * Method which is called when the telescope is clicked.
   *
   * <p>Updates game state to indicate that the telescope has been found.
   */
  @FXML
  private void telescopeClicked() {
    System.out.println("Telescope clicked");
    telescopeImage.opacityProperty().set(0);
    GameState.isTelescopeFound = true;
    GameState.itemsPickedUp = GameState.itemsPickedUp + "Telescope\n";
    GameMaster.displayGameMasterText(telescopeMessage, gamemasterText);
  }

  /**
   * Method which is called when the exit button is clicked.
   *
   * <p>Loads the past scene.
   *
   * @throws IOException if there is an IO error.
   */
  @FXML
  private void exitButtonClicked() throws IOException {
    System.out.println("Exit button clicked");
    App.loadScene("past", SceneName.PAST);
  }

  /**
   * Method which is called when the game master is clicked.
   *
   * <p>Loads the chat scene.
   *
   * @throws IOException if there is an IO error.
   */
  @FXML
  private void clickGameMaster() throws IOException {
    GameState.sceneBeforeChat = SceneName.PASTDRAWER;
    GameMaster.newChat();
    App.loadScene("gamemaster", SceneName.CHAT);
  }

  // This displays the Game Master text.

  // Generates the message for the telescope using GPT.

  /** Method which is called to create a GPT message when the telescope is found. */
  private void generateTelescopeMessage() {
    Task<Void> runGpt =
        new Task<Void>() {
          // This method will be executed in a background thread.
          // It will return the result of the GPT query.
          @Override
          protected Void call() throws Exception {
            gpt = new Gpt(1, 0.5, 50);
            ChatMessage msg =
                new ChatMessage("user", GptPromptEngineering.getItemFoundMessage("a telescope"));
            telescopeMessage = gpt.run(msg).getContent();
            return null;
          }
        };
    new Thread(runGpt).start();
  }

  /** Method which is called to mute the game. */
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
