package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

/** Controller class for the ending 2 view. */
public class Ending2Controller {

  @FXML private Button exitButton;
  @FXML private Button replayButton;
  @FXML private Label outcome;
  @FXML private Label playerOneTime;
  @FXML private Label playerTwoTime;

  /**
   * Initializes the ending 2 view, displays the appropriate message depending on whether the game.
   */
  @FXML
  private void initialize() {
    // display the time taken by each player
    HoverEffect.addHoverEffect(exitButton, 1.2);
    HoverEffect.addHoverEffect(replayButton, 1.2);
    if (GameState.pOneTime == 1000) {
      playerOneTime.setText("Player 1: " + "DID NOT FINISH \n");
      if (GameState.pOneEasterEgg) {
        playerOneTime.setText(playerOneTime.getText() + "Easter Egg was found by Player 1!");
      } else {
        playerOneTime.setText(playerOneTime.getText() + "Easter Egg was not found by Player 1!");
      }
    } else {
      playerOneTime.setText("Player 1: " + GameState.pOneTime + " seconds \n");
      if (GameState.pOneEasterEgg) {
        playerOneTime.setText(playerOneTime.getText() + "Easter Egg was found by Player 1!");
      } else {
        playerOneTime.setText(playerOneTime.getText() + "Easter Egg was not found by Player 1!");
      }
    }
    if (GameState.pTwoTime == 1000) {
      playerTwoTime.setText("Player 2: " + "DID NOT FINISH \n");
      if (GameState.pTwoEasterEgg) {
        playerTwoTime.setText(playerTwoTime.getText() + "Easter Egg was found by Player 2!");
      } else {
        playerTwoTime.setText(playerTwoTime.getText() + "Easter Egg was not found by Player 2!");
      }
    } else {
      playerTwoTime.setText("Player 2: " + GameState.pTwoTime + " seconds \n");
      if (GameState.pTwoEasterEgg) {
        playerTwoTime.setText(playerTwoTime.getText() + "Easter Egg was found by Player 2!");
      } else {
        playerTwoTime.setText(playerTwoTime.getText() + "Easter Egg was not found by Player 2!");
      }
    }

    // if the game is won, display the winning message
    if (GameState.pOneTime < GameState.pTwoTime) {
      outcome.setText("Player 1 wins! \n Player 2 loses!");
    } else if (GameState.pOneTime > GameState.pTwoTime) {
      outcome.setText("Player 2 wins! \n Player 1 loses!");
    } else {
      outcome.setText("It's a draw! \n Maybe rematch?");
    }
  }

  /**
   * Handles the event when the exit button is pressed, displays a confirmation dialog before.
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
   *
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  private void replayPressed() throws IOException {

    // create a new thread
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // reset the game state when the replay button is pressed
            GameState.resetGame();
            GameState.isPlayerScore = 0;
            GameState.whichPlayer = 0;
            GameState.pOneEasterEgg = false;
            GameState.pTwoEasterEgg = false;
            return null;
          }
        };

    // start the thread
    Thread thread = new Thread(task);
    thread.start();
    App.setRoot("loading");
    // load the start menu
    task.setOnSucceeded(
        e -> {
          try {
            App.setRoot("startMenu");
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });
  }
}
