package cs3500.music.provider;

/**
 * Extension to IMusicView so that we can pass around the time.
 */
public interface IMidiMusicView extends IMusicView {
  /**
   * Get the time from the underlying sequencer.
   *
   * @return the time in the song in milliseconds.
   */
  long getTimeMilli();
}
