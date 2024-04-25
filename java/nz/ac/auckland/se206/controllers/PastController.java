package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
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

/** Controller for the past scene, handling interactions and actions in the past room. */
public class PastController {

  // "https://www.freepik.com/free-vector/realist-illustration-room-interior_15066643.htm#query=room%20old%20style&position=44&from_view=search&track=ais"
  // Image by pikisuperstar on Freepik

  @FXML private ImageView crowbarImage;
  @FXML private ImageView gamemaster;
  @FXML private Label objectivesPast;
  @FXML private Rectangle drawer;
  @FXML private ImageView present;
  @FXML private Rectangle telephone;
  @FXML private Label pastTimer;
  @FXML private Label drawerLabel;
  @FXML private TextField gamemasterText;
  @FXML private ProgressBar progressBarPast;
  @FXML private Rectangle sidebar;
  @FXML private Rectangle inventoryTab;
  @FXML private ImageView returnButtonPresent;
  @FXML private ImageView checklist;
  @FXML private Label inventoryLabel;
  @FXML private Label label1;
  @FXML private Label objectivesPresent;
  @FXML private ImageView telephoneImage;
  @FXML private ImageView drawerImage;
  @FXML private ImageView inventory;
  @FXML private Line line;
  @FXML private Label labelHint;
  @FXML private Button mute;

  private boolean isInventoryVisible = false;
  private boolean isChecklistVisible = false;

  private String crowbarMessage = "";
  private Gpt gpt;
  private boolean isMedium;

  /**
   * Method which is called when the present scene is created.
   *
   * <p>Initializes the timer and the progress bar.
   *
   * <p>Initializes the inventory and the sidebar.
   *
   * <p>Initializes the hover effects.
   *
   * <p>Initializes the win and lose conditions.
   */
  @FXML
  private void initialize() {
    sidebar.setVisible(false);
    inventoryTab.setVisible(false);
    returnButtonPresent.setVisible(false);
    inventoryLabel.setVisible(false);
    label1.setVisible(false);
    HoverEffect.addHoverEffect(telephoneImage, 1.2);
    HoverEffect.addHoverEffect(drawerImage, 1.2);
    HoverEffect.addHoverEffect(gamemaster, 1.2);
    HoverEffect.addHoverEffect(inventory, 1.2);
    HoverEffect.addHoverEffect(checklist, 1.2);
    objectivesPast.setVisible(false);
    HoverEffect.addHoverEffect(present, 1.2);
    HoverEffect.addHoverEffect(mute, 1.2);

    // This generates the message for the crowbar.
    if (GameState.isCrowBarVisible) {
      // This makes the crowbar visible.
      crowbarImage.setVisible(true);
      HoverEffect.addHoverEffect(crowbarImage, 1.2);
      generateCrowbarMessage();
    }
    // This updates the timer every 500ms.
    Timeline progressBarUpdater =
        new Timeline(
            new KeyFrame(
                // This updates the progress bar every second.
                Duration.seconds(1),
                event -> {
                  int timerValue = GameState.timeLeft;
                  int timerMax = GameState.isTime * 60;
                  double progress = ((double) timerValue) / timerMax;
                  // This updates the progress bar every second.
                  progressBarPast.setProgress(progress);
                }));
    // This updates the progress bar every second.
    progressBarUpdater.setCycleCount(Timeline.INDEFINITE);
    progressBarUpdater.play();

    Timer updateTimer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          // Runs this in the background.
          @Override
          public void run() {
            isMedium = GameState.isLevel == 2;

            // update objectives
            Platform.runLater(
                () -> {
                  // Updates the labels.
                  if (isMedium) {
                    labelHint.setVisible(true);
                    labelHint.setText("Hints Left: " + GameState.hintsLeft);
                  }
                  mute.setText(GameState.mute);
                  pastTimer.setText(GameState.toPrintTime);
                  objectivesPast.setText(GameState.globalObjectives);
                });
          }
        };

    updateTimer.scheduleAtFixedRate(timerTask, 0, 500);
  }

  /**
   * Method which is called when the checklist icon is clicked.
   *
   * <p>Opens the checklist.
   */
  @FXML
  public void onChecklistClicked() {
    // if checklist is visible, hide it
    if (isChecklistVisible) {
      objectivesPast.setVisible(false);
      isChecklistVisible = false;
    } else {
      objectivesPast.setVisible(true);
      isChecklistVisible = true;
    }
  }

  /**
   * Method which is called when the crowbar is clicked.
   *
   * <p>"https://www.freepik.com/icon/crowbar_7047739".
   */
  @FXML
  private void crowbarClicked() {
    // is crowbar clicked
    System.out.println("Crowbar clicked");
    crowbarImage.opacityProperty().set(0);
    if (GameState.isCrowBarVisible && !GameState.isCrowbarFound) {
      GameState.isCrowbarFound = true;
      GameMaster.displayGameMasterText(crowbarMessage, gamemasterText);
      GameState.itemsPickedUp = GameState.itemsPickedUp + "Crowbar\n";
    }
  }

  /**
   * Method which is called when the drawer is clicked. "https://www.vecteezy.com/free-vector/8bit">
   * 8bit Vectors by Vecteezy.
   *
   * @throws IOException if an IO operation fails.
   */
  @FXML
  private void drawerClicked() throws IOException {
    System.out.println("Drawer clicked");
    // If the crowbar is found, the drawer is unlocked.
    // If the key is found, the drawer is unlocked.
    // Updates a label to tell the user what is happening.
    if (GameState.isCrowbarFound || GameState.isKeyFound) {
      drawerLabel.setText("");
      App.loadScene("pastdrawer", SceneName.PASTDRAWER);
    } else if (GameState.isCrowBarVisible) {
      drawerLabel.setText("The drawer is locked \nmaybe there is a key \nor tool somewhere...");
    } else {
      drawerLabel.setText("The drawer is locked\n maybe there is a key \nsomewhere...");
    }
  }

  /**
   * Handles the event when the telephone is clicked, navigating to the past telephone scene.
   *
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  private void telephoneClicked() throws IOException {
    System.out.println("Telephone clicked");
    App.loadScene("pasttelephone", SceneName.PASTTELEPHONE);
  }

  /**
   * Handles the event when the "Present" area is clicked, navigating to the present scene.
   *
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  private void clickPresent() throws IOException {
    App.loadScene("present", SceneName.PRESENT);
  }

  /**
   * Handles the event when the "Game Master" icon is clicked, initiating a chat with the game
   * master.
   *
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  private void clickGameMaster() throws IOException {
    GameState.sceneBeforeChat = SceneName.PAST;
    GameMaster.newChat();
    App.loadScene("gamemaster", SceneName.CHAT);
  }

  /** Generates the GPT message for the crowbar and displays it when the crowbar is found. */
  private void generateCrowbarMessage() {
    Task<Void> runGpt =
        new Task<Void>() {
          // Runs this in the background.
          // This is so the UI doesn't freeze while GPT is running.
          @Override
          protected Void call() throws Exception {
            gpt = new Gpt(1, 0.5, 50);
            ChatMessage msg =
                new ChatMessage("user", GptPromptEngineering.getItemFoundMessage("a crowbar"));
            crowbarMessage = gpt.run(msg).getContent();
            return null;
          }
        };
    new Thread(runGpt).start();
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

  /** Toggles mute state and updates the mute button text accordingly. */
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
