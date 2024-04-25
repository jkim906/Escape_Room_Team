package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.HoverEffect;
import nz.ac.auckland.se206.SceneManager.SceneName;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.Gpt;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller for the introduction scene in the 'Lost in Time' game. */
public class IntroSceneController {

  @FXML private Label introText;
  @FXML private ImageView startButton;

  private Gpt gpt;

  /**
   * Initializes the controller and sets up the required components.
   *
   * @throws ApiProxyException if an error occurs with the API proxy.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    HoverEffect.addHoverEffect(startButton, 1.2);

    // runs gpt to get the prompt for the chat when player opens chat
    gpt = new Gpt(0.5, 0.5, 100);
    ChatMessage msg =
        gpt.run(
            new ChatMessage(
                "user",
                "welcome me to the game 'lost in time'. tell me in less than 50 words in"
                    + " order to escape, i need to retrieve a clock hand to fix the broken"
                    + " clock before time runs out. tell me to find a hidden easter egg."));

    introText.setText(msg.getContent());
  }

  /**
   * Handles the event when the start button is pressed.
   *
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  public void startPressed() throws IOException {
    App.loadScene("startMenu", SceneName.MENU);
  }
}
