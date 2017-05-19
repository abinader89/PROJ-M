package cs3500.music.provider;

import java.util.HashMap;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Class to facilitate creating a MIDI track from our music model's song representation.
 */
public class TrackBuilder {

  private Sequence seq;
  private Track track;
  private final int millisecondsPerTick;
  private HashMap<Integer, Integer> channels;
  private int nextChannel;

  /**
   * Constructs a TrackBuilder given the microseconds per "tick."
   * In a song, a tick is a beat, so this number represents the tempo of this track.
   *
   * @param microPerTick microseconds per tick, which represents the tempo of this track
   * @throws InvalidMidiDataException when the MIDI data is invalid.
   */
  public TrackBuilder(int microPerTick) throws InvalidMidiDataException {
    if (microPerTick < 1000) {
      throw new IllegalArgumentException("Note enough time between ticks.");
    }

    this.nextChannel = 0;

    this.millisecondsPerTick = microPerTick / 1000;

    this.seq = new Sequence(Sequence.SMPTE_25, 40);
    this.channels = new HashMap<>();
    this.track = seq.createTrack();
  }

  /**
   * Adds the given note to this track.
   *
   * @param note to be added.
   * @throws InvalidMidiDataException when the MIDI data is invalid.
   */
  public void addNote(INote note) throws InvalidMidiDataException {
    int timestamp = note.getStart() * this.millisecondsPerTick;
    int duration = timestamp + (note.getDuration() * this.millisecondsPerTick);
    int velocity = note.getVolume();
    int rank = note.pitchPlease().getRank() + (note.getOctave() * 12) - 1;

    if (!this.channels.containsKey(note.getInstrument())) {
      if (this.nextChannel > 15) {
        throw new IllegalStateException("Too many instruments");
      } else {
        this.channels.put(note.getInstrument(), this.nextChannel);
        this.track.add(new MidiEvent(new ShortMessage(
                ShortMessage.PROGRAM_CHANGE,
                this.channels.get(note.getInstrument()),
                note.getInstrument(), 0), -1));
        this.nextChannel++;
      }
    }

    MidiEvent start = new MidiEvent(new ShortMessage(
            ShortMessage.NOTE_ON, this.channels.get(note.getInstrument()), rank, velocity),
            timestamp);
    MidiEvent end = new MidiEvent(new ShortMessage(
            ShortMessage.NOTE_OFF, this.channels.get(note.getInstrument()), rank, velocity),
            duration);
    this.track.add(start);
    this.track.add(end);
  }

  /**
   * Gets the sequence of this track.
   *
   * @return Sequence of this track.
   */
  public Sequence getSequence() {
    return this.seq;
  }

}
