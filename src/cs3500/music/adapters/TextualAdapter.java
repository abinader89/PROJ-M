package cs3500.music.adapters;

import cs3500.music.model.ReadOnlyModel;
import cs3500.music.provider.TextMusicView;
import cs3500.music.view.View;

/**
 * Created by Abinader on 11/26/16.
 * This is an adapter for the clients console view.
 */
public class TextualAdapter extends TextMusicView implements View, IViewAdapter {
  
  /**
   * Constructs a TextMusicView.
   *
   * @param ap Appendable so the TextMusicView can be rendered in the console.
   */
  public TextualAdapter(Appendable ap) {
    super(ap);
  }
  
  @Override
  public void initialize(ReadOnlyModel model) {
    super.render(NoteAdapter.toneConverter(model.viewTones(), model.viewTempo()));
  }
  
  @Override
  public void increaseCurrentBeat() {
    // UNUSED
  }
  
  @Override
  public void resetBeat() {
    // UNUSED
  }
}
