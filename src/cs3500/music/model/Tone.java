package cs3500.music.model;

import java.util.Objects;

/**
 * Created by Abinader on 10/17/16.
 * Represents the Tone class. <p>These sounds are bounded from 0 - 127 inclusive because those are
 * the sounds that MIDI supports. there is no point in supporting more than that if using a Music
 * Editor that is designed to be used by MIDI.</p>
 */
public final class Tone implements Comparable<Tone>, ITone {
  private final int sound;
  private final int startBeat;
  private final int duration;
  private final int volume;
  private final int instrument;
  
  /*
  INVARIANT: Tones must be between the values (inclusive) 0 and 127.
  INVARIANT: Durations must be at least 1.
  INVARIANT: Starting Beats must be at least 0.
  INVARIANT: Instruments must be between the values (inclusive) 0 and 127.
  INVARIANT: Volumes must be between the values (inclusive) 0 and 127.
  */
  
  /**
   * Constructor for Tones.
   * @param tone int
   * @throws Exception if creating a tone with an invalid input.
   */
  public Tone(int tone, int startBeat, int duration, int volume, int instrument) {
    if ((tone < 0
            || tone > 127)
            || (duration < 1)
            || (startBeat < 0)
            || (instrument < 0)
            || (instrument > 127)
            || (volume < 0)
            || (volume > 127)) {
      throw new IllegalArgumentException("Invalid tone.");
    } else {
      this.sound = tone;
      this.startBeat = startBeat;
      this.duration = duration;
      this.volume = volume;
      this.instrument = instrument;
    }
  }
  
  @Override
  public String toString() {
    return noteSelector(sound) + octaveSelector(sound);
  }
  
  /**
   * Selects the octave based on the param int.
   * @param tone int
   * @return String
   */
  private String octaveSelector(int tone) {
    return String.valueOf(tone / 12);
  }
  
  // INVARIANT: Note values must be a value specified in the method below (inclusive) 0 and 11.
  /**
   * Selects the pitch based on the param int.
   * @param tone int
   * @return String
   */
  private String noteSelector(int tone) {
    int outputValue = tone % 12;
    switch (outputValue) {
      case 0:
        return "C";
      case 1:
        return "C#";
      case 2:
        return "D";
      case 3:
        return "D#";
      case 4:
        return "E";
      case 5:
        return "F";
      case 6:
        return "F#";
      case 7:
        return "G";
      case 8:
        return "G#";
      case 9:
        return "A";
      case 10:
        return "A#";
      case 11:
        return "B";
      default: // DEAD CODE
        return "";
    }
  }
  
  @Override
  public int compareTo(Tone o) {
    if (o != null) {
      return this.sound - o.sound;
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof Tone) {
      return this.sound - ((Tone) o).sound == 0;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(this.sound,
            this.startBeat,
            this.duration,
            this.volume,
            this.instrument);
  }
  
  @Override
  public int getStartBeat() {
    return startBeat;
  }
  
  @Override
  public int getDuration() {
    return duration;
  }
  
  @Override
  public int getNote() {
    return sound;
  }
  
  @Override
  public int getVolume() {
    return volume;
  }
  
  @Override
  public int getInstrument() {
    return instrument;
  }
  
  @Override
  public Tone next() {
    if (getNote() < 127) {
      return new Tone(getNote() + 1, 0, 1, 1, 0);
    } else {
      // DEAD CODE
      throw new IllegalArgumentException();
    }
  }
  
  @Override
  public boolean isSustain(int i) {
    if (i == 0
            || i == this.startBeat
            || this.duration == 1
            || (this.duration - 1) + this.startBeat < i) {
      return false;
    } else {
      if (this.startBeat >= 1) {
        return (this.startBeat < i
                && (this.duration - 1 + this.startBeat) > i);
      } else {
        return this.duration - 1 > i;
      }
    }
  }
  
  @Override
  public boolean isStartBeat(int i) {
    return this.startBeat == i;
  }
}
