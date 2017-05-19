package cs3500.music;

import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.controller.IMusicEditorController;
import cs3500.music.model.ConcreteMusicModel;
import cs3500.music.model.IMusicModel;
import cs3500.music.util.ControllerFactory;
import cs3500.music.util.MusicReader;
import cs3500.music.util.ViewFactory;
import cs3500.music.view.View;

/**
 * This is the main class, it runs the program with the specified command line arguments.
 */
public class MusicEditor {
  
  /**
   * Main method that runs the program.
   * @param args String[]
   * @throws IOException this exception.
   * @throws InvalidMidiDataException this exception.
   */
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    IMusicModel model = MusicReader.parseFile(new FileReader(args[0]),
            new ConcreteMusicModel.Builder());
    View view = ViewFactory.initializeView(args[1]);
    IMusicEditorController controller = ControllerFactory.initializeController(view, model);
    controller.execute();
  }
}
