package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;

/** Controller for the menu scene, allowing the player to set game options and start the game. */
public class MenuController {

  @FXML private Label myLabel1;
  @FXML private Label myLabel2;
  @FXML private Button startButton;
  @FXML private TextField nameInput;
  @FXML private Label errorLabel;
  @FXML private RadioButton easyButton;
  @FXML private RadioButton mediumButton;
  @FXML private RadioButton hardButton;
  @FXML private RadioButton twoMin;
  @FXML private RadioButton fourMin;
  @FXML private RadioButton sixMin;
  @FXML private RadioButton singlePlayer;
  @FXML private RadioButton twoPlayer;

  /**
   * Initializes the controller and sets up the menu scene.
   *
   * <p>Disables the buttons if the game is in two player mode and only the first player is playing.
   */
  @FXML
  public void initialize() {
    HoverEffect.addHoverEffect(startButton, 1.2);
    // Disables the buttons if the second player is playing or if the game is in two player mode.
    if (GameState.whichPlayer == 2 || GameState.isPlayerScore == 2) {
      easyButton.setDisable(true);
      mediumButton.setDisable(true);
      hardButton.setDisable(true);
      twoMin.setDisable(true);
      fourMin.setDisable(true);
      sixMin.setDisable(true);
      singlePlayer.setDisable(true);
      twoPlayer.setDisable(true);
      myLabel1.setText("");
      myLabel2.setText("");
      nameInput.setText("");
      // If the game isn't in two player mode enables the buttons.
    } else {
      easyButton.setDisable(false);
      mediumButton.setDisable(false);
      hardButton.setDisable(false);
      twoMin.setDisable(false);
      fourMin.setDisable(false);
      sixMin.setDisable(false);
      singlePlayer.setDisable(false);
      twoPlayer.setDisable(false);
    }
  }

  /**
   * Handles the event when a difficulty level button is clicked. Sets the game difficulty based on
   * the selected button.
   *
   * @param event The action event triggered by the button click.
   */
  @FXML
  private void onGetLevelClicked(ActionEvent event) {
    // set diffciulty chosen
    if (easyButton.isSelected()) {
      System.out.println("Easy");
      myLabel1.setText(easyButton.getText() + " selected");
      GameState.isLevel = 1;
    } else if (mediumButton.isSelected()) {
      System.out.println("Medium");
      myLabel1.setText(mediumButton.getText() + " selected");
      GameState.isLevel = 2;
    } else if (hardButton.isSelected()) {
      System.out.println("Hard");
      myLabel1.setText(hardButton.getText() + " selected");
      GameState.isLevel = 3;
    }
  }

  /**
   * Handles the event when a game time button is clicked. Sets the game time based on the selected
   * button.
   *
   * @param event The action event triggered when button is pressed.
   */
  @FXML
  private void onGetTimeClicked(ActionEvent event) {
    if (twoMin.isSelected()) {
      // set timesselected for the game
      System.out.println("2 Minutes");
      myLabel2.setText(twoMin.getText() + " selected");
      GameState.isTime = 2;
    } else if (fourMin.isSelected()) {
      System.out.println("4 Minutes");
      myLabel2.setText(fourMin.getText() + " selected");
      GameState.isTime = 4;
    } else if (sixMin.isSelected()) {
      System.out.println("6 Minutes");
      myLabel2.setText(sixMin.getText() + " selected");
      GameState.isTime = 6;
    }
  }

  /**
   * Handles the event when a player number button is clicked. Sets the player number based on the
   * selected button.
   *
   * @param event The action event triggered when button is pressed.
   */
  @FXML
  private void onGetPlayerClicked(ActionEvent event) {
    // get oplayers
    if (singlePlayer.isSelected()) {
      System.out.println("Single Player");
      GameState.isPlayerScore = 1;
    } else if (twoPlayer.isSelected()) {
      System.out.println("Two Player");
      GameState.isPlayerScore = 2;
    }
  }

  /**
   * Handles the event when the "Start" button is pressed. Initiates the game based on selected
   * options and starts the game scene.
   *
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  private void startPressed() throws IOException {

    if (GameState.whichPlayer == 0) {
      GameState.whichPlayer = 1;
    } else {
      GameState.whichPlayer = 2;
    }
    // check if all inputs r given
    String playerName = nameInput.getText();
    GameState.setPlayerName(playerName);
    System.out.println(GameState.getPlayerName());
    if (GameState.isLevel == 0 && GameState.isTime == 0) {
      System.out.println("Please select a level and time");
      errorLabel.setText("*Please select a level and time");
    } else if (GameState.isLevel == 0) {
      System.out.println("Please select a level");
      errorLabel.setText("*Please select a level");
    } else if (GameState.isTime == 0) {
      System.out.println("Please select a time");
      errorLabel.setText("*Please select a time");
    } else if (GameState.isPlayerScore == 0) {
      System.out.println("Please select a player number");
      errorLabel.setText("*Please select a player number");
    } else if (GameState.getPlayerName().equals("")) {
      System.out.println("Please enter a name");
      errorLabel.setText("*Please enter a name");

    } else {
      System.out.println(GameState.whichPlayer + " " + GameState.isPlayerScore);
      GameState.startGame();

      if (GameState.isPlayerScore == 1) {
        // reset all the buttons
        easyButton.setSelected(false);
        mediumButton.setSelected(false);
        hardButton.setSelected(false);
        twoMin.setSelected(false);
        fourMin.setSelected(false);
        sixMin.setSelected(false);
        singlePlayer.setSelected(false);
        twoPlayer.setSelected(false);
        myLabel1.setText("");
        myLabel2.setText("");
        nameInput.setText("");
        App.loadScene("present", SceneName.PRESENT);
        // int totalTimeInSeconds = GameState.isTime * 60;
        // Start the timer thread and store the total time in GameState
        // GameState.time = totalTimeInSeconds;
      } else {
        easyButton.setDisable(true);
        mediumButton.setDisable(true);
        hardButton.setDisable(true);
        twoMin.setDisable(true);
        fourMin.setDisable(true);
        sixMin.setDisable(true);
        singlePlayer.setDisable(true);
        myLabel1.setText("");
        myLabel2.setText("");
        nameInput.setText("");
        App.loadScene("present", SceneName.PRESENT);
      }
    }
  }
}
