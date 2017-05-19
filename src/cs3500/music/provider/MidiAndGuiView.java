package cs3500.music.provider;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.sound.midi.Sequencer;

/**
 * The MidiAndGuiView class is an adapter that binds the MidiMusicView and GUIMusicView classes
 * together.
 */
public class MidiAndGuiView implements IMusicView {

  private final IGUIMusicView guiView;
  private final IMidiMusicView midiView;

  /**
   * Construct a new MidiAndGuiView.
   * @param gui the GUI portion of the combined view.
   * @param midi the MIDI portion of the combined view.
   */
  public MidiAndGuiView(IGUIMusicView gui, IMidiMusicView midi) {
    this.guiView = gui;
    this.midiView = midi;

    ActionListener onPlay = (ActionEvent e) -> {
      gui.play();
      midi.play();
    };

    ActionListener onPause = (ActionEvent e) -> {
      gui.pause();
      midi.pause();
    };

    ActionListener onStop = (ActionEvent e) -> {
      gui.stop();
      midi.stop();
    };

    // Set the default functions.
    this.guiView.setOnPlayAction(onPlay);
    this.guiView.setOnPauseAction(onPause);
    this.guiView.setOnStopAction(onStop);
    this.guiView.setTimeGetter(this.midiView::getTimeMilli);

  }

  public MidiAndGuiView(Sequencer seq) {
    this(new GUIMusicView(), new MidiMusicView(seq));
  }

  @Override
  public void render(List<ISong> songList) {
    this.guiView.render(songList);
    this.midiView.render(songList);
  }

  @Override
  public void play() {
    this.guiView.play();
    this.midiView.play();
  }

  @Override
  public void pause() {
    this.guiView.pause();
    this.midiView.pause();
  }

  @Override
  public void stop() {
    this.guiView.stop();
    this.midiView.stop();
  }

  @Override
  public void cleanUp() {
    this.guiView.cleanUp();
    this.midiView.cleanUp();
  }

  @Override
  public void setSongAdder(Consumer<ISong> howToAddSong) {
    this.guiView.setSongAdder(howToAddSong);
  }

  @Override
  public void setSongRemover(Consumer<ISong> howToRemoveSong) {
    this.guiView.setSongRemover(howToRemoveSong);
  }

  @Override
  public void showMessage(String message) {
    this.guiView.showMessage(message);
  }
}
