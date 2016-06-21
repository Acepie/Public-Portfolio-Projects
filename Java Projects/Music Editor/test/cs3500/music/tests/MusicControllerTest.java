package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import cs3500.music.controller.Controller;
import cs3500.music.controller.KeyboardHandler;
import cs3500.music.controller.MouseHandler;
import cs3500.music.controller.events.AddNote;
import cs3500.music.controller.events.AddNoteInstrument;
import cs3500.music.controller.events.BackNote;
import cs3500.music.controller.events.ChangeInstrument;
import cs3500.music.controller.events.Clear;
import cs3500.music.controller.events.ClearSelection;
import cs3500.music.controller.events.DecreaseDuration;
import cs3500.music.controller.events.DownNote;
import cs3500.music.controller.events.ForwardNotes;
import cs3500.music.controller.events.IncreaseDuration;
import cs3500.music.controller.events.MockMouseRunnable;
import cs3500.music.controller.events.MockRunnable;
import cs3500.music.controller.events.MoveBackward;
import cs3500.music.controller.events.MoveForward;
import cs3500.music.controller.events.MultiSelectSpace;
import cs3500.music.controller.events.RemoveNote;
import cs3500.music.controller.events.ScrollBack;
import cs3500.music.controller.events.ScrollEnd;
import cs3500.music.controller.events.SelectSpace;
import cs3500.music.controller.events.TogglePlaying;
import cs3500.music.controller.events.UpNote;
import cs3500.music.model.MusicScoreImpl;
import cs3500.music.view.GuiView;
import cs3500.music.view.MockGuiView;
import cs3500.music.view.ViewFactory;

import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_END;
import static java.awt.event.KeyEvent.VK_HOME;
import static java.awt.event.KeyEvent.VK_SPACE;
import static org.junit.Assert.assertEquals;

/**
 * Tests controller, handlers, and all the events
 */
public class MusicControllerTest {

  KeyboardHandler keyboard;
  MouseHandler mouse;
  KeyboardHandler keyboard2;
  GuiView view;
  GuiView view2;
  GuiView view3;
  Controller controller;
  Controller controller2;
  Controller controller3;

