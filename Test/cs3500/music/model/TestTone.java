package cs3500.music.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Abinader on 10/17/16.
 * Test suite for the Tone class.
 */
public class TestTone {
  Tone tone0 = new Tone(0, 2, 2, 60, 0);
  Tone tone60 = new Tone(60, 8, 4, 60, 0);
  Tone tone127A = new Tone(127, 6, 3, 4, 1);
  Tone tone127B = new Tone(127, 6, 2, 5, 2);
  
  private List<String> expectedToString() {
    List<String> notesArray = new ArrayList<String>();
    notesArray.add("C");
    notesArray.add("C#");
    notesArray.add("D");
    notesArray.add("D#");
    notesArray.add("E");
    notesArray.add("F");
    notesArray.add("F#");
    notesArray.add("G");
    notesArray.add("G#");
    notesArray.add("A");
    notesArray.add("A#");
    notesArray.add("B");
    List<String> outputValue = new ArrayList<String>();
    for (int j = 0; j < 12; j++) {
      if (j == 11) {
        for (int i = 0; i < 8; i++) {
          outputValue.add(notesArray.get(i) + Integer.toString(j));
        }
      }
      for (int i = 0; i < 12; i++) {
        outputValue.add(notesArray.get(i) + Integer.toString(j));
      }
    }
    return outputValue;
  }
  
  private List<Tone> noteList() {
    List<Tone> noteList = new ArrayList<Tone>();
    for (int i = 0; i < 128; i++) {
      noteList.add(new Tone(i, i + 1, i + 3, 60, 0));
    }
    return noteList;
  }
  
  @Test
  public void testToString() {
    List<String> expectedOutput = expectedToString();
    List<Tone> noteList = noteList();
    assertEquals("C0", tone0.toString());
    assertEquals("C5", tone60.toString());
    assertEquals("G10", tone127A.toString());
    for (int i = 0; i < 128; i++) {
      assertEquals(expectedOutput.get(i), noteList.get(i).toString());
    }
  }
  
  @Test
  public void testCompareTo() {
    assertEquals(60, tone60.compareTo(tone0));
    assertEquals(-60, tone0.compareTo(tone60));
    assertEquals(0, tone127A.compareTo(tone127B));
  }
  
  @Test
  public void testEquals() {
    assertEquals(true, tone127A.equals(tone127B));
    assertEquals(true, tone127B.equals(tone127A));
    assertEquals(true, tone127A.equals(tone127A));
    assertEquals(false, tone0.equals(tone60));
    assertEquals(false, tone0.equals("foobar"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void creatingToneWithDurationsLessThanZeroThrowsException() {
    new Tone(60, 0, -1, 60, 0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void creatingInvalidTonesThrowsException1() {
    new Tone(-1, 0, 1, 60 , 0);
  }
  
  
  @Test(expected = IllegalArgumentException.class)
  public void creatingInvalidTonesThrowsException2() {
    new Tone(128, 0, 1, 60, 0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void creatingToneWithNegativeStartingBeatsThrowsException() {
    new Tone(60, -1, 1, 60, 0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void invokingCompareToWithNullArgThrowsException() {
    tone0.compareTo(null);
  }
}
