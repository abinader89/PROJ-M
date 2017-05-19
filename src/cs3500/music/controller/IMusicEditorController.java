package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.util.Timer;

/**
 * Created by Abinader on 11/1/16.
 * Interface for controllers.
 */
public interface IMusicEditorController {
  
  /**
   * Runs the controller.
   */
  void execute();
  
  /**
   * Getter method for this controllers timer.
   * @return Timer
   */
  Timer getTimer();
  
  /**
   * Sets the beat to the end of the song.
   */
  void setEnd();
  
  /**
   * Sets the beat to the start of the song.
   */
  void setStart();
  
  /**
   * Sets the color according to the status of the controller.
   */
  void setColor(AbstractMusicEditorController.Status s);
  
  /**
   * This will run runnables.
   * @param keyPressed int
   */
  void testRunnable(int keyPressed);
  
  /**
   * This method facilitates testing the mouse handler capabilities of the controller.
   * @param me MouseEvent
   */
  void testMouseEvent(MouseEvent me);
  
  /**
   * Method to set values that otherwise would need to be specified by the user using a JOptionPane.
   * @param inst int
   * @param vol int
   */
  void setVolumeAndInstrumentValues(int inst, int vol);
}
