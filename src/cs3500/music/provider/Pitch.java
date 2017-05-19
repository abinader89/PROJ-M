package cs3500.music.provider;

/**
 * The class representing the Pitch of a Note. Each Pitch has a String name and int intra-octave
 * rank.
 * </p>
 * Changes from Alexa's HW5: Switched intra-octave rank to be zero-indexed.
 * Changes from Campbell's HW5: Pitch was originally called "NoteType" and was housed within the
 * ANote class.
 */
public enum Pitch {
  C("C", 0),
  C_SHARP("C#", 1),
  D("D", 2),
  D_SHARP("D#", 3),
  E("E", 4),
  F("F", 5),
  F_SHARP("F#", 6),
  G("G", 7),
  G_SHARP("G#", 8),
  A("A", 9),
  A_SHARP("A#", 10),
  B("B", 11);

  // fields
  private final String name;
  private final int rank;

  /**
   * Constructor for the Pitch enum.
   */
  Pitch(String name, int rank) {
    this.name = name;
    this.rank = rank;
  }

  /**
   * Gets the name of this Pitch.
   *
   * @return String name
   */
  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Gets the rank of this Pitch.
   *
   * @return int rank
   */
  public int getRank() {
    return this.rank;
  }

}