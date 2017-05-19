package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Created by Abinader on 11/11/16.
 * This is they KeyboardHandler class. It has methods that set the fields to the given maps. If a
 * keyboard input matches up with a runnable on the map, it will invoke that runnables run method.
 */
public final class KeyboardHandler implements KeyListener {
  private Map<Integer, Runnable> keyPressed;
  
  /**
   * Set the map for key pressed events. Key pressed events in Java Swing are integer codes
   */
  public void setKeyPressedMap(Map<Integer, Runnable> map) {
    keyPressed = map;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // UNUSED
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    if (keyPressed.containsKey(e.getKeyCode())) {
      keyPressed.get(e.getKeyCode()).run();
    }
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    // UNUSED
  }
}