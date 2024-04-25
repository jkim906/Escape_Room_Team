package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.SceneName;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  /**
   * The main method for the JavaFX application.
   *
   * @param args The command line arguments.
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Sets the root of the scene to the input file. The method expects that the file is located in
   *
   * @param fxml The name of the FXML file (without extension).
   * @throws IOException If the file is not found.
   */
  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Sets the root of the scene to the input file. The method expects that the file is located in
   */
  public static void loadSceneBeforeChat() {
    scene.setRoot(SceneManager.getScene(GameState.sceneBeforeChat));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  public static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    SceneManager.addScene(SceneName.INTRO, loadFxml("introScene"));
    // set visiblity
    GameState.setCrowBarVisiblity();
    GameState.setWindowObjectVisiblity();
    startScenes();
    // add intor scene

    Parent root = SceneManager.getScene(SceneName.INTRO);
    scene = new Scene(root, 600, 400);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  /**
   * Loads the scene with the given fxml file and scenename.
   *
   * @param fxml the fxml file
   * @param scenename the scenename
   * @throws IOException if there is an I/O error
   */
  public static void loadScene(String fxml, SceneName scenename) throws IOException {
    // check if scene exists
    if (!SceneManager.containsScene(scenename)) {
      SceneManager.addScene(scenename, loadFxml(fxml));
    }
    scene.setRoot(SceneManager.getScene(scenename));
  }

  /**
   * Starts the scenes.
   *
   * @throws IOException if there is an I/O error
   */
  public static void startScenes() throws IOException {
    // add scenes
    SceneManager.addScene(SceneName.PRESENT, loadFxml("present"));
    SceneManager.addScene(SceneName.PAST, loadFxml("past"));
    SceneManager.addScene(SceneName.FUTURE, loadFxml("future"));
    // add scenes
    SceneManager.addScene(SceneName.FUTURECOMPUTER, loadFxml("futurecomputer"));
    SceneManager.addScene(SceneName.PASTDRAWER, loadFxml("pastdrawer"));
    SceneManager.addScene(SceneName.FUTURESAFE, loadFxml("futuresafe"));
    SceneManager.addScene(SceneName.PASTTELEPHONE, loadFxml("pasttelephone"));
    SceneManager.addScene(SceneName.PRESENTLAMP, loadFxml("presentLamp"));
    SceneManager.addScene(SceneName.ENDING1, loadFxml("endingScene1"));
    SceneManager.addScene(SceneName.PRESENTWINDOW, loadFxml("presentWindow"));
    SceneManager.addScene(SceneName.LOADING, loadFxml("loading"));
    // addscenes
    SceneManager.addScene(SceneName.MENU, loadFxml("startMenu"));
  }

  /**
   * Returns the current scene.
   *
   * @return the current scene
   */
  public static Scene getScene() {
    return scene;
  }
}
