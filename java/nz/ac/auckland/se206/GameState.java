package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager.SceneName;

/** Represents the state of the game. */
public class GameState {

  private static String playerName;

  public static boolean isCrowBarVisible = false;

  public static int whichPlayer = 0;

  public static int playerEndTime = -1;

  public static int pOneTime = -1;

  public static int pTwoTime = -1;

  public static boolean isObject1Visible = false;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isClockHandFound = false;

  public static boolean isGameWon = false;

  public static boolean isPhoneFound = false;

  public static boolean isCrowbarFound = false;

  public static boolean isWindowObjectFound = false;

  public static boolean isPhonePickedUp = false;

  public static boolean isTelescopeFound = false;

  public static boolean isPhoneCalled = false;

  public static boolean isKeyFound = false;

  public static String safeCode = "-1";

  public static int hintsLeft = 5;

  public static SceneName sceneBeforeChat;

  public static int isLevel = 0;

  public static int isTime = 0;

  public static String windowObjective = "BONUS: Look out in the distance\n from the window";

  public static String clockHandObjective = "Fix the stop watch to escape";

  public static String phoneObjective = "Call the future";

  public static String pickUpPhoneObjective = "Pick up the phone";

  public static String globalObjectives =
      clockHandObjective + "\n" + windowObjective + "\n" + phoneObjective + "\n";

  public static String itemsPickedUp = "";

  public static String name = "";

  public static int isPlayerScore = 0;

  public static boolean isDifficultySet = false;

  public static int timeLeft = -1;

  public static int timeLeftEnd = -1;

  public static String toPrintTime = "";

  public static boolean isGameStarted = false;

  public static boolean isEasterEggFound = false;

  public static boolean isMute = false;

  public static String mute = "Mute";

  public static boolean pOneEasterEgg = false;

  public static boolean pTwoEasterEgg = false;

  /**
   * Sets the player name that is currently playing.
   *
   * @param playerName The player name.
   */
  public static void setPlayerName(String playerName) {
    GameState.playerName = playerName;
  }

  /**
   * Retrieves the name of the player playing the current game.
   *
   * @return The player name.
   */
  public static String getPlayerName() {
    return playerName;
  }

  /** Starts the game timer and the objective list task. */
  public static void startGame() {
    // set time left
    if (isTime == 2) {
      timeLeft = 120;
      timeLeftEnd = 120;
    } else if (isTime == 4) {
      timeLeft = 240;
      timeLeftEnd = 240;
    } else if (isTime == 6) {
      timeLeft = 360;
      timeLeftEnd = 360;
    }

    isDifficultySet = true;

    // start timer
    if (!isGameStarted) {
      startTimer();
      objectiveListTask();
      isGameStarted = true;
    }
  }

  /**
   * Resets all the static variables fo a new game to begin.
   *
   * @throws IOException if there is an I/O error
   */
  public static void resetGame() throws IOException {
    // reset all the variables
    isCrowBarVisible = false;
    isObject1Visible = false;
    setCrowBarVisiblity();
    setWindowObjectVisiblity();
    // startthe scenes
    isClockHandFound = false;
    isWindowObjectFound = false;
    // reset riddle solved
    isRiddleResolved = false;
    isPhoneFound = false;
    // reset phone found
    isGameWon = false;
    isCrowbarFound = false;
    isTelescopeFound = false;
    isPhonePickedUp = false;
    isKeyFound = false;
    // reset telescope found
    isPhoneCalled = false;
    isEasterEggFound = false;
    // reset the objectives
    safeCode = "-1";
    hintsLeft = 5;
    globalObjectives = clockHandObjective + "\n" + windowObjective + "\n" + phoneObjective + "\n";
    name = "";
    // reset player name
    playerName = "";
    toPrintTime = "";
    isDifficultySet = false;

    itemsPickedUp = "";

    isMute = false;
    mute = "Mute";

    App.startScenes();
  }

