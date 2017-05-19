package cs3500.music.provider;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * A class for pairing a Rectangle object with a Color.
 * </p>
 * This class is used for drawing a single note. It helps decouple the painting of the notes from
 * the time of their creation.
 */
public class NoteRectangle extends Rectangle2D.Double {
  private final Color color;

  /**
   * Construct a NoteRectangle.
   *
   * @param x     the top left corner X coord of the note rectangle.
   * @param y     the top left corner Y coord of the note rectangle.
   * @param h     the height of the rectangle in pixels.
   * @param w     the width of the rectangle in pixels.
   * @param color the color of this rectangle.
   */
  public NoteRectangle(double x, double y, double h, double w, Color color) {
    super(x, y, h, w);
    this.color = color;
  }

  /**
   * Get the color of this rectangle.
   *
   * @return the color of this rectangle.
   */
  public Color getColor() {
    return this.color;
  }

}
