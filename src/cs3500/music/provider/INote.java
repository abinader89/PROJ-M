package cs3500.music.provider;

/**
 * Interface for a note.
 * </p>
 * Changes from Alexa's HW5: There wasn't an interface for the Note class, only the Note class
 * itself.
 * Changes from Campbell's HW5: The Note class was abstracted to ANote instead of implementing an
 * INote class. Also removed extraneous methods.
 */
public interface INote {
  /**
   * Gets the Pitch of this Note.
   *
   * @return the Pitch.
   */
  Pitch pitchPlease();

  /**
   * Gets the octave of this Note.
   *
   * @return the octave.
   */
  int getOctave();

  /**
   * Calculates the integer representation of a Note's octave and intra-octave rank.
   * </p>
   * Change from Campbell's HW5: Added separate method for getting the totalRank.
   *
   * @return integer representation of a Note's octave and intra-octave rank
   */
  int getTotalRank();

  /**
   * Compares this Note to the given Note.
   * If this Note is a lower octave + Pitch than the given Note, then the output is < 0.
   * If this Note is the same octave + Pitch as the given Note, then the output is 0.
   * If this Note is a higher octave + Pitch than the given Note, then the output is > 0.
   * </p>
   * Changes from Alexa's HW5: Added Campbell's compare method.
   *
   * @param that the Note with which we are comparing this Note.
   * @return the above integer representation of octave + pitch comparison.
   */
  int compare(INote that);

  /**
   * Gets the beat at which this Note starts to play.
   *
   * @return the beat offset from beat 0.
   */
  int getStart();

  /**
   * Gets the first beat at which this Note no longer plays.
   *
   * @return the beat offset from beat 0.
   */
  int getEnd();

  /**
   * Gets the number of beats for which this Note plays.
   *
   * @return the number of beats.
   */
  int getDuration();

  /**
   * Gets the integer representation of an instrument with which this Note is played.
   *
   * @return integer representation of an instrument
   */
  int getInstrument();

  /**
   * Gets the volume at which this Note is played.
   *
   * @return int volume
   */
  int getVolume();

  /**
   * Move the start point of the song by the given beat offset.
   * </p>
   * Change from Alexa's HW5: Took Campbell's approach of having the concept of an offset be
   * captured by one single method, instead of two.
   *
   * @param offset a positive or negative amount of beats to shift.
   * @throws IllegalArgumentException if the offset would move the start point below 0.
   */
  void moveStart(int offset) throws IllegalArgumentException;

  /**
   * Change the length of time this Note plays for.
   * </p>
   * Change from Alexa's HW5: Added this method, which was already present in Campbell's model.
   *
   * @param resize the change in amount of time that the note plays for.
   * @throws IllegalArgumentException if the duration of the note after resizing is less than 1.
   */
  void resizeDuration(int resize) throws IllegalArgumentException;

}
