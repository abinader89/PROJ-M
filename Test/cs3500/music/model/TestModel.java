package cs3500.music.model;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Abinader on 10/18/16.
 * Test suite for the Music model class.
 */
public class TestModel {
  
  String outputString1 =
      "   C0 C#0  D0 D#0  E0  F0 F#0  G0 G#0  A0 A#0  B0  C1 C#1  D1 D#1  E1\n" +
      "0   X                                                                \n" +
      "1                   X                                                \n" +
      "2                                   X                                \n" +
      "3                                   X                                \n" +
      "4                                   |                                \n" +
      "5                                   |                                \n" +
      "6                                                                    \n" +
      "7                                                                   X\n" +
      "8                                                                   |";
  String outputString2 =
      "     C0 C#0  D0 D#0  E0  F0 F#0  G0 G#0  A0 A#0  B0  C1 C#1  D1 D#1  E1\n" +
      "0     X                                                                \n" +
      "1                     X                                                \n" +
      "2                                     X                                \n" +
      "3                                     X                                \n" +
      "4                                     |                                \n" +
      "5                                     |                                \n" +
      "6                                                                      \n" +
      "7                                                                     X\n" +
      "8                                                         X           |\n" +
      "9     X                                               X   |            \n" +
      "10    |                                               |                \n" +
      "11                                                                     \n" +
      "12                                                                     \n" +
      "13                                                                     \n" +
      "14                                                                     \n" +
      "15                                                                     \n" +
      "16                                                                     \n" +
      "17                                                                     \n" +
      "18                                                                     \n" +
      "19                                                                     \n" +
      "20                                                                     \n" +
      "21                                                                     \n" +
      "22                                                                     \n" +
      "23                                                                     \n" +
      "24                                                                     \n" +
      "25                                                                     \n" +
      "26                                                                     \n" +
      "27                                                                     \n" +
      "28                                                                     \n" +
      "29                                                                     \n" +
      "30                                                                     \n" +
      "31                                                                     \n" +
      "32                                                                     \n" +
      "33                                                                     \n" +
      "34                                                                     \n" +
      "35                                                                     \n" +
      "36                                                                     \n" +
      "37                                                                     \n" +
      "38                                                                     \n" +
      "39                                                                     \n" +
      "40                                                                     \n" +
      "41                                                                     \n" +
      "42                                                                     \n" +
      "43                                                                     \n" +
      "44                                                                     \n" +
      "45                                                                     \n" +
      "46                                                                     \n" +
      "47                                                                     \n" +
      "48                                                                     \n" +
      "49                                                                     \n" +
      "50                                                                     \n" +
      "51                                                                     \n" +
      "52                                                                     \n" +
      "53                                                                     \n" +
      "54                                                                     \n" +
      "55                                                                     \n" +
      "56                                                                     \n" +
      "57                                                                     \n" +
      "58                                                                     \n" +
      "59                                                                     \n" +
      "60                                                                     \n" +
      "61                                                                     \n" +
      "62                                                                     \n" +
      "63                                                                     \n" +
      "64                                                                     \n" +
      "65                                                                     \n" +
      "66                                                                     \n" +
      "67                                                                     \n" +
      "68                                                                     \n" +
      "69                                                                     \n" +
      "70                                                                     \n" +
      "71                                                                     \n" +
      "72                                                                     \n" +
      "73                                                                     \n" +
      "74                                                                     \n" +
      "75                                                                     \n" +
      "76                                                                     \n" +
      "77                                                                     \n" +
      "78                                                                     \n" +
      "79                                                                     \n" +
      "80                                                                     \n" +
      "81                                                                     \n" +
      "82                                                                     \n" +
      "83                                                                     \n" +
      "84                                                                     \n" +
      "85                                                                     \n" +
      "86                                                                     \n" +
      "87                                                                     \n" +
      "88                                                                     \n" +
      "89                                                                     \n" +
      "90                                                                     \n" +
      "91                                                                     \n" +
      "92                                                                     \n" +
      "93                                                                     \n" +
      "94                                                                     \n" +
      "95                                                                     \n" +
      "96                                                                     \n" +
      "97                                                                     \n" +
      "98                                                                     \n" +
      "99    X                                                                \n" +
      "100   |                                                                ";
  
  @Test
  public void testAddAndCompositionLength() {
    IMusicModel testModel = new ConcreteMusicModel();
    assertEquals(0, testModel.compositionLength());
    testModel.add(new Tone(0, 1, 1, 60, 1));
    assertEquals(1, testModel.compositionLength());
  }
  
  @Test
  public void testEditAndCompositionLength() {
    IMusicModel testModel = new ConcreteMusicModel();
    assertEquals(0, testModel.compositionLength());
    testModel.add(new Tone(0, 1, 1, 60, 1));
    assertEquals("C0", testModel.getTonesAtStartBeat(1).get(0).toString());
    testModel.edit(new Tone(0, 1, 1, 60, 0), 12, 0, 1, 60, 1);
    assertEquals("C1", testModel.getTonesAtStartBeat(0).get(0).toString());
    
  }
  
