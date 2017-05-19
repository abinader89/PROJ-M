package cs3500.music.controller;

import cs3500.music.model.IMusicModel;
import cs3500.music.view.View;

/**
 * Created by Abinader on 11/15/16.
 * Represents the BasicMusicEditorController.
 */
public final class BasicMusicEditorController extends AbstractMusicEditorController {
  /**
   * Constructor for a BasicMusicEditorController.
   * @param view  View
   * @param model IMusicModel
   */
  public BasicMusicEditorController(View view, IMusicModel model) {
    super(view, model);
    super.playing = true;
  }
}
