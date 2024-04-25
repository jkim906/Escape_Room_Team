package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
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
import nz.ac.auckland.se206.speech.TextToSpeechThread;

/** Controller class for the future view. */
public class FutureController {

  @FXML private Button buttonPhoneExit;
  @FXML private Button mute;
  @FXML private ImageView checklist;
  @FXML private ImageView gamemaster;
  @FXML private ImageView inventory;
  @FXML private ImageView phone;
  @FXML private ImageView phoneBig;
  @FXML private ImageView present;
  @FXML private ImageView returnButtonPresent;
  @FXML private ImageView safe;
  @FXML private Label codeText;
  @FXML private Label futureTimer;
  @FXML private Label inventoryLabel;
  @FXML private Label label1;
  @FXML private Label labelHint;
  @FXML private Label objectivesFuture;
  @FXML private Line line;
  @FXML private ProgressBar progressBarFuture;
  @FXML private Rectangle computer;
  @FXML private Rectangle inventoryTab;
  @FXML private Rectangle sidebar;
  @FXML private TextField gamemasterText;

  private boolean isChecklistVisible = false;
  private boolean isInventoryVisible = false;
  private boolean isMedium;
  private Gpt gpt;
  private Gpt gpt2;
  private String codeMessage = "";
  private String errorMessage = "";
  private TextToSpeechThread tts;

