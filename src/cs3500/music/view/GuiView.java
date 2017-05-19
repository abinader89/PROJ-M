package cs3500.music.view;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.util.Direction;

/**
 * Created by Abinader on 11/1/16.
 * Represents the GuiView interface.
 */
public interface GuiView extends View {
  /**
   * Adds a key listener to the GuiView.
   * @param listener KeyListener
   */
  void addKListener(KeyListener listener);
  
  /**
   * Adds a mouse listener to the GuiView.
   * @param listener MouseListener
   */
  void addMListener(MouseListener listener);
  
  /**
   * Scrolls to the given direction.
   * @param dir Direction
   */
  void scroll(Direction dir);
  
  /**
   * Jumps to the end or the start of the composition.
   * @param toEnd boolean
   */
  void jump(boolean toEnd);
  
  /**
   * Sets the title of the JFrame to the specified String.
   * @param s String
   */
  void title(String s);
  
  /**
   * Sets the color of the window.
   * @param color Color
   */
  void setWindowColor(Color color);
  
  /**
   * Prompts the user with a window signaling some kind of error.
   * @param s String
   */
  void promptUser(String... s);
  
  /**
   * Repaints the display.
   */
  void repaintDisplay();
  
  /**
   * This gets the numerical value needed from the user to create Tones.
   * @param s String
   * @return int
   */
  int getInputs(String s);
}
