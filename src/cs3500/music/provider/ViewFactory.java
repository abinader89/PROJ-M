package cs3500.music.provider;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

/**
 * Factory class for constructing a view object.
 * The factory takes a string containing a type of view and returns and IMusicView pointing to an
 * instance of that type of View.
 */
public class ViewFactory {

  /**
   * Make an instance of TextMusicView
   * Output goes to System.out
   *
   * @return the new instance of Text view.
   */
  private static IMusicView makeConsoleView() {
    return new TextMusicView(System.out);
  }

  /**
   * Make a new instance of GUIMusicView.
   *
   * @return the new GUIMusicView.
   */
  private static IGUIMusicView makeGuiView() {
    return new GUIMusicView();
  }

  /**
   * Make a new instance of MidiMusicView
   *
   * @return the new MidiMusicView.
   */
  private static IMidiMusicView makeMidiView() throws MidiUnavailableException {
    return new MidiMusicView(MidiSystem.getSequencer());
  }

  /**
   * Uses the MIDI and GUI views and makes a combined view.
   *
   * @return IMusicView with synchronized MIDI and GUI components.
   * @throws MidiUnavailableException if the Java MIDI framework cannot provide a sequencer.
   */
  public static IMusicView makeCombinedView() throws MidiUnavailableException {
    return new MidiAndGuiView(makeGuiView(), makeMidiView());
  }

  /**
   * Make a view based on the input type string.
   *
   * @param type the type of view to make.
   * @return a new IMusicView of the given type.
   * @throws IllegalArgumentException if the given type is not supported.
   */
  public static IMusicView makeView(String type)
          throws IllegalArgumentException, MidiUnavailableException {
    switch (type) {
      case "console":
        return makeConsoleView();
      case "visual":
        return makeGuiView();
      case "midi":
        return makeMidiView();
      case "combined":
        return makeCombinedView();
      default:
        String oneOf = "\nChoose one of... console, visual, midi, or combined";
        throw new IllegalArgumentException("Unsupported view: " + type + oneOf);
    }
  }
}
