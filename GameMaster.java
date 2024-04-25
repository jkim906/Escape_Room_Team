package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nz.ac.auckland.se206.SceneManager.SceneName;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.speech.TextToSpeechThread;

/** Class for the game master, contains methods for the game master. */
public class GameMaster {

  /**
   * Checks if the input is a hint.
   *
   * @param input the input to check
   * @return true if the input is a hint, false otherwise
   */
  public static boolean isHint(ChatMessage input) {
    return input.getRole().equals("assistant") && input.getContent().startsWith("Hint");
  }

  /**
   * Checks if the input is a hint.
   *
   * @throws IOException if there is an error loading the fxml file
   */
  public static void newChat() throws IOException {
    SceneManager.addScene(SceneName.CHAT, App.loadFxml("chat"));
  }

  /**
   * Method which is called to display the game master text.
   *
   * @param msg The message to be displayed
   * @param gamemasterText The text field to append message to
   */
  public static void displayGameMasterText(String msg, TextField gamemasterText) {
    gamemasterText.setVisible(true);
    gamemasterText.setText(msg);
    if (!GameState.isMute) {
      new TextToSpeechThread(msg).start();
    }

    // close text timer task. using timer to implement this method. close text after 3 seconds
    closeTextTimerTask(gamemasterText);
  }

  /**
   * Method which is called to close the game master text.
   *
   * @param gamemasterText The text field to be closed
   */
  private static void closeTextTimerTask(TextField gamemasterText) {
    Timer timer = new Timer();
    // run method to close text
    TimerTask myTask =
        new TimerTask() {
          @Override
          public void run() {
            Platform.runLater(() -> gamemasterText.setVisible(false));
          }
        };
    // close text after 3 seconds
    timer.schedule(myTask, 3000);
  }

  /**
   * Method which is called to append a message to the chat.
   *
   * @param color The color of the message
   * @param position The position of the message
   * @param response The message to be appended
   * @param vbox The vbox to append the message to
   * @param scrollpane The scrollpane to append the message to
   */
  public static void appendMessage(
      String color, Pos position, ChatMessage response, VBox vbox, ScrollPane scrollpane) {
    // append message to chat
    HBox hbox = new HBox();
    hbox.setAlignment(position);
    hbox.setPadding(new Insets(5, 5, 5, 10));
    Text text = new Text(response.getContent());
    TextFlow textFlow = new TextFlow(text);
    // set style of text
    textFlow.setStyle("-fx-background-color:" + color + ";" + "-fx-background-radius: 30px;");
    textFlow.setPadding(new Insets(5, 10, 5, 10));
    text.setFill(Color.color(1, 1, 1));
    hbox.getChildren().add(textFlow);
    // add to vbox
    vbox.getChildren().add(hbox);
    scrollpane.applyCss();
    scrollpane.layout();
    scrollpane.setVvalue(1.0);
  }
}