  /** Starts the timer task for the game. */
  public static void startTimer() {
    Timer timer = new Timer();

    // timer task to update the time left
    TimerTask timerTask =
        new TimerTask() {
          @Override
          public void run() {

            // set the sconds left remianing
            String secondsLeft;
            if (timeLeft % 60 < 10) {
              secondsLeft = "0" + timeLeft % 60;
            } else {
              // update seconds left remaining
              secondsLeft = "" + timeLeft % 60;
            }
            // set the minutes left raemainig
            String minutesLeft;
            minutesLeft = "" + timeLeft / 60;
            toPrintTime = minutesLeft + ":" + secondsLeft;
            if (timeLeft == 0) {
              Platform.runLater(
                  () -> {
                    try {
                      // This loads the ending scene if the player has won.
                      if (GameState.whichPlayer == 1
                          && GameState.isPlayerScore == 2
                          && GameState.isGameWon) {
                        App.setRoot("midGame");
                        // This changes the player time to 1000 if the player has lost.
                      } else if (GameState.whichPlayer == 1
                          && GameState.isPlayerScore == 2
                          && GameState.isGameWon == false) {
                        GameState.pOneTime = 1000;
                        App.setRoot("midGame");
                        // This loads the ending scene if the player 1 has won.
                      } else if (GameState.whichPlayer == 1 && GameState.isPlayerScore == 1) {
                        System.out.println("Player 1 won");
                        App.setRoot("endingScene1");
                        // This changes the player time to 1000 if the player 2 has lost.
                      } else if (GameState.whichPlayer == 2
                          && GameState.isPlayerScore == 2
                          && GameState.isGameWon == false) {
                        GameState.pTwoTime = 1000;
                        App.setRoot("endingScene2");
                        // This loads the ending scene if the player 2 has won.
                      } else if (GameState.whichPlayer == 2 && GameState.isPlayerScore == 1) {
                        App.setRoot("endingScene2");
                      } else {
                        App.setRoot("endingScene2");
                      }
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  });
            }
            timeLeft--;
          }
        };

    // timer
    timer.scheduleAtFixedRate(timerTask, 0, 1000);
  }

  /** Starts the objective list task. */
  private static void objectiveListTask() {
    // update objectives
    Timer updateObjectives = new Timer();
    updateObjectives.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            // update objectives
            if (GameState.isWindowObjectFound
                && GameState.globalObjectives.indexOf(GameState.windowObjective + " (Done)" + "\n")
                    == -1) {
              int j = GameState.globalObjectives.indexOf(GameState.windowObjective + "\n");
              StringBuilder newString = new StringBuilder(GameState.globalObjectives);
              newString.insert(j + GameState.windowObjective.length(), " (Done)");
              GameState.globalObjectives = newString.toString();
            }
            // update objectives
            if (GameState.isPhoneCalled
                && GameState.globalObjectives.indexOf(GameState.phoneObjective + " (Done)" + "\n")
                    == -1) {
              int i = GameState.globalObjectives.indexOf(GameState.phoneObjective + "\n");
              StringBuilder newString = new StringBuilder(GameState.globalObjectives);
              newString.insert(
                  i + GameState.phoneObjective.length(),
                  " (Done)" + "\n" + GameState.pickUpPhoneObjective + "\n");
              GameState.globalObjectives = newString.toString();
            }
            // update objectives
            if (GameState.isPhonePickedUp
                && GameState.globalObjectives.indexOf(
                        GameState.pickUpPhoneObjective + " (Done)" + "\n")
                    == -1) {
              int i = GameState.globalObjectives.indexOf(GameState.pickUpPhoneObjective + "\n");
              StringBuilder newString = new StringBuilder(GameState.globalObjectives);
              newString.insert(i + GameState.pickUpPhoneObjective.length(), " (Done)");
              GameState.globalObjectives = newString.toString();
            }
            // update objectives

            if (GameState.isClockHandFound
                && GameState.globalObjectives.indexOf(
                        GameState.clockHandObjective + " (Done)" + "\n")
                    == -1) {
              int i = GameState.globalObjectives.indexOf(GameState.clockHandObjective + "\n");
              StringBuilder newString = new StringBuilder(GameState.globalObjectives);
              newString.insert(i + GameState.clockHandObjective.length(), " (Done)");
              GameState.globalObjectives = newString.toString();
            }
          }
        },
        0,
        34);
  }

  /**
   * Sets the visibility of the inventory.
   *
   * @param isActive Indicates whether the inventory is active.
   * @param returnButtonPresent The return button.
   * @param inventoryTab The inventory tab.
   * @param sidebar The sidebar.
   * @param inventoryLabel The inventory label.
   * @param label1 The label.
   * @param line The line.
   */
  public static void inventorySetter(
      Boolean isActive,
      ImageView returnButtonPresent,
      Rectangle inventoryTab,
      Rectangle sidebar,
      Label inventoryLabel,
      Label label1,
      Line line) {
    // Sets the visibility of the inventory.
    returnButtonPresent.setVisible(!isActive);
    inventoryTab.setVisible(!isActive);
    sidebar.setVisible(!isActive);
    sidebar.setOpacity(!isActive ? 0.3 : 0.0);
    inventoryLabel.setVisible(!isActive);
    label1.setVisible(!isActive);
    // Sets the text of the inventory label.
    label1.setText(GameState.itemsPickedUp);
    line.setVisible(!isActive);
  }

  /** Sets the visibility of the crowbar. */
  public static void setCrowBarVisiblity() {
    double random = Math.random();
    System.out.println(random);
    // set visibility of crowbar
    if (random < 0.60) {
      isCrowBarVisible = true;
    }
  }

  /** Sets the visibility of the window object. */
  public static void setWindowObjectVisiblity() {
    double random = Math.random();
    // set visibility of clock hand
    if (random < 0.60) {
      isObject1Visible = true;
    }
  }
}
