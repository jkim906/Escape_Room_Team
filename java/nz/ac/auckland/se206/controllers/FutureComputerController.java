package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.Gpt;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the future riddle computer view. */
public class FutureComputerController {

  @FXML private ImageView back;
  @FXML private ImageView send;
  @FXML private Label futureComputerTimer;
  @FXML private Label hintCount;
  @FXML private ProgressBar progressBarComp;
  @FXML private ScrollPane scrollpane;
  @FXML private TextArea textArea;
  @FXML private TextField inputText;
  @FXML private VBox vbox;

  private boolean isMedium;
  private Gpt gpt;

  /**
   * Initializes the future computer view, generates the prompt for the chat, begins timer, checks
   * for difficulty to display hint count.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  private void initialize() throws ApiProxyException {
    HoverEffect.addHoverEffect(send, 1.2);
    HoverEffect.addHoverEffect(back, 1.2);
    // progress bar updates every second
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  // update progress bar with current progress
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  double progress = ((double) timerValue) / timerMax;
                  progressBarComp.setProgress(progress);
                }));
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    // progress bar play when player opens chat
    progressBarUpdater.play();

    // get the riddle priompt from fgpt
    generateRiddle();
    startTimer();
  }

  /** Generates the prompt for the chat. */
  private void hintUpdater() {
    // update hint count
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          @Override
          public void run() {
            Platform.runLater(
                () -> {
                  // update hint count
                  hintCount.setText("Hints Left:\n" + GameState.hintsLeft);
                });
          }
        };
    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  /** Generates the prompt for the riddle. */
  private void generateRiddle() {
    Task<Void> runGpt =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (!GameState.isDifficultySet) {
              // wait for the difficulty to be chosen by player before generating prompt
              gpt = new Gpt(0.5, 0.5, 100);
            }
            isMedium = GameState.isLevel == 2;
            if (isMedium) {
              hintUpdater();
            }
            ChatMessage msg = new ChatMessage("user", GptPromptEngineering.getRiddle());
            System.out.println(GptPromptEngineering.getRiddle());
            gpt = new Gpt(0.5, 0.5, 100);
            ChatMessage riddle = gpt.run(msg);
            Platform.runLater(
                // append riddle to text area
                () -> {
                  if (isMedium) {
                    hintCount.setVisible(true);
                  }
                  GameMaster.appendMessage("#808080", Pos.CENTER_LEFT, riddle, vbox, scrollpane);
                });

            return null;
          }
        };

    new Thread(runGpt).start();
  }

  /** Starts the timer task for the game. */
  private void startTimer() {
    Timer updateTimer = new Timer();
    // updates the timer every 500 milliseconds
    TimerTask timerTask =
        new TimerTask() {
          @Override
          // update timer
          public void run() {
            isMedium = GameState.isLevel == 2;
            Platform.runLater(() -> futureComputerTimer.setText(GameState.toPrintTime));
          }
        };

    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  /**
   * Handles the event when the send button is pressed, sends the user's input to the chat.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void clickSend() throws ApiProxyException, IOException {
    String input = inputText.getText();
    if (input.isEmpty()) {
      return;
    }
    // clear input
    inputText.clear();
    ChatMessage userAnswer = new ChatMessage("user", input);
    GameMaster.appendMessage("#010000", Pos.CENTER_RIGHT, userAnswer, vbox, scrollpane);
    // add hint update if medium
    if (isMedium) {
      userAnswer =
          new ChatMessage(
              "user",
              input
                  + ". I can only ask you for a hint for the riddle "
                  + GameState.hintsLeft
                  + " times. Only give me a hint if I explicitly ask for a hint"
                  + ", otherwise don't give me any hint.");
    }
    runGpt(userAnswer);
  }

  /**
   * Runs the GPT-3 model to generate a response to the user's input.
   *
   * @param userAnswer the user's input
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  private void runGpt(ChatMessage userAnswer) throws ApiProxyException, IOException {
    Task<Void> runGpt =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage gptAnswer = gpt.run(userAnswer);
            // check for correct answer
            checkForCorrectAnswer(gptAnswer);
            if (isMedium && GameMaster.isHint(gptAnswer) && GameState.hintsLeft > 0) {
              GameState.hintsLeft--;
            }
            Platform.runLater(
                () -> {
                  if (GameState.isRiddleResolved) {
                    inputText.setVisible(false);
                    send.setVisible(false);
                  }
                  // check for correct answer if it wss a hint
                  GameMaster.appendMessage("#808080", Pos.CENTER_LEFT, gptAnswer, vbox, scrollpane);
                  displayComputerScene();
                });
            return null;
          }
        };

    new Thread(runGpt).start();

    displayLoadingScene();
  }

  /**
   * Checks if the user's answer is correct for the riddle, and sets the riddle to be resolved if it
   * is.
   *
   * @param input the user's answer to the riddle to be checked
   */
  private void checkForCorrectAnswer(ChatMessage input) {
    if (isCorrect(input)) {
      GameState.isRiddleResolved = true;
    }
  }

  /**
   * Checks if the input is by the assistant and says 'Correct'.
   *
   * @param input the input to be checked
   * @return true if the input is by the assistant and says 'Correct', false otherwise
   */
  private boolean isCorrect(ChatMessage input) {
    return input.getRole().equals("assistant") && input.getContent().startsWith("Correct");
  }

  /** Retrieves the loading scene from scenemanager, then displays the loading scene. */
  private void displayLoadingScene() {
    try {
      App.loadScene("loading", SceneName.LOADING);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /** Loads and displays the future computer scene for the riddle. */
  private void displayComputerScene() {
    try {
      App.loadScene("futurecomputer", SceneName.FUTURECOMPUTER);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Navigates back to the previous view, the future room.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void clickReturn() throws IOException {
    App.loadScene("future", SceneName.FUTURE);
  }

  /**
   * Sends a message to the GPT model when the Enter key is pressed.
   *
   * @param event the key event that was fired
   */
  @FXML
  private void onKeyPressed(KeyEvent event) {
    if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
      try {
        // send message to gpt
        clickSend();
      } catch (ApiProxyException | IOException e) {
        e.printStackTrace();
      }
    }
  }
}
