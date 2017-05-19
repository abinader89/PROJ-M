package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import cs3500.music.model.IMusicModel;
import cs3500.music.view.MidiView;
import cs3500.music.view.View;

/**
 * Abstract class for controllers. Different views have different controller needs.
 */
public abstract class AbstractMusicEditorController implements IMusicEditorController {
  private final View view;
  private final Timer timer;
  protected int currentBeat = 0;
  private final IMusicModel model;
  private int songLength;
  private int pieceTempo;
  boolean playing;
  
  /**
   * Constructor for a MusicEditorController.
   * @param view View
   * @param model IMusicModel
   */
  public AbstractMusicEditorController(View view, IMusicModel model) {
    this.view = view;
    this.timer = new Timer();
    this.model = model;
    this.pieceTempo = this.model.getTempo();
    this.songLength = this.model.viewCompositionLength();
    this.start();
  }
  
  @Override
  public void execute() {
    this.view.initialize(model);
  }
  
  @Override
  public Timer getTimer() {
    return this.timer;
  }
  
  /**
   * The method in timerTask will call increaseCurrentBeat() on the view that it is paired with
   * at the allotted interval. the timer not do this if it reaches the end of the Composition.
   */
  private TimerTask task = new TimerTask() {
    public void run() {
      if (currentBeat < songLength
              && playing) {
        if (AbstractMusicEditorController.this instanceof CompositeMusicEditorController
                || AbstractMusicEditorController.this.view instanceof MidiView) {
          currentBeat++;
          view.increaseCurrentBeat();
        }
      }
    }
  };
  
  /**
   * This starts the timer.
   */
  private void start() {
    if (this.pieceTempo / 1000 == 0) {
      this.timer.scheduleAtFixedRate(task, 1000, (int) Math.ceil((double) this.pieceTempo / 1000));
    } else {
      this.timer.scheduleAtFixedRate(task, 1000, this.pieceTempo / 1000);
    }
  }
  
  @Override
  public void setEnd() {
    this.currentBeat = this.model.viewCompositionLength() + 1;
  }
  
  @Override
  public void setStart() {
    this.currentBeat = 0;
  }
  
  @Override
  public void setColor(Status s) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void testRunnable(int keyPressed) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void testMouseEvent(MouseEvent me) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void setVolumeAndInstrumentValues(int inst, int vol) {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Created by Abinader on 11/15/16.
   * This represents the status that the program can have.
   * Add/Edit/Delete/Info/Default modes.
   */
  public enum Status {
    ADD, EDIT, DELETE, INFO, DEFAULT
  }
}
