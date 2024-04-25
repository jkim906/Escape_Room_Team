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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.Gpt;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/** Controller for the present lamp scene. */
public class PresentLampController {

  @FXML private ImageView gamemaster;
  @FXML private ImageView keys;
  @FXML private Slider mySlider;
  @FXML private ProgressBar progressBarLamp;
  @FXML private Label presentLampTimer;
  @FXML private TextField gamemasterText;
  @FXML private ImageView returnButton;
  @FXML private Label labelHint;
  @FXML private Button mute;

  private Gpt gpt;
  private String keyMessage = "";
  private boolean isMedium;

  /** Method which is called when the present lamp scene is created. */
  @FXML
  public void initialize() {

    HoverEffect.addHoverEffect(gamemaster, 1.2);
    HoverEffect.addHoverEffect(keys, 1.2);
    HoverEffect.addHoverEffect(returnButton, 1.2);
    HoverEffect.addHoverEffect(mute, 1.2);
    generateKeyMessage();

    // progress bar used to display time left with Timeline
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  double progress = ((double) timerValue) / timerMax;
                  progressBarLamp.setProgress(progress);
                }));
    // set cycle count to indefinite so that the progress bar keeps updating
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    progressBarUpdater.play();

    double threshold = 50.0; // The threshold where the key starts appearing

    // Bind the opacity of the key image to the slider value, with a threshold
    keys.opacityProperty()
        .bind(
            mySlider
                .valueProperty()
                .subtract(threshold)
                .divide(100.0 - threshold)
                .multiply(0.5) // Adjust opacity factor as needed (0.5 means minimum opacity is 0.5)
                .add(threshold / 100.0));

    // update timer
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          // Run method to update timer.
          // This method will be executed in a background thread.
          // It checks if the time is up and if so, loads the ending scene.
          @Override
          public void run() {
            isMedium = GameState.isLevel == 2;
            // Updates the timer and changes labels.
            Platform.runLater(
                () -> {
                  if (isMedium) {
                    labelHint.setVisible(true);
                    labelHint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                  mute.setText(GameState.mute);
                  presentLampTimer.setText(GameState.toPrintTime);
                });
          }
        };

    // update timer every 0.5 seconds
    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  /**
   * Method which is called when the return button is pressed.
   *
   * @throws IOException If there is an IO error.
   */
  @FXML
  public void returnPressed() throws IOException {
    System.out.println("Back button pressed");
    App.loadScene("present", SceneName.PRESENT);
  }

  // event handler for when key is clicked. if the opacity is greater than 0.5, the key is
  // pressable.

  /** Method which is called when the key is grabbed. */
  @FXML
  public void keyGrabbed() {
    if (keys.getOpacity() > 0.5) {
      System.out.println("key pressed");
      GameState.isKeyFound = true;
      keys.setVisible(false);
      GameState.isKeyFound = true;
      GameMaster.displayGameMasterText(keyMessage, gamemasterText);
      GameState.itemsPickedUp = GameState.itemsPickedUp + "Key\n";
    } else {
      // else do nothing.
      System.out.println("Key is not pressable at the current opacity.");
    }
  }

  // event handler for when game master is clicked.

  /**
   * Method which is called when the game master is clicked.
   *
   * @throws IOException If there is an IO error.
   */
  @FXML
  private void clickGameMaster() throws IOException {
    GameState.sceneBeforeChat = SceneName.PRESENTLAMP;
    GameMaster.newChat();
    App.loadScene("gamemaster", SceneName.CHAT);
  }

  // generate key message when the key is found.

  /** Method which is called to create a GPT message when the key is found. */
  private void generateKeyMessage() {
    Task<Void> runGpt =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            gpt = new Gpt(1, 0.5, 50);
            // generates a message using chatGPT
            ChatMessage msg =
                new ChatMessage("user", GptPromptEngineering.getItemFoundMessage("a key"));
            keyMessage = gpt.run(msg).getContent();
            return null;
          }
        };
    new Thread(runGpt).start();
  }

  // display game master text

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
