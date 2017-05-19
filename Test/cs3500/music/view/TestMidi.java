package cs3500.music.view;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import cs3500.music.controller.IMusicEditorController;
import cs3500.music.model.ConcreteMusicModel;
import cs3500.music.model.IMusicModel;
import cs3500.music.util.ControllerFactory;
import cs3500.music.util.MusicReader;
import cs3500.music.util.ViewFactory;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Abinader on 11/4/16.
 * Testing suite for the MidiView.
 */
public class TestMidi {
  String testStringMock =
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 800000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1800000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1000000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1000000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1800000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1800000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1800000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 600000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1800000\n" +
      "status byte: 144 timeStamp value: 1000000\n" +
      "status byte: 128 timeStamp value: 1800000\n";
  StringBuilder expectedOutputMock = new StringBuilder().append(testStringMock);
  StringBuilder realOutputMock = new StringBuilder();
  
  @Test
  /**
   * Here I am testing that the MIDI with the Mock Synthesizer is hooked up correctly to get the
   * Mock Receiver and the Mock Receiver appends information on all the messages passed to it to
   * the StringBuilder object that it is initialized with.
   */
  public void testMidiViewMockSynthesizer() throws FileNotFoundException, InterruptedException {
    IMusicModel model = MusicReader.parseFile(new FileReader("mary-little-lamb.txt"),
            new ConcreteMusicModel.Builder());
    View view = ViewFactory.initializeTestView("midi", realOutputMock);
    IMusicEditorController controller = ControllerFactory.initializeController(view, model);
    controller.execute();
    Thread.sleep(model.compositionLength() * 1000);
    assertEquals(expectedOutputMock.toString(), realOutputMock.toString());
  }
  
  @Test(expected = FileNotFoundException.class)
  public void tryingToPlayASongWithAInvalidFilePathThrowsException() throws FileNotFoundException {
    IMusicModel model = MusicReader.parseFile(new FileReader("txt.bmal-elttil-yram"),
            new ConcreteMusicModel.Builder());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void tryingToInitializeATestMIDIWithInvalidParamsThrowsException() throws
          FileNotFoundException {
    IMusicModel model = MusicReader.parseFile(new FileReader("mary-little-lamb.txt"),
            new ConcreteMusicModel.Builder());
    View view = ViewFactory.initializeTestView("bananas", realOutputMock);
  }
}
