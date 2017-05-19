package cs3500.music.view;

import cs3500.music.model.ReadOnlyModel;

/**
 * Created by Abinader on 11/1/16.
 * This represents the union of all types of views for the Model.
 */
public interface View {
  
  /**
   * Initializes the view according to the model passed in.
   * @param model ReadOnlyModel
   */
  void initialize(ReadOnlyModel model);
  
  /**
   * Increases the current beat of the piece.
   */
  void increaseCurrentBeat();
  
  /**
   * This resets the beat for the view.
   */
  void resetBeat();
}
