package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.SceneName;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.Gpt;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the chat view. */
public class ChatController {

  @FXML private ImageView back;
  @FXML private ImageView send;
  @FXML private Label chatTimer;
  @FXML private Label hint;
  @FXML private ScrollPane scrollpane;
  @FXML private TextField inputField;
  @FXML private VBox vbox;

  private boolean isMedium;
  private Gpt gpt;

  /**
   * Initializes the chat view, generates the prompt for the chat, begins timer, checks for
   * difficulty to display hint count.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  private void initialize() throws ApiProxyException {
    HoverEffect.addHoverEffect(back, 1.2);
    HoverEffect.addHoverEffect(send, 1.2);
    // is difficulty medium
    isMedium = (GameState.isLevel == 2);
    if (isMedium) {
      hint.setVisible(true);
      hint.setText("Hints Left: " + GameState.hintsLeft);
    }

    // generates the prompt for the game master to use for the chat
    generatePrompt();
    startTimer();
    GameMaster.appendMessage(
        "#808080",
        Pos.CENTER_LEFT,
        new ChatMessage("assistant", "What would you like to ask me?"),
        vbox,
        scrollpane);
  }

  /** Generates the prompt for the chat. */
  private void generatePrompt() {
    Task<Void> runGpt =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // runs gpt to get the prompt for the chat when player opens chat
            gpt = new Gpt(0.5, 0.5, 100);
            gpt.run(new ChatMessage("user", GptPromptEngineering.getPrompt()));
            return null;
          }
        };
    new Thread(runGpt).start();
  }

  /** Starts the timer task for the game. */
  private void startTimer() {
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        // updates the timer every 500 milliseconds
        new TimerTask() {
          @Override
          public void run() {
            // is difficulty medium
            isMedium = GameState.isLevel == 2;
            Platform.runLater(() -> chatTimer.setText(GameState.toPrintTime));
          }
        };

    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  /**
   * Sends a message to the GPT model.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage() throws ApiProxyException, IOException {
    String message = inputField.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    inputField.clear();

    // send message to gpt
    ChatMessage msg = new ChatMessage("user", message);
    GameMaster.appendMessage("#010000", Pos.CENTER_RIGHT, msg, vbox, scrollpane);

    // run gpt
    Task<Void> runGpt =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage response = gpt.run(msg);
            if (GameMaster.isHint(response) && isMedium && GameState.hintsLeft > 0) {
              GameState.hintsLeft--;
            }
            Platform.runLater(
                () -> {
                  // update hint count if medium
                  if (isMedium) {
                    hint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                  // append message
                  GameMaster.appendMessage("#808080", Pos.CENTER_LEFT, response, vbox, scrollpane);
                  displayChatScene();
                });
            return null;
          }
        };

    new Thread(runGpt).start();

    displayLoadingScene();
  }

  /** Displays the chat scene from the scene manager and displays the loading scene. */
  private void displayLoadingScene() {
    App.getScene().setRoot(SceneManager.getScene(SceneName.LOADING));
  }

  /** Gets the chat scene from scene manager and displays the chat scene. */
  private void displayChatScene() {
    App.getScene().setRoot(SceneManager.getScene(SceneName.CHAT));
  }

  /**
   * Navigates back to the previous view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack() throws ApiProxyException, IOException {
    App.loadSceneBeforeChat();
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
        onSendMessage();
      } catch (ApiProxyException | IOException e) {
        e.printStackTrace();
      }
    }
  }
}
