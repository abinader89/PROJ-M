package cs3500.music.provider;

import java.util.Collection;

/**
 * Interface for a song.
 * </p>
 * Changes from Alexa's HW5: Added interface implementation of the Song class. Removed redundant
 * changeNote method. Because console representation of a Song now exists as a View and is no longer
 * in the Model, a number of methods relating to legacy method `printSong` have been removed from
 * the Model representation of a Song and relocated to TextMusicView.
 * Changes from Campbell's HW5: Concept of a Song did not exist; the MusicModel contained a
 * Collection of notes, therefore not allowing for multiple music pieces to exist within the model
 * at once.
 */
public interface ISong {
  /**
   * Gets the name of this Song.
   *
   * @return the name of this Song.
   */
  String getName();

  /**
   * Gets all Notes in this Song.
   * </p>
   * Change from Alexa's HW5: Songs now contain a Collection of INotes instead of the more specific
   * ArrayList of Notes.
   *
   * @return the Notes of this Song.
   */
  Collection<INote> getNotes();

  /**
   * Set the number of beats per measure.
   * </p>
   * Change from Alexa's HW5: Added this method.
   *
   * @param bpm the new number of beats per measure.
   * @throws IllegalArgumentException if the given BPM is negative.
   */
  void setBPM(int bpm);

  /**
   * Get the tempo of the song.
   * Units: milliseconds per tick. (used by MIDI).
   *
   * @return the song's tempo.
   */
  int getTempo();

  /**
   * Set the current tempo of this song to the given tempo.
   *
   * @param tempo the new tempo to use in the song.
   * @throws IllegalArgumentException if the given tempo is negative.
   */
  void setTempo(int tempo);

  /**
   * Get the beats per measure.
   *
   * @return beats per measure.
   */
  int getBPM();

  /**
   * Add the given Note to this Song.
   *
   * @param n the Note to be added.
   */
  void addNote(INote n);

  /**
   * Add all of the given notes to the song.
   * </p>
   * Change from Alexa's HW5: Added this method.
   *
   * @param notes the collection of Notes to be added.
   */
  void addAllNotes(Collection<INote> notes);

  /**
   * Remove the first Note that equals the given Note in this Song.
   *
   * @param n the Note to remove.
   * @throws IllegalArgumentException if the given Note is not in the Song.
   */
  void removeNote(INote n) throws IllegalArgumentException;

  /**
   * Gets the length of this Song in number of beats.
   *
   * @return length of this Song in number of beats.
   */
  int getLength();

  /**
   * Gets the lowest and highest Notes in this Song.
   * </p>
   * Change from Alexa's HW5: Instead of separate methods to find lowestNote and highestNote,
   * those are now returned as a Pair.
   *
   * @return lowest and highest Notes in this Song.
   * @throws IllegalStateException if called on a Song with no Notes.
   */
  Pair<INote, INote> getLowHigh() throws IllegalStateException;

  /**
   * Gets the first and last Notes in this Song.
   * </p>
   * Change from Alexa's HW5: Added this concept/method.
   *
   * @return the first and last Notes in this Song.
   * @throws IllegalStateException if called on a Song with no Notes.
   */
  Pair<INote, INote> getFirstLast() throws IllegalStateException;

}
