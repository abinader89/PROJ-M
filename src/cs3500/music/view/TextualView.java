package cs3500.music.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.music.model.ReadOnlyModel;
import cs3500.music.model.ITone;

/**
 * Created by Abinader on 11/1/16.
 * Represents a text view for the MusicEditor.
 */
public final class TextualView implements View {
  private ReadOnlyModel model;
  private List<ITone> range;
  private List<ITone> tones;
  private final Appendable ap;
  
  /**
   * Constructor for TextualView.
   *
   * @param ap Appendable
   */
  public TextualView(Appendable ap) {
    this.ap = ap;
  }
  
  @Override
  public void initialize(ReadOnlyModel model) {
    this.model = model;
    this.range = this.model.viewRange();
    this.tones = this.model.viewTones();
    try {
      this.ap.append(pad(0) + " " + this.printNoteLabel(this.range)
              + "\n" + this.printITones(this.range));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void increaseCurrentBeat() {
    // EMPTY BECAUSE THIS VIEW DOES NOT REQUIRE A CONCEPT OF TIME
  }
  
  @Override
  public void resetBeat() {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Pads the Note label relative to the length of composition in beats and the current beat
   * being printed.
   *
   * @return String
   */
  private String pad(int row) {
    String pad = "";
    for (int i = 0; i < (int) Math.log10(this.model.viewCompositionLength()); i++) {
      pad += " ";
    }
    if (row >= 10) {
      return pad.substring((int) Math.log10(row));
    } else {
      return pad;
    }
  }
  
  /**
   * Prints out the note label for use with the toString method.
   *
   * @param range List[ITone]
   * @return String
   */
  private String printNoteLabel(List<ITone> range) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < range.size(); i++) {
      builder.append(String.format("%4s", range.get(i).toString()));
    }
    return builder.toString();
  }
  
  /**
   * Prints out the tones for use with the toString method.
   *
   * @param range List[ITone]
   * @return String
   */
  private String printITones(List<ITone> range) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.model.viewCompositionLength(); i++) {
      sb.append(Integer.toString(i) + pad(i) + prepRow(i, tones, range));
      
      if (i + 1 < this.model.viewCompositionLength()) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
  
  /**
   * Preps a the given row to be initialized.
   *
   * @param row   int
   * @param tones List[ITone]
   * @param range List[ITone]
   */
  private String prepRow(int row, List<ITone> tones, List<ITone> range) {
    List<ITone> sustainITones = new ArrayList<ITone>();
    List<ITone> startBeats = new ArrayList<ITone>();
    for (int i = 0; i < tones.size(); i++) {
      if (this.tones.get(i).isSustain(row)) {
        sustainITones.add(this.tones.get(i));
      }
      if (this.tones.get(i).isStartBeat(row)) {
        startBeats.add(this.tones.get(i));
      }
    }
    return this.printRow(sustainITones, startBeats, range);
  }
  
  /**
   * Prints a textual representation of the row.
   * @param sustainITones List[ITone]
   * @param startBeats List[ITone]
   * @param range List[ITone]
   * @return String
   */
  private String printRow(List<ITone> sustainITones, List<ITone> startBeats, List<ITone> range) {
    StringBuilder builder = new StringBuilder();
    for (int j = 0; j < range.size(); j++) {
      if (startBeats.contains(range.get(j))) {
        builder.append(String.format("%4s", "X"));
      } else {
        if (sustainITones.contains(range.get(j))) {
          builder.append(String.format("%4s", "|"));
        } else {
          builder.append(String.format("%4s", " "));
        }
      }
    }
    return builder.toString();
  }
}
