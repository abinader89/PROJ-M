package cs3500.music.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Renders the text-based view of the model.
 * TextMusicView only supports rendering the Song at index O of the given songList.
 */
public class TextMusicView implements IMusicView {
  private final Appendable ap;

  /**
   * Constructs a TextMusicView.
   *
   * @param ap Appendable so the TextMusicView can be rendered in the console.
   */
  public TextMusicView(Appendable ap) {
    if (ap == null) {
      throw new IllegalStateException("Appendable cannot be null.");
    }

    this.ap = ap;
  }

  @Override
  public void render(List<ISong> songList) {
    StringBuilder console = new StringBuilder();
    ISong toPrint = songList.get(0);

    console.append(renderHeader(toPrint));
    console.append(renderMinusHeader(toPrint));

    try {
      this.ap.append(console.toString());
    } catch (IOException e) {
      System.err.println("Caught IOException: " + e.getMessage());
      System.exit(4);
    }
  }

  @Override
  public void play() {
    throw new IllegalArgumentException("This method is unsupported by TextMusicView.");
  }

  @Override
  public void pause() {
    throw new IllegalArgumentException("This method is unsupported by TextMusicView.");
  }

  @Override
  public void stop() {
    throw new IllegalArgumentException("This method is unsupported by TextMusicView.");
  }

  @Override
  public void cleanUp() {
    throw new IllegalArgumentException("This method is unsupported by TextMusicView.");
  }

  @Override
  public void setSongAdder(Consumer<ISong> howToAddSong) {
    // TextView does not do anything for this method.
  }

  @Override
  public void setSongRemover(Consumer<ISong> howToRemoveSong) {
    // TextView does not do anything for this method.
  }

  @Override
  public void showMessage(String message) {
    System.out.print(message);
  }

  /**
   * Helper for render method.
   * </p>
   * Adds leading spaces before render's header to offset for the ultimate size of the
   * beat-numbered column.
   *
   * @return String of space characters to add to render's header
   */
  // NOTE: This method is a helper method and is therefore tested by the methods that implement it.
  // The passing of such tests indicates the functioning of this helper method.
  private String renderHeader(ISong song) {
    String console = "";
    console = console + headerLeadingSpaces(song);

    // Numerical representation of lowest and highest notes
    int lowestNoteRank = song.getLowHigh().getFirst().getTotalRank();
    int highestNoteRank = song.getLowHigh().getSecond().getTotalRank();


    for (int i = lowestNoteRank; i <= highestNoteRank; i++) {
      console = console + noteStringWithSpaces(this.noteStringAt(i));
    }
    return console + "\n";
  }

  /**
   * Helper for renderHeader method.
   * </p>
   * Adds leading spaces before render's header to offset for the ultimate size of the
   * beat-numbered column.
   *
   * @return String of space characters to add to render's header
   */
  // NOTE: This method is a helper method and is therefore tested by the methods that implement it.
  // The passing of such tests indicates the functioning of this helper method.
  private String headerLeadingSpaces(ISong song) {
    String spaces = "";
    int currentNumSpaces = 0;
    int columnWidth = String.valueOf(song.getLength()).length(); // based on beatNum w/most digits

    while (currentNumSpaces < columnWidth) {
      spaces = spaces + " ";
      currentNumSpaces++;
    }
    return spaces;
  }

  /**
   * Helper for render method.
   * </p>
   * Utilizes noteStringAt method to print note string and adds spaces necessary for header-printing
   * in render method.
   *
   * @param noteString String name of Note Pitch and octave
   * @return String name of Note (Pitch and octave) cushioned with spaces to make it five characters
   */
  // NOTE: This method is a helper method and is therefore tested by the methods that implement it.
  // The passing of such tests indicates the functioning of this helper method.
  private String noteStringWithSpaces(String noteString) {
    if (noteString.length() < 2) {
      throw new IllegalArgumentException("Hmm. No case in which a Note name and " +
              "octave as a string should be less than 2 characters.");
    } else if (noteString.length() == 2) {
      return "  " + noteString + " ";
    } else if (noteString.length() == 3) {
      return " " + noteString + " ";
    } else if (noteString.length() == 4) {
      return "  " + noteString;
    } else {
      return " " + noteString + " ";
    }
  }

  /**
   * Helper for noteStringWithSpaces method.
   * </p>
   * Gets the String representation of a Note based on an integer total rank.
   *
   * @param totalRank int representing octave plus intra-octave rank
   * @return String name of a Note corresponding to totalRank
   */
  // NOTE: This method is a helper method and is therefore tested by the methods that implement it.
  // The passing of such tests indicates the functioning of this helper method.
  private String noteStringAt(int totalRank) {
    int octave = this.atOctave(totalRank);
    int rank = totalRank - (octave * 12);
    Pitch pitch = this.pitchAt(rank);

    return pitch.toString() + octave;
  }

