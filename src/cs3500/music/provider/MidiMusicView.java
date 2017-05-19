package cs3500.music.provider;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

/**
 * Class used to make a Song audible using Java's MIDI framework.
 * </p>
 * The view generates a new Track using the notes in the song and the TrackBuilder class. Each of
 * the songs is converted into two MidiEvents which hold the Messages for starting and stopping the
 * note at the calculated offset times. These notes are part of a sequence which is fed into a
 * Sequencer object that is then used to play the song. The Sequencer class was chosen in this
 * implementation to avoid keeping track of scheduling each of the notes.
 * </p>
 * Usage Note: Although the IMusicView interface passes a list of ISongs, only the first song is
 * rendered. In the future, it is possible that we would play multiple songs back to back.
 */
public class MidiMusicView implements IMidiMusicView {

  private Sequencer sequencer;
  private long currentTick;

  /**
   * Constructor for a MidiMusicView.
   *
   * @param sequencer the sequencer to play.
   */
  public MidiMusicView(Sequencer sequencer) {
    this.sequencer = sequencer;
    this.currentTick = 0; // Beginning of the song.
  }

  @Override
  public void render(List<ISong> songList) {
    Objects.requireNonNull(songList);
    if (songList.size() == 0) {
      throw new IllegalArgumentException("No song provided");
    }

    // Only the first song is rendered; this program does not currently support multiple songs
    // at a time.
    ISong song = songList.get(0);

    this.currentTick = 0; // Hold the current tick position.

    try {
      TrackBuilder builder = new TrackBuilder(song.getTempo());

      for (INote n : song.getNotes()) {
        builder.addNote(n);
      }

      this.sequencer.open();
      this.sequencer.setSequence(builder.getSequence());

    } catch (MidiUnavailableException e) {
      System.err.println("Could not obtain Midi resources: " + e.getMessage());
      System.exit(2);
    } catch (InvalidMidiDataException e) {
      System.err.println("Midi file data invalid: " + e.getMessage());
      System.exit(3);
    }
  }

  /**
   * Ensures that the sequencer is open before playing the MIDI track.
   *
   * @throws IllegalStateException when the sequencer is not open.
   */
  private void ensureSequencerState() throws IllegalStateException {
    if (!this.sequencer.isOpen()) {
      throw new IllegalStateException("Must call render before calling play");
    }
  }

  @Override
  public void play() throws IllegalStateException {
    this.ensureSequencerState();

    this.sequencer.setTickPosition(this.currentTick);
    this.sequencer.start();
  }

  @Override
  public void pause() {
    this.ensureSequencerState();
    this.currentTick = this.sequencer.getTickPosition();
    this.sequencer.stop();
  }

  @Override
  public void stop() {
    this.ensureSequencerState();
    this.currentTick = 0;
    this.sequencer.stop();
  }

  @Override
  public void cleanUp() {
    this.sequencer.close();
  }

  @Override
  public void setSongAdder(Consumer<ISong> howToAddSong) {
    // MIDI does not do anything for this method.
  }

  @Override
  public void setSongRemover(Consumer<ISong> howToRemoveSong) {
    // MIDI does not do anything for this method.
  }

  @Override
  public void showMessage(String message) {
    throw new IllegalArgumentException("Don't call showMessage on a Midi view pls");
  }

  @Override
  public long getTimeMilli() {
    return this.sequencer.getMicrosecondPosition();
  }
}
