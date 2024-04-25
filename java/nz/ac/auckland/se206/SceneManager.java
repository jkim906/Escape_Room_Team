package nz.ac.auckland.se206;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Parent;

/** Class for managing scenes. */
public class SceneManager {

  /** Enum for the names of the each scene in this App. */
  public enum SceneName {
    FUTURE,
    FUTURECOMPUTER,
    FUTURESAFE,
    LOADING,
    CHAT,
    PAST,
    PASTDRAWER,
    PASTTELEPHONE,
    PRESENT,
    PRESENTWINDOW,
    PRESENTLAMP,
    MENU,
    ENDING1,
    INTRO,
    ENDING2,
    MIDGAME
  }

  private static Map<SceneName, Parent> scenes = new HashMap<>();

  /**
   * Adds a scene to the scene manager.
   *
   * @param scene the scene to add
   * @param root the root of the scene
   */
  public static void addScene(SceneName scene, Parent root) {
    scenes.put(scene, root);
  }

  /**
   * Returns the scene with the given name.
   *
   * @param scene the name of the scene to return
   * @return the scene with the given name
   */
  public static Parent getScene(SceneName scene) {
    return scenes.get(scene);
  }

  /**
   * Checks if the scene manager contains the given scene.
   *
   * @param scene the scene to check
   * @return true if the scene manager contains the given scene, false otherwise
   */
  public static boolean containsScene(SceneName scene) {
    return scenes.containsKey(scene);
  }
}
