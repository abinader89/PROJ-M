package cs3500.music.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import cs3500.music.util.CompositionBuilder;

/**
 * Created by Abinader on 10/17/16.
 * <h4>Represents the Music Model.</h4>
 * <p>This class holds the data for a composition that the views can render and the controller
 * can perform operatations on as specified by the user input.</p>
 */
public final class ConcreteMusicModel implements IMusicModel<ITone> {
  private final TreeMap<Integer, List<ITone>> composition;
  private int tempo;
  
  /**
   * Constructor for the MusicModel.
   */
  public ConcreteMusicModel() {
    this.composition = new TreeMap<Integer, List<ITone>>();
  }
  
  @Override
  public List<ITone> viewTones() {
    return this.allTones();
  }
  
  @Override
  public List<ITone> viewRange() {
    return this.getRange();
  }
  
  @Override
  public int viewCompositionLength() {
    return this.compositionLength();
  }
  
  @Override
  public int viewTempo() {
    return this.getTempo();
  }
  
  @Override
  public List<ITone> viewTonesAtStartBeat(int beat) {
    return this.getTonesAtStartBeat(beat);
  }
  
  @Override
  public List<ITone> viewTonesThatSustatin(int beat) {
    return this.getTonesThatSustain(beat);
  }
  
  private enum Position {
    FIRST, LAST
  }
  
  /**
   * This is the Builder inner class for the MusicModel.
   */
  public static final class Builder implements CompositionBuilder<IMusicModel> {
    private IMusicModel model;
    
    public Builder() {
      this.model = new ConcreteMusicModel();
    }
    
    @Override
    public IMusicModel build() {
      return model;
    }
    
    @Override
    public CompositionBuilder<IMusicModel> setTempo(int tempo) {
      model.setTempo(tempo);
      return this;
    }
    
    @Override
    public CompositionBuilder<IMusicModel> addNote(int start, int end, int instrument,
                                                   int pitch, int volume) {
      model.add(new Tone(pitch, start, end - start + 1, volume, instrument));
      return this;
    }
  }
  
  @Override
  public void edit(ITone toneToEdit, int sound, int startBeat, int duration, int volume,
                   int instrument) {
    Tone addMe = null;
    try {
      addMe = new Tone(sound, startBeat, duration, volume, instrument);
    } catch (IllegalArgumentException e) {
      // HANDLE
      return;
    }
    this.remove(toneToEdit);
    this.add(addMe);
  }
  
  @Override
  public void add(ITone tone) {
    Objects.requireNonNull(tone);
    if (composition.containsKey(tone.getStartBeat())) {
      composition.get(tone.getStartBeat()).add(tone);
    } else {
      List<ITone> newList = new ArrayList<ITone>();
      newList.add(tone);
      composition.put(tone.getStartBeat(), newList);
    }
  }
  
  @Override
  public void remove(ITone toneToBeEdited) {
    Objects.requireNonNull(toneToBeEdited);
    if (composition.containsKey(toneToBeEdited.getStartBeat())
            && composition.get(toneToBeEdited.getStartBeat()).contains(toneToBeEdited)) {
      if ((composition.get(toneToBeEdited.getStartBeat()).size()) > 1) {
        composition.get(toneToBeEdited.getStartBeat()).remove(toneToBeEdited);
      } else {
        composition.remove(toneToBeEdited.getStartBeat());
      }
    } else {
      throw new IllegalArgumentException("The given tone is not in the model!");
    }
  }
  
  @Override
  public void combineSimul(IMusicModel comp) {
    Objects.requireNonNull(comp);
    List<Tone> compTones = comp.allTones();
    if (compTones.size() > 1) {
      for (int i = 0; i < compTones.size(); i++) {
        add(compTones.get(i));
      }
    }
  }
  