  @Before
  public void init() {
    keyboard = new KeyboardHandler();
    mouse = new MouseHandler();
    try {
      view = ViewFactory.makeGuiView("visual", new MusicScoreImpl.Builder().build());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      view2 = ViewFactory.makeGuiView("composite", new MusicScoreImpl.Builder().build());
    } catch (Exception e) {
      e.printStackTrace();
    }
//    controller = new MusicController(view);
//    controller2 = new MusicController(view2);
    try {
      view3 = new MockGuiView();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testKeyboardHandler() throws AWTException {
    StringBuilder sb = new StringBuilder();
    keyboard.addReleasedRunnable(VK_SPACE, new MockRunnable(sb));
    keyboard.addTypedRunnable(VK_HOME, new MockRunnable(sb));
    keyboard.addPressedRunnable(VK_END, new MockRunnable(sb));
    assertEquals(keyboard.toString(),
        "Typed events: \n" +
        "key 36: MockRunnable\n" +
        "Pressed events: \n" +
        "key 35: MockRunnable\n" +
        "Released events: \n" +
        "key 32: MockRunnable\n");
  }

  @Test
  public void testMouseHandler() {
    StringBuilder sb2 = new StringBuilder();
    mouse.addMousePressed(MouseEvent.BUTTON1, new MockMouseRunnable(sb2));
    mouse.addMousePressed(MouseEvent.BUTTON2, new MockMouseRunnable(sb2));
    mouse.addMousePressed(MouseEvent.BUTTON3, new MockMouseRunnable(sb2));
    assertEquals(mouse.toString(),
        "Clicked events: \n" +
        "key 3: MockMouseRunnable\n" +
        "key 2: MockMouseRunnable\n" +
        "key 1: MockMouseRunnable\n");
  }

  @Test
  public void testController() throws Exception {
    StringBuilder sb = new StringBuilder();
    keyboard.addReleasedRunnable(VK_C, new MockRunnable(sb));
    view3.addKeyListener(keyboard);
    assertEquals(view3.displayLogData(),
        "added keylistener\n");
    KeyEvent event = new KeyEvent((MockGuiView) view3, KeyEvent.KEY_RELEASED, System
        .currentTimeMillis(), 0, VK_C, 'c');
    ((MockGuiView) view3).getKeyListeners()[0].keyReleased(event);
    assertEquals(sb.toString(),
        "Mock runnable has been run\n");
    mouse.addMouseReleased(MouseEvent.BUTTON1, new MockMouseRunnable(sb));
    view3.addMouseListener(mouse);
    assertEquals(view3.displayLogData(),
        "added keylistener\n" +
        "added mouselistener\n");
    MouseEvent event2 =
        new MouseEvent((MockGuiView) view3,
            MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 1, 1, 1, true, MouseEvent
            .BUTTON1);
    ((MockGuiView) view3).getMouseListeners()[0].mouseReleased(event2);
    assertEquals(sb.toString(),
        "Mock runnable has been run\n" +
        "Mock mouse runnable has been run with coordinates:1, 1\n");
  }

  @Test
  public void testRunnables() throws Exception {
    new MultiSelectSpace(view).run(61, 151);
    new MultiSelectSpace(view).run(51, 151);
    assertEquals(view.getSelections().size(), 2);
    new AddNote(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n"
        + "Drawing string: C 4 at: 10, 160\n"
        + "Drawing string: C#4 at: 10, 150\n"
        + "Drawing string: D 4 at: 10, 140\n"
        + "Drawing string: D#4 at: 10, 130\n"
        + "Drawing string: E 4 at: 10, 120\n"
        + "Drawing string: F 4 at: 10, 110\n"
        + "Drawing string: F#4 at: 10, 100\n"
        + "Drawing string: G 4 at: 10, 90\n"
        + "Drawing string: G#4 at: 10, 80\n"
        + "Drawing string: A 4 at: 10, 70\n"
        + "Drawing string: A#4 at: 10, 60\n"
        + "Drawing string: B 4 at: 10, 50\n"
        + "Drawing string: 0 at: 40, 20\n"
        + "setting color to: java.awt.Color[r=0,g=255,b=0]\n"
        + "Making rect at point: 50, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=0,g=0,b=0]\n"
        + "Making rect at point: 50, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=0,g=255,b=0]\n"
        + "Making rect at point: 60, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=0,g=0,b=0]\n"
        + "Making rect at point: 60, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=255,g=0,b=0]\n"
        + "Drawing line from: 40,160 to 40,40\n"
        + "setting color to: java.awt.Color[r=0,g=0,b=255]\n"
        + "Making rect at point: 60, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=0,g=0,b=255]\n"
        + "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    new Clear(view).run();
    new MultiSelectSpace(view).run(61, 151);
    new MultiSelectSpace(view).run(51, 151);
    assertEquals(view.getSelections().size(), 2);
    new ClearSelection(view).run(51, 151);
    assertEquals(view.getSelections().size(), 0);
    new SelectSpace(view).run(51, 151);
    assertEquals(view.getSelections().size(), 1);
    assertEquals(view.getSelections().get(0).beat, 1);
    assertEquals(view.getSelections().get(0).pitch, 0);
    assertEquals(view.getSelections().get(0).octave, 4);
    new AddNote(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    new RemoveNote(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    new AddNote(view).run();
    assertEquals(view.instrumentViewing(), 0);
    new SelectSpace(view).run(51, 161);
    assertEquals(view.getSelections().size(), 1);
    new AddNoteInstrument(view, 1).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    assertEquals(view.instrumentViewing(), 1);
    new ChangeInstrument(view).run();
    assertEquals(view.instrumentViewing(), -2);
    assertEquals(view.beat(), 0);
    new MoveForward(view).run();
    assertEquals(view.beat(), 1);
    new MoveBackward(view).run();
    assertEquals(view.beat(), 0);
    new MoveForward(view).run();
    assertEquals(view.beat(), 1);
    new ScrollBack(view).run();
    assertEquals(view.beat(), 0);
    new ScrollEnd(view).run();
    assertEquals(view.beat(), 2);
    assertEquals(view.instrumentViewing(), -2);
    new ChangeInstrument(view).run();
    assertEquals(view.instrumentViewing(), 0);
    new ChangeInstrument(view).run();
    assertEquals(view.instrumentViewing(), 1);
    new IncreaseDuration(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 20 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    new DecreaseDuration(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    new ForwardNotes(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 60, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 60, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    new SelectSpace(view).run(61, 161);
    new BackNote(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 60, 160 with dimensions: 10 by 10\n");
    new SelectSpace(view).run(51, 161);
    new UpNote(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    new SelectSpace(view).run(51, 151);
    new DownNote(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    new Clear(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    assertEquals(view.playing(), false);
    new TogglePlaying(view).run();
    assertEquals(view.playing(), true);
  }

  @Test
  public void testRunnablesView2() throws Exception {
    new MultiSelectSpace(view).run(61, 151);
    new MultiSelectSpace(view).run(51, 151);
    assertEquals(view.getSelections().size(), 2);
    new AddNote(view).run();
    assertEquals(view.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n"
        + "Drawing string: C 4 at: 10, 160\n"
        + "Drawing string: C#4 at: 10, 150\n"
        + "Drawing string: D 4 at: 10, 140\n"
        + "Drawing string: D#4 at: 10, 130\n"
        + "Drawing string: E 4 at: 10, 120\n"
        + "Drawing string: F 4 at: 10, 110\n"
        + "Drawing string: F#4 at: 10, 100\n"
        + "Drawing string: G 4 at: 10, 90\n"
        + "Drawing string: G#4 at: 10, 80\n"
        + "Drawing string: A 4 at: 10, 70\n"
        + "Drawing string: A#4 at: 10, 60\n"
        + "Drawing string: B 4 at: 10, 50\n"
        + "Drawing string: 0 at: 40, 20\n"
        + "setting color to: java.awt.Color[r=0,g=255,b=0]\n"
        + "Making rect at point: 50, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=0,g=0,b=0]\n"
        + "Making rect at point: 50, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=0,g=255,b=0]\n"
        + "Making rect at point: 60, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=0,g=0,b=0]\n"
        + "Making rect at point: 60, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=255,g=0,b=0]\n"
        + "Drawing line from: 40,160 to 40,40\n"
        + "setting color to: java.awt.Color[r=0,g=0,b=255]\n"
        + "Making rect at point: 60, 150 with dimensions: 10 by 10\n"
        + "setting color to: java.awt.Color[r=0,g=0,b=255]\n"
        + "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    new Clear(view).run();
    new MultiSelectSpace(view).run(61, 151);
    new MultiSelectSpace(view).run(51, 151);
    assertEquals(view.getSelections().size(), 2);
    new ClearSelection(view).run(51, 151);
    assertEquals(view.getSelections().size(), 0);
    new SelectSpace(view2).run(51, 151);
    assertEquals(view2.getSelections().size(), 1);
    assertEquals(view2.getSelections().get(0).beat, 1);
    assertEquals(view2.getSelections().get(0).pitch, 0);
    assertEquals(view2.getSelections().get(0).octave, 4);
    new AddNote(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    new RemoveNote(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    new AddNote(view2).run();
    assertEquals(view2.instrumentViewing(), 0);
    new SelectSpace(view2).run(51, 161);
    assertEquals(view2.getSelections().size(), 1);
    new AddNoteInstrument(view2, 1).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    assertEquals(view2.instrumentViewing(), 1);
    new ChangeInstrument(view2).run();
    assertEquals(view2.instrumentViewing(), -2);
    assertEquals(view2.beat(), 0);
    new MoveForward(view2).run();
    assertEquals(view2.beat(), 1);
    new MoveBackward(view2).run();
    assertEquals(view2.beat(), 0);
    new MoveForward(view2).run();
    assertEquals(view2.beat(), 1);
    new ScrollBack(view2).run();
    assertEquals(view2.beat(), 0);
    new ScrollEnd(view2).run();
    assertEquals(view2.beat(), 2);
    assertEquals(view2.instrumentViewing(), -2);
    new ChangeInstrument(view2).run();
    assertEquals(view2.instrumentViewing(), 0);
    new ChangeInstrument(view2).run();
    assertEquals(view2.instrumentViewing(), 1);
    new IncreaseDuration(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 20 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    new DecreaseDuration(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    new ForwardNotes(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 60, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 60, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    new SelectSpace(view2).run(61, 161);
    new BackNote(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 60, 160 with dimensions: 10 by 10\n");
    new SelectSpace(view2).run(51, 161);
    new UpNote(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n");
    new SelectSpace(view2).run(51, 151);
    new DownNote(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 3 at: 10, 280\n" +
        "Drawing string: C#3 at: 10, 270\n" +
        "Drawing string: D 3 at: 10, 260\n" +
        "Drawing string: D#3 at: 10, 250\n" +
        "Drawing string: E 3 at: 10, 240\n" +
        "Drawing string: F 3 at: 10, 230\n" +
        "Drawing string: F#3 at: 10, 220\n" +
        "Drawing string: G 3 at: 10, 210\n" +
        "Drawing string: G#3 at: 10, 200\n" +
        "Drawing string: A 3 at: 10, 190\n" +
        "Drawing string: A#3 at: 10, 180\n" +
        "Drawing string: B 3 at: 10, 170\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 160 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    new Clear(view2).run();
    assertEquals(view2.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=255]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n");
    assertEquals(view2.playing(), false);
    new TogglePlaying(view2).run();
    assertEquals(view2.playing(), true);

  }


}