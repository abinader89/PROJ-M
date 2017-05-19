package cs3500.music.provider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Render a Song Graphically.
 * </p>
 * The SongRenderPane is intended to be used to render a Single ISong. It can be used as is, or as
 * part of a larger project. The purpose of this class is to pull the actual drawing of the songs
 * into a single location so that the rest of the GUIMusicView can be focused on managing the Swing
 * components for user interaction and ergonomics.
 */
public class SongRenderPane extends JPanel {

  public static final int TRACKER_WIDTH = 5; // pxls.
  public static final int NOTE_SIZE = 20; //pxls.
  public static final Color HIT_COLOR = new Color(42, 74, 159);
  public static final Color HOLD_COLOR = new Color(0, 167, 157);
  public static final Color SELECTED_HIT_COLOR = new Color(102, 45, 145);
  public static final Color SELECTED_HOLD_COLOR = new Color(207, 179, 214);


  public static final Color LINE_COLOR = Color.black;

  private ISong song;
  private int trackerOffset;
  private final HashSet<INote> selectedNotes;

  protected int borderXOffset;
  protected int borderYOffset;

  private final MouseListener selectionListener;
  private final MouseListener additionListener;


  /**
   * Constructor for the Song Render Pane.
   *
   * @param song initial song to render.
   */
  public SongRenderPane(ISong song, Consumer<INote> updateNoteInput) {
    Objects.requireNonNull(song);
    if (song.getNotes().isEmpty()) {
      throw new IllegalArgumentException("Cannot render an empty song.");
    }
    this.song = song;
    this.borderXOffset = 0;
    this.borderYOffset = 0;

    this.setPreferredSize(this.calculateSongRenderSize());
    this.trackerOffset = borderXOffset;
    this.selectedNotes = new HashSet<>();
    this.setFocusable(true);

    // Create and add Selection Listener
    this.selectionListener = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        SongRenderPane.this.requestFocus();
        Optional<INote> maybeNote = SongRenderPane.this.noteAtPos(e.getX(), e.getY());
        if (maybeNote.isPresent()) {
          if (e.getButton() == MouseEvent.BUTTON1) {
            SongRenderPane.this.selectedNotes.add(maybeNote.get());
          } else {
            SongRenderPane.this.selectedNotes.remove(maybeNote.get());
          }
          SwingUtilities.invokeLater(() -> {
            SongRenderPane.this.repaint();
          });
        }
      }
    };

    this.additionListener = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        SongRenderPane.this.requestFocus();
        INote newNote = SongRenderPane.this.newNoteAtPos(e.getX(), e.getY());
        updateNoteInput.accept(newNote);
      }
    };

    // Use addition listener by default.
    this.addMouseListener(this.additionListener);
    this.addMouseListener(this.selectionListener);
  }

  /**
   * Try to get a note at a given mouse position in the RenderPane.
   *
   * @param x the x mouse position.
   * @param y the y mouse position.
   * @return Optional note at the mouse position.
   */
  protected Optional<INote> noteAtPos(int x, int y) {
    Pair<Pitch, Integer> pitchNOctave = this.calcPitchAndOctave(y);
    int beat = this.calcBeatFromX(x);

    for (INote n : this.song.getNotes()) {
      if (n.pitchPlease().equals(pitchNOctave.getFirst())
              && n.getOctave() == pitchNOctave.getSecond()
              && n.getStart() <= beat
              && n.getEnd() >= beat) {
        return Optional.of(n);
      }
    }
    return Optional.empty();
  }

  /**
   * Get the pitch and octave from a click y position.
   *
   * @param y the y position.
   * @return the pitch and octave as a pair.
   */
  private Pair<Pitch, Integer> calcPitchAndOctave(int y) {
    y -= this.borderYOffset + SongRenderPane.NOTE_SIZE;
    Pair<INote, INote> minMax = this.song.getLowHigh();
    Pitch cPitch = minMax.getSecond().pitchPlease();
    int octave = minMax.getSecond().getOctave();
    while (y > 0) {
      int next = (cPitch.getRank() - 1);
      if (next < 0) {
        next = 11;
        octave--;
      }
      cPitch = Pitch.values()[next];
      y -= SongRenderPane.NOTE_SIZE;
    }
    return new Pair<Pitch, Integer>(cPitch, octave);
  }

  /**
   * Get the beat from the mouse position.
   *
   * @param x the position.
   * @return the beat.
   */
  private int calcBeatFromX(int x) {
    x -= this.borderXOffset;
    return x / SongRenderPane.NOTE_SIZE;
  }

  /**
   * Create pseudo note for the input panel.
   *
   * @param x the x mouse location.
   * @param y the y mouse location.
   * @return the dummy note.
   */
  protected INote newNoteAtPos(int x, int y) {
    Pair<Pitch, Integer> p = this.calcPitchAndOctave(y);
    int start = this.calcBeatFromX(x);
    // Duration, Instrument, and Volume are dummy values.
    return new Note(p.getFirst(), p.getSecond(), start, 1, 1, 100);
  }

  /**
   * Reset the tracker position.
   */
  public void resetTracker() {
    this.updateTracker(this.borderXOffset);
  }

  public int getTrackerOffset() {
    return this.trackerOffset;
  }

  /**
   * Update the tracker location.
   *
   * @param millisecondOffset the new tracker location.
   */
  public void updateTracker(long millisecondOffset) {
    int nextOffset = this.borderXOffset
            + (int) ((1.0 * millisecondOffset) / this.song.getTempo()) * SongRenderPane.NOTE_SIZE;
    this.trackerOffset = nextOffset;
  }

  /**
   * Set the selected song.
   *
   * @param song the new song to use.
   */
  public void setSong(ISong song) {
    Objects.requireNonNull(song);
    if (song.getNotes().isEmpty()) {
      throw new IllegalArgumentException("Cannot render an empty song.");
    }
    this.song = song;
    this.setPreferredSize(this.calculateSongRenderSize());
  }

  /**
   * Get the current song that the render pane is trying to render.
   *
   * @return the current song.
   */
  public ISong getSong() {
    return this.song;
  }

  /**
   * Calculate the size of the Panel that would be needed to display the entire song.
   * While this dimension will often be too large to reasonable display in a single window,
   * it is useful for setting the preferred size of the window. In general, the SongRenderPane
   * is well suited to live inside of a JScrollPane.
   *
   * @return the Dimension of the Panel needed to display the entire song at once.
   */
  private Dimension calculateSongRenderSize() {
    Pair<INote, INote> firstLast = this.song.getFirstLast();
    Pair<INote, INote> lowHigh = this.song.getLowHigh();

    int totalBeats = firstLast.getSecond().getEnd();

    INote lowNote = lowHigh.getFirst();
    INote highNote = lowHigh.getSecond();

    int width = (firstLast.getSecond().getEnd()) * SongRenderPane.NOTE_SIZE;
    width += this.borderXOffset;

    // Extend the last measure.
    int measureExtend = firstLast.getSecond().getEnd() % this.song.getBPM();
    measureExtend *= SongRenderPane.NOTE_SIZE;
    width += measureExtend;

    int height = (highNote.pitchPlease().getRank() + highNote.getOctave() * 12)
            - (lowNote.pitchPlease().getRank() + lowNote.getOctave() * 12) + 1;
    height *= SongRenderPane.NOTE_SIZE;
    height += this.borderYOffset;

    return new Dimension(width, height);
  }

  /**
   * Render the border of this song.
   * </p>
   * The border consists of the labels for each of the pitches in the song, as well as the
   * beat count at the beginning of each measure.
   *
   * @param g2d the graphics object to draw to.
   */
  private void renderBorder(Graphics2D g2d) {
    this.borderXOffset = 50;
    this.borderYOffset = 50;

    int x = this.borderXOffset / 5; // Pixels from edge.
    int y = this.borderYOffset + (SongRenderPane.NOTE_SIZE / 2);

    Pair<INote, INote> lowHigh = this.song.getLowHigh();
    g2d.setFont(new Font("Garamond", Font.BOLD, 12));
    // Print all the
    for (INote n = lowHigh.getSecond(); n.compare(lowHigh.getFirst()) >= 0; ) {
      g2d.drawString(n.toString(), x, y);
      y += SongRenderPane.NOTE_SIZE;

      // Get next note.

      int nextPitchRank = n.pitchPlease().getRank() - 1; // rank counts from 1, and we want 1 less.
      if (nextPitchRank < 0) {
        nextPitchRank = 11;
      }


      Pitch nPitch = Pitch.values()[nextPitchRank];
      int nOctave = (nextPitchRank != 11) ? n.getOctave() : n.getOctave() - 1;

      n = new Note(nPitch,
              nOctave,
              n.getStart(),
              n.getDuration(),
              n.getInstrument(),
              n.getVolume());
    }

    x = this.borderXOffset + (SongRenderPane.NOTE_SIZE / 2);
    y = this.borderYOffset / 5 * 4;

    int bpm = this.song.getBPM();
    for (int i = 0; i < this.song.getFirstLast().getSecond().getEnd() + bpm; i += bpm) {
      g2d.drawString("" + i, x, y);
      x += SongRenderPane.NOTE_SIZE * bpm;
    }

  }

  public ArrayList<INote> getSelectedNotes() {
    return new ArrayList<>(this.selectedNotes);
  }

  public void clearSelectedNotes() {
    this.selectedNotes.clear();
  }

  private ArrayList<NoteRectangle> renderNotesHelper(Collection<INote> notes,
                                                     Color hit,
                                                     Color hold) {
    ArrayList<NoteRectangle> noteRecs = new ArrayList<>();

    INote highNote = this.song.getLowHigh().getSecond();
    int highNoteRank = highNote.pitchPlease().getRank() + (highNote.getOctave() * 12);

    // Generate the note rectangles.
    for (INote n : notes) {
      int x = (n.getStart() * SongRenderPane.NOTE_SIZE) + this.borderXOffset;
      int y = this.borderYOffset
              + ((highNoteRank - (n.pitchPlease().getRank() + (12 * n.getOctave())))
              * SongRenderPane.NOTE_SIZE);
      int durationWidth = (n.getDuration() - 1) * SongRenderPane.NOTE_SIZE;
      int durationStartX = x + SongRenderPane.NOTE_SIZE;

      noteRecs.add(new NoteRectangle(x,
              y,
              SongRenderPane.NOTE_SIZE,
              SongRenderPane.NOTE_SIZE,
              hit));

      if (durationWidth > 0) {
        noteRecs.add(new NoteRectangle(durationStartX,
                y,
                durationWidth,
                SongRenderPane.NOTE_SIZE,
                hold));
      }
    }
    return noteRecs;
  }


  /**
   * Render all of the note rectangles.
   * </p>
   * Each of the Notes is given a Hit box that is the size of one beat.
   * All subsequent beats in the notes duration are drawn as a separate rectangle which is
   * distinguished by its color.
   *
   * @param g2d the graphics object to render the notes to.
   */
  private void renderNotes(Graphics2D g2d) {
    ArrayList<NoteRectangle> notes = this.renderNotesHelper(this.song.getNotes(),
            SongRenderPane.HIT_COLOR,
            SongRenderPane.HOLD_COLOR);
    // Draw the note rectangles.
    for (NoteRectangle rec : notes) {
      g2d.setColor(rec.getColor());
      g2d.fill(rec);
    }
  }

  /**
   * Render all of the note rectangles.
   * </p>
   * Each of the Notes is given a Hit box that is the size of one beat.
   * All subsequent beats in the notes duration are drawn as a separate rectangle which is
   * distinguished by its color.
   *
   * @param g2d the graphics object to render the notes to.
   */
  private void renderSelectedNotes(Graphics2D g2d) {
    ArrayList<NoteRectangle> notes = this.renderNotesHelper(this.selectedNotes,
            SongRenderPane.SELECTED_HIT_COLOR,
            SongRenderPane.SELECTED_HOLD_COLOR);
    // Draw the note rectangles.
    for (NoteRectangle rec : notes) {
      g2d.setColor(rec.getColor());
      g2d.fill(rec);
    }
  }


  /**
   * Draw lines to the given graphic until either X or Y exceeds the input stopValue.
   *
   * @param g2d       the graphics object to draw to.
   * @param startX    the initial X coordinate for the first line.
   * @param startY    the intial Y coordinate for the first line.
   * @param endX      the last X coordinate in the first line.
   * @param endY      the last Y coordinate in the first line.
   * @param deltaX    the amount to increment the X coordinates by for the next line.
   * @param deltaY    the amount to increment the Y coordinates by for the next line.
   * @param stopValue the value to draw until.
   */
  private void drawLines(Graphics2D g2d,
                         int startX,
                         int startY,
                         int endX,
                         int endY,
                         int deltaX,
                         int deltaY,
                         int stopValue) {


    Color old = g2d.getColor();
    g2d.setColor(LINE_COLOR);

    while (startX <= stopValue && startY <= stopValue) {
      g2d.drawLine(startX, startY, endX, endY);
      startX += deltaX;
      startY += deltaY;
      endX += deltaX;
      endY += deltaY;
    }

    // Pop the old color back in :hair_flip:
    g2d.setColor(old);
  }

  /**
   * Draw the measure lines.
   * </p>
   * Measure lines are drawn in the view ever BPM number of beats.
   *
   * @param g2d the graphics object to draw the lines to.
   */
  private void renderMeasureLines(Graphics2D g2d) {
    int bpm = this.song.getBPM();
    Dimension size = this.calculateSongRenderSize();
    this.drawLines(g2d,
            this.borderXOffset,
            this.borderYOffset,
            this.borderXOffset,
            size.height,
            SongRenderPane.NOTE_SIZE * bpm,
            0,
            size.width);
  }

  /**
   * Render the lines to distinguish between each pitch.
   *
   * @param g2d the graphics object to draw to.
   */
  private void renderNoteLines(Graphics2D g2d) {
    Dimension size = this.calculateSongRenderSize();

    this.drawLines(g2d,
            this.borderXOffset,
            this.borderYOffset,
            size.width,
            this.borderYOffset,
            0,
            SongRenderPane.NOTE_SIZE,
            size.height);
  }

  /**
   * Render the tracking line if it is not at the beginnning of the song.
   *
   * @param g2d where to render the line.
   */
  private void renderTrackingLine(Graphics2D g2d) {
    Dimension size = this.calculateSongRenderSize();
    Color old = g2d.getColor();
    g2d.setColor(Color.red);

    if (this.trackerOffset > this.borderXOffset
            || this.trackerOffset > this.borderXOffset + size.width) {
      Rectangle2D rekt = new Rectangle2D.Double(this.trackerOffset,
              this.borderYOffset,
              SongRenderPane.TRACKER_WIDTH,
              size.getHeight());
      g2d.fill(rekt);
    }
    g2d.setColor(old);
  }

  /**
   * Render the song to the pane. </p> This is the primary render point of entry for the Graphical
   * View. There are 4 steps to rendering a song which have been broken out into subsequent helper
   * methods. First, we render the border. The border contains the names of each pitch, as well as
   * indicators for the number of beats at the start of each measure. Next, the notes are rendered.
   * The notes are basically just rectangles. For specifics about how the notes are rendered, see
   * renderNotes, and the NoteRectangle class. The note lines and measure lines are rendered last.
   * These lines are what give the model a definite shape. The choice to render them last was made
   * to avoid the note rectangles overwriting the lines.
   *
   * @param g2d the graphics object.
   */
  protected void renderSong(Graphics2D g2d) {
    this.renderBorder(g2d);
    this.renderNotes(g2d);
    this.renderSelectedNotes(g2d);
    this.renderNoteLines(g2d);
    this.renderMeasureLines(g2d);
    this.renderTrackingLine(g2d);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    this.renderSong(g2d);
  }

}
