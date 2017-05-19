package cs3500.music.adapters;

import java.util.List;

import javax.sound.midi.Sequencer;

import cs3500.music.model.ReadOnlyModel;
import cs3500.music.provider.ISong;
import cs3500.music.provider.MidiMusicView;
import cs3500.music.view.MidiView;

/**
 * Created by Abinader on 11/25/16.
 * This is an adapter for the clients MIDI.
 */
public class MIDIAdapter extends MidiMusicView implements MidiView, IViewAdapter {
  
  /**
   * Constructor for a MidiMusicView.
   *
   * @param sequencer the sequencer to play.
   */
  public MIDIAdapter(Sequencer sequencer) {
    super(sequencer);
  }
  
  @Override
  public void initialize(ReadOnlyModel model) {
    List<ISong> songList = NoteAdapter.toneConverter(model.viewTones(), model.viewTempo());
    super.render(songList);
    try {
      super.play();
    } catch (Exception e) {
      // DO NOTHING INSTEAD
    }
  }
  
  @Override
  public void increaseCurrentBeat() {
    // UNUSED CLIENT VIEW DOES NOT KEEP CONCEPT OF TIME
  }
  
  @Override
  public void resetBeat() {
    // UNUSED CLIENT VIEW USES OTHER MEANS TO RESET ITSELF
  }
}
