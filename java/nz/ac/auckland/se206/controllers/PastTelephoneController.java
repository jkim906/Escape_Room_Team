package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
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
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.Gpt;

/** Controller for the past telephone scene. */
public class PastTelephoneController {

  @FXML private Button one;
  @FXML private Button two;
  @FXML private Button three;
  @FXML private Button four;
  @FXML private Button five;
  @FXML private Button six;
  @FXML private Button seven;
  @FXML private Button eight;
  @FXML private Button nine;
  @FXML private Button zero;
  @FXML private Button clear;
  @FXML private Button dial;
  @FXML private Button handset;
  @FXML private ImageView gamemaster;
  @FXML private ImageView exit;
  @FXML private Label pastTelephoneTimer;
  @FXML private Label pastTelephoneDisplay;
  @FXML private Label infoLabel;
  @FXML private TextField gamemasterText;
  @FXML private ProgressBar progressBarPhone;
  @FXML private Label labelHint;
  @FXML private Button mute;

  private Gpt gpt;
  private int rightCode = ThreadLocalRandom.current().nextInt(1000, 9999);
  private String code = "";
  private String phoneMessage = "";
  private boolean isMedium;

  /**
   * Method that is called when the past telephone scene is created.
   *
   * <p>This method is used to initialise the scene. It also sets up the timer and the progress bar.
   * It also sets up the hover effects for the buttons. It also sets up the loose condition.
   */
  @FXML
  private void initialize() {

    HoverEffect.addHoverEffect(gamemaster, 1.2);
    HoverEffect.addHoverEffect(handset, 1.2);
    HoverEffect.addHoverEffect(exit, 1.2);
    HoverEffect.addHoverEffect(one, 1.2);
    HoverEffect.addHoverEffect(two, 1.2);
    HoverEffect.addHoverEffect(three, 1.2);
    HoverEffect.addHoverEffect(four, 1.2);
    HoverEffect.addHoverEffect(five, 1.2);
    HoverEffect.addHoverEffect(six, 1.2);
    HoverEffect.addHoverEffect(seven, 1.2);
    HoverEffect.addHoverEffect(eight, 1.2);
    HoverEffect.addHoverEffect(nine, 1.2);
    HoverEffect.addHoverEffect(clear, 1.2);
    HoverEffect.addHoverEffect(dial, 1.2);
    HoverEffect.addHoverEffect(mute, 1.2);

    generatePhoneMessage();

    // This timeline is used to update the progress bar.
    // It is updated every second to match the proportion of time left.
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  double progress = ((double) timerValue) / timerMax;
                  progressBarPhone.setProgress(progress);
                }));
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    progressBarUpdater.play();

    // disable buttons and do timer
    disableButtons();
    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          // This method checks if the time is up and if it is, it will
          // change the scene to the ending scene.
          // Also prints the time left on the screen.
          @Override
          public void run() {
            isMedium = GameState.isLevel == 2;
            // This updates the text on the screen.
            Platform.runLater(() -> pastTelephoneTimer.setText(GameState.toPrintTime));
            Platform.runLater(() -> pastTelephoneDisplay.setText(code));
            Platform.runLater(
                () -> {
                  // update objectives
                  mute.setText(GameState.mute);
                  pastTelephoneTimer.setText(GameState.toPrintTime);
                  if (isMedium) {
                    labelHint.setVisible(true);
                    labelHint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                });
          }
        };

    updateTimer.scheduleAtFixedRate(timerTask, 0, 50);
  }

  /** Method that is called when the one button is clicked. */
  @FXML
  private void onOneClicked() {
    code += "1";
    System.out.println(code);
  }

  /** Method that is called when the two button is clicked. */
  @FXML
  private void onTwoClicked() {
    code += "2";
    System.out.println(code);
  }

  /** Method that is called when the three button is clicked. */
  @FXML
  private void onThreeClicked() {
    code += "3";
    System.out.println(code);
  }

  /** Method that is called when the four button is clicked. */
  @FXML
  private void onFourClicked() {
    code += "4";
    System.out.println(code);
  }

  /** Method that is called when the five button is clicked. */
  @FXML
  private void onFiveClicked() {
    code += "5";
    System.out.println(code);
  }

  /** Method that is called when the six button is clicked. */
  @FXML
  private void onSixClicked() {
    code += "6";
    System.out.println(code);
  }

  /** Method that is called when the seven button is clicked. */
  @FXML
  private void onSevenClicked() {
    code += "7";
    System.out.println(code);
  }

  /** Method that is called when the eight button is clicked. */
  @FXML
  private void onEightClicked() {
    code += "8";
    System.out.println(code);
  }

  /** Method that is called when the nine button is clicked. */
  @FXML
  private void onNineClicked() {
    code += "9";
    System.out.println(code);
  }

  /** Method that is called when the zero button is clicked. */
  @FXML
  private void onZeroClicked() {
    code += "0";
    System.out.println(code);
  }

  /**
   * Method that is called when the clear button is clicked.
   *
   * <p>Clears the code string.
   */
  @FXML
  private void onClearClicked() {
    code = "";
    System.out.println(code);
  }

  // Handles the dial button being clicked.
  // Checks if the user did the challenge correctly.

  /**
   * Method that is called when the dial button is clicked.
   *
   * <p>Checks if the user did the challenge correctly. If the user did the challenge correctly, the
   * user is told that they have the correct code.
   */
  @FXML
  private void onDialClicked() {
    System.out.println(code);
    // If the code is correct, the user is told that they have the correct code.
    // Also the handset is disabled and the game master text is displayed.
    if (code.equals(Integer.toString(rightCode))) {
      System.out.println("Correct code");
      infoLabel.setText("Correct Number! I wonder who's number it is...");
      disableButtons();
      // disable handset
      handset.setDisable(true);
      GameState.isPhoneCalled = true;
      GameMaster.displayGameMasterText(phoneMessage, gamemasterText);
      // If the code is incorrect, the user is told that they have the incorrect code.
    } else {
      System.out.println("Incorrect code");
      infoLabel.setText("Wrong Number!");
      disableButtons();
      reGenerateCode();
      code = "";
    }
  }

  // Exits the scene when the exit button is clicked.

  /**
   * Method that is called when the exit button is clicked.
   *
   * <p>Changes scenes to the past scene.
   *
   * @throws IOException if there is an IO error.
   */
  @FXML
  private void onExitClicked() throws IOException {
    System.out.println("Exit clicked");
    App.loadScene("past", SceneName.PAST);
  }

  // Display the game master text when the game master is clicked.

  /**
   * Method that is called when the game master is clicked.
   *
   * @throws IOException if there is an IO error.
   */
  @FXML
  private void onClickGameMaster() throws IOException {
    GameState.sceneBeforeChat = SceneName.PASTTELEPHONE;
    GameMaster.newChat();
    App.loadScene("gamemaster", SceneName.CHAT);
  }

  /** Method that is called when the handset button is hovered over. */
  @FXML
  private void onExitHover() {
    exit.setOpacity(1);
  }

  /** Method that is called when the handset button is hovered over. */
  @FXML
  private void onExitExited() {
    exit.setOpacity(0.7);
  }

  // Method to run when the handset button is clicked.

  /**
   * Method that is called when the handset button is clicked.
   *
   * <p>This method is used to run the puzzle. It also re-generates the code.
   */
  @FXML
  private void onPickUpHandSet() {
    System.out.println("Pick up handset");
    reGenerateCode();
    runPuzzle();
  }

  // This method is used to re-generate the code.

  /** Method that is called to re-generate the code. */
  private void reGenerateCode() {
    rightCode = ThreadLocalRandom.current().nextInt(1000, 9999);
  }

  // This method runs the handset button is clicked.

  /**
   * Method that is called to run the puzzle.
   *
   * <p>Makes the buttons flash in the order on buttons created by regenerate code. Enables the
   * buttons after the puzzle is given.
   */
  private void runPuzzle() {
    System.out.println("PastTelephoneController running puzzle");
    Task<Void> task =
        new Task<Void>() {
          // This method is used to run the puzzle on a thread so the
          // GUI doesn't freeze.
          @Override
          public Void call() throws InterruptedException {
            String codeString = Integer.toString(rightCode);
            // Disables the handset button while the puzzle is running.
            Platform.runLater(() -> handset.setDisable(true));
            // For loop that runs through the code string.
            for (int i = 0; i < codeString.length(); i++) {
              Thread.sleep(1000);
              // Hides buttons one at a time for 1 second.
              if (codeString.charAt(i) == '1') {
                Platform.runLater(() -> hideOne());
              } else if (codeString.charAt(i) == '2') {
                Platform.runLater(() -> hideTwo());
              } else if (codeString.charAt(i) == '3') {
                Platform.runLater(() -> hideThree());
              } else if (codeString.charAt(i) == '4') {
                Platform.runLater(() -> hideFour());
              } else if (codeString.charAt(i) == '5') {
                Platform.runLater(() -> hideFive());
              } else if (codeString.charAt(i) == '6') {
                Platform.runLater(() -> hideSix());
              } else if (codeString.charAt(i) == '7') {
                Platform.runLater(() -> hideSeven());
              } else if (codeString.charAt(i) == '8') {
                Platform.runLater(() -> hideEight());
              } else if (codeString.charAt(i) == '9') {
                Platform.runLater(() -> hideNine());
              } else if (codeString.charAt(i) == '0') {
                Platform.runLater(() -> hideZero());
              }

              Thread.sleep(1000);

              // Shows numbers after they are hidden.
              if (codeString.charAt(i) == '1') {
                Platform.runLater(() -> showOne());
              } else if (codeString.charAt(i) == '2') {
                Platform.runLater(() -> showTwo());
              } else if (codeString.charAt(i) == '3') {
                Platform.runLater(() -> showThree());
              } else if (codeString.charAt(i) == '4') {
                Platform.runLater(() -> showFour());
              } else if (codeString.charAt(i) == '5') {
                Platform.runLater(() -> showFive());
              } else if (codeString.charAt(i) == '6') {
                Platform.runLater(() -> showSix());
              } else if (codeString.charAt(i) == '7') {
                Platform.runLater(() -> showSeven());
              } else if (codeString.charAt(i) == '8') {
                Platform.runLater(() -> showEight());
              } else if (codeString.charAt(i) == '9') {
                Platform.runLater(() -> showNine());
              } else if (codeString.charAt(i) == '0') {
                Platform.runLater(() -> showZero());
              }
            }
            // This re-enables the buttons after the puzzle is given.
            one.setDisable(false);
            two.setDisable(false);
            three.setDisable(false);
            four.setDisable(false);
            five.setDisable(false);
            six.setDisable(false);
            seven.setDisable(false);
            eight.setDisable(false);
            nine.setDisable(false);
            zero.setDisable(false);
            clear.setDisable(false);
            dial.setDisable(false);
            handset.setDisable(false);
            return null;
          }
        };

    Thread thread = new Thread(task);
    // Starts the thread.
    thread.start();
  }

  // This method is used to hide the buttons.

  /** Method that is called to hide the one button. */
  @FXML
  private void hideOne() {
    one.setVisible(false);
  }

  /** Method that is called to hide the two button. */
  @FXML
  private void hideTwo() {
    two.setVisible(false);
  }

  /** Method that is called to hide the three button. */
  @FXML
  private void hideThree() {
    three.setVisible(false);
  }

  /** Method that is called to hide the four button. */
  @FXML
  private void hideFour() {
    four.setVisible(false);
  }

  /** Method that is called to hide the five button. */
  @FXML
  private void hideFive() {
    five.setVisible(false);
  }

  /** Method that is called to hide the six button. */
  @FXML
  private void hideSix() {
    six.setVisible(false);
  }

  /** Method that is called to hide the seven button. */
  @FXML
  private void hideSeven() {
    seven.setVisible(false);
  }

  /** Method that is called to hide the eight button. */
  @FXML
  private void hideEight() {
    eight.setVisible(false);
  }

  /** Method that is called to hide the nine button. */
  @FXML
  private void hideNine() {
    nine.setVisible(false);
  }

  /** Method that is called to hide the zero button. */
  @FXML
  private void hideZero() {
    zero.setVisible(false);
  }

  /** Method that is called to show the one button. */
  @FXML
  private void showOne() {
    one.setVisible(true);
  }

  /** Method that is called to show the two button. */
  @FXML
  private void showTwo() {
    two.setVisible(true);
  }

  /** Method that is called to show the three button. */
  @FXML
  private void showThree() {
    three.setVisible(true);
  }

  /** Method that is called to show the four button. */
  @FXML
  private void showFour() {
    four.setVisible(true);
  }

  /** Method that is called to show the five button. */
  @FXML
  private void showFive() {
    five.setVisible(true);
  }

  /** Method that is called to show the six button. */
  @FXML
  private void showSix() {
    six.setVisible(true);
  }

  /** Method that is called to show the seven button. */
  @FXML
  private void showSeven() {
    seven.setVisible(true);
  }

  /** Method that is called to show the eight button. */
  @FXML
  private void showEight() {
    eight.setVisible(true);
  }

  /** Method that is called to show the nine button. */
  @FXML
  private void showNine() {
    nine.setVisible(true);
  }

  /** Method that is called to show the zero button. */
  @FXML
  private void showZero() {
    zero.setVisible(true);
  }

  // This helper method is used to disable all the buttons.

  /** Method that is called to disable all the buttons. */
  @FXML
  private void disableButtons() {
    // This disables the numbers on the phone.
    one.setDisable(true);
    two.setDisable(true);
    three.setDisable(true);
    four.setDisable(true);
    five.setDisable(true);
    six.setDisable(true);
    seven.setDisable(true);
    eight.setDisable(true);
    nine.setDisable(true);
    zero.setDisable(true);
    // This disables the dial and clear buttons.
    clear.setDisable(true);
    dial.setDisable(true);
  }

  // This method is used to display the game master text.

  // This method is used to generate the phone message from GPT.

  /** Method that is called to generate the phone message from GPT. */
  private void generatePhoneMessage() {
    Task<Void> runGpt =
        new Task<Void>() {
          // This method is used to run chat GPT on a thread so the
          // GUI doesn't freeze.
          @Override
          protected Void call() throws Exception {
            gpt = new Gpt(1, 0.5, 50);
            // This uses the chat message below as a prompt to GPT to generate the message.
            ChatMessage msg =
                new ChatMessage(
                    "user",
                    "tell me that the phone is ringing and i should go pick up the phone call"
                        + ". keep it under 10 words.");
            // This gets the content of the message from GPT.
            phoneMessage = gpt.run(msg).getContent();
            return null;
          }
        };

    new Thread(runGpt).start();
  }

  // This method is used to mute the game.

  /** Method that is called to mute the game. */
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
