package cs3500.music.view;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.model.ReadOnlyModel;
import cs3500.music.util.Direction;

/**
 * Created by Abinader on 11/11/16.
 * CompositeView which provides auditory and visual feedback
 */
public final class CompositeView implements GuiView {
  private final MidiView midi;
  private final GuiView gui;
  private StringBuilder sb;
  
  /**
   * Constructor for a CompositeView.
   * @param midi MidiView
   * @param gui GuiView
   */
  public CompositeView(MidiView midi, GuiView gui) {
    this.midi = midi;
    this.gui = gui;
  }
  
  /**
   * Constructor for a CompositeView to facilitate testing.
   * @param midi MidiView
   * @param gui GuiView
   * @param sb StringBuilder
   */
  public CompositeView(MidiView midi, GuiView gui, StringBuilder sb) {
    this.midi = midi;
    this.gui = gui;
    this.sb = sb;
  }
  
  @Override
  public void initialize(ReadOnlyModel model) {
    this.midi.initialize(model);
    this.gui.initialize(model);
  }
  
  @Override
  public void increaseCurrentBeat() {
    this.midi.increaseCurrentBeat();
    this.gui.increaseCurrentBeat();
  }
  
  @Override
  public void resetBeat() {
    this.gui.resetBeat();
    this.midi.resetBeat();
  }
  
  @Override
  public void addKListener(KeyListener listener) {
    this.gui.addKListener(listener);
  }
  
  @Override
  public void title(String s) {
    this.gui.title(s);
  }
  
  @Override
  public void setWindowColor(Color color) {
    this.gui.setWindowColor(color);
  }
  
  @Override
  public void promptUser(String... s) {
    this.gui.promptUser(s);
  }
  
  @Override
  public void repaintDisplay() {
    this.gui.repaintDisplay();
  }
  
  @Override
  public int getInputs(String s) {
    return this.gui.getInputs(s);
  }
  
  @Override
  public void addMListener(MouseListener listener) {
    this.gui.addMListener(listener);
  }
  
  @Override
  public void scroll(Direction dir) {
    this.gui.scroll(dir);
  }
  
  @Override
  public void jump(boolean toEnd) {
    this.gui.jump(toEnd);
  }
}