  /**
   * Initializes the future view, begins timer, checks for difficulty to display hint count, adds
   * hover effects for nodes.
   */
  @FXML
  private void initialize() {

    // add hover effects
    HoverEffect.addHoverEffect(inventory, 1.2);
    HoverEffect.addHoverEffect(gamemaster, 1.2);
    HoverEffect.addHoverEffect(safe, 1.2);
    HoverEffect.addHoverEffect(phone, 1.2);
    HoverEffect.addHoverEffect(checklist, 1.2);
    // add hover effects
    HoverEffect.addHoverEffect(present, 1.2);
    HoverEffect.addHoverEffect(mute, 1.2);
    HoverEffect.addHoverEffect(buttonPhoneExit, 1.2);

    objectivesFuture.setVisible(false);

    // set visibility of inventory
    sidebar.setVisible(false);
    inventoryTab.setVisible(false);
    returnButtonPresent.setVisible(false);
    inventoryLabel.setVisible(false);
    label1.setVisible(false);
    line.setVisible(false);

    // progress bar
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  // update progress bar
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  // get the current progress ratio
                  double progress = ((double) timerValue) / timerMax;
                  progressBarFuture.setProgress(progress);
                }));
    // progress bar
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    progressBarUpdater.play();

    generateSafeCode();
    generateCodeMessage();
    generateErrorMessage();

    // update objectives
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          @Override
          public void run() {
            isMedium = GameState.isLevel == 2;
            Platform.runLater(
                () -> {
                  // update objectives
                  if (isMedium) {
                    labelHint.setVisible(true);
                    labelHint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                  mute.setText(GameState.mute);
                  futureTimer.setText(GameState.toPrintTime);
                  objectivesFuture.setText(GameState.globalObjectives);
                });
          }
        };

    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  /** Generates a random code for the safe. */
  private void generateSafeCode() {
    GameState.safeCode =
        Integer.toString((int) (Math.floor(Math.random() * (9999 - 1000 + 1) + 1000)));
  }

  /** Generates a message for the code when it is found. */
  private void generateCodeMessage() {
    Task<Void> runGpt =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // genrates a new message when code is found
            gpt = new Gpt(1, 0.5, 50);
            ChatMessage msg =
                new ChatMessage("user", GptPromptEngineering.getItemFoundMessage("a code"));
            codeMessage = gpt.run(msg).getContent();
            // runs gpt to get the message for the code
            return null;
          }
        };
    new Thread(runGpt).start();
  }

  /** Generates an error message when player tries to open safe. */
  private void generateErrorMessage() {
    Task<Void> runGpt =
        // run gpt to get the message when player tries to open safe
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            gpt2 = new Gpt(1, 0.5, 50);
            // runs gpt to get the message if taks have not been solved yet
            ChatMessage msg =
                new ChatMessage(
                    "user",
                    "you are the master of an escape room. tell me that i still have tasks to solve"
                        + ". keep it under 10 words.");
            errorMessage = gpt2.run(msg).getContent();
            return null;
          }
        };

    new Thread(runGpt).start();
  }

  /** Handles the event when the computer is hovered. */
  @FXML
  private void hoverComputer() {
    computer.setOpacity(0.2);
  }

  /** Handles the event when the phone is hovered. */
  @FXML
  private void hoverPhone() {
    phone.setOpacity(0.2);
  }

  /** Handles the event when the safe is hovered. */
  @FXML
  private void hoverSafe() {
    safe.setOpacity(0.2);
  }

  /** Handles the event when the mouse exits the computer hover. */
  @FXML
  private void exitComputer() {
    computer.setOpacity(0);
  }

  /** Handles the event the phone is no longer hovered. */
  @FXML
  private void exitPhone() {
    phone.setOpacity(0);
  }

  /** Handles the event when the safe is no longer hovered. */
  @FXML
  private void exitSafe() {
    safe.setOpacity(0);
  }

  /**
   * Handles the event when the computer is clicked.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void clickComputer() throws IOException {
    App.loadScene("futurecomputer", SceneName.FUTURECOMPUTER);
  }

  /**
   * Handles the event when the safe is clicked.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void clickSafe() throws IOException {
    // check if all objectives are completed
    if (!GameState.isRiddleResolved || !GameState.isPhoneCalled || !GameState.isPhonePickedUp) {
      displayGameMasterText(errorMessage);
      if (!GameState.isMute) {
        tts = new TextToSpeechThread(errorMessage);
        tts.start();
      }
      // play error message
      return;
    }
    App.loadScene("futuresafe", SceneName.FUTURESAFE);
  }

  /**
   * Display what the game master says in the text box.
   *
   * @param msg the message to display
   */
  private void displayGameMasterText(String msg) {
    gamemasterText.setVisible(true);
    // play text to speech
    gamemasterText.setText(msg);
    closeTextTimerTask();
  }

  /** Closes the game master text after 3 seconds. */
  private void closeTextTimerTask() {
    Timer timer = new Timer();
    TimerTask myTask =
        new TimerTask() {
          @Override
          public void run() {
            // close text after 3 seconds
            Platform.runLater(() -> gamemasterText.setVisible(false));
          }
        };
    timer.schedule(myTask, 3000);
  }

  /** Handles the event when the checklist is clicked. */
  @FXML
  private void onChecklistClicked() {
    // check if the checklist is visible
    if (isChecklistVisible) {
      objectivesFuture.setVisible(false);
      isChecklistVisible = false;
    } else {
      // set the visibility of the objectives
      objectivesFuture.setVisible(true);
      isChecklistVisible = true;
    }
  }

  /**
   * Handles the event when the phone is clicked.
   *
   * @param event the event when the phone is clicked
   */
  @FXML
  private void clickPhone(MouseEvent event) {
    if (GameState.isPhoneCalled) {
      GameState.isPhonePickedUp = true;
      // set the visibility of the phone
      codeText.setText(GameState.safeCode);
      displayGameMasterText(codeMessage);
      if (!GameState.isMute) {
        // play text to speech
        tts = new TextToSpeechThread(codeMessage);
        tts.start();
      }
    }
    displayPhone();
  }

  /** Displays the phone image and the code for the safe, and the exit button. */
  private void displayPhone() {
    // display phone
    buttonPhoneExit.setVisible(true);
    codeText.setVisible(true);
    phoneBig.setVisible(true);
  }

  /**
   * Handles the event when the phone exit button is clicked.
   *
   * @param event the event when the phone exit button is clicked
   */
  @FXML
  private void onExit(ActionEvent event) {
    // close phone
    buttonPhoneExit.setVisible(false);
    codeText.setVisible(false);
    phoneBig.setVisible(false);
  }

  /**
   * Handles the event when the present is clicked.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void clickPresent() throws IOException {
    App.loadScene("present", SceneName.PRESENT);
  }

  /** Handles the event when the present is hovered. */
  @FXML
  private void hoverPresent() {
    present.setOpacity(0.2);
  }

  /** Handles the event when the present is no longer hovered. */
  @FXML
  private void exitPresent() {
    present.setOpacity(0);
  }

  /**
   * Handles the event when the inventory is clicked.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void clickGameMaster() throws IOException {
    // load chat scene
    GameState.sceneBeforeChat = SceneName.FUTURE;
    GameMaster.newChat();
    App.loadScene("chat", SceneName.CHAT);
  }

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

  /** Handles the event when the mute button is pressed. */
  @FXML
  private void mute() {
    if (!GameState.isMute) {
      GameState.mute = "Unmute";
    } else {
      GameState.mute = "Mute";
    }
    // mute the game

    GameState.isMute = !GameState.isMute;
  }
}
