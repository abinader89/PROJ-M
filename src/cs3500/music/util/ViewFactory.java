package cs3500.music.util;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import cs3500.music.adapters.GUIAdapter;
import cs3500.music.adapters.GUIAdapterComposite;
import cs3500.music.adapters.MIDIAdapter;
import cs3500.music.adapters.TextualAdapter;
import cs3500.music.view.CompositeView;
import cs3500.music.view.GuiViewFrame;
import cs3500.music.view.MidiViewImpl;
import cs3500.music.view.TextualView;
import cs3500.music.view.View;

/**
 * Created by Abinader on 11/4/16.
 * This is the factory for views. It is capable of producing the COMPOSITE, TEXTUAL, GUI, or MIDI
 * views. If the string "client-" is placed before any of the viewtypes (console, gui, midi,
 * composite) then client views will be instantiated using class adapters.
 */
public final class ViewFactory {
  
  /**
   * This creates a view to use with the controller.
   * @param viewType String
   * @return View
   */
  public static View initializeView(String viewType) {
    switch (viewType) {
      case "client-console":
        return new TextualAdapter(System.out);
      case "client-gui":
        return new GUIAdapter();
      case "client-midi":
        try {
          return new MIDIAdapter(MidiSystem.getSequencer());
        } catch (MidiUnavailableException e) {
          // HANDLE
          System.out.println("Something went wrong. Unable to initialize MIDI View.");
          break;
        }
      case "client-composite":
        try {
          return new GUIAdapterComposite(new GUIAdapter(),
                  new MIDIAdapter(MidiSystem.getSequencer()));
        } catch (MidiUnavailableException e) {
          // HANDLE
          System.out.println("Something went wrong. Unable to initialize MIDI View.");
          break;
        }
      case "console":
        return new TextualView(System.out);
      case "gui":
        return new GuiViewFrame();
      case "midi":
        try {
          return new MidiViewImpl();
        } catch (MidiUnavailableException e) {
          // HANDLE
          System.out.println("Something went wrong. Unable to initialize MIDI View.");
          break;
        }
      case "composite":
        try {
          return new CompositeView(new MidiViewImpl(), new GuiViewFrame());
        } catch (MidiUnavailableException e) {
          // HANDLE
          System.out.println("Something went wrong. Unable to initialize MIDI View.");
          break;
        }
      default: // NO OTHER OPTION
    }
    throw new IllegalArgumentException();
  }
  
  /**
   * This creates a view that facilitates testing.
   * @param viewType String
   * @param ap StringBuilder
   * @return View
   */
  public static View initializeTestView(String viewType,
                                        StringBuilder ap) {
    switch (viewType) {
      case "console":
        return new TextualView(ap);
      case "midi":
        try {
          return new MidiViewImpl(ap);
        } catch (MidiUnavailableException e) {
          // HANDLE
          System.out.println("Something went wrong. Unable to initialize MIDI View.");
          break;
        }
      case "composite":
        try {
          return new CompositeView(new MidiViewImpl(), new GuiViewFrame(), ap);
        } catch (MidiUnavailableException e) {
          // HANDLE
          System.out.println("Something went wrong. Unable to initialize MIDI View.");
          break;
        }
      default: // NO OTHER OPTION
    }
    throw new IllegalArgumentException();
  }
}
