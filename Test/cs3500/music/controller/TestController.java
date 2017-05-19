package cs3500.music.controller;

import org.junit.Test;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;

import cs3500.music.model.ConcreteMusicModel;
import cs3500.music.model.IMusicModel;
import cs3500.music.util.ControllerFactory;
import cs3500.music.util.MusicReader;
import cs3500.music.util.ViewFactory;
import cs3500.music.view.GuiViewFrame;

import cs3500.music.view.View;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Abinader on 11/18/16.
 * Testing Suite for a the CompositeController with Mock runnables.
 */
public class TestController {
  StringBuilder realOutputMock = new StringBuilder();
  IMusicModel model = null;
  String beforeAdd =
      "    E4  F4 F#4  G4 G#4  A4 A#4  B4  C5 C#5  D5 D#5  E5  F5 F#5  G5\n" +
      "0                X                                   X            \n" +
      "1                |                                   |            \n" +
      "2                |                           X                    \n" +
      "3                |                           |                    \n" +
      "4                |                   X                            \n" +
      "5                |                   |                            \n" +
      "6                |                           X                    \n" +
      "7                                            |                    \n" +
      "8                X                                   X            \n" +
      "9                |                                   |            \n" +
      "10               |                                   X            \n" +
      "11               |                                   |            \n" +
      "12               |                                   X            \n" +
      "13               |                                   |            \n" +
      "14               |                                   |            \n" +
      "15                                                                \n" +
      "16               X                           X                    \n" +
      "17               |                           |                    \n" +
      "18               |                           X                    \n" +
      "19               |                           |                    \n" +
      "20               |                           X                    \n" +
      "21               |                           |                    \n" +
      "22               |                           |                    \n" +
      "23               |                           |                    \n" +
      "24               X                                   X            \n" +
      "25               |                                   |            \n" +
      "26                                                               X\n" +
      "27                                                               |\n" +
      "28                                                               X\n" +
      "29                                                               |\n" +
      "30                                                               |\n" +
      "31                                                               |\n" +
      "32               X                                   X            \n" +
      "33               |                                   |            \n" +
      "34               |                           X                    \n" +
      "35               |                           |                    \n" +
      "36               |                   X                            \n" +
      "37               |                   |                            \n" +
      "38               |                           X                    \n" +
      "39               |                           |                    \n" +
      "40               X                                   X            \n" +
      "41               |                                   |            \n" +
      "42               |                                   X            \n" +
      "43               |                                   |            \n" +
      "44               |                                   X            \n" +
      "45               |                                   |            \n" +
      "46               |                                   X            \n" +
      "47               |                                   |            \n" +
      "48               X                           X                    \n" +
      "49               |                           |                    \n" +
      "50               |                           X                    \n" +
      "51               |                           |                    \n" +
      "52               |                                   X            \n" +
      "53               |                                   |            \n" +
      "54               |                           X                    \n" +
      "55               |                           |                    \n" +
      "56   X                               X                            \n" +
      "57   |                               |                            \n" +
      "58   |                               |                            \n" +
      "59   |                               |                            \n" +
      "60   |                               |                            \n" +
      "61   |                               |                            \n" +
      "62   |                               |                            \n" +
      "63   |                               |                            ";
  
