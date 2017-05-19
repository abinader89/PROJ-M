package cs3500.music.provider;

import java.util.ArrayList;
import java.util.Collection;

import cs3500.music.util.CompositionBuilder;

/**
 * Concrete implementation of ASong.
 * Implements the storage of Notes as an ArrayList.
 * </p>
 * Changes from HW5: Added implementation of CompositionBuilder and SongBuilder, new for this HW.
 */
public class Song extends ASong {

  /**
   * Static class SongBuilder, which implements general utility interface CompositionBuilder.
   */
  public static class SongBuilder implements CompositionBuilder<Song> {

    private int tempo;
    private final Song song;

    /**
     * Constructs a SongBuilder.
     *
     * @param songName as a String.
     */
    public SongBuilder(String songName) {
      this.song = new Song(songName, new ArrayList<>());
      this.tempo = 240; // Default tempo.
    }

    @Override
    public Song build() {
      return this.song;
    }

    @Override
    public CompositionBuilder<Song> setTempo(int tempo) {
      this.song.setTempo(tempo);
      return this;
    }

    @Override
    public CompositionBuilder<Song> addNote(int start,
                                            int end,
                                            int instrument,
                                            int pitch,
                                            int volume) {
      int duration = (end - start) + 1;
      Pitch p = Pitch.values()[pitch % 12];
      int octave = pitch / 12;
      this.song.addNote(new Note(p, octave, start, duration, instrument, volume));

      return this;
    }
  }

  /**
   * Get a Song Builder.
   *
   * @param songName the name of the song that the builder is building.
   * @return a song builder.
   */
  // NOTE: This method is tested via the main() method in MusicEditor.
  public static SongBuilder getBuilder(String songName) {
    return new SongBuilder(songName);
  }

  /**
   * Constructor for Song.
   *
   * @param name         the name of this Song.
   * @param initialNotes the notes that this song contains to begin with.
   */
  public Song(String name, ArrayList<INote> initialNotes) {
    super(name, initialNotes);
  }

  @Override
  public Collection<INote> getNotes() {
    return new ArrayList<INote>(this.notes);
  }

}
