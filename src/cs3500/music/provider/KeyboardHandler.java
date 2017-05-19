package cs3500.music.provider;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * Handle keyboard events using a registry system.
 */
public class KeyboardHandler implements KeyListener {
  private final HashMap<Integer, Runnable> typedDispatcher;
  private final HashMap<Integer, Runnable> pressedDispatcher;
  private final HashMap<Integer, Runnable> releasedDispatcher;

  /**
   * Constructs a KeyboardHandler.
   */
  public KeyboardHandler() {
    this.typedDispatcher = new HashMap<>();
    this.pressedDispatcher = new HashMap<>();
    this.releasedDispatcher = new HashMap<>();
  }

  /**
   * Register a key typed handler.
   *
   * @param keyChar    the key char to register the handler with.
   * @param onKeyTyped what to do when the key is typed.
   */
  public void registerKeyTypedHandler(char keyChar, Runnable onKeyTyped) {
    this.typedDispatcher.put((int) keyChar, onKeyTyped);
  }

  /**
   * Register a key pressed handler.
   *
   * @param keyCode      the code to handle.
   * @param onKeyPressed what to do when the key is pressed.
   */
  public void registerKeyPressedHandler(int keyCode, Runnable onKeyPressed) {
    this.pressedDispatcher.put(keyCode, onKeyPressed);
  }

  /**
   * Register a key released handler.
   *
   * @param keyCode       the code to handle.
   * @param onKeyReleased what to do when the key is released.
   */
  public void registerKeyReleasedHandler(int keyCode, Runnable onKeyReleased) {
    this.releasedDispatcher.put(keyCode, onKeyReleased);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    Runnable r = this.typedDispatcher.get((int) e.getKeyChar());
    if (r != null) {
      r.run();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    Runnable r = this.pressedDispatcher.get(e.getKeyCode());
    if (r != null) {
      r.run();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    Runnable r = this.releasedDispatcher.get(e.getKeyCode());
    if (r != null) {
      r.run();
    }
  }
}