  String afterAdd =
      "    E4  F4 F#4  G4 G#4  A4 A#4  B4  C5 C#5  D5 D#5  E5  F5 F#5  G5\n" +
      "0    X           X                                   X            \n" +
      "1    |           |                                   |            \n" +
      "2    |           |                           X                    \n" +
      "3                |                           |                    \n" +
      "4                |                   X                            \n" +
      "5                |                   |                            \n" +
      "6                |                           X                    \n" +
      "7                                            |                    \n" +
      "8                X                                   X            \n" +
      "9                |                                   |            \n" +
      "10               |                                   X            \n" +
      "11               |                                   |            \n" +
      "12               |                                   X            \n" +
      "13               |                                   |            \n" +
      "14               |                                   |            \n" +
      "15                                                                \n" +
      "16               X                           X                    \n" +
      "17               |                           |                    \n" +
      "18               |                           X                    \n" +
      "19               |                           |                    \n" +
      "20               |                           X                    \n" +
      "21               |                           |                    \n" +
      "22               |                           |                    \n" +
      "23               |                           |                    \n" +
      "24               X                                   X            \n" +
      "25               |                                   |            \n" +
      "26                                                               X\n" +
      "27                                                               |\n" +
      "28                                                               X\n" +
      "29                                                               |\n" +
      "30                                                               |\n" +
      "31                                                               |\n" +
      "32               X                                   X            \n" +
      "33               |                                   |            \n" +
      "34               |                           X                    \n" +
      "35               |                           |                    \n" +
      "36               |                   X                            \n" +
      "37               |                   |                            \n" +
      "38               |                           X                    \n" +
      "39               |                           |                    \n" +
      "40               X                                   X            \n" +
      "41               |                                   |            \n" +
      "42               |                                   X            \n" +
      "43               |                                   |            \n" +
      "44               |                                   X            \n" +
      "45               |                                   |            \n" +
      "46               |                                   X            \n" +
      "47               |                                   |            \n" +
      "48               X                           X                    \n" +
      "49               |                           |                    \n" +
      "50               |                           X                    \n" +
      "51               |                           |                    \n" +
      "52               |                                   X            \n" +
      "53               |                                   |            \n" +
      "54               |                           X                    \n" +
      "55               |                           |                    \n" +
      "56   X                               X                            \n" +
      "57   |                               |                            \n" +
      "58   |                               |                            \n" +
      "59   |                               |                            \n" +
      "60   |                               |                            \n" +
      "61   |                               |                            \n" +
      "62   |                               |                            \n" +
      "63   |                               |                            ";
  
  /**
   * This is an initializer for a test model, which needs to be surrounded by a try/catch.
   */
  public void modelSetup() {
    try {
      model = MusicReader.parseFile(new FileReader("mary-little-lamb.txt"),
              new ConcreteMusicModel.Builder());
    } catch (FileNotFoundException e) {
      // HANDLE
      e.printStackTrace();
    }
  }
  
  @Test
  // This test ensures that a called runnable for keyPresses are is actually called.
  public void testMockRunnablesInController() {
    this.modelSetup();
    View view = ViewFactory.initializeView("composite");
    IMusicEditorController controller =
            ControllerFactory.initializeTestController(view, model,
                    this.realOutputMock);
    controller.execute();
    controller.testRunnable(KeyEvent.VK_Y);
    assertEquals("foobar", realOutputMock.toString());
  }
  
  @Test
  // This test is concurrently testing mouse events as well as testing the delete runnable.
  public void testDeleteRunnableInControllerAndMouseClickedEvent() {
    this.modelSetup();
    View view = ViewFactory.initializeView("composite");
    IMusicEditorController controller = ControllerFactory.initializeController(view,
            model);
    controller.execute();
    assertEquals(34, this.model.allTones().size());
    controller.testRunnable(KeyEvent.VK_DELETE);
    GuiViewFrame view0 = new GuiViewFrame();
    view.initialize(model);
    MouseEvent me = new MouseEvent(view0, MouseEvent.MOUSE_CLICKED, ((long)0), 2, 85, 114, 1,
            false, 1);
    controller.testMouseEvent(me);
    assertEquals(33, this.model.allTones().size());
  }
  
  @Test
  // This test is concurrently testing mouse events as well as testing the add runnable.
  public void testAddRunnableInControllerAndMousePressedAndMouseReleasedEvent() {
    this.modelSetup();
    View view = ViewFactory.initializeView("composite");
    IMusicEditorController controller = ControllerFactory.initializeController(view,
            model);
    controller.execute();
    assertEquals(this.beforeAdd, this.model.toString());
    controller.setVolumeAndInstrumentValues(0, 100);
    controller.testRunnable(KeyEvent.VK_A);
    GuiViewFrame view0 = new GuiViewFrame();
    view.initialize(model);
    MouseEvent me = new MouseEvent(view0, MouseEvent.MOUSE_PRESSED, ((long)0), 2, 82, 414, 1,
            false, 1);
    controller.testMouseEvent(me);
    MouseEvent me2 = new MouseEvent(view0, MouseEvent.MOUSE_RELEASED, ((long)0), 2, 184, 413, 1,
            false, 1);
    controller.testMouseEvent(me2);
    assertEquals(this.afterAdd, this.model.toString());
  }
}
