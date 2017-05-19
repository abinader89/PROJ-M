package cs3500.music.provider;

import java.awt.event.ActionListener;
import java.util.function.Supplier;

/**
 * View extension for GUI-specific view functionality.
 */
public interface IGUIMusicView extends IMusicView {

  /**
   * Take an ActionListener and apply it to the play button.
   *
   * @param action ActionListener to be applied to play button.
   */
  void setOnPlayAction(ActionListener action);

  /**
   * Take an ActionListener and apply it to the pause button.
   *
   * @param action ActionListener to be applied to pause button.
   */
  void setOnPauseAction(ActionListener action);

  /**
   * Take an ActionListener and apply it to the stop button.
   *
   * @param action ActionListener to be applied to stop button.
   */
  void setOnStopAction(ActionListener action);

  /**
   * Gets the current time of the MIDI timer.
   *
   * @param howToGetTime a function that tells the method how to get the time.
   */
  void setTimeGetter(Supplier<Long> howToGetTime);

  /**
   * As of Homework 8 patch.
   * CSA 11/29/16
   */
  /**
   * Allows client to inject keyTyped map code from the controller into the view.
   * @param keyCode char
   * @param onKeyTyped Runnable
   */
  void assignKeyTyped(char keyCode, Runnable onKeyTyped);

  /**
   * Allows client to inject keyPressed map code from the controller into the view.
   * @param keyCode Runnable
   * @param onKeyPressed int
   */
  void assignKeyPressed(int keyCode, Runnable onKeyPressed);

  /**
   * Allows client to inject keyReleased map code from the controller into the view.
   * @param keyCode int
   * @param onKeyReleased Runnable
   */
  void assignKeyReleased(int keyCode, Runnable onKeyReleased);

  /**
   * End of Homework 8 patch.
   */

  /**
   * Gets the selected song.
   *
   * @return selected ISong.
   */
  ISong getSelectedSong();
}
