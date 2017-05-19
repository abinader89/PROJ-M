package cs3500.music.provider;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interface for viewing an IMusicModel.
 * </p>
 * The interface only provides a single method "render" which takes a list of songs as input. Render
 * may be called many times, it is up to the implementers of IMusicView to decide how to handle new
 * data that is passed to render.
 */
public interface IMusicView {
  /**
   * Render each of the Songs in the given list.
   * The view is only responsible for showing the Song data to a user for now.
   *
   * @param songList the list of songs to render.
   */
  void render(List<ISong> songList);

  /**
   * Play the song or songs from it's current position.
   */
  void play();

  /**
   * Pause the current song and hold the current position.
   */
  void pause();

  /**
   * Stop the song and send the current position back to the beginning.
   */
  void stop();

  /**
   * Tell the view to clean up its resources before dying.
   */
  void cleanUp();

  /**
   * Set the song adder for the relevant views.
   *
   * @param howToAddSong Consumer used to add song.
   */
  void setSongAdder(Consumer<ISong> howToAddSong);

  /**
   * Set the song remover for the relevant views.
   *
   * @param howToRemoveSong Consumer used to remove song.
   */
  void setSongRemover(Consumer<ISong> howToRemoveSong);

  /**
   * Convey a message to the client in some form.
   *
   * @param message String message to be conveyed to the client.
   */
  void showMessage(String message);
}
