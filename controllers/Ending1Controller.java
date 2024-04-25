package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;

/** Controller class for the ending 1 view. */
public class Ending1Controller {

  @FXML private Label outcome;
  @FXML private Label outcome1;
  @FXML private Button exitButton;
  @FXML private Button replayButton;

  /**
   * Initializes the ending 1 view, displays the appropriate message depending on whether the game
   * is won or lost.
   */
  @FXML
  private void initialize() {
    // if the game is won, display the winning message
    HoverEffect.addHoverEffect(exitButton, 1.2);
    HoverEffect.addHoverEffect(replayButton, 1.2);
    if (GameState.isGameWon) {
      outcome.setText(
          "Congratulations! \nYou have won the game! \n your time was "
              + GameState.playerEndTime
              + " seconds.");
    } else {
      // if the game is lost, display the losing message
      outcome.setText("You have lost the game. \nBetter luck next time!");
    }
    if (GameState.isWindowObjectFound) {
      // if the easter egg is found, display the easter egg message
      outcome1.setText("You have found the easter egg! \n Nice work!");
    } else {
      outcome1.setText("Unfortunately...\n you have not found the easter egg.\n Maybe try again?");
    }
  }

  /**
   * Handles the event when the exit button is pressed, displays a confirmation dialog before
   * exiting the application.
   *
   * @param event the event when the exit button is pressed
   */
  @FXML
  private void onExitPressed(ActionEvent event) {
    // Display a confirmation dialog before exiting the application
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirm Exit");
    alert.setHeaderText("Are you sure you want to exit?");
    alert.setContentText("Press OK to exit or Cancel to stay.");

    // Handle the user's response to the confirmation dialog
    alert
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.OK) {
                // Exit the application
                Platform.exit();
              }
            });
  }

  /**
   * Handles the event when the replay button is pressed, resets the game and loads the start menu.
   */
  @FXML
  private void replayPressed() {
    try {
      Thread resetThread =
          new Thread(
              () -> {
                // reset the game
                try {
                  GameState.resetGame();
                  GameState.whichPlayer = 0;
                  GameState.isPlayerScore = 0;
                  GameState.pOneEasterEgg = false;
                  GameState.pTwoEasterEgg = false;
                } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              });
      // start the thread
      resetThread.start();
      GameState.whichPlayer = 0;
      App.loadScene("startMenu", SceneName.MENU);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
