package cs3500.music.provider;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * A View that is a GUI.
 * </p>
 * The GUIMusicView is a JFrame that relies heavily on the SongRenderPane to do the dirty work of
 * displaying the Song in a user friendly way. The GUIMusicView itself is the top-most parent
 * Component in the design. A GUIMusicView has a BorderLayout that contains a JTabbedPane at its
 * center.
 * </p>
 * In each call to render, the GUI checks to see if each song is part of the current tabbed pane. If
 * it is, the SongRenderPane is updated to display the newest version of the song. Otherwise, a new
 * SongRenderPane is created using the new Song.
 */
public class GUIMusicView extends JFrame implements IGUIMusicView {

  public static final long SLEEP_TIME = 100; // milliseconds.

  private final JTabbedPane songTabs;
  private final NoteMakerPane noteMaker;
  protected Consumer<ISong> addSong;
  protected Consumer<ISong> removeSong;
  protected Supplier<Long> getTime;

  private JButton playButton;
  private JButton pauseButton;
  private JButton stopButton;

  private volatile boolean isPlaying;

  private final KeyboardHandler keyHandler;
  private int selectedInstrument;
  private int selectedVolume;

  /**
   * Construct a new GUIMusicView.
   */
  public GUIMusicView() {
    super("Music Editor");
    setSize(600, 800);
    this.setLayout(new BorderLayout());

    this.selectedInstrument = 1; // Default is piano, but doesn't matter
    this.selectedVolume = 100; // Default is 100, but doesn't matter

    // Initialize Song Tabs.
    this.songTabs = new JTabbedPane();

    // Set up error throwing adder, remover and time getter.
    this.addSong = (ISong s) -> {
      throw new IllegalStateException("No song adder set.");
    };

    this.removeSong = (ISong s) -> {
      throw new IllegalStateException("No song remover set.");
    };

    this.getTime = () -> {
      throw new IllegalStateException("No time getter set.");
    };


    this.noteMaker = new NoteMakerPane();
    this.noteMaker.setActionListener((ActionEvent e) -> {
      ISong song = this.getSelectedSong();
      try {
        song.addNote(this.noteMaker.getNote());
      } catch (IllegalArgumentException exp) {
        this.showMessage("Invalid note input...");
      }
      GUIMusicView.this.addSong.accept(song);
    });

    this.add(this.noteMaker, BorderLayout.NORTH);

    // Add the tabbed pane to the center of the Border Layout
    this.add(songTabs, BorderLayout.CENTER);

    // Setup control panel.
    JPanel control = new JPanel();
    this.playButton = new JButton("Play");
    this.pauseButton = new JButton("Pause");
    this.stopButton = new JButton("Stop");

    control.setLayout(new FlowLayout(FlowLayout.CENTER));
    control.add(this.playButton);
    control.add(this.pauseButton);
    control.add(this.stopButton);

    this.add(control, BorderLayout.SOUTH);
    // end of control panel setup.


    // Add keyboard handler
    this.keyHandler = new KeyboardHandler();

    this.keyHandler.registerKeyPressedHandler(KeyEvent.VK_BACK_SPACE,
        () -> {
        for (INote n : this.getSelectedSongPane().getSelectedNotes()) {
          this.getSelectedSong().removeNote(n);
        }
        this.addSong.accept(this.getSelectedSong());
        this.getSelectedSongPane().clearSelectedNotes();
      });

    this.keyHandler.registerKeyPressedHandler(KeyEvent.VK_LEFT,
        () -> {
        JScrollPane selected = (JScrollPane) this.songTabs.getSelectedComponent();
        int cVal = selected.getHorizontalScrollBar().getValue();
        int scrollAmt = SongRenderPane.NOTE_SIZE * this.getSelectedSong().getBPM();
        selected.getHorizontalScrollBar().setValue(cVal - scrollAmt);

      });
    this.keyHandler.registerKeyPressedHandler(KeyEvent.VK_RIGHT,
        () -> {
        JScrollPane selected = (JScrollPane) this.songTabs.getSelectedComponent();
        int cVal = selected.getHorizontalScrollBar().getValue();
        int scrollAmt = SongRenderPane.NOTE_SIZE * this.getSelectedSong().getBPM();
        selected.getHorizontalScrollBar().setValue(cVal + scrollAmt);

      });
    this.keyHandler.registerKeyPressedHandler(KeyEvent.VK_UP,
        () -> {
        JScrollPane selected = (JScrollPane) this.songTabs.getSelectedComponent();
        int cVal = selected.getHorizontalScrollBar().getValue();
        selected.getVerticalScrollBar().setValue(cVal - SongRenderPane.NOTE_SIZE);

      });
    this.keyHandler.registerKeyPressedHandler(KeyEvent.VK_DOWN,
        () -> {
        JScrollPane selected = (JScrollPane) this.songTabs.getSelectedComponent();
        int cVal = selected.getHorizontalScrollBar().getValue();
        selected.getVerticalScrollBar().setValue(cVal + SongRenderPane.NOTE_SIZE);

      });

    this.keyHandler.registerKeyPressedHandler(KeyEvent.VK_HOME,
        () -> {
        JScrollPane selected = (JScrollPane) this.songTabs.getSelectedComponent();
        int min = selected.getHorizontalScrollBar().getMinimum();
        selected.getHorizontalScrollBar().setValue(min);

      });

    this.keyHandler.registerKeyPressedHandler(KeyEvent.VK_END,
        () -> {
        JScrollPane selected = (JScrollPane) this.songTabs.getSelectedComponent();
        int max = selected.getHorizontalScrollBar().getMaximum();
        selected.getHorizontalScrollBar().setValue(max);

      });

    this.addKeyListener(this.keyHandler);
    this.songTabs.addKeyListener(this.keyHandler);
    this.noteMaker.addKeyListener(this.keyHandler);
    control.addKeyListener(this.keyHandler);

    this.isPlaying = false;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  /**
   * Get the selected song pane.
   * @return the selected song pane.
   */
  private SongRenderPane getSelectedSongPane() {
    return (SongRenderPane)
            (((JScrollPane) this.songTabs.getSelectedComponent()).getViewport().getView());
  }


  @Override
  public void render(List<ISong> songList) {
    ArrayList<String> newSongTitles = new ArrayList<>();

    for (ISong song : songList) {
      // Keep track of the names of the songs in the model.
      newSongTitles.add(song.getName());
      int tabIndex = this.songTabs.indexOfTab(song.getName());

      if (tabIndex == -1) {

        SongRenderPane songPane = new SongRenderPane(song, this.noteMaker::setNoteValues);
        songPane.addKeyListener(this.keyHandler);
        songPane.addKeyListener(this.keyHandler);
        JScrollPane newSongPane = new JScrollPane(
                songPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        int scrollAmt = SongRenderPane.NOTE_SIZE * song.getBPM();
        newSongPane.getHorizontalScrollBar().setUnitIncrement(scrollAmt);
        newSongPane.getVerticalScrollBar().setUnitIncrement(SongRenderPane.NOTE_SIZE);
        newSongPane.addKeyListener(this.keyHandler);

        this.songTabs.addTab(song.getName(), newSongPane);
      } else {
        JScrollPane oldPane = (JScrollPane) this.songTabs.getComponentAt(tabIndex);
        SongRenderPane oldSong = (SongRenderPane) oldPane.getViewport().getView();
        oldSong.setSong(song);
      }
    }
    // Last step is to clean up any song that has a tab that shouldn't exist anymore.

    for (int ii = this.songTabs.getTabCount() - 1; ii >= 0; ii--) {
      String tabTitle = this.songTabs.getTitleAt(ii);
      if (!newSongTitles.contains(tabTitle) && !tabTitle.equals("*")) {
        // delete songs at a given tab if it is no longer in the song list.
        this.songTabs.removeTabAt(ii);
      }
    }
    // Validate the changes to the Tabbed Pane.
    this.songTabs.validate();
    // Force a repaint.
    this.repaint();
  }

  @Override
  public void play() {
    this.isPlaying = true;
    Thread renderTracker = new Thread(() -> {
      while (this.isPlaying) {
        this.getSelectedSongPane().updateTracker(this.getTime.get());
        try {
          Thread.sleep(GUIMusicView.SLEEP_TIME);
          SwingUtilities.invokeLater(() -> {
            this.getSelectedSongPane().repaint();
          });
          int offset = this.getSelectedSongPane().getTrackerOffset();
          // Adjust the scrollpane if the offset is not in frame.
          JScrollPane selected = (JScrollPane) this.songTabs.getSelectedComponent();
          Rectangle bounds = selected.getViewport().getViewRect();
          if (!bounds.contains(offset, bounds.getY())) {
            selected.getHorizontalScrollBar().setValue(offset);
          }

        } catch (InterruptedException ie) {
          throw new IllegalStateException("Tracker thread should not be interrupted.");
        }
      }
    });
    renderTracker.start();
  }

  @Override
  public void pause() {
    this.isPlaying = false;
  }

  @Override
  public void stop() {
    this.isPlaying = false;
    this.getSelectedSongPane().resetTracker();
  }

  @Override
  public void cleanUp() {
    this.isPlaying = false; // Just to be safe.
  }

  @Override
  public ISong getSelectedSong() {
    return ((SongRenderPane)
            ((JScrollPane) this.songTabs.getSelectedComponent()).getViewport().getView()).getSong();
  }

  public boolean isPlaying() {
    return this.isPlaying;
  }

  @Override
  public void setSongAdder(Consumer<ISong> howToAddSong) {
    this.addSong = howToAddSong;
  }

  @Override
  public void setSongRemover(Consumer<ISong> howToRemoveSong) {
    this.removeSong = howToRemoveSong;
  }

  @Override
  public void setOnPlayAction(ActionListener action) {
    this.playButton.addActionListener(action);
  }

  @Override
  public void setOnPauseAction(ActionListener action) {
    this.pauseButton.addActionListener(action);
  }

  @Override
  public void setOnStopAction(ActionListener action) {
    this.stopButton.addActionListener(action);
  }

  @Override
  public void setTimeGetter(Supplier<Long> howToGetTime) {
    this.getTime = howToGetTime;
  }

  @Override
  public void assignKeyTyped(char keyCode, Runnable onKeyTyped) {
    this.keyHandler.registerKeyTypedHandler(keyCode, onKeyTyped);
  }

  @Override
  public void assignKeyPressed(int keyCode, Runnable onKeyPressed) {
    this.keyHandler.registerKeyPressedHandler(keyCode, onKeyPressed);
  }

  @Override
  public void assignKeyReleased(int keyCode, Runnable onKeyReleased) {
    this.keyHandler.registerKeyReleasedHandler(keyCode, onKeyReleased);
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }
}
