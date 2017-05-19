package cs3500.music.model;

/**
 * Created by Abinader on 12/2/16.
 * Interface for Tones to implement.
 */
public interface ITone {
  
  String toString();
  
  /**
   * Model uses this method to get the starting beat of the Tone invoking it.
   * @return int
   */
  int getStartBeat();
  
  /**
   * Model uses this method to get the duration of the Tone invoking it.
   * @return int
   */
  int getDuration();
  
  /**
   * Model uses this to get the sound of the Tone invoking it.
   * @return int
   */
  int getNote();
  
  /**
   * Model uses this to get the volume of the Tone invoking it.
   * @return int
   */
  int getVolume();
  
  /**
   * Model uses this to get the instrument of the Tone invoking it.
   * @return int
   */
  int getInstrument();
  
  /**
   * Next logical Tone in the series.
   * @return Tone
   */
  Tone next();
  
  /**
   * Determines if this Tone sustains in the given beat.
   * @param i int
   * @return boolean
   */
  boolean isSustain(int i);
  
  /**
   * Determines if this Tone starts in the given beat.
   * @param i int
   * @return boolean
   */
  boolean isStartBeat(int i);
}