  @Override
  public void combineConsec(IMusicModel comp) {
    Objects.requireNonNull(comp);
    List<Tone> compTones = comp.allTones();
    if (compTones.size() > 1) {
      Tone intermediary;
      int compositionLength = compositionLength();
      for (int i = 0; i < compTones.size(); i++) {
        if (compTones.get(i).getStartBeat() == 0) {
          intermediary = new Tone(compTones.get(i).getNote(), 1 + compositionLength,
                  compTones.get(i).getDuration(), compTones.get(i).getVolume(),
                  compTones.get(i).getInstrument());
          add(intermediary);
        } else {
          intermediary = new Tone(compTones.get(i).getNote(), compTones.get(i).getStartBeat()
                  + compositionLength, compTones.get(i).getDuration(), compTones.get(i)
                  .getVolume(), compTones.get(i).getInstrument());
          add(intermediary);
        }
      }
    }
  }
  
  @Override
  public String toString() {
    if (composition.size() == 0) {
      return "";
    } else {
      List<ITone> range = this.getRange();
      return pad(0) + " " + printNoteLabel(range) + "\n" + printTones(range);
    }
  }
  
  /**
   * Prints out the tones for use with the toString method.
   * @param range List[Tone]
   * @return String
   */
  private String printTones(List<ITone> range) {
    StringBuilder sb = new StringBuilder();
    List<ITone> tones = allTones();
    for (int i = 0; i < compositionLength(); i++) {
      sb.append(Integer.toString(i) + pad(i) + prepRow(i, tones, range));
      
      if (i + 1 < compositionLength()) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
  
  /**
   * Preps a the given row to be initialized.
   * @param row int
   * @param tones List[Tone]
   * @param range List[Tone]
   */
  private String prepRow(int row, List<ITone> tones, List<ITone> range) {
    List<ITone> sustainTones = new ArrayList<ITone>();
    List<ITone> startBeats = new ArrayList<ITone>();
    for (int i = 0; i < tones.size(); i++) {
      if (tones.get(i).isSustain(row)) {
        sustainTones.add(tones.get(i));
      }
      if (tones.get(i).isStartBeat(row)) {
        startBeats.add(tones.get(i));
      }
    }
    return printRow(sustainTones, startBeats, range);
  }
  
  /**
   * Prints a textual representation of the row.
   * @param sustainTones List[Tone]
   * @param startBeats List[Tone]
   * @param range List[Tone]
   * @return String
   */
  private String printRow(List<ITone> sustainTones, List<ITone> startBeats, List<ITone> range) {
    StringBuilder builder = new StringBuilder();
    for (int j = 0; j < range.size(); j++) {
      if (startBeats.contains(range.get(j))) {
        builder.append(String.format("%4s", "X"));
      } else {
        if (sustainTones.contains(range.get(j))) {
          builder.append(String.format("%4s", "|"));
        } else {
          builder.append(String.format("%4s", " "));
        }
      }
    }
    return builder.toString();
  }
  
  /**
   * Prints out the note label for use with the toString method.
   * @param range List[Tone]
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
   * Pads the Note label relative to the length of composition in beats and the current beat
   * being printed.
   * @return String
   */
  private String pad(int row) {
    String pad = "";
    for (int i = 0; i < (int)Math.log10(compositionLength()); i++) {
      pad += " ";
    }
    if (row >= 10) {
      return pad.substring((int)Math.log10(row));
    } else {
      return pad;
    }
  }
  
  @Override
  public List<ITone> getRange() {
    List<ITone> noteList = new ArrayList<ITone>();
    noteList.add(getLowest());
    for (int i = getLowest().getNote(); i < getHighest().getNote(); i++) {
      noteList.add(new Tone(i, 0, 1, 1, 1).next());
    }
    return noteList;
  }
  
  /**
   * Finds the highest sound in the composition.
   * @return Tone
   */
  private ITone getHighest() {
    return rangeSorter(Position.LAST);
  }
  
  /**
   * Finds the lowest sound in the composition.
   * @return Tone
   */
  private ITone getLowest() {
    return rangeSorter(Position.FIRST);
  }
  
  /**
   * Sorts all the tones in the composition for use with getLowest()/getHighest() methods.
   * @return Tone
   */
  private ITone rangeSorter(Position firstOrLast) {
    List<ITone> tones = allTones();
    tones.sort(null);
    switch (firstOrLast) {
      case FIRST:
        return tones.get(0);
      case LAST:
        return tones.get(tones.size() - 1);
      default: throw new IllegalArgumentException(); // DEAD CODE
    }
  }
  
  @Override
  public int compositionLength() {
    if (composition.size() > 0) {
      ITone acc = new Tone(0, 0, 1, 1, 0);
      ITone outputValue = lastSound(acc, this.allTones());
      if (outputValue.getStartBeat() > 0) {
        return outputValue.getStartBeat() + outputValue.getDuration() - 1;
      } else {
        return outputValue.getStartBeat() + outputValue.getDuration();
      }
    } else {
      return 0;
    }
  }
  
  /**
   * Iterates through the composition and finds the last tone played.
   * @param acc Tone
   * @param list List[Tone]
   * @return Tone
   */
  private ITone lastSound(ITone acc, List<ITone> list) {
    for (int i = 0; i < list.size() - 1; i++) {
      ITone candidateAcc = list.get(i + 1);
      if (candidateAcc.getStartBeat() + candidateAcc.getDuration()
              > acc.getStartBeat() + acc.getDuration()) {
        acc = candidateAcc;
      }
    }
    return acc;
  }
  
  @Override
  public List<ITone> allTones() {
    List<ITone> tones = new ArrayList<ITone>();
    if (composition.size() != 0) {
      int stoppingCondition = composition.lastKey() + 1;
      for (int i = 0; i < stoppingCondition; i++) {
        if (composition.containsKey(i)) {
          tones.addAll(composition.get(i));
        }
      }
    }
    return tones;
  }
  
  // ADDED THE THREE METHODS BELOW THIS LINE ON 10/23/2016.
  @Override
  public List<ITone> getTonesAtStartBeat(int beat) {
    if (composition.containsKey(beat)) {
      return composition.get(beat);
    }
    return null;
  }
  
  @Override
  public List<ITone> getTonesThatSustain(int beat) {
    Set<ITone> sustainTones = new HashSet<ITone>();
    List<ITone> returnValue = new ArrayList<ITone>();
    for (int i = 0; i < beat; i++) {
      sustainTones.addAll(addTonesToSustainList(sustainTones, i, beat));
    }
    returnValue.addAll(sustainTones);
    return returnValue;
  }
  
  /**
   * Checks if the Tones that start at the current beat sustain through the target beat.
   * @param sustainTones Set[Tone]
   * @param curBeat int
   * @param targetBeat int
   * @return Set[Tone]
   */
  private Set<ITone> addTonesToSustainList(Set<ITone> sustainTones, int curBeat, int
          targetBeat) {
    if (composition.containsKey(curBeat)) {
      List<ITone> candidateTones = composition.get(curBeat);
      for (ITone t : candidateTones) {
        if (t.isSustain(targetBeat)) {
          sustainTones.add(t);
        }
      }
    }
    return sustainTones;
  }
  
  @Override
  public ITone viewTone(ITone tone) {
    return this.getTone(tone);
  }
  
  @Override
  public ITone getTone(ITone tone) {
    List<ITone> tempList = this.getTonesAtStartBeat(tone.getStartBeat());
    for (ITone t : tempList) {
      if (tone.getNote() == t.getNote()) {
        return t;
      }
    }
    throw new IllegalArgumentException("Tone not found!");
  }
  
  
  @Override
  public void setTempo(int tempo) {
    this.tempo = tempo;
  }
  
  @Override
  public int getTempo() {
    return this.tempo;
  }
}
