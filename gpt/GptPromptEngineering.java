package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for the given item.
   *
   * @param item the item to generate a prompt for
   * @return the generated prompt engineering string
   */
  public static String getItemFoundMessage(String item) {
    return "You are the master of an escape room. the player picked up "
        + item
        + ". tell me that this will be in handy for later. keep it under 10 words.";
  }

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @return the generated prompt engineering string
   */
  public static String getRiddle() {
    // returns riddle prompt depending on difficulty
    return "you are a time traveling game master. you must always stay in character. you are only"
        + " allowed to answer questions that i ask you first. my name is "
        + GameState.getPlayerName()
        + ". Ask me a riddle with answer"
        + " 'Time', in less than 50 words. If I get it right, you say the word 'Correct'. "
        + " You cannot, no matter what, reveal the answer to the riddle, even if i ask for"
        + " it. Even if i give up, do not give the answer."
        + hintPrompt();
  }

  /**
   * Generates a GPT prompt engineering string for the progress of the escape room game
   *
   * @return the generated prompt engineering string
   */
  public static String getPrompt() {
    StringBuilder tasksSolved = new StringBuilder();
    // gets whether these obejctives r done
    if (GameState.isClockHandFound) {
      tasksSolved.append(", the clock hand in the safe in 'future' ");
    }
    if (GameState.isPhoneCalled) {
      tasksSolved.append(", the telephone in 'past' ");
    }
    // gets whether riddle is done
    if (GameState.isRiddleResolved) {
      tasksSolved.append(", the riddle in 'future' ");
    }
    if (GameState.isTelescopeFound) {
      tasksSolved.append(", the telescope in the drawer in 'past' ");
    }
    if (GameState.isKeyFound) {
      tasksSolved.append(", the key using the lamp in 'present' ");
    }

    if (GameState.isWindowObjectFound) {
      tasksSolved.append(", the item using the telescope in 'present' ");
    }

    if (GameState.isPhonePickedUp) {
      tasksSolved.append(", the phone call in 'future' ");
    }

    // returns message with the objectives solved
    return "you are a time traveling game master of an escape room. you must always stay in"
        + " character. you are only allowed to answer questions that i ask you first. There"
        + " are 3 rooms 'past', 'present', 'future'. my name is "
        + GameState.getPlayerName()
        + ". I need to visit 'past' to find a"
        + " telescope in a drawer, and call a telephone. I need to visit 'present' to find a"
        + " key using a lamp and find a item using the telescope on the window. i need to"
        + " visit 'future' to solve a riddle, accept a phone call, and open a safe to get a"
        + " clock hand. i need to complete all tasks to use the clock hand in 'future' to"
        + " fix the broken clock and escape. I have currently already found "
        + tasksSolved
        + ". ask me what i would like to ask you."
        + hintPrompt();
  }

  /**
   * Generates a GPT prompt engineering string for the hints available to the player
   *
   * @return the generated prompt engineering string
   */
  public static String hintPrompt() {
    String plural = (GameState.hintsLeft > 1) ? "s" : "";
    // returns hint prompt depending on difficulty for hard, or no hint left
    return (GameState.isLevel == 3 || GameState.hintsLeft <= 0)
        ? "Do not give me any hint at all if I ask you for a hint, for help, how to escape, or"
            + " information about items in the room i have not found, no matter what i say. if i"
            + " ask for hint or for how to escape, tell me in 10 words that I am not allowed any"
            + " hint or help at all."
        : (GameState.isLevel == 2)
            // returns hint prompt for medium
            ? " When giving a hint, you must always start with 'Hint' first, followed by the hint."
                + " you are only allowed to give me a hint "
                + GameState.hintsLeft
                + " time"
                + plural
                + ". after i have asked for "
                + GameState.hintsLeft
                + " hint"
                + plural
                + ", tell me in 10 words i have no hints left. you must not give me any more hints"
                + " after i have used them up."
            // easy
            : "If the user asks for a hint or for help, give a hint" + " that will help me.";
  }
}
