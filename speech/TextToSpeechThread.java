package nz.ac.auckland.se206.speech;

/** Thread class for text to speech. */
public class TextToSpeechThread extends Thread {

  private String msg;
  private TextToSpeech tts;

  /**
   * Creates a new TextToSpeechThread with the given message.
   *
   * @param msg the message to speak
   */
  public TextToSpeechThread(String msg) {
    this.msg = msg;
    this.tts = new TextToSpeech();
  }

  /** Runs the thread. */
  @Override
  public void run() {
    tts.speak(msg);
  }
}
