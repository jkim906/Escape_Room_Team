package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;

/** Controller for the mid-game scene, displaying player time and navigation to the menu. */
public class MidGameController {

  @FXML private Button backToMenu;
  @FXML private Label playerOneTime;

  /** Initializes the controller and displays player one's time. */
  @FXML
  public void initialize() {
    // add hover effect to buttons
    HoverEffect.addHoverEffect(backToMenu, 1.2);
    // set player one time
    if (GameState.pOneTime == 1000) {
      playerOneTime.setText("Player 1: " + "DID NOT FINISH");
      // if player one found the easter egg, display a message
      if (GameState.pOneEasterEgg) {
        playerOneTime.setText(
            playerOneTime.getText() + "\nEaster Egg Found By " + GameState.getPlayerName() + "!");
      } else {
        // if player one did not find the easter egg, display the correct message
        playerOneTime.setText(
            playerOneTime.getText()
                + "\nEaster Egg was not found by "
                + GameState.getPlayerName()
                + "!");
      }
    } else {
      playerOneTime.setText("Player 1: " + GameState.pOneTime + " seconds");
      // if player one found the easter egg, display a message
      if (GameState.pOneEasterEgg) {
        playerOneTime.setText(
            playerOneTime.getText()
                + "\nEaster Egg Was Found By "
                + GameState.getPlayerName()
                + "!");
      } else {
        // if player one did not find the easter egg, display the correct message
        playerOneTime.setText(
            playerOneTime.getText()
                + "\nEaster Egg was not found by "
                + GameState.getPlayerName()
                + "!");
      }
    }
  }

  /**
   * Handles the event when the "Back to Menu" button is clicked.
   *
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  private void onBackToMenuClicked() throws IOException {
    GameState.whichPlayer = 2;
    try {
      Thread resetThread =
          new Thread(
              () -> {
                try {
                  GameState.resetGame();
                } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              });
      resetThread.start();
      GameState.whichPlayer = 2;
      App.loadScene("startMenu", SceneName.MENU);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
