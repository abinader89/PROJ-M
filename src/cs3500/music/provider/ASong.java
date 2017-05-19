package cs3500.music.provider;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents the abstract Song class. Because we utilized a Collection, we abstracted the Song
 * class in such a case that we wanted to change the container used. Most of the functionality can
 * still be defined within this abstract class.
 * </p>
 * Changes from Alexa's HW5: Added abstraction of Song class and overrode equals and hashCode.
 */
public abstract class ASong implements ISong {

  protected final String name;
  protected final Collection<INote> notes;
  protected int bpm;
  protected int tempo;

  /**
   * Construct a song with a given Collection of starting Notes.
   * Beats per measure is assumed to be 4. (Change from Alexa's HW5.)
   * Tempo is assumed to be 100 microseconds per beat. (Added from Alexa's HW5.)
   *
   * @param name         the name of this Song.
   * @param initialNotes the Notes to have in this Song to begin with.
   */
  public ASong(String name, Collection<INote> initialNotes) {
    this.name = name;
    this.notes = initialNotes;
    this.bpm = 4;
    this.tempo = 100;
  }

  /**
   * Construct a song with a given Collection of starting Notes.
   *
   * @param name         the name of this Song.
   * @param initialNotes the Notes to have in this Song to begin with.
   * @param bpm          the beats per measure.
   */
  public ASong(String name, Collection<INote> initialNotes, int bpm, int tempo) {
    this.name = name;
    this.notes = initialNotes;
    this.bpm = bpm;
    this.tempo = tempo;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getBPM() {
    return this.bpm;
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public void setTempo(int tempo) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Tempo cannot be less than or equal to 0.");
    } else {
      this.tempo = tempo;
    }
  }

  @Override
  public void setBPM(int bpm) {
    if (bpm <= 0) {
      throw new IllegalArgumentException("Beats per minute cannot be less than or equal to 0.");
    } else {
      this.bpm = bpm;
    }
  }


  @Override
  public void addNote(INote n) {
    Objects.requireNonNull(n);
    this.notes.add(n);
  }

  @Override
  public void addAllNotes(Collection<INote> notes) {
    Objects.requireNonNull(notes);
    this.notes.addAll(notes);
  }

  @Override
  public void removeNote(INote n) throws IllegalArgumentException {
    Objects.requireNonNull(n);
    if (this.notes.contains(n)) {
      this.notes.remove(n);
    } else {
      throw new IllegalArgumentException("The note must be in the song if you want to remove it.");
    }
  }

  @Override
  public int getLength() {
    if (this.notes.size() < 1) {
      return 0;
    }

    return this.getFirstLast().getSecond().getEnd();
  }

  @Override
  public Pair<INote, INote> getLowHigh() {

    if (this.notes.size() < 1) {
      throw new IllegalStateException("Cannot get lowest and highest notes of an empty song");
    } else {
      INote lowest = null;
      INote highest = null;
      for (INote n : this.notes) {
        if (lowest == null || lowest.compare(n) > 0) {
          lowest = n;
        }

        if (highest == null || highest.compare(n) < 0) {
          highest = n;
        }
      }

      return new Pair<INote, INote>(lowest, highest);
    }
  }

  @Override
  public Pair<INote, INote> getFirstLast() {

    if (this.notes.size() < 1) {
      throw new IllegalStateException("Cannot get first and last notes of an empty song");
    } else {
      INote first = null;
      INote last = null;

      for (INote n : this.notes) {
        if (first == null || first.getStart() > n.getStart()) {
          first = n;
        }

        if (last == null || last.getEnd() < n.getEnd()) {
          last = n;
        }
      }

      return new Pair<INote, INote>(first, last);
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ASong) {
      ASong that = (ASong) other;
      if (that.getNotes().size() != this.notes.size()
              || !that.name.equals(this.name)) {
        return false;
      }

      for (INote n : that.notes) {
        if (!this.notes.contains(n)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    // Two song that are not the same might have the same hash code.
    return Objects.hash(this.name);
  }
}
