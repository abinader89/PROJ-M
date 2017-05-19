package cs3500.music.util;

import cs3500.music.controller.BasicMusicEditorController;
import cs3500.music.controller.CompositeMusicEditorController;
import cs3500.music.controller.IMusicEditorController;
import cs3500.music.model.IMusicModel;
import cs3500.music.view.CompositeView;
import cs3500.music.view.View;

/**
 * Created by Abinader on 11/26/16.
 * This is the factory for controllers. It is capable of producing the a basic controller and a
 * composite controller.
 * views.
 */
public class ControllerFactory {
  
  /**
   * Gets a controller based on the view.
   * @param view View
   * @param model IMusicModel
   * @return IMusicEditorController
   */
  public static IMusicEditorController initializeController(View view, IMusicModel model) {
    if (view instanceof CompositeView) {
      return new CompositeMusicEditorController(view, model);
    } else {
      return new BasicMusicEditorController(view, model);
    }
  }
  
  /**
   * Gets a controller and passes in a StringBuilder with testable output.
   * @param view View
   * @param model IMusicModel
   * @param sb StringBuilder
   * @return IMusicEditorController
   */
  public static IMusicEditorController initializeTestController(View view, IMusicModel model,
                                                                StringBuilder sb) {
    return new CompositeMusicEditorController(view, model, sb);
  }
}
