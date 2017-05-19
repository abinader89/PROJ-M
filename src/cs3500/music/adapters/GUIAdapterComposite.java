package cs3500.music.adapters;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import cs3500.music.model.ReadOnlyModel;
import cs3500.music.provider.IGUIMusicView;
import cs3500.music.provider.IMidiMusicView;
import cs3500.music.provider.ISong;
import cs3500.music.provider.MidiAndGuiView;
import cs3500.music.util.Direction;
import cs3500.music.view.GuiView;

/**
 * Created by Abinader on 11/25/16.
 * This is an adapter for the clients combined view.
 */
public class GUIAdapterComposite extends MidiAndGuiView implements GuiView, IViewAdapter {
  
  public GUIAdapterComposite(IGUIMusicView gui, IMidiMusicView midi) {
    super(gui, midi);
  }
  
  @Override
  public void addKListener(KeyListener listener) {
    // UNUSED
  }
  
  @Override
  public void addMListener(MouseListener listener) {
    // UNUSED
  }
  
  @Override
  public void scroll(Direction dir) {
    // UNUSED
  }
  
  @Override
  public void jump(boolean toEnd) {
    // UNUSED
  }
  
  @Override
  public void title(String s) {
    // UNUSED
  }
  
  @Override
  public void setWindowColor(Color color) {
    // UNUSED
  }
  
  @Override
  public void promptUser(String... s) {
    // UNUSED
  }
  
  @Override
  public void repaintDisplay() {
    // UNUSED
  }
  
  @Override
  public int getInputs(String s) {
    // UNUSED
    return 0;
  }
  
  @Override
  public void initialize(ReadOnlyModel model) {
    List<ISong> songList = NoteAdapter.toneConverter(model.viewTones(), model.viewTempo());
    super.render(songList);
    super.setSongAdder((ISong song) -> {
      // DON'T THROW AN EXCEPTION
      super.render(songList);
    });
    super.setSongRemover((ISong song) -> {
      // DON't THROW AN EXCEPTION
      super.render(songList);
    });
  }
  
  @Override
  public void increaseCurrentBeat() {
    // UNUSED
  }
  
  @Override
  public void resetBeat() {
    // UNUSED
  }
}
