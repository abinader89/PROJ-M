package cs3500.music.controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.music.model.IMusicModel;
import cs3500.music.model.Tone;
import cs3500.music.util.Direction;
import cs3500.music.view.GuiView;
import cs3500.music.view.View;

/**
 * Created by Abinader on 11/15/16.
 * Represents the CompositeMouseEditorController.
 */
public final class CompositeMusicEditorController
        extends AbstractMusicEditorController
        implements MouseListener {
  
  private final GuiView view;
  private final IMusicModel model;
  private StringBuilder sb;
  private Map<Integer, Runnable> keyPresses;
  private Status status = Status.DEFAULT;
  private int candidateSound;
  private int candidateStartBeat;
  private int candidateDuration;
  private int candidateVolume;
  private int candidateInstrument;
  private int startingX;
  private Tone highlighted = null;
  
  /**
   * Constructor for a CompositeMusicEditorController.
   * @param view ICompositeView
   * @param model IMusicModel
   */
  public CompositeMusicEditorController(View view, IMusicModel model) {
    super(view, model);
    this.view = (GuiView)view;
    this.model = model;
    super.playing = false;
    this.configureListeners();
    this.candidateSound = -1;
    this.candidateStartBeat = -1;
    this.candidateDuration = -1;
    this.candidateVolume = -1;
    this.candidateInstrument = -1;
  }
  
  /**
   * Constructor for a CompositeMusicEditorController with mock runnables to facilitate testing.
   * @param view ICompositeView
   * @param model IMusicModel
   * @param sb StringBuilder
   */
  public CompositeMusicEditorController(View view, IMusicModel model, StringBuilder sb) {
    super(view, model);
    this.view = (GuiView)view;
    this.model = model;
    super.playing = false;
    this.sb = sb;
    this.configureMockKeyboardListener();
  }
  
  /**
   * Configures a listener that facilitates testing.
   */
  private void configureMockKeyboardListener() {
    this.keyPresses = new HashMap<Integer, Runnable>();
    
    this.keyPresses.put(KeyEvent.VK_Y, () -> {
      this.sb.append("foobar");
      // TESTING PRESSES
    });
  }
  
  /**
   * This configures the mappings to the anonymous functions that run the desired behavior.
   */
  private void configureListeners() {
    this.keyPresses = new HashMap<Integer, Runnable>();
    
    this.keyPresses.put(KeyEvent.VK_A, () -> {
      if (!super.playing
              && this.status == Status.DEFAULT) {
        this.status = Status.ADD;
        this.view.title("Music Editor (Add Mode)");
        this.setColor(this.status);
      }
      // ADD MODE
    });
    
    this.keyPresses.put(KeyEvent.VK_E, () -> {
      if (!super.playing
              && this.status == Status.DEFAULT) {
        this.status = Status.EDIT;
        this.view.title("Music Editor (Edit Mode)");
        this.setColor(this.status);
      }
      // EDIT MODE
    });
  
    this.keyPresses.put(KeyEvent.VK_I, () -> {
      if (!super.playing
              && this.status == Status.DEFAULT) {
        this.status = Status.INFO;
        this.view.title("Music Editor (Informational Mode)");
        this.setColor(status);
      } else {
        if (this.status == Status.INFO) {
          this.status = Status.DEFAULT;
          this.view.title("Music Editor (Paused)");
          this.setColor(status);
        }
      }
      // INFO MODE
    });
  
  
    this.keyPresses.put(KeyEvent.VK_P, () -> {
      if (super.playing) {
        super.playing = false;
        this.view.title("Music Editor (Paused)");
      }
      // PAUSE
    });
    
    this.keyPresses.put(KeyEvent.VK_O, () -> {
      if (!super.playing
              && this.status == Status.DEFAULT
              && super.currentBeat < this.model.viewCompositionLength()) {
        super.playing = true;
        this.view.title("Music Editor (Playing @ " + this.model.getTempo() + ")");
      }
      // PLAY
    });
    
    this.keyPresses.put(KeyEvent.VK_DELETE, () -> {
      if (!super.playing
              && this.status == Status.DEFAULT) {
        this.status = Status.DELETE;
        this.view.title("Music Editor (Delete Mode)");
        this.setColor(this.status);
        return;
      }
      if (this.status == Status.DELETE) {
        this.status = Status.DEFAULT;
        this.view.title("Music Editor (Paused)");
        this.setColor(this.status);
      }
      // TOGGLE DELETE MODE
    });
    
    this.keyPresses.put(KeyEvent.VK_PERIOD, () -> {
      if (!super.playing
              && this.status == Status.DEFAULT) {
        this.view.title("End");
        this.view.jump(true);
        this.view.repaintDisplay();
        super.setEnd();
      }
      // JUMP TO START
    });
    
    this.keyPresses.put(KeyEvent.VK_COMMA, () -> {
      if (!super.playing
              && this.status == Status.DEFAULT) {
        this.view.title("Start");
        this.view.jump(false);
        this.view.repaintDisplay();
        this.view.resetBeat();
        super.setStart();
      }
      // JUMP TO END
    });
    
    this.keyPresses.put(KeyEvent.VK_LEFT, () -> {
      if (!super.playing) {
        this.view.scroll(Direction.LEFT);
      }
      // SCROLL LEFT
    });
    
    this.keyPresses.put(KeyEvent.VK_RIGHT, () -> {
      if (!super.playing) {
        this.view.scroll(Direction.RIGHT);
      }
      // SCROLL RIGHT
    });
    
    this.keyPresses.put(KeyEvent.VK_UP, () -> {
      if (!super.playing) {
        this.view.scroll(Direction.UP);
      }
      // SCROLL UP
    });
    
    this.keyPresses.put(KeyEvent.VK_DOWN, () -> {
      if (!super.playing) {
        this.view.scroll(Direction.DOWN);
      }
      // SCROLL DOWN
    });
    
    this.keyPresses.put(KeyEvent.VK_T, () -> {
      if (!super.playing) {
        try {
          int candidateTempo = this.view.getInputs("t");
          if (candidateTempo == 0) {
            this.promptUser("Tempo cannot be 0!");
          } else {
            this.model.setTempo(candidateTempo);
          }
        } catch (Exception e) {
          this.promptUser("Invalid Input!");
        }
      }
      // TEMPO SETTER
    });
    
    KeyboardHandler kbd = new KeyboardHandler();
    kbd.setKeyPressedMap(this.keyPresses);
    this.view.addKListener(kbd);
    this.view.addMListener(this);
  }
  
  /**
   * Sends a message to the gui to pop up an error message.
   * @param message String
   */
  private void promptUser(String... message) {
    this.view.promptUser(message);
  }
  
  @Override
  public void testRunnable(int keyPressed) {
    if (this.keyPresses.containsKey(keyPressed)) {
      this.keyPresses.get(keyPressed).run();
    }
  }
  
  @Override
  public void testMouseEvent(MouseEvent me) {
    switch (me.getID()) {
      case MouseEvent.MOUSE_CLICKED:
        this.mouseClicked(me);
        break;
      case MouseEvent.MOUSE_PRESSED:
        this.mousePressed(me);
        break;
      case MouseEvent.MOUSE_RELEASED:
        this.mouseReleased(me);
        break;
      default: // NO OTHER OPTION
    }
  }
  
  @Override
  public void setVolumeAndInstrumentValues(int inst, int vol) {
    this.candidateInstrument = inst;
    this.candidateVolume = vol;
  }
  
  /**
   * Sets the color of the window depending on the status of the controller.
   * @param s Status
   */
  @Override
  public void setColor(Status s) {
    Color teal = new Color(95, 158, 160);
    switch (s) {
      case DEFAULT:
        this.view.setWindowColor(Color.DARK_GRAY);
        break;
      case ADD:
        this.view.setWindowColor(Color.BLUE);
        break;
      case DELETE:
        this.view.setWindowColor(Color.RED);
        break;
      case EDIT:
        this.view.setWindowColor(Color.ORANGE);
        break;
      case INFO:
        this.view.setWindowColor(teal);
        break;
      default: // NO OTHER OPTION
    }
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    switch (status) {
      case DELETE:
        if ((e.getX() > 75)
                && (e.getX() < (75
                + (this.model.compositionLength() * 25)))
                && (e.getY() > 25)
                && (e.getY()
                < (25 + (model.getRange().size() * 25)))) {
          List<Tone> range = this.model.getRange();
          Collections.reverse(range);
          this.candidateSound = range.get((e.getY() - 25) / 25).getNote();
          this.candidateStartBeat = ((e.getX() - 75) / 25);
          try {
            Tone deleteMe = new Tone(this.candidateSound,
                    this.candidateStartBeat, 1, 1, 0);
            this.model.remove(deleteMe);
            this.view.repaintDisplay();
            return;
          } catch (IllegalArgumentException iae) {
            // HANDLE
            this.view.promptUser("There's No Note Here!");
          }
        }
        break;
      case EDIT:
        if ((e.getX() > 75)
                && (e.getX() < (75
                + (this.model.compositionLength() * 25)))
                && (e.getY() > 25)
                && (e.getY()
                < (25 + (this.model.getRange().size() * 25)))) {
          List<Tone> range = this.model.getRange();
          Collections.reverse(range);
          this.candidateSound = range.get((e.getY() - 25) / 25).getNote();
          this.candidateStartBeat = ((e.getX() - 75) / 25);
          List<Tone> tonesAtTargetBeat = this.model.getTonesAtStartBeat(this.candidateStartBeat);
          try {
            for (Tone t : tonesAtTargetBeat) {
              if (candidateSound == t.getNote()) {
                this.highlighted = t;
              }
            }
          } catch (NullPointerException npe) {
            // HANDLE
            this.status = Status.DEFAULT;
            this.setColor(this.status);
            this.view.title("Music Editor (Paused)");
            this.view.promptUser("There's No Note Here!");
            return;
          }
          if (this.highlighted != null) {
            this.status = Status.ADD;
            this.setColor(this.status);
            return;
          }
        }
        break;
      case INFO:
        if ((e.getX() > 75)
                && (e.getX() < (75
                + (CompositeMusicEditorController.this.model.compositionLength() * 25)))
                && (e.getY() > 25)
                && (e.getY() <
                (25 + (CompositeMusicEditorController.this.model.getRange().size() * 25)))) {
          List<Tone> range = CompositeMusicEditorController.this.model.getRange();
          Collections.reverse(range);
          this.candidateSound = range.get((e.getY() - 25) / 25).getNote();
          this.candidateStartBeat = ((e.getX() - 75) / 25);
          Tone checkMe = new Tone(this.candidateSound, this.candidateStartBeat, 1, 1, 0);
          try {
            checkMe = (Tone) CompositeMusicEditorController.this.model.getTone(checkMe);
            CompositeMusicEditorController.this.promptUser("Note: " + checkMe.toString(),
                    String.valueOf("Start: " + checkMe.getStartBeat()),
                    String.valueOf("Duration: " + checkMe.getDuration()),
                    String.valueOf("Instrument: " + checkMe.getInstrument()),
                    String.valueOf("Volume: " + checkMe.getVolume()));
          } catch (NullPointerException npe) {
            // HANDLE
            CompositeMusicEditorController.this.promptUser("There's No Note Here!");
          }
          return;
        }
    default: // NO OTHER OPTION
    }
    this.view.title("Music Editor (Paused)");
    this.status = Status.DEFAULT;
    this.setColor(status);
    this.resetDummyTone();
  }
  
  @Override
  public void mousePressed(MouseEvent e) {
    if (status == Status.ADD) {
      if ((e.getX() > 75)
              && (e.getX() < (75
              + (this.model.compositionLength() * 25)))
              && (e.getY() > 25)
              && (e.getY() < (25 + (this.model.getRange().size()
              * 25)))) {
        List<Tone> range = this.model.getRange();
        Collections.reverse(range);
        this.startingX = e.getX();
        this.candidateSound = range.get((e.getY() - 25) / 25).getNote();
        this.candidateStartBeat = ((e.getX() - 75) / 25);
      }
    }
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    if (status == Status.ADD) {
      if ((e.getX() > 75)
              && (e.getX() < (75
              + (this.model.compositionLength() * 25)))
              && (e.getY() > 25)
              && (e.getY() < (25 + (this.model.getRange().size()
              * 25)))) {
        this.candidateDuration = (((e.getX() - 75) / 25) - ((this.startingX - 75) / 25));
        try {
          if (this.candidateInstrument == -1
                  && this.candidateVolume == -1) {
            this.candidateInstrument =
                    this.view.getInputs("i");
            this.candidateVolume =
                    this.view.getInputs("v");
          }
          Tone addMe = new Tone(this.candidateSound, this.candidateStartBeat,
                  this.candidateDuration, this.candidateVolume, this.candidateInstrument);
          this.model.add(addMe);
          this.view.repaintDisplay();
        } catch (IllegalArgumentException iae) {
          // HANDLE
          this.view.promptUser("Something went wrong!");
          this.highlighted = null;
        }
      }
      this.status = Status.DEFAULT;
      this.setColor(status);
      this.view.title("Music Editor (Paused)");
      if (this.highlighted != null) {
        this.model.remove(this.highlighted);
        this.view.repaintDisplay();
      }
      this.resetDummyTone();
    }
  }
  
  @Override
  public void mouseEntered(MouseEvent e) {
    // UNUSED
  }
  
  @Override
  public void mouseExited(MouseEvent e) {
    // UNUSED
  }
  
  /**
   * Resets the dummy values for a tone.
   */
  private void resetDummyTone() {
    this.candidateSound = -1;
    this.candidateStartBeat = -1;
    this.candidateDuration = -1;
    this.candidateVolume = -1;
    this.candidateInstrument = -1;
    this.startingX = -1;
    this.highlighted = null;
  }
}
