package cs3500.music.model;

import java.util.List;

/**
 * Created by Abinader on 10/17/16.
 * Interface for the Model Class. It is generic to use with some type of Note representation,
 * although my model is tightly coupled to my Tone class, it can still be parameterized over
 * another class analogous to Notes and adapted.
 */
public interface IMusicModel<K> extends ReadOnlyModel<K> {
  
  /**
   * Aggregate of adding and removing a Tone.
   * @param toneToEdit K
   * @param sound int
   * @param startBeat int
   * @param duration int
   * @param volume int
   * @param instrument int
   */
  void edit(K toneToEdit, int sound, int startBeat, int duration, int volume, int instrument);
  
  /**
   * Adds a Tone to the composition.
   * @param tone K
   */
  void add(K tone);
  
  /**
   * Removes a Tone from the composition.
   * @param toneToBeRemoved K
   */
  void remove(K toneToBeRemoved);
  
  /**
   * Combines this composition and the param simultaneously.
   * @param comp IMusicModel
   */
  void combineSimul(IMusicModel comp);
  
  /**
   * Combines this composition and the param consecutively.
   * @param comp IMusicModel
   */
  void combineConsec(IMusicModel comp);
  
  /**
   * Renders a textual representation of this composition.
   * @return String
   */
  String toString();
  
  /**
   * The length of the composition in beats.
   * @return int
   */
  int compositionLength();
  
  /**
   * Dumps all of the Tones in this composition into an ArrayList.
   * @return List[K]
   */
  List<K> allTones();
  
  /**
   * (Added this functionality after hw5 to facilitate easier implementation of views).
   * Gets a list of all the Tones at the given beat.
   * @param beat int
   * @return List[K]
   */
  List<K> getTonesAtStartBeat(int beat);
  
  /**
   * (Added this functionality after hw5 to facilitate easier implementation of views).
   * Gets a list of all the Tones at that sustain at the given beat.
   * @param beat int
   * @return List[K]
   */
  List<K> getTonesThatSustain(int beat);
  
  /**
   * Builds an List from the lowest Note, to the highest Note.
   * @return List of Tone
   */
  List<K> getRange();
  
  /**
   * This sets the tempo of the composition.
   * @param tempo int
   */
  void setTempo(int tempo);
  
  /**
   * This gets the tempo for use with the midi.
   * @return int
   */
  int getTempo();
  
  /**
   * Gets a K that matches the signature of the param K
   * @param tone K
   * @return K
   */
  K getTone(K tone);
}
