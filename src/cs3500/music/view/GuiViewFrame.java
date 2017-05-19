package cs3500.music.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import cs3500.music.model.ReadOnlyModel;
import cs3500.music.model.ITone;
import cs3500.music.util.Direction;

/**
 * Incorporates the given ReadOnlyModel and the Concrete Panel to create a Swing window view.
 */
public final class GuiViewFrame extends JFrame implements GuiView {
  private ReadOnlyModel model;
  private int rangeSize;
  private int timerCurrentBeat = 0;
  private JPanel displayPanel;
  
  private MouseListener mlistener;
  private KeyListener klistener;
  private JScrollPane scrollbar;
  
  private int duration;
  private int measures;
  private int offset = 0;
  
  /**
   * Constructs a window to hold the data represented by the GUI.
   */
  public GuiViewFrame() {
    this.setTitle("Start");
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
  
  @Override
  public void initialize(ReadOnlyModel model) {
    this.model = model;
    this.rangeSize = this.model.viewRange().size();
    this.duration = compositionLengthIncreaser(this.model.viewCompositionLength());
    this.measures = this.duration / 4;
    if (offset > 0) {
      offset -= 1;
    }
    displayPanel = new ConcreteGuiViewPanel(model);
    this.getContentPane().add(this.displayPanel);
    this.scrollbar = new JScrollPane(this.displayPanel, JScrollPane
            .VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.add(scrollbar);
    this.displayPanel.setPreferredSize(getPreferredSize());
    this.displayPanel.setBackground(Color.darkGray);
    this.pack();
    this.setVisible(true);
    this.displayPanel.addMouseListener(mlistener);
    this.addKeyListener(klistener);
  }
  
  @Override
  public void increaseCurrentBeat() {
    this.timerCurrentBeat++;
    this.displayPanel.repaint();
    if (this.timerCurrentBeat == this.duration) {
      this.title("End");
    }
    if ((this.timerCurrentBeat % 52) == 0) {
      this.scrollbar.getHorizontalScrollBar().setValue(this.timerCurrentBeat * 25);
    }
  }
  
  @Override
  public void resetBeat() {
    this.timerCurrentBeat = 0;
    
  }
  
  @Override
  public Dimension getPreferredSize() {
    return new Dimension((200 + (this.model.viewCompositionLength() * 25)),
            (125 + (this.rangeSize * 25)));
  }
  
  @Override
  public void addKListener(KeyListener listener) {
    this.klistener = listener;
  }
  
  @Override
  public void addMListener(MouseListener listener) {
    this.mlistener = listener;
  }
  
  @Override
  public void scroll(Direction dir) {
    switch (dir) {
      case LEFT:
        this.scrollbar.getHorizontalScrollBar()
                .setValue(scrollbar.getHorizontalScrollBar().getValue() - 100);
        break;
      case RIGHT:
        this.scrollbar.getHorizontalScrollBar()
                .setValue(scrollbar.getHorizontalScrollBar().getValue() + 100);
        break;
      case UP:
        this.scrollbar.getVerticalScrollBar()
                .setValue(scrollbar.getVerticalScrollBar().getValue() - 100);
        break;
      case DOWN:
        this.scrollbar.getVerticalScrollBar()
                .setValue(scrollbar.getVerticalScrollBar().getValue() + 100);
        break;
      default: // NO OTHER OPTION
    }
  }
  
  @Override
  public void jump(boolean toEnd) {
    if (toEnd) {
      this.timerCurrentBeat = this.model.viewCompositionLength();
      this.scrollbar.getViewport().setViewPosition(new Point(this.model.viewCompositionLength()
              * 25 + 25, 0));
    } else {
      this.timerCurrentBeat = 0;
      this.scrollbar.getViewport().setViewPosition(new Point(0, 0));
    }
  }
  
  @Override
  public void title(String s) {
    this.setTitle(s);
  }
  
  @Override
  public void setWindowColor(Color color) {
    this.displayPanel.setBackground(color);
  }
  
  @Override
  public void promptUser(String... s) {
    StringBuilder messageActual = new StringBuilder();
    for (String str : s) {
      messageActual.append(str + "\n");
    }
    JOptionPane.showMessageDialog(this, messageActual.toString());
  }
  
  @Override
  public void repaintDisplay() {
    this.displayPanel.repaint();
  }
  
  @Override
  public int getInputs(String s) {
    switch (s) {
      case "v":
        String vol = JOptionPane.showInputDialog("Input Volume:");
        int valueVolume = Integer.valueOf(vol);
        return valueVolume;
      case "i":
        String inst = JOptionPane.showInputDialog("Input Instrument:");
        int valueInstrument = Integer.valueOf(inst);
        return valueInstrument;
      case "t":
        String tempo = JOptionPane.showInputDialog("Input Tempo:");
        int valueTempo = Integer.valueOf(tempo);
        return valueTempo;
      default: // NO OTHER OPTION
        throw new IllegalArgumentException();
    }
  }
  
  /**
   * This method makes sure that the length of the song is the always measured in 4 beats (measure).
   * @param songLength int
   * @return int
   */
  private int compositionLengthIncreaser(int songLength) {
    if (songLength % 4 != 0) {
      songLength += 1;
      offset += 1;
      compositionLengthIncreaser(songLength);
    }
    return songLength;
  }

  
  /**
   * Class representing the GUI View for the MusicEditor.
   */
  public final class ConcreteGuiViewPanel extends JPanel {
    
    private final ReadOnlyModel model;
    private final List<ITone> range;
    private final List<ITone> reverseRange;
    
    /**
     * Public constructor for the ConcreteGuiViewPanel class.
     * @param model represents the given ReadOnlyModel model.
     */
    ConcreteGuiViewPanel(ReadOnlyModel model) {
      this.model = model;
      // WE WANT TO SUBTRACT 1 FROM THE OFFSET (unless its 0).
      this.range = model.viewRange();
      this.reverseRange = model.viewRange();
      Collections.reverse(this.reverseRange);
    }
    
    @Override
    public void paintComponent(Graphics g) {
      g.setFont(new Font("Courier", Font.BOLD, 20));
      super.paintComponent(g);
      drawStarters(g);
      drawSustaining(g);
      drawGrid(g);
      drawLabels(g);
      drawLine(g, GuiViewFrame.this.timerCurrentBeat);
    }
  
    /**
     * Draws the labels for every 16th measure.
     * @param g Graphics
     */
    private void drawLabels(Graphics g) {
      for (int i = 0; i < GuiViewFrame.this.duration; i += 16) {
        g.drawString(String.valueOf(i), i * 50 + 70, 25);
      }
    }
  
    /**
     * Higher tones will have a higher index, but should have a lower multiplier (higher up view).
     * @param t represents the given tone.
     * @return multiplier utilized for adjusting placement of given tone in relation to range.
     */
    private int toneMultiplier(ITone t) {
      return this.range.size() - this.range.indexOf(t);
    }
    
    /**
     * Draws a row of measures with consideration of the song's duration.
     * @param g the Graphics object to protect.
     * @param y the y coordinate.
     */
    public void drawRow(Graphics g, int y) {
      g.drawRect(75, y, (GuiViewFrame.this.duration + GuiViewFrame.this.offset) * 25, 25);
    }
    
    /**
     * Draws a grid of rows with consideration of the song's range of notes.
     * @param g the Graphics object to protect.
     */
    public void drawGrid(Graphics g) {
      g.setColor(Color.white);
      ITone prior = this.range.get(1);
      int startY = 45;
      for (int i = 0; i < GuiViewFrame.this.measures; i++) {
        g.drawRect(75 + 100 * i, 25, 100, 25 + (this.range.size() - 1) * 25);
      }
      for (ITone t : reverseRange) {
        g.drawString(t.toString(), 10, startY);
        startY += 25;
      }
      for (ITone t : range) {
        drawRow(g, toneMultiplier(t) * 25);
        if (((t.getNote() / 12) != (prior.getNote() / 12)) && (range.indexOf(t) != 0)) {
          g.fillRect(75, toneMultiplier(t) * 25, ((duration + offset) * 25), 3);
        }
        prior = t;
      }
    }
    
    /**
     * Draws a square that represents a start note.
     * @param g the Graphics object to protect.
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public void drawStartRep(Graphics g, int x, int y) {
      g.setColor(Color.black);
      g.fillRect(x, y, 25, 25);
      g.setColor(Color.lightGray);
      g.drawRect(x, y, 25, 25);
    }
    
    /**
     * Draws a start note given the tone.
     * @param g the Graphics object to protect.
     * @param t the ITone.
     */
    public void drawStartNote(Graphics g, ITone t) {
      int startX = 75;
      drawStartRep(g, startX + (t.getStartBeat() * 25), toneMultiplier(t) * 25);
    }
    
    /**
     * Draws sustaining note(s) given the tone.
     * @param g the Graphics object to protect.
     * @param t the ITone.
     */
    public void drawSustainNote(Graphics g, ITone t) {
      int startX = 100 + (t.getStartBeat() * 25);
      int dur = t.getDuration() - 2;
      g.fillRect(startX, toneMultiplier(t) * 25, dur * 25, 25);
    }
    
    /**
     * Draws the start notes throughout the song.
     * @param g the Graphics object to protect.
     */
    public void drawStarters(Graphics g) {
      List<ITone> notes = this.model.viewTones();
      for (ITone t : notes) {
        drawStartNote(g, t);
      }
    }
    
    /**
     * Draws the sustaining notes throughout the song.
     * @param g the Graphics object to protect.
     */
    public void drawSustaining(Graphics g) {
      List<ITone> notes = this.model.viewTones();
      for (ITone t : notes) {
        drawSustainNote(g, t);
      }
    }
    
    /**
     * Draws the vertical line which indicates the current beat of the song.
     * @param g the Graphics object to protect.
     * @param beat the integer representing the current beat being played.
     */
    public void drawLine(Graphics g, int beat) {
      int xOffset = 75;
      int x = (beat * 25) + xOffset;
      int rangeCount = this.range.size() + 1;
      g.setColor(Color.GREEN);
      g.drawLine(x, 25, x, rangeCount * 25);
      g.drawLine(x + 1, 25, x + 1, rangeCount * 25);
    }
  }
}