  /**
   * Helper for noteStringAt method.
   * </p>
   * Gets the octave based on an integer total rank. Only octaves within humans' audible range
   * are represented; in this model, they are numbered 0-9.
   *
   * @param totalRank int representing octave and intra-octave rank
   * @return int octave number
   * @throws IllegalArgumentException if given totalRank refers to octave out of audible range
   */
  // NOTE: This method is a helper method and is therefore tested by the methods that implement it.
  // The passing of such tests indicates the functioning of this helper method.
  private int atOctave(int totalRank) {
    if (totalRank < 0) {
      throw new IllegalArgumentException("Humans can't hear notes at that octave. " +
              "Are you sure it should exist in a song?");
    } else {
      return ((totalRank) / 12);
    }
  }

  /**
   * Helper for the noteRange method.
   * </p>
   * Gets the Pitch based on an integer intra-octave rank.
   *
   * @param rank int representing intra-octave rank
   * @return Pitch corresponding to intra-octave rank
   * @throws IllegalArgumentException if given rank does not correspond to an existing pitch
   */
  // NOTE: This method is a helper method and is therefore tested by the methods that implement it.
  // The passing of such tests indicates the functioning of this helper method.
  private Pitch pitchAt(int rank) {

    if (rank < 0 || rank > Pitch.values().length) {
      throw new IllegalArgumentException("No Pitch corresponds to that intra-octave rank.");
    }
    return Pitch.values()[rank];
  }

  /**
   * Helper method for render.
   * </p>
   * Renders the line numbers for the beats and the Xs and |s representing the song notes.
   *
   * @param song ISong being rendered.
   * @return String representation of a song without note header.
   */
  private String renderMinusHeader(ISong song) {
    StringBuilder console = new StringBuilder();
    int lowestNoteRank = song.getLowHigh().getFirst().getTotalRank();
    int highestNoteRank = song.getLowHigh().getSecond().getTotalRank();

    int index = 0;

    ArrayList<ArrayList<INote>> allBeats = new ArrayList<>();

    for (int b = 0; b < song.getLength(); b++) {
      allBeats.add(this.notesAtBeat(song, b));
    }


    for (int b = 0; b < song.getLength(); b++) {

      String[] slots = new String[highestNoteRank - lowestNoteRank + 1];
      Arrays.fill(slots, "     ");

      // Adds the beat number identifier for the column, making the column width as many characters
      // wide as the max digits in any beat number
      console.append(beatLeadingSpaces(song, b));
      console.append("" + (b + 1));

      // Within each beat, looks at Notes starting from lowest totalRank to highest totalRank
      // This will allow the Notes printed to correspond with the render header
      for (INote n : allBeats.get(b)) {
        if (n.getStart() == b) {
          slots[n.getTotalRank() - lowestNoteRank] = "  X  ";
        } else if (n.getDuration() > 1 &&
                (n.getStart() >= (b - n.getDuration() + 1))) {
          slots[n.getTotalRank() - lowestNoteRank] = "  |  ";
        } else {
          slots[n.getTotalRank() - lowestNoteRank] = "     ";
        }
      }

      StringBuilder line = new StringBuilder();
      for (String slot : slots) {
        line.append(slot);
      }
      console.append(line.toString() + "\n");
    }
    return console.toString();
  }

  /**
   * Helper for renderMinusHeader method.
   * </p>
   * Creates an ArrayList of Notes that are playing at the given beat.
   *
   * @param beat integer beat number
   * @return ArrayList of Notes
   * @throws IllegalArgumentException if this Song has no Notes
   */
  // NOTE: This method is a helper method and is therefore tested by the methods that implement it.
  // The passing of such tests indicates the functioning of this helper method.
  private ArrayList<INote> notesAtBeat(ISong song, int beat) {
    if (song.getNotes().isEmpty()) {
      throw new IllegalArgumentException("Song has no Notes.");
    } else {
      ArrayList<INote> noteList = new ArrayList<>();
      for (INote n : song.getNotes()) {
        if ((beat >= n.getStart()) && (beat <= n.getEnd())) {
          noteList.add(n);
        }
      }
      return noteList;
    }
  }

  /**
   * Helper for renderMinusHeader method.
   * </p>
   * If necessary, adds leading spaces before the numbered beat-number column to ensure all columns
   * are aligned regardless of the number of digits in the beat number.
   *
   * @param thisBeat integer representing current beat
   * @return String of space characters to add to each row of renderMinusHeader
   */
  // NOTE: This method is a helper method and is therefore tested by the methods that implement it.
  // The passing of such tests indicates the functioning of this helper method.
  private String beatLeadingSpaces(ISong song, int thisBeat) {
    String spaces = "";
    // "columnWidth" (below) is based on beat number with most digits
    int columnWidth = String.valueOf(song.getLength()).length();
    int thisBeatWidth = String.valueOf(thisBeat + 1).length();

    while (thisBeatWidth < columnWidth) {
      spaces = spaces + " ";
      thisBeatWidth++;
    }
    return spaces;
  }
}