  @Test
  public void testRemoveAndAddAndCompositionLength() {
    IMusicModel testModel = new ConcreteMusicModel();
    assertEquals(0, testModel.compositionLength());
    testModel.add(new Tone(0, 0, 1, 60, 1));
    assertEquals(1, testModel.compositionLength());
    testModel.add(new Tone(3, 1, 2, 60, 1));
    assertEquals(2, testModel.compositionLength());
    testModel.add(new Tone(20, 1, 2, 60, 1));
    testModel.remove(new Tone(3, 1, 2, 60, 1));
    testModel.remove(new Tone(0, 0, 1, 60, 1));
    assertEquals(1, testModel.compositionLength());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void removeThrowsExceptionIfGivenToneIsNotInModel() {
    IMusicModel testModel = new ConcreteMusicModel();
    testModel.remove(new Tone(3, 1, 2, 60, 1));
  }
  
  @Test
  public void testAllTones() {
    IMusicModel testModel = new ConcreteMusicModel();
    assertEquals(0, testModel.compositionLength());
    testModel.add(new Tone(4, 0, 1, 60, 1));
    testModel.add(new Tone(12, 1, 1, 60, 1));
    testModel.add(new Tone(60, 2, 1, 60, 1));
    testModel.add(new Tone(30, 3, 1, 60, 1));
    testModel.add(new Tone(8, 7, 1, 60, 1));
    assertEquals(5, testModel.allTones().size());
  }
  
  @Test
  public void testCombineSimul1() {
    IMusicModel testModel = new ConcreteMusicModel();
    IMusicModel testModel2 = new ConcreteMusicModel();
    assertEquals(0, testModel.compositionLength());
    testModel.add(new Tone(4, 0, 1, 60, 1));
    testModel.add(new Tone(12, 1, 1, 60, 1));
    testModel.add(new Tone(60, 2, 1, 60, 1));
    testModel.add(new Tone(30, 3, 1, 60, 1));
    testModel.add(new Tone(8, 7, 1, 60, 1));
    testModel.combineSimul(testModel2);
    assertEquals(5, testModel.allTones().size());
    testModel2.add(new Tone(5, 0, 1, 60, 1));
    testModel2.add(new Tone(13, 1, 1, 60, 1));
    testModel2.add(new Tone(61, 2, 1, 60, 1));
    testModel2.add(new Tone(31, 3, 1, 60, 1));
    testModel2.add(new Tone(9, 7, 1, 60, 1));
    testModel.combineSimul(testModel2);
    assertEquals(10, testModel.allTones().size());
    assertEquals(7, testModel.compositionLength());
  }
  
  @Test
  public void testCombineSimul2() {
    IMusicModel testModel = new ConcreteMusicModel();
    IMusicModel testModel2 = new ConcreteMusicModel();
    testModel.combineSimul(testModel2);
    assertEquals(0, testModel.compositionLength());
    testModel.add(new Tone(4, 0, 1, 60, 1));
    testModel.add(new Tone(12, 1, 1, 60, 1));
    testModel.add(new Tone(60, 2, 1, 60, 1));
    testModel.add(new Tone(30, 3, 1, 60, 1));
    testModel.add(new Tone(8, 7, 2, 60, 1));
    testModel.combineSimul(testModel2);
    assertEquals(5, testModel.allTones().size());
    testModel2.add(new Tone(5, 0, 1, 60, 1));
    testModel2.add(new Tone(13, 1, 1, 60, 1));
    testModel2.add(new Tone(61, 2, 1, 60, 1));
    testModel2.add(new Tone(31, 3, 1, 60, 1));
    testModel2.add(new Tone(9, 7, 2, 60, 1));
    testModel.combineSimul(testModel2);
    assertEquals(10, testModel.allTones().size());
    assertEquals(8, testModel.compositionLength());
  }
  
  @Test
  public void testCombineConsec1() {
    IMusicModel testModel = new ConcreteMusicModel();
    IMusicModel testModel2 = new ConcreteMusicModel();
    testModel.add(new Tone(4, 0, 1, 60, 1));
    testModel.add(new Tone(12, 1, 1, 60, 1));
    testModel.add(new Tone(60, 2, 1, 60, 1));
    testModel.add(new Tone(30, 3, 1, 60, 1));
    testModel.add(new Tone(8, 7, 1, 60, 1));
    assertEquals(7, testModel.compositionLength());
    testModel2.add(new Tone(5, 0, 1, 60, 1));
    testModel2.add(new Tone(13, 1, 1, 60, 1));
    testModel2.add(new Tone(61, 2, 1, 60, 1));
    testModel2.add(new Tone(31, 3, 1, 60, 1));
    testModel2.add(new Tone(9, 7, 1, 60, 1));
    testModel.combineConsec(testModel2);
    assertEquals(14, testModel.compositionLength());
  }
  
  @Test
  public void testCombineConsec2() {
    IMusicModel testModel = new ConcreteMusicModel();
    IMusicModel testModel2 = new ConcreteMusicModel();
    testModel.combineConsec(testModel2);
    assertEquals(0, testModel.compositionLength());
    testModel.add(new Tone(4, 0, 1, 60, 1));
    testModel.add(new Tone(12, 1, 1, 60, 1));
    testModel.add(new Tone(60, 2, 1, 60, 1));
    testModel.add(new Tone(30, 3, 1, 60, 1));
    testModel.add(new Tone(8, 7, 2, 60, 1));
    assertEquals(8, testModel.compositionLength());
    testModel2.add(new Tone(5, 0, 1, 60, 1));
    testModel2.add(new Tone(13, 1, 1, 60, 1));
    testModel2.add(new Tone(61, 2, 1, 60, 1));
    testModel2.add(new Tone(31, 3, 1, 60, 1));
    testModel2.add(new Tone(9, 7, 2, 60, 1));
    testModel.combineConsec(testModel2);
    assertEquals(16, testModel.compositionLength());
  }
  
  @Test
  public void testToString() {
    IMusicModel testModel = new ConcreteMusicModel();
    assertEquals("", testModel.toString());
    testModel.add(new Tone(0, 0, 2, 60, 1));
    testModel.add(new Tone(4, 1, 1, 60, 1));
    testModel.add(new Tone(8, 2, 1, 60, 1));
    testModel.add(new Tone(8, 3, 4, 60, 1));
    testModel.add(new Tone(16, 7, 3, 60, 1));
    assertEquals(outputString1, testModel.toString());
    testModel.add(new Tone(0, 99, 3, 60, 1));
    testModel.add(new Tone(0, 9, 3, 60, 1));
    testModel.add(new Tone(12, 9, 3, 60, 1));
    testModel.add(new Tone(13, 8, 3, 60, 1));
    assertEquals(outputString2, testModel.toString());
  }
  
  @Test
  public void testGetNotesAtStartBeat() {
    IMusicModel testModel = new ConcreteMusicModel();
    testModel.add(new Tone(0, 99, 3, 60, 1));
    testModel.add(new Tone(0, 9, 3, 60, 1));
    testModel.add(new Tone(12, 9, 3, 60, 1));
    testModel.add(new Tone(13, 8, 3, 60, 1));
    testModel.add(new Tone(0, 0, 2, 60, 1));
    testModel.add(new Tone(4, 1, 1, 60, 1));
    testModel.add(new Tone(8, 2, 1, 60, 1));
    testModel.add(new Tone(8, 3, 4, 60, 1));
    testModel.add(new Tone(16, 7, 3, 60, 1));
    assertEquals("C0", testModel.getTonesAtStartBeat(9).get(0).toString());
    assertEquals("C1", testModel.getTonesAtStartBeat(9).get(1).toString());
  }
  
  @Test
  public void testGetNotesThatSustain() {
    IMusicModel testModel = new ConcreteMusicModel();
    testModel.add(new Tone(0, 99, 3, 60, 1));
    testModel.add(new Tone(0, 9, 3, 60, 1));
    testModel.add(new Tone(12, 9, 3, 60, 1));
    testModel.add(new Tone(13, 8, 3, 60 , 1));
    testModel.add(new Tone(0, 0, 2, 60, 1));
    testModel.add(new Tone(4, 1, 1, 60, 1));
    testModel.add(new Tone(8, 2, 1, 60, 1));
    testModel.add(new Tone(8, 3, 4, 60, 1));
    testModel.add(new Tone(16, 7, 3, 60, 1));
    List<Tone> notesThatSustainAt10 = testModel.getTonesThatSustain(10);
    assertEquals(2, notesThatSustainAt10.size());
    assertEquals("C0", notesThatSustainAt10.get(0).toString());
    assertEquals("C1", notesThatSustainAt10.get(1).toString());
  }
  
  @Test(expected = NullPointerException.class)
  public void addWithNullArgThrowsException() {
    IMusicModel testModel = new ConcreteMusicModel();
    testModel.add(null);
  }
  
  @Test(expected = NullPointerException.class)
  public void removeWithNullArgThrowsException() {
    IMusicModel testModel = new ConcreteMusicModel();
    testModel.remove(null);
  }
  
  @Test(expected = NullPointerException.class)
  public void combineConsecWithNullArgThrowsException() {
    IMusicModel testModel = new ConcreteMusicModel();
    testModel.combineConsec(null);
  }
  
  @Test(expected = NullPointerException.class)
  public void combineSimulWithNullArgThrowsException() {
    IMusicModel testModel = new ConcreteMusicModel();
    testModel.combineSimul(null);
  }
}
