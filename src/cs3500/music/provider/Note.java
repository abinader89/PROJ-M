package cs3500.music.provider;

import java.util.Objects;

/**
 * Class implementation of a Note.
 * </p>
 * Changes from Alexa's HW5: Fields originally used 'end' instead of 'duration'; instrument and
 * volume were also unaccounted for.
 * Changes from Campbell's HW5: Fields did not account for instrument or volume.
 */
public class Note implements INote {

  private Pitch pitch;
  private int octave;
  private int start;
  private int duration;
  private int instrument;
  private int volume;

  /**
   * Constructor for the Note class.
   * </p>
   * Changes from Alexa's HW5: Did not do any error-throwing.
   * Changes from Campbell's HW5: Added relevant error-throwing.
   *
   * @param pitch      of this Note as a Pitch.
   * @param octave     of this Note as an integer.
   * @param start      beat of this Note as an integer.
   * @param duration   of this Note in beats as an integer.
   * @param instrument of this Note as a MIDI instrument integer.
   * @param volume     of this Note as an integer.
   */
  public Note(Pitch pitch,
              int octave,
              int start,
              int duration,
              int instrument,
              int volume) {
    if (pitch == null) {
      throw new IllegalArgumentException("Pitch must be non-null.");
    }

    if (octave < 0) {
      throw new IllegalArgumentException("Octave must be greater than or equal to 0.");
    }
    if (start < 0) {
      throw new IllegalArgumentException("Start cannot be before 0.");
    }

    if (duration < 1) {
      throw new IllegalArgumentException("Cannot have a duration less than 1.");
    }

    if (instrument < 1 || instrument > 128) {
      throw new IllegalArgumentException("MIDI does not contain instruments corresponding to "
              + instrument + ".");
    }

    if (volume < 0) {
      throw new IllegalArgumentException("Volume should be between 0 and 100");
    }

    this.pitch = pitch;
    this.octave = octave;
    this.start = start;
    this.duration = duration;
    this.instrument = instrument;
    this.volume = volume;
  }


  @Override
  public Pitch pitchPlease() {
    return this.pitch;
  }

  @Override
  public int getOctave() {
    return this.octave;
  }

  @Override
  public int getTotalRank() {
    return (this.getOctave() * 12) + this.pitchPlease().getRank();
  }

  @Override
  public int compare(INote that) {
    if (that == null) {
      throw new IllegalArgumentException("Cannot compare to a null INote");
    }
    return (this.pitch.getRank() + (this.octave * 12))
            - (that.pitchPlease().getRank()
            + (that.getOctave() * 12));
  }

  @Override
  public int getStart() {
    return this.start;
  }

  @Override
  public int getEnd() {
    return this.start + this.duration;
  }

  @Override
  public int getDuration() {
    return this.duration;
  }

  @Override
  public int getInstrument() {
    return this.instrument;
  }

  @Override
  public int getVolume() {
    return this.volume;
  }

  @Override
  public void moveStart(int offset) throws IllegalArgumentException {
    if (this.start + offset < 0) {
      throw new IllegalArgumentException("Cannot start a Note before the start of the Song.");
    } else {
      this.start = this.start + offset;
    }
  }

  @Override
  public void resizeDuration(int resize) throws IllegalArgumentException {
    if (this.duration + resize < 0) {
      throw new IllegalArgumentException("Cannot resize a Note to less than 1 beat.");
    } else {
      this.duration = this.duration + resize;
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Note) {
      Note that = (Note) other;
      return that.pitch == this.pitch
              && that.octave == this.octave
              && that.start == this.start
              && that.duration == this.duration
              && that.instrument == this.instrument
              && that.volume == this.volume;
    }
    return false;
  }

  @Override
  public String toString() {
    return this.pitch.toString() + this.octave;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pitch,
            this.octave,
            this.start,
            this.duration,
            this.instrument,
            this.volume);
  }
}