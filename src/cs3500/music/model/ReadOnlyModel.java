package cs3500.music.model;

import java.util.List;

/**
 * Created by Abinader on 11/4/16.
 * Interface to prohibit mutation of the model. The views are passed a model under this interface
 * type.
 */
public interface ReadOnlyModel<K> {
  /**
   * This gets all the tones in the composition.
   * @return List[K]
   */
  List<K> viewTones();
  
  /**
   * This gets the width of the composition.
   * @return List[K]
   */
  List<K> viewRange();
  
  /**
   * This gets the length of the composition in beats.
   * @return int
   */
  int viewCompositionLength();
  
  /**
   * This gets the current value for tempo from the model.
   * @return int
   */
  int viewTempo();
  
  /**
   * This gets a list of all the Tones that start at the given beat.
   * @param beat int
   * @return List[Tone]
   */
  List<K> viewTonesAtStartBeat(int beat);
  
  /**
   * This gets a list of all the Tones that sustain at the given beat.
   * @param beat int
   * @return List[Tone]
   */
  List<K> viewTonesThatSustatin(int beat);
  
  /**
   * Gets a K that matches the signature of the param K
   * @param tone K
   * @return K
   */
  K viewTone(K tone);
}